// /**
//  * 最简单的基于FFmpeg的推流器（推送RTMP）
//  * Simplest FFmpeg Streamer (Send RTMP)
//  * 
//  * 雷霄骅 Lei Xiaohua
//  * leixiaohua1020@126.com
//  * 中国传媒大学/数字电视技术
//  * Communication University of China / Digital TV Technology
//  * http://blog.csdn.net/leixiaohua1020
//  * 
//  * 本例子实现了推送本地视频至流媒体服务器（以RTMP为例）。
//  * 是使用FFmpeg进行流媒体推送最简单的教程。
//  *
//  * This example stream local media files to streaming media 
//  * server (Use RTMP as example). 
//  * It's the simplest FFmpeg streamer.
//  * 
//  */
 
// #include <stdio.h>
 
// #define __STDC_CONSTANT_MACROS
 
// #ifdef _WIN32
// //Windows
// extern "C"
// {
// #include "libavformat/avformat.h"
// #include "libavutil/mathematics.h"
// #include "libavutil/time.h"
// };
// #else
// //Linux...
// #ifdef __cplusplus
// extern "C"
// {
// #endif
// #include <libavformat/avformat.h>
// #include <libavutil/mathematics.h>
// // #include "libavutil/mathematics.h"
// #include <libavutil/time.h>
// #ifdef __cplusplus
// };
// #endif
// #endif
 
// int main(int argc, char* argv[])
// {
// 	AVOutputFormat *ofmt = NULL;
// 	//输入对应一个AVFormatContext，输出对应一个AVFormatContext
// 	//（Input AVFormatContext and Output AVFormatContext）
// 	AVFormatContext *ifmt_ctx = NULL, *ofmt_ctx = NULL;
// 	AVPacket pkt;
// 	const char *in_filename, *out_filename;
// 	int ret, i;
// 	int videoindex=-1;
// 	int frame_index=0;
// 	int64_t start_time=0;
// 	//in_filename  = "cuc_ieschool.mov";
// 	//in_filename  = "cuc_ieschool.mkv";
// 	//in_filename  = "cuc_ieschool.ts";
// 	//in_filename  = "cuc_ieschool.mp4";
// 	//in_filename  = "cuc_ieschool.h264";
// 	in_filename  = "../data/video2.mp4";//输入URL（Input file URL）
// 	//in_filename  = "shanghai03_p.h264";
	
// 	out_filename = "rtmp://localhost:1935/livestream";//输出 URL（Output URL）[RTMP]
// 	//out_filename = "rtp://233.233.233.233:6666";//输出 URL（Output URL）[UDP]
 
// 	av_register_all();
// 	//Network
// 	avformat_network_init();
// 	//输入（Input）
// 	if ((ret = avformat_open_input(&ifmt_ctx, in_filename, 0, 0)) < 0) {
// 		printf( "Could not open input file.");
// 		goto end;
// 	}
// 	if ((ret = avformat_find_stream_info(ifmt_ctx, 0)) < 0) {
// 		printf( "Failed to retrieve input stream information");
// 		goto end;
// 	}
 
// 	for(i=0; i<ifmt_ctx->nb_streams; i++) 
// 		if(ifmt_ctx->streams[i]->codec->codec_type==AVMEDIA_TYPE_VIDEO){
// 			videoindex=i;
// 			break;
// 		}
 
// 	av_dump_format(ifmt_ctx, 0, in_filename, 0);
 
// 	//输出（Output）
	
// 	avformat_alloc_output_context2(&ofmt_ctx, NULL, "flv", out_filename); //RTMP
// 	//avformat_alloc_output_context2(&ofmt_ctx, NULL, "mpegts", out_filename);//UDP
 
// 	if (!ofmt_ctx) {
// 		printf( "Could not create output context\n");
// 		ret = AVERROR_UNKNOWN;
// 		goto end;
// 	}
// 	ofmt = ofmt_ctx->oformat;
// 	for (i = 0; i < ifmt_ctx->nb_streams; i++) {
// 		//根据输入流创建输出流（Create output AVStream according to input AVStream）
// 		AVStream *in_stream = ifmt_ctx->streams[i];
// 		AVStream *out_stream = avformat_new_stream(ofmt_ctx, in_stream->codec->codec);
// 		if (!out_stream) {
// 			printf( "Failed allocating output stream\n");
// 			ret = AVERROR_UNKNOWN;
// 			goto end;
// 		}
// 		//复制AVCodecContext的设置（Copy the settings of AVCodecContext）
// 	// ret = avcodec_parameters_from_context(in_stream->codecpar,in_stream->codecpar->codec);

