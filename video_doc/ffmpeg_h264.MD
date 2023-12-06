
https://www.cnblogs.com/tinywan/p/6402007.html

## H264编码profile & level控制

**H.264有四种画质级别,分别是baseline, extended, main, high：**

|profile|含义|释义|
| ---- | ---- | ---- |
| baseline  | 基本画质 | 基本画质。支持I/P 帧，只支持无交错（Progressive）和CAVLC； |
| extended  | 进阶画质 | 进阶画质。支持I/P/B/SP/SI 帧，只支持无交错（Progressive）和CAVLC；(用的少)  |
| main  | 主流画质 | 主流画质。提供I/P/B 帧，支持无交错（Progressive）和交错（Interlaced）， 也支持CAVLC 和CABAC 的支持；  |
| high  | 高级画质 | 在main Profile 的基础上增加了8x8内部预测、自定义量化、 无损视频编码和更多的YUV 格式；  |


H.264 Baseline profile、Extended profile和Main profile都是针对8位样本数据、4:2:0格式(YUV)的视频序列。在相同配置情况下，High profile（HP）可以比Main profile（MP）降低10%的码率。

根据应用领域的不同，Baseline profile多应用于实时通信领域，Main profile多应用于流媒体领域，High profile则多应用于广电和存储领域。
```c
// profile
av_dict_set(&param, "profile", "main", 0);
```

下图清楚的给出不同的profile&level的性能区别。 

**profile**
![11](./images/20160516164141047.jpg)

**level**

![22](./images/20160516164454535.jpg)


从压缩比例来说，baseline< main < high，对于带宽比较局限的在线视频，可能会选择high，但有些时候，做个小视频，希望所有的设备基本都能解码（有些低端设备或早期的设备只能解码 baseline），那就牺牲文件大小吧，用baseline。自己取舍吧！


## 编码效率和视频质量的取舍(preset, crf)

除了上面提到的，强行配置biterate，或者强行配置profile/level，还有2个参数可以控制编码效率。 

一个是preset，一个是crf。 

preset也挺粗暴，基本原则就是，如果你觉得编码太快或太慢了，想改改，可以用profile。 
preset有如下参数可用：

ultrafast, superfast, veryfast, faster, fast, medium, slow, slower, veryslow and placebo. 

编码加快，意味着信息丢失越严重，输出图像质量越差。

CRF(Constant Rate Factor): 范围 0-51: 0是编码毫无丢失信息, 23 is 默认, 51 是最差的情况。相对合理的区间是18-28. 
值越大，压缩效率越高，但也意味着信息丢失越严重，输出图像质量越差。

