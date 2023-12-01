
https://github.com/0voice/audio_video_streaming/tree/main

# 视频数据处理：RGB、YUV像素数据处理

https://github.com/0voice/audio_video_streaming/blob/main/article/020-%E8%A7%86%E9%9F%B3%E9%A2%91%E6%95%B0%E6%8D%AE%E5%A4%84%E7%90%86%EF%BC%9ARGB%E3%80%81YUV%E5%83%8F%E7%B4%A0%E6%95%B0%E6%8D%AE%E5%A4%84%E7%90%86.md

# 视音频数据处理：H.264视频码流解析


https://github.com/0voice/audio_video_streaming/blob/main/article/022-%E8%A7%86%E9%9F%B3%E9%A2%91%E6%95%B0%E6%8D%AE%E5%A4%84%E7%90%86%EF%BC%9AH.264%E8%A7%86%E9%A2%91%E7%A0%81%E6%B5%81%E8%A7%A3%E6%9E%90.md

----------------

# 视频相关参数

**帧率**: 帧率（Frame rate）是用于测量显示帧数的量度。所谓的测量单位为每秒显示帧数(Frames per Second，简称：FPS)或“赫兹”（Hz）

**分辨率**: 显示分辨率（屏幕分辨率）是屏幕图像的精密度，是指显示器所能显示的像素有多少。

**DTS(Decoding Time Stamp)**: 即解码时间戳，是解码器进行解码时相对于SCR(系统参考时间)的时间戳。它主要标识读入内存的bit流在什么时候开始送入解码器中进行解码。

**PTS(Presentation Time Stamp)**: 即显示时间戳，是显示帧时相对于SCR的时间戳。它主要是度量解码后的视频什么时候被显示出来。

> DTS主要用于视频的解码，在解码阶段使用。PTS主要用于视频的同步和输出，在 display 的时候使用。在没有B帧的情况下，DTS和PTS的输出顺序是一样的。
>

**码率(比特率)**: 码率又称比特率，是指在压缩视频的时候给这个视频指定一个参数，用以告诉压缩软件期望的压缩后视频的大小。码率的英文名为bps(bit per second)，就是用平均每秒多少bit来衡量一个视频大小。对于时间长度相同的视频，码率越大，视频的大小越大，视频的画质就越清晰

> 一段1080P的视频长度为100分钟，大小为1GB，那么该视频的比特率是多少？
>
> 100min = 100*60s = 6000s;
1G = 1024M = 1024*1024KB = 1024*1024*1024Byte = 1024*1024*1024*8bit = 8589934592bit;
比特率 = 8589934592/6000s = 1431655b/s = 1.4Mbit/s;


<!-- 1 Byte = 8 Bits；
1 KB = 1024 Bytes；
1 MB = 1024 KB；
1 GB = 1024 MB；
1 TB = 1024  GB。 -->

https://github.com/0voice/audio_video_streaming/blob/main/article/020-%E8%A7%86%E9%9F%B3%E9%A2%91%E6%95%B0%E6%8D%AE%E5%A4%84%E7%90%86%EF%BC%9ARGB%E3%80%81YUV%E5%83%8F%E7%B4%A0%E6%95%B0%E6%8D%AE%E5%A4%84%E7%90%86.md


## H264视频编码

### profile

### preset

### tune

------

# 码率控制

# 码率控制基本概念

码率控制是指视频编码中决定输出码率的过程。首先介绍一下 X264 中使用到的与码率控制相关的几个概念：

CQP(Constant QP) 恒 定QP（Quantization Parameter）,追求量化失真的恒定，瞬时码率会随场景 复杂度而波动，该模式基本被淘汰(被 CRF 取代)，只有用”-pq 0”来进行无损编码还有价值。

CRF(Constant Rate Factor),恒定质量因子，与恒定 QP 类似，但追求主观感知到的质量恒定，瞬时码率也 会随场景复杂度波动。对于快速运动或细节丰富的场景会适当增大量化失真（因为人眼不易注意到），反之 对于静止或平坦区域则减少量化失真。

ABR(Average Bitrate),平均码率，追求整个文件的码率平均达到指定值（对于流媒体有何特殊之处？）。瞬时码率也会随着场景复杂度波动，但最终要受平均值的约束。

CBR(Constant Bitrate),恒定码率。前面几个模式都属于可变码率（瞬时码率在波动），即VBR（Variable Bitrate）；恒定码率与之相对，即码率保持不变。