// /**
//  * Fill the codec context based on the values from the supplied codec
//  * parameters. Any allocated fields in codec that have a corresponding field in
//  * par are freed and replaced with duplicates of the corresponding field in par.
//  * Fields in codec that do not have a counterpart in par are not touched.
//  *
//  * @return >= 0 on success, a negative AVERROR code on failure.
//  */
// // int avcodec_parameters_to_context(AVCodecContext *codec,
//                                 //   const AVCodecParameters *par);

// 		ret = avcodec_copy_context(out_stream->codec, in_stream->codec);
// 		if (ret < 0) {
// 			printf( "Failed to copy context from input to output stream codec context\n");
// 			goto end;
// 		}
// 		out_stream->codec->codec_tag = 0;
// 		if (ofmt_ctx->oformat->flags & AVFMT_GLOBALHEADER) {
//             // out_stream->codec->flags |= CODEC_FLAG_GLOBAL_HEADER;
//             out_stream->codec->flags |= AV_CODEC_FLAG_GLOBAL_HEADER;
//         }
			
// 	}
// 	//Dump Format------------------
// 	av_dump_format(ofmt_ctx, 0, out_filename, 1);
// 	//打开输出URL（Open output URL）
// 	if (!(ofmt->flags & AVFMT_NOFILE)) {
// 		ret = avio_open(&ofmt_ctx->pb, out_filename, AVIO_FLAG_WRITE);
// 		if (ret < 0) {
// 			printf( "Could not open output URL '%s'", out_filename);
// 			goto end;
// 		}
// 	}
// 	//写文件头（Write file header）
// 	ret = avformat_write_header(ofmt_ctx, NULL);
// 	if (ret < 0) {
// 		printf( "Error occurred when opening output URL\n");
// 		goto end;
// 	}
 
// 	start_time=av_gettime();
// 	while (1) {
// 		AVStream *in_stream, *out_stream;
// 		//获取一个AVPacket（Get an AVPacket）
// 		ret = av_read_frame(ifmt_ctx, &pkt);
// 		if (ret < 0)
// 			break;
// 		//FIX：No PTS (Example: Raw H.264)
// 		//Simple Write PTS
// 		if(pkt.pts==AV_NOPTS_VALUE){
// 			//Write PTS
// 			AVRational time_base1=ifmt_ctx->streams[videoindex]->time_base;
// 			//Duration between 2 frames (us)
// 			int64_t calc_duration=(double)AV_TIME_BASE/av_q2d(ifmt_ctx->streams[videoindex]->r_frame_rate);
// 			//Parameters
// 			pkt.pts=(double)(frame_index*calc_duration)/(double)(av_q2d(time_base1)*AV_TIME_BASE);
// 			pkt.dts=pkt.pts;
// 			pkt.duration=(double)calc_duration/(double)(av_q2d(time_base1)*AV_TIME_BASE);
// 		}
// 		//Important:Delay
// 		if(pkt.stream_index==videoindex){
// 			AVRational time_base=ifmt_ctx->streams[videoindex]->time_base;
// 			AVRational time_base_q={1,AV_TIME_BASE};
// 			int64_t pts_time = av_rescale_q(pkt.dts, time_base, time_base_q);
// 			int64_t now_time = av_gettime() - start_time;
// 			if (pts_time > now_time)
// 				av_usleep(pts_time - now_time);
 
// 		}
 
