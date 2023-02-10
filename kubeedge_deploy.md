# kubeedge 部署应用解析



- [KubeEdge设备添加](https://blog.csdn.net/qq_43546999/article/details/120918529)

## DeviceModel

一个DeviceModel描述设备公开的设备属性Properties以及访问这些属性的属性访问者Property Visitors。DeviceModel就像一个可重用的模板，使用它可以创建和管理许多设备。

## DeviceInstance

一个DeviceInstance表示实际的设备对象。它就像DeviceModel的一个实例并引用模型中定义的属性。设备spec是静态的，而设备状态包含动态变化的数据，如设备属性的所需（desired）状态和设备报告（reported）状态。