x264 并没有直接提供 CBR 这种模式，但可以通过在 VBR 模式的基础上做进一步限制来达到恒定码率的目标。CRF 和 ABR 模式都能通过--vbv-maxrate --vbv-bufsize来限制码率波动。

>关于这几个概念的参考如下： 

+ 1.Waht are CBR,VBV and CPB?
+ 2.FFmpeg and H.264 Encoding Guide
+ 3.CRF Guide(Constant Rate Factor in X264 and X265)
+ 4.MeGUI/x264 setting

## X264 中码率控制

X264 中对于码率控制方法有三种：X264_RC_CQP、X264_RC_CRF、X264_RC_ABR。默认情况是选择 CRF 方法，设置是在 x264_param_default函数里设置的

```C++
param->rc.i_rc_method = X264_RC_CRF;
param->rc.f_rf_constant = 23;
```

关于这三种方法，网上有提到优先级是ABR>CQP>CRF的，但分析 X264 的源码，并没有看出有优先级顺序，关于码率控制方法的设置代码如下：

```C++
OPT("bitrate")
{
    p->rc.i_bitrate = atoi(value);
    p->rc.i_rc_method = X264_RC_ABR;
}
OPT2("qp", "qp_constant")
{
    p->rc.i_qp_constant = atoi(value);
    p->rc.i_rc_method = X264_RC_CQP;
}
OPT("crf")
{
    p->rc.f_rf_constant = atof(value);
    p->rc.i_rc_method = X264_RC_CRF;
}
```

## X264 中关于 QP设置
首先看一段 X264 中关于 QP 值的代码，该段代码在x264_ratecontrol_new：

```C++
rc->ip_offset = 6.0 * log2f(h->param.rc.f_ip_factor);
rc->pb_offset = 6.0 * log2f(h->param.rc.f_pb_factor);
rc->qp_constant[SLICE_TYPE_P] = h->param.rc.i_qp_constant;
rc->qp_constant[SLICE_TYPE_I] = x264_clip3(h->param.rc.i_qp_constant - rc->ip_offset + 0.5, 0, QP_MAX);
rc->qp_constant[SLICE_TYPE_B] = x264_clip3(h->param.rc.i_qp_constant + rc->pb_offset + 0.5, 0, QP_MAX);
```

从上面的代码可以看出，默认的i_qp_constant或者通过命令行传入的qp qp_constant实际设置的是 P 帧的 QP。I 帧和 B 帧的 QP 设置是根据f_ip_factor f_pb_factor计算得到。

在研究编码算法的时候，一般会选用 CQP 方法，设定 QP 为 24、28、32、36、40等（一般选 4 个 QP 值），然后比较算法优劣。在 X264 中，关于QPmin、QPmax、QPstep的默认设置如下：

```C++
param->rc.i_qp_min = 0;
param->rc.i_qp_max = QP_MAX;
param->rc.i_qp_step = 4;
```

<h4>QPmin,默认值：</h4>0.    定义 X264 可以使用的最小量化值，量化值越小，输出视频质量越好。当 QP 小于某一个值后， 编码输出的宏块质量与原始块极为相近，此时没必要继续降低 QP。如果开启了自适应量化器（默认开启），不建议 提高 QPmin 的值，因为这会降低平滑背景区域的视觉质量。

<h4>QPmax，默认值：</h4>51.   定义 X264 可以使用的最大量化值。默认值 51 是 H.264 规格中可供使用的最大量化值。如果 想要控制 X264 输出的最低品质，可以将此值设置的小一些。QPmin 和 QPmax 在CRF，ABR方法下是有效的，过低的设置 QPmax，可能造成 ABR 码率控制失败。不建议调整该参数。

<h4>QPstep，默认值：</h4>4.设置两帧间量化值的最大变化幅度。

. 比较三种码率控制方式如下：

码率控制方法| 视觉质量稳定性 |	即时输出码率 |	输出文件大小
:--|:--|:--|:--
CBR	|不稳定|	恒定|	可控
VBR	|稳定	|变化	|不可控
ABR	|基本稳定|	变化|	可控

帧间 QP 变化，帧内宏块 QP 不变，输出码率未知，各帧输出视觉质量有变化（高 QP 低码率的情况下会更明显）。

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

文章原作者：  音视频开发训练营