// 		in_stream  = ifmt_ctx->streams[pkt.stream_index];
// 		out_stream = ofmt_ctx->streams[pkt.stream_index];
// 		/* copy packet */
// 		//转换PTS/DTS（Convert PTS/DTS）
// 		pkt.pts = av_rescale_q_rnd(pkt.pts, in_stream->time_base, out_stream->time_base, (AVRounding)(AV_ROUND_NEAR_INF|AV_ROUND_PASS_MINMAX));
// 		pkt.dts = av_rescale_q_rnd(pkt.dts, in_stream->time_base, out_stream->time_base, (AVRounding)(AV_ROUND_NEAR_INF|AV_ROUND_PASS_MINMAX));
// 		pkt.duration = av_rescale_q(pkt.duration, in_stream->time_base, out_stream->time_base);
// 		pkt.pos = -1;
// 		//Print to Screen
// 		if(pkt.stream_index==videoindex){
// 			printf("Send %8d video frames to output URL\n",frame_index);
// 			frame_index++;
// 		}
// 		//ret = av_write_frame(ofmt_ctx, &pkt);
// 		ret = av_interleaved_write_frame(ofmt_ctx, &pkt);
 
// 		if (ret < 0) {
// 			printf( "Error muxing packet\n");
// 			break;
// 		}
		
// 		av_free_packet(&pkt);
		
// 	}
// 	//写文件尾（Write file trailer）
// 	av_write_trailer(ofmt_ctx);
// end:
// 	printf("end \n");
// 	avformat_close_input(&ifmt_ctx);
// 	/* close output */
// 	if (ofmt_ctx && !(ofmt->flags & AVFMT_NOFILE))
// 		avio_close(ofmt_ctx->pb);
// 	avformat_free_context(ofmt_ctx);
// 	if (ret < 0 && ret != AVERROR_EOF) {
// 		printf( "Error occurred.\n");
// 		return -1;
// 	}
// 	return 0;
// }
 

