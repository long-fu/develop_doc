#include <stdio.h>
#include <string.h>
#include <math.h>
#include <unistd.h>
#include <libavutil/avassert.h>
#include <libavutil/channel_layout.h>
#include <libavutil/opt.h>
#include <libavutil/mathematics.h>
#include <libavutil/timestamp.h>
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include <libswresample/swresample.h>
#include <libavdevice/avdevice.h>
#include <libavutil/imgutils.h>

#include <libavfilter/buffersink.h>
#include <libavfilter/buffersrc.h>
#include <libavutil/avutil.h>
#include <libavutil/imgutils.h>
#include <libavutil/opt.h>

#include <SDL.h>
#include <pthread.h>
#include <unistd.h>
#define VIDEO_DEV "/dev/video0"
char file_name[100]="rtmp://124.21.108.66:8088/live/ashui";//视频编码文件名
#define STREAM_FRAME_RATE 25 /*每秒25帧*/
#define STREAM_PIX_FMT AV_PIX_FMT_YUV420P /*图像格式yuv420p*/
#define STREAM_DURATION 10.0 /*录制时间10s*/
pthread_mutex_t fastmutex = PTHREAD_MUTEX_INITIALIZER;//互斥锁
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;//条件变量
int width;
int height;
int size;
int mp4_decode_stat=0;
unsigned char *rgb_buff=NULL;
unsigned char video_flag=1;
void *Video_CollectImage(void *arg);
void *Video_savemp4(void*arg);
typedef enum
{
	false=0,
	true,
}bool;
typedef struct OutputStream
{
	AVStream *st;
	AVCodecContext *enc;
	int64_t next_pts;/*将生成的下一帧的pts*/
	AVFrame *frame;/*保存编解码数据*/
	AVFrame *tmp_frame;
	struct SwsContext *sws_ctx;
	struct SwrContext *swr_ctx;
}OutputStream;
typedef struct IntputDev
{
	AVCodecContext *pcodecCtx;
	AVCodec *pCodec;
	AVFormatContext *v_ifmtCtx;
	int videoindex;//视频帧ID
	struct SwsContext *img_convert_ctx;
	AVPacket *in_packet;
	AVFrame *pFrame,*pFrameYUV;
}IntputDev;
IntputDev video_input={0};//视频输入流
/*添加一个输出流*/
static void add_stream(OutputStream *ost, AVFormatContext *oc, AVCodec **codec, enum AVCodecID codec_id)
{
	AVCodecContext *c;
	int i;
	/*查找编码器*/
	*codec=avcodec_find_encoder(codec_id);
	if(*codec==NULL)
	{
		printf("Could not find encoder for ' %s' \n",avcodec_get_name(codec_id));
		exit(1);
	}
	/*向媒体文件添加新流。*/
	ost->st=avformat_new_stream(oc,NULL);
	if(ost->st==NULL)
	{
		printf("Could not allocate stream \n");
		exit(1);
	}
	ost->st->id=oc->nb_streams-1;
	/*分配AvcodeContext并将其字段设置为默认值*/
	c=avcodec_alloc_context3(*codec);
	if(c==NULL)
	{
		printf("avcodec_alloc_context3 failed \n");
	}
	ost->enc=c;		
	switch((*codec)->type)
	{
		case AVMEDIA_TYPE_AUDIO:
			
			break;	
		case AVMEDIA_TYPE_VIDEO:/*视频流*/
		
			c->codec_id=codec_id;
			c->bit_rate=2500000;//比特率
			/*分辨率必须是2的倍数。*/
			c->width=width;
			c->height=height;
			/*时基：这是时间的基本单位（秒）
				其中帧时间戳被表示。对于固定fps内容，时基应为1/帧率，时间戳增量应与1相同*/
			ost->st->time_base=(AVRational){1,STREAM_FRAME_RATE};
			c->time_base=ost->st->time_base;
			c->gop_size=12;/*最多每12帧发射一帧*/
			c->pix_fmt=STREAM_PIX_FMT;/*图像格式*/
			if(c->codec_id == AV_CODEC_ID_MPEG2VIDEO)
			{
				/*
				为了测试，我们还添加了B帧
				*/
				c->max_b_frames=2;
			}
			if(c->codec_id == AV_CODEC_ID_MPEG1VIDEO)
			{
				/*
				需要避免使用某些系数溢出的宏块。
				这种情况不会发生在普通视频中，只会发生在这里
				色度平面的运动与亮度平面不匹配。*/
				c->mb_decision=2;
			}
			break;
		default:
			break;
	}
	/*有些格式希望流头是分开的。*/
	if(oc->oformat->flags & AVFMT_GLOBALHEADER)
	{
		c->flags|=AV_CODEC_FLAG_GLOBAL_HEADER;
	}
}
/*视频输出*/
static AVFrame *alloc_picture(enum AVPixelFormat pix_fmt, int width, int height)
{
	AVFrame *picture;
	int ret;
	/*
		分配AVFrame并将其字段设置为默认值。结果
		必须使用av_frame_free（）释放结构。
	*/
	picture=av_frame_alloc();
	if(picture==NULL)
	{
		return  NULL;
	}
	picture->format=pix_fmt;
	picture->width=width;
	picture->height=height;
	/*为帧数据分配缓冲区*/
	ret=av_frame_get_buffer(picture,32);/*缓冲区以32位对齐*/
	if(ret<0)
	{
		printf("Could not allocate frame data\n");/*无法分配帧数据*/
		exit(1);
	}
	return picture;
}

