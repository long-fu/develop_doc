#include <stdio.h>


extern "C"
{
#include "libavformat/avformat.h"
#include "libavutil/mathematics.h"
#include "libavutil/time.h"
};

int avError(int errNum);

int main(int argc, char* argv[]){
    //输入文件
    const char *fileAddress = "cuc_ieschool.flv";
    //推流地址
    const char *rtmpAddress = "rtmp://video-center.alivecdn.com/record/1513?vhost=im-broadcast17.sdp.101.com&auth_key=1843197296-0-0-f6bac834f49cce39c3fd5f3b7ec0bcb1";

    //注册所有库
    av_register_all();
    //初始化网络库
    avformat_network_init();


    ////////////////////////////////////////////////////////////////
    //                       输入流处理部分                      ///
    ////////////////////////////////////////////////////////////////
    AVFormatContext *ictx = NULL;
    //打开文件
    int ret = avformat_open_input(&ictx, fileAddress, 0, NULL);
    if (ret < 0)
    {
        return avError(ret);
    }
    std::cout << "avformat_open_input succeeded" << std::endl;
    //获取流信息
    ret = avformat_find_stream_info(ictx, 0);
    if (ret != 0)
    {
        return avError(ret);
    }
    //打印视频信息
    av_dump_format(ictx, 0, fileAddress, 0);


    ////////////////////////////////////////////////////////////////
    //                       输出流处理部分                      ///
    ////////////////////////////////////////////////////////////////
    AVFormatContext *octx = NULL;
    //创建输出上下文
    ret = avformat_alloc_output_context2(&octx, NULL, "flv", rtmpAddress); 
    if (ret < 0) {
        return avError(ret);
    }
    // std::cout << "avformat_alloc_output_context2 succeeded" << std::endl;
    //配置输出流
    for (int i = 0; i < ictx->nb_streams; i++) {
        //创建一个新的流
        AVStream *outStream = avformat_new_stream(octx, ictx->streams[i]->codec->codec);
        if (!outStream) {
            return avError(0);
        }
        //复制配置信息
        ret = avcodec_parameters_copy(outStream->codecpar, ictx->streams[i]->codecpar);
        if (ret < 0) {
            return avError(ret);
        }
        outStream->codec->codec_tag = 0;
    }
    //打印输出流的信息
    av_dump_format(octx, 0, rtmpAddress, 1);


    ////////////////////////////////////////////////////////////////
    //                         准备推流                          ///
    ////////////////////////////////////////////////////////////////
    //打开io
    ret = avio_open(&octx->pb, rtmpAddress, AVIO_FLAG_WRITE);
    if (ret < 0) {
        avError(ret);
    }
    //写入头部信息
    ret = avformat_write_header(octx, NULL);
    if ( ret < 0) {
        avError(ret);
    }
    // std::cout << "avformat_write_header succeeded" << std::endl;
    //推流每一帧数据
    AVPacket avPacket;
    long long startTime = av_gettime();
    while (true)
    {
        ret = av_read_frame(ictx, &avPacket);
        if (ret < 0 )
        {
            break;
        }
        // std::cout << avPacket.pts << " " << std::flush;
        //计算转换时间戳
        //获取时间基数
        AVRational itime = ictx->streams[avPacket.stream_index]->time_base;
        AVRational otime = octx->streams[avPacket.stream_index]->time_base;
        avPacket.pts = av_rescale_q_rnd(avPacket.pts, itime, otime, (AVRounding)(AV_ROUND_NEAR_INF | AV_ROUND_NEAR_INF));
        avPacket.dts = av_rescale_q_rnd(avPacket.dts, itime, otime, (AVRounding)(AV_ROUND_NEAR_INF | AV_ROUND_NEAR_INF));
        //到这一帧经历了多长时间
        avPacket.duration = av_rescale_q_rnd(avPacket.duration, itime, otime, (AVRounding)(AV_ROUND_NEAR_INF | AV_ROUND_NEAR_INF));
        avPacket.pos = -1;
        //视频帧推送速度
        if (ictx->streams[avPacket.stream_index]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO)
        {
            AVRational tb = ictx->streams[avPacket.stream_index]->time_base;
            //已经过去的时间
            long long now = av_gettime() - startTime;
            long long dts = 0;
            dts = avPacket.dts * (1000 * 1000 * av_q2d(tb));
            if (dts > now)
            {
                av_usleep(dts - now);
            }
        }
        ret = av_interleaved_write_frame(octx, &avPacket);
        if (ret < 0)
        {
            break;
        }
    }
    

    // std::cin.get();
    return 0;
}

int avError(int errNum) {
    char buf[1024];
    //获取错误信息
    av_strerror(errNum, buf, sizeof(buf));
    // std::cout << " failed! " << buf << std::endl;
    return -1;
}