/*
 * Copyright(C) 2022. Huawei Technologies Co.,Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <iostream>
#include <map>
#include <fstream>
#include "unistd.h"
#include <memory>
#include <queue>
#include <thread>
#include "boost/filesystem.hpp"

#include "MxBase/DeviceManager/DeviceManager.h"
#include "MxBase/DvppWrapper/DvppWrapper.h"
#include "MxBase/MemoryHelper/MemoryHelper.h"

#include "MxBase/E2eInfer/ImageProcessor/ImageProcessor.h"
#include "MxBase/E2eInfer/VideoDecoder/VideoDecoder.h"
#include "MxBase/E2eInfer/VideoEncoder/VideoEncoder.h"
#include "MxBase/E2eInfer/DataType.h"

#include "MxBase/MxBase.h"
#include "MxBase/Log/Log.h"

#include "BlockingQueue.h"

extern "C"
{
#include <libavformat/avformat.h>
}

namespace
{
    using namespace MxBase;
    const uint32_t SRC_WIDTH = 1920;
    const uint32_t SRC_HEIGHT = 1080;
    const uint32_t MAX_WIDTH = 3840;
    const uint32_t MAX_HEIGHT = 2160;
    const uint32_t MAX_FRAME_COUNT = 5000;
    const uint32_t TARGET_FRAME_COUNT = 400;
    const uint32_t MS_TIMEOUT = 2000;
    const uint32_t DEVICE_ID = 0;
    const uint32_t CHANNEL_ID = 1;
    const uint32_t TIME_QUEUE = 500; // ms
    const uint32_t EOS_WAITE = 5;
}
namespace fs = boost::filesystem;

struct FrameImage
{
    Image image; // video Image Class
    uint32_t frameId = 0;
    uint32_t channelId = 0;
};
namespace
{
    // 内存队列
    BlockingQueue<FrameImage> frameImageQueue;
    AVFormatContext *pFormatCtx = nullptr;
    BlockingQueue<FrameImage> encodeQueue;
    
    bool stop_flag = false;
}

// ffmpeg 拉流
AVFormatContext *CreateFormatContext(std::string filePath)
{
    LogInfo << "start to CreateFormatContext!";
    // creat message for stream pull
    AVFormatContext *formatContext = nullptr;
    AVDictionary *options = nullptr;

    LogInfo << "start to avformat_open_input!";
    int ret = avformat_open_input(&formatContext, filePath.c_str(), nullptr, &options);
    if (options != nullptr)
    {
        av_dict_free(&options);
    }
    if (ret != 0)
    {
        LogError << "Couldn`t open input stream " << filePath.c_str() << " ret=" << ret;
        return nullptr;
    }
    ret = avformat_find_stream_info(formatContext, nullptr);
    if (ret != 0)
    {
        LogError << "Couldn`t open input stream information";
        return nullptr;
    }
    return formatContext;
}

// 获取H264中的帧
void GetFrame(AVPacket &pkt, FrameImage &frameImage, AVFormatContext *pFormatCtx)
{
    av_init_packet(&pkt);
    int ret = av_read_frame(pFormatCtx, &pkt);
    if (ret != 0)
    {
        LogInfo << "[StreamPuller] channel Read frame failed, continue!";
        if (ret = +AVERROR_EOF)
        {
            LogInfo << "[StreamPuller] channel StreamPuller is EOF, over!";
            return;
        }
        return;
    }
    else
    {
        if (pkt.size <= 0)
        {
            LogError << "Invalid pkt.size: " << pkt.size;
            return;
        }

        // send to the device
        auto hostDeleter = [](void *dataPtr) -> void {};
        MemoryData data(pkt.size, MemoryData::MEMORY_HOST);
        MemoryData src((void *)(pkt.data), pkt.size, MemoryData::MEMORY_HOST);
        APP_ERROR ret = MemoryHelper::MxbsMallocAndCopy(data, src);
        if (ret != APP_ERR_OK)
        {
            LogError << "MxbsMallocAndCopy failed!";
        }
        std::shared_ptr<uint8_t> imageData((uint8_t *)data.ptrData, hostDeleter);

        Image subImage(imageData, pkt.size);
        frameImage.image = subImage;

        LogDebug << "'channelId = " << frameImage.channelId << ", frameId = " << frameImage.frameId << " , dataSize = " << frameImage.image.GetDataSize();

        av_packet_unref(&pkt);
    }
    return;
}

// 视频解码回调 userData为videoDecoder->Decode()传入的自定义数据，参阅文档：API参考（C++） V2>类参考>VideoDecoder>Decode
APP_ERROR CallBackVdec(Image &decodedImage, uint32_t channelId, uint32_t frameId, void *userData)
{
    FrameImage frameImage;
    frameImage.image = decodedImage;
    frameImage.channelId = channelId;
    frameImage.frameId = frameId;

    BlockingQueue<FrameImage> *p_frameImageQueue = (BlockingQueue<FrameImage> *)userData;
    p_frameImageQueue->Push(frameImage);
    LogInfo << "frameId(" << frameImage.frameId << ") decoded successfully.";
    return APP_ERR_OK;
}

// 视频编码回调 userData为videoEncoder->Encode()传入的自定义数据，参阅文档：API参考（C++） V2>类参考>VideoEncoder>Encode
APP_ERROR CallBackVenc(std::shared_ptr<uint8_t> &outDataPtr, uint32_t &outDataSize, uint32_t &channelId,
                       uint32_t &frameId, void *userData)
{
    Image image(outDataPtr, outDataSize, -1, Size(MAX_WIDTH, MAX_HEIGHT));
    FrameImage frameImage;
    frameImage.image = image;
    frameImage.channelId = channelId;
    frameImage.frameId = frameId;

    BlockingQueue<FrameImage> *p_encodeQueue = (BlockingQueue<FrameImage> *)userData;
    p_encodeQueue->Push(frameImage);
    LogInfo << "frameId(" << frameImage.frameId << ") encoded successfully.";
    return APP_ERR_OK;
}

// 视频流解码线程
void VdecThread(size_t frameCount)
{
    AVPacket pkt;
    uint32_t frameId = 0;
    int32_t channelId = CHANNEL_ID;
    // 解码器参数
    VideoDecodeConfig config;
    VideoDecodeCallBack cPtr = CallBackVdec;
    config.width = SRC_WIDTH;
    config.height = SRC_HEIGHT;
    config.callbackFunc = cPtr;
    config.skipInterval = 0; // 跳帧控制

    std::shared_ptr<VideoDecoder> videoDecoder = std::make_shared<VideoDecoder>(config, DEVICE_ID, channelId);
    while (!stop_flag)
    {
        if (frameId >= TARGET_FRAME_COUNT)
        {
            return;
        }
        Image subImage;
        FrameImage frame;
        frame.channelId = 0;
        frame.frameId = frameId;
        frame.image = subImage;

        GetFrame(pkt, frame, pFormatCtx);
        APP_ERROR ret = videoDecoder->Decode(frame.image.GetData(), frame.image.GetDataSize(), frameId, &frameImageQueue);
        if (ret != APP_ERR_OK)
        {
            LogError << "videoDecoder Decode failed. ret is: " << ret;
        }
        frameId++;
    }
}

// 图像编码h264线程
void VencThread()
{
    // 编码器参数
    VideoEncodeConfig vEConfig;
    VideoEncodeCallBack cEPtr = CallBackVenc;
    vEConfig.callbackFunc = cEPtr;
    vEConfig.width = 1280;
    vEConfig.height = 720;
    // 用户可自定义编码参数
    vEConfig.keyFrameInterval = 50;
    vEConfig.srcRate = 25;
    vEConfig.rcMode = 0;
    vEConfig.maxBitRate = 2000;
    vEConfig.ipProp = 50;
    std::shared_ptr<VideoEncoder> videoEncoder = std::make_shared<VideoEncoder>(vEConfig, DEVICE_ID);

    int frameCount = 0;
    while (!stop_flag)
    {
        if (frameCount >= TARGET_FRAME_COUNT)
        {
            return;
        }
        
        if (frameImageQueue.GetSize() > 0)
        {
            FrameImage frameImage;
            APP_ERROR ret = frameImageQueue.Pop(frameImage, TIME_QUEUE);
            if (ret != APP_ERR_OK)
            {
                LogError << "frameImageQueue pop failed" << ret;
                continue;
            }
            videoEncoder->Encode(frameImage.image, frameImage.frameId, &encodeQueue);
            frameCount += 1;
        }
    }
}

void WriteThread(std::string saveFilePath)
{
    FILE *fp = fopen(saveFilePath.c_str(), "wb");
    if (fp == nullptr)
    {
        LogError << "Failed to open file.";
        return;
    }

    int frameCount = 0;
    int empty_count = 0;
    while (!stop_flag)
    {
        if (empty_count > EOS_WAITE || frameCount >= TARGET_FRAME_COUNT)
        {
            stop_flag = true;
            LogInfo << "process and write frame:" << frameCount;
        }
        
        FrameImage encodeTemp;
        APP_ERROR ret = encodeQueue.Pop(encodeTemp, TIME_QUEUE);
        if (ret != APP_ERR_OK)
        {
            LogError << "encodeQueue pop failed:" << ret;
            if (encodeQueue.GetSize() == 0)
            {
                empty_count++;
            }
            continue;
        }

        std::shared_ptr<uint8_t> data_sp = encodeTemp.image.GetData();
        void *data_p = data_sp.get();
        if (fwrite(data_p, encodeTemp.image.GetDataSize(), 1, fp) != 1)
        {
            LogInfo << "write frame to file fail";
        }

        frameCount++;
    }
}

void PullStream(std::string filePath)
{
    // av_register_all();
    avformat_network_init();
    pFormatCtx = avformat_alloc_context();
    pFormatCtx = CreateFormatContext(filePath);
    av_dump_format(pFormatCtx, 0, filePath.c_str(), 0);
}

int main(int argc, char *argv[])
{
    if (argc <= 1)
    {
        LogWarn << "Please input image path, such as './video_sample test.264'.";
        return APP_ERR_OK;
    }
    APP_ERROR ret;

    // global init
    ret = MxInit();
    if (ret != APP_ERR_OK)
    {
        LogError << "MxInit failed, ret=" << ret << ".";
    }

    std::string filePath = argv[1];
    std::string outPath = "out_" + filePath;
    PullStream(filePath);
    std::thread threadVdec(VdecThread, TARGET_FRAME_COUNT);
    std::thread threadVenc(VencThread);
    std::thread threadWrite(WriteThread, outPath);

    threadVdec.join();
    threadVenc.join();
    threadWrite.join();

    LogInfo << "ALL DONE";
}
 