static void open_video(AVFormatContext *oc, AVCodec *codec, OutputStream *ost, AVDictionary *opt_arg)
{
	int ret;
	AVCodecContext *c=ost->enc;
	AVDictionary *opt=NULL;
	av_dict_copy(&opt,opt_arg, 0);
	/*初始化AvcodeContext以使用给定的AVCodec*/
	ret=avcodec_open2(c, codec,&opt);
	/*释放为AVDictionary结构分配的所有内存*以及所有键和值。*/
	av_dict_free(&opt);
	if(ret<0)
	{
		printf("could not open video codec :%s\n",av_err2str(ret));//无法打开视频编解码器
		exit(1);
	}
	/*视频输出*/
	ost->frame=alloc_picture(AV_PIX_FMT_YUV420P,c->width,c->height);
	if(ost->frame==NULL)
	{
		printf("could not allocate video frame\n");/*无法分配视频帧*/
		exit(1);
	}
	printf("ost->frame alloc success fmt=%d w=%d h=%d\n",c->pix_fmt,c->width,c->height);
	ost->tmp_frame=NULL;
	if(c->pix_fmt!=AV_PIX_FMT_YUV420P)
	{
		ost->tmp_frame=alloc_picture(AV_PIX_FMT_YUV420P,c->width, c->height);/*视频帧格式*/
		if(ost->tmp_frame==NULL)
		{
			printf("conld not allocate temporary picture\n");/*无法分配临时帧*/
			exit(1);
		}
	}
	/*根据提供的编解码器上下文中的值填充参数结构。*/
	ret=avcodec_parameters_from_context(ost->st->codecpar,c);
	if(ret)
	{
		printf("Could not copy the stream parameters\n");/*无法复制流参数*/
		exit(1);
	}

}
static void log_packet(const AVFormatContext * fmt_ctx, const AVPacket * pkt)
{
	AVRational *time_base=&fmt_ctx->streams[pkt->stream_index]->time_base;
    printf("pts:%s pts_time:%s dts:%s dts_time:%s duration:%s duration_time:%s stream_index:%d\n",
           									av_ts2str(pkt->pts), av_ts2timestr(pkt->pts, time_base),
           									av_ts2str(pkt->dts), av_ts2timestr(pkt->dts, time_base),
           									av_ts2str(pkt->duration), av_ts2timestr(pkt->duration, time_base),
           									pkt->stream_index);
										
}

/*写入数据*/
static int write_frame(AVFormatContext *fmt_ctx, const AVRational *time_base, AVStream *st, AVPacket *pkt)
{
	/*数据包中的有效时间字段（时间戳/持续时间）从一个时基转换为另一个时基。*/
	av_packet_rescale_ts(pkt,*time_base,st->time_base);
	pkt->stream_index=st->index;
	/*打印信息*/
	//log_packet(fmt_ctx, pkt);
	return av_interleaved_write_frame(fmt_ctx, pkt);/*将数据包写入输出媒体文件，确保正确的交织。*/
}
static int write_video_frame(AVFormatContext * oc, OutputStream * ost,AVFrame *frame)
{
	int ret;
	AVCodecContext *c;
	int got_packet=0;
	AVPacket pkt={0};
	if(frame==(void *)-1)return 1;
	c=ost->enc;
	/*使用默认值初始化数据包*/
	av_init_packet(&pkt);
	
	/*对一帧视频进行编码。*/
	ret=avcodec_encode_video2(c,&pkt,frame,&got_packet);
	if(ret<0)
	{
		printf("Error encoding video frame:%s\n",av_err2str(ret));//编码视频流错误
		exit(1);
	}
	/*
	printf("--------vidoe pkt.pts=%s\n",av_ts2str(pkt.pts));
	printf("------st.num=%d st.den=%d codec.num=%d codec.den=%d-------\n",ost->st->time_base.num,\
															  ost->st->time_base.den,\
															  c->time_base.num,\
															  c->time_base.den);
	*/
	if(got_packet)
	{

		ret=write_frame(oc,&c->time_base,ost->st,&pkt);/*写入流数据*/
	}
	else
	{
		ret=0;
	}
	if(ret<0)
	{
		printf("Error while writing video frame:%s\n",av_err2str(ret));/*写入流出错*/
		exit(1);
	}
	return (frame||got_packet)?0:1;
}
static AVFrame *get_video_frame(OutputStream *ost,IntputDev* input, int *got_pic)
{
	
	int ret,got_picture;
	AVCodecContext *c=ost->enc;
	AVFrame *ret_frame=NULL;
	/*在各自的时基中比较两个时间戳。*/
	if(av_compare_ts(ost->next_pts,c->time_base,STREAM_DURATION, (AVRational){1,1})>=0)
	{
		//return  (void*)-1;
	}
	/*确保帧数据可写，尽可能避免数据复制。*/
	if(av_frame_make_writable(ost->frame)<0)
	{
		exit(1);
	}
	/*此函数返回文件中存储的内容，并且不验证是否存在解码器的有效帧。*/
	if(av_read_frame(input->v_ifmtCtx,input->in_packet)>=0)
	{
		if(input->in_packet->stream_index == input->videoindex)
		{
			/*解码一帧视频数据。输入一个压缩编码的结构体AVPacket，输出一个解码后的结构体AVFrame*/
			ret=avcodec_decode_video2(input->pcodecCtx, input->pFrame,&got_picture,input->in_packet);
			*got_pic=got_picture;
			if(ret<0)
			{
				printf("Decode Error.\n");
				av_packet_unref(input->in_packet);
				return NULL;
			}
			if(got_picture)
			{
				sws_scale(input->img_convert_ctx, (const unsigned char * const *)input->pFrame->data,input->pFrame->linesize,0,input->pcodecCtx->height,input->pFrameYUV->data,input->pFrameYUV->linesize);
				sws_scale(input->img_convert_ctx, (const unsigned char * const *)input->pFrame->data,input->pFrame->linesize,0,input->pcodecCtx->height,ost->frame->data,ost->frame->linesize);

				pthread_mutex_lock(&fastmutex);//互斥锁上锁
				memcpy(rgb_buff,input->pFrameYUV->data[0],size);
				pthread_cond_broadcast(&cond);//广播唤醒所有线程
				pthread_mutex_unlock(&fastmutex);//互斥锁解锁
				ost->frame->pts=ost->next_pts++;

				//水印添加处理
				//frame->frame->format=AV_PIX_FMT_YUV420P;
				//AVFrame *frame_out=av_frame_alloc();
				//unsigned char *frame_buffer_out;
				//frame_buffer_out=(unsigned char *)av_malloc(size);
				//av_image_fill_arrays(frame_out->data,frame_out->linesize,frame_buffer_out,AV_PIX_FMT_YUV420P,width,height,32);
				//添加水印,调用libavfilter库实现
				//time_t sec;
				//sec=time(NULL);
				//struct tm* today = localtime(&sec);
				//char sys_time[64];
				//strftime(sys_time, sizeof(sys_time), "%Y/%m/%d %H\\:%M\\:%S", today);  
				//waterMark(ost->frame,frame_out,width,height,sys_time);
				//yuv420p，y表示亮度，uv表示像素颜色
				//ost->frame=frame_out;
				//ost->frame->pts=ost->next_pts++;

				ret_frame=ost->frame;
			}
		}
		av_packet_unref(input->in_packet);
	}
	return ret_frame;
}
static void close_stream(AVFormatContext * oc, OutputStream * ost)
{
	avcodec_free_context(&ost->enc);
	av_frame_free(&ost->frame);
	av_frame_free(&ost->tmp_frame);
	sws_freeContext(ost->sws_ctx);
	swr_free(&ost->swr_ctx);
}

int main()
{
	/*创建摄像头采集线程*/
	pthread_t pthid[2];
    pthread_create(&pthid[0],NULL,Video_CollectImage, NULL);
	pthread_detach(pthid[0]);/*设置分离属性*/
	sleep(1);
	while(1)
	{
		if(width!=0 && height!=0 && size!=0)break;
		if(video_flag==0)return 0;
	}
	printf("image:%d * %d,%d\n",width,height,size);
	unsigned char *rgb_data=malloc(size);
	/*创建mp4视频编码线程*/
	pthread_create(&pthid[1],NULL,Video_savemp4, NULL);
	pthread_detach(pthid[1]);/*设置分离属性*/
	int count=0;
	mp4_decode_stat=1;
	pause();
    pthread_mutex_destroy(&fastmutex);/*销毁互斥锁*/
    pthread_cond_destroy(&cond);/*销毁条件变量*/
	free(rgb_buff);
	free(rgb_data);
	return 0;
}

void *Video_CollectImage(void *arg)
{
	int res=0;
	AVFrame *Input_pFrame=NULL;
	AVFrame *Output_pFrame=NULL;
	printf("pth:%s\n",avcodec_configuration());
	/*注册设备*/
	avdevice_register_all();
	/*查找输入格式*/
	AVInputFormat *ifmt=av_find_input_format("video4linux2");
	if(ifmt==NULL)
	{
		printf("av_find_input_format failed\n");
		video_flag=0;
		return 0;
	}
	/*打开输入流并读取头部信息*/
	AVFormatContext *ps=NULL;
	//分配一个AVFormatContext。
	ps=avformat_alloc_context();	
	res=avformat_open_input(&ps,VIDEO_DEV,ifmt,NULL);
	if(res)
	{
		printf("open input failed\n");
		video_flag=0;
		return 0;
	}
	/*查找流信息*/
	res=avformat_find_stream_info(ps,NULL);
	if(res)
	{
		printf("find stream failed\n");
		video_flag=0;
		return 0;
	}
	/*打印有关输入或输出格式信息*/
	av_dump_format(ps, 0, "video4linux2", 0);
	/*寻找视频流*/
	int videostream=-1;
	videostream=av_find_best_stream(ps,AVMEDIA_TYPE_VIDEO,-1,-1,NULL,0);
	printf("videostram=%d\n",videostream);
	/*寻找编解码器*/
	AVCodec *video_avcodec=NULL;/*保存解码器信息*/
	AVStream *stream = ps->streams[videostream];
	AVCodecContext *context=stream->codec;
	video_avcodec=avcodec_find_decoder(context->codec_id);
	if(video_avcodec==NULL)
	{
		printf("find video decodec failed\n");
		video_flag=0;
		return 0;
	}	
	/*初始化音视频解码器*/
	res=avcodec_open2(context,video_avcodec,NULL);
	if(res)
	{
		printf("avcodec_open2 failed\n");
		video_flag=0;
		return 0;
	}	
	AVPacket *packet=av_malloc(sizeof(AVPacket));/*分配包*/
	AVFrame *frame=av_frame_alloc();/*分配视频帧*/
	AVFrame *frameyuv=av_frame_alloc();/*申请YUV空间*/
	/*分配空间，进行图像转换*/
	width=context->width;
	height=context->height;
	int fmt=context->pix_fmt;/*流格式*/
	size=av_image_get_buffer_size(AV_PIX_FMT_YUV420P,width,height,16);
	unsigned char *buff=NULL;
	printf("w=%d,h=%d,size=%d\n",width,height,size);	
	buff=av_malloc(size);
	rgb_buff=malloc(size);//保存RGB颜色数据
	/*存储一帧图像数据*/
	av_image_fill_arrays(frameyuv->data,frameyuv->linesize,buff,AV_PIX_FMT_YUV420P,width,height, 16);	
	/*转换上下文，使用sws_scale（）执行缩放/转换操作。*/
	struct SwsContext *swsctx=sws_getContext(width,height, fmt,width,height, AV_PIX_FMT_YUV420P,SWS_BICUBIC,NULL,NULL,NULL);
	/*视频输入流信息*/
	video_input.img_convert_ctx=swsctx;//格式转换上下文
	video_input.in_packet=packet;//数据包
	video_input.pcodecCtx=context;
	video_input.pCodec=video_avcodec;/*保存解码器信息*/
	video_input.v_ifmtCtx=ps;//输入流并读取头部信息
	video_input.videoindex=videostream;/*视频流*/
	video_input.pFrame=frame;/*视频帧*/
	video_input.pFrameYUV=frameyuv;/*申请YUV空间*/

	//水印添加处理
	frameyuv->width=width;
	frameyuv->height=height;
	frameyuv->format=AV_PIX_FMT_YUV420P;
	AVFrame *frame_out=av_frame_alloc();
	unsigned char *frame_buffer_out;
	frame_buffer_out=(unsigned char *)av_malloc(size);
	av_image_fill_arrays(frame_out->data,frame_out->linesize,frame_buffer_out,AV_PIX_FMT_YUV420P,width,height,16);
	/*读帧*/
	char *p=NULL;

	int go=0;
	int Framecount=0;
	time_t sec,sec2;
	char sys_time[64]; 
	while(video_flag)
	{
		if(!mp4_decode_stat)
		{
			res=av_read_frame(ps,packet);//读取数据
			if(res>=0)
			{
				if(packet->stream_index == AVMEDIA_TYPE_VIDEO)//视频流
				{
					/*解码一帧视频数据。输入一个压缩编码的结构体AVPacket，输出一个解码后的结构体AVFrame*/
					res=avcodec_decode_video2(ps->streams[videostream]->codec,frame,&go,packet);
					if(res<0)
					{
						printf("avcodec_decode_video2 failed\n");
						break;
					}
					if(go)
					{
						/*转换像素的函数*/
						sws_scale(swsctx,(const uint8_t * const*)frame->data,frame->linesize,0,context->height,frameyuv->data,frameyuv->linesize);
						
						//添加水印,调用libavfilter库实现
						
						sec=time(NULL);
						if(sec!=sec2)
						{
							struct tm* today = localtime(&sec);
							strftime(sys_time, sizeof(sys_time), "%Y/%m/%d %H\\:%M\\:%S", today);  
						}
						waterMark(frameyuv,frame_out,width,height,sys_time);
						//yuv420p，y表示亮度，uv表示像素颜色
						p=frame_buffer_out;
						memcpy(p,frame_out->data[0],frame_out->height*frame_out->width);//y,占用空间w*h
						p+=frame_out->width*frame_out->height;
						memcpy(p,frame_out->data[1],frame_out->height/2*frame_out->width/2);//u,占用空间(w/2)*(h/2)
						p+=frame_out->height/2*frame_out->width/2;
						memcpy(p,frame_out->data[2],frame_out->height/2*frame_out->width/2);//v,占用空间(w/2)*(h/2)
						p+=frame_out->height/2*frame_out->width/2;
						pthread_mutex_lock(&fastmutex);//互斥锁上锁
						memcpy(rgb_buff,frame_buffer_out,size);
						pthread_cond_broadcast(&cond);//广播唤醒所有线程
						pthread_mutex_unlock(&fastmutex);//互斥锁解锁
					}
				}
			}
		}
	}
	sws_freeContext(swsctx);/*释放上下文*/
	av_frame_free(&frameyuv);/*释放YUV空间*/
	av_packet_unref(packet);/*释放包*/
	av_frame_free(&frame);/*释放视频帧*/
	avformat_close_input(&ps);/*关闭流*/

	sws_freeContext(video_input.img_convert_ctx);
	avcodec_close(video_input.pcodecCtx);
	av_free(video_input.pFrameYUV);
	av_free(video_input.pFrame);
	avformat_close_input(&video_input.v_ifmtCtx);
	video_flag=0;
	pthread_exit(NULL);	
}
/*MP4格式数据保存*/
void *Video_savemp4(void*arg)
{
	while(1)
	{
		if(mp4_decode_stat)
		{
			int res;
			AVFormatContext *oc=NULL;
			AVDictionary *opt=NULL;
			/* 创建的AVFormatContext结构体。*/
			avformat_alloc_output_context2(&oc,NULL,"flv",NULL);//通过文件名创建
			if(oc==NULL)
			{
				printf("为输出格式分配AVFormatContext失败\n");
				avformat_alloc_output_context2(&oc,NULL,"flv",NULL);//通过文件名创建
				return 0;
			}
			if(oc==NULL)return (void*)1;
			/*输出流信息*/
			AVOutputFormat *ofmt=oc->oformat;
			printf("ofmt->video_codec=%d\n",ofmt->video_codec);
			int have_video=1;
			int encode_video=0;
			OutputStream video_st={0};
			if(ofmt->video_codec !=AV_CODEC_ID_NONE)
			{
				/*添加一个输出流*/
				add_stream(&video_st,oc,&video_input.pCodec,ofmt->video_codec);
				have_video=1;
				encode_video=1;
			}
			printf("w=%d,h=%d,size=%d\n",width,height,size);
			/*视频帧处理*/
			if(have_video)open_video(oc,video_input.pCodec,&video_st,opt);
			printf("打开输出文件成功\r\n");
			/*打印有关输入或输出格式信息*/
			printf("file_name=%s\n",file_name);
			av_dump_format(oc, 0,file_name,1);
			if(!(ofmt->flags & AVFMT_NOFILE))
			{
				/*打开输出文件，成功之后创建的AVFormatContext结构体*/
				res=avio_open(&oc->pb,file_name,AVIO_FLAG_WRITE);
				if(res<0)
				{
					printf("%s open failed :%s\n",file_name,av_err2str(res));
					return  (void*)1;
				}
			}
			/*写入流数据头*/
			res=avformat_write_header(oc,&opt);
			if(res<0)
			{
				printf("open output faile:%s\n",av_err2str(res));
				return  (void*)1;
			}
			if(res<0)
			{
				printf("open output faile:%s\n",av_err2str(res));
				return  (void*)1;
			}
			int got_pic;
			while(encode_video)
			{
				/*获取流数据*/
				AVFrame *frame=get_video_frame(&video_st,&video_input,&got_pic);
				if(!got_pic || frame==NULL)
				{
					usleep(100);
					continue;
				}
				encode_video=!write_video_frame(oc,&video_st,frame);
			}
			/*将流数据写入输出媒体文件并释放文件私有数据。*/
			av_write_trailer(oc);
			/*关闭AVIOContext*s访问的资源，释放它，并将指向它的指针设置为NULL*/
			if(!(ofmt->flags & AVFMT_NOFILE))avio_closep(&oc->pb);
			/*释放AVFormatContext及其所有流。*/
			avformat_free_context(oc);
			/*关闭流*/
			if(have_video)close_stream(oc, &video_st);
			mp4_decode_stat=0;
		}
	}
}
