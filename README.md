# Carry
AppData Collected for BigData


## 项目介绍
    1. carry_sdk 为 android library项目，无埋点统计 日志记录及数据抓取的sdk具体实现
    2. carry_plugin ：修改class文件的gradle插件项目，用了在 项目编译时期 插入 carry_sdk的代码，进行埋点
    3. SpecialFocus 为 悦读圈 示例项目，用来测试 无埋点sdk和其插件
## 使用注意事项说明
1. 有两个android工程项目，一个SpecialFocus为示例项目，一个CarryIO_Sample 为Demo项目
2. 示例项目配置了settings.gradle 使其直接引用carry_sdk 模块
3. carry_plugin项目 配置了 upload Task 可以直接提交插件到 CarryIO_Sample/repo目录下面，同时 示例项目 SpecialFocus配置了此maven地址

## 调研任务： 已完成、未完成 ##

1. 项目构建：carry_sdk进行
    - [x] 示例项目 SpecialFocus （直接进入实战状态，而不是写一个简单的demo测试，这样要求更高，会提前踩坑）
    - [x] SDK项目 carry_sdk
    - [x] gradle插件项目搭建 carry_plugin
1. carry_sdk功能
    - [x] viewpath获取
    - [x] viewdata获取
    - [ ] viewpage获取
    - [ ] viewpageData获取
    - [ ] viewpath唯一定位view
    - [ ] 唯一定位点击时的属性获取
3. carry_plugin功能
    1. 插件发布及引用
        - [x] 上传插件到本地
        - [x] 示例项目引用本地插件踩吭
    2. 查找onClick方法，并插入carry_sdk中的方法
        - [x] 插件引用android等库问题原因（目前为代码写死）
        - [ ] 通过配置文件获取android sdk，support等本地地址
        - [x] 插件查找指定类的方法
        - [x] 插件获取指定方法的参数名字
        - [x] 插入carry_sdk中引用代码，找不到类(实际是没有找到准确的参数名字)
        - [x] carry_sdk工程打成jar包
        - [x] 解决上上个问题，先插入第三方jar（fastjson)代码试验
        - [x] 编译后onCLick插入的代码运行
        - [ ] 指定包名内的onClick方法插入
        - [ ] 去掉指定包名中的onClick方法插入
        - [ ] 断点调试功能调研(看网上好多人可以，尚未试验成功)
        - [ ] 通过注解插入onClick代码（可控）


由于现在的版本是增量更新的，所有要clean后再重新build后生产jar包方可使用

判断后发现 onViewClick(Intent) 是不是找错方法了，
通过参数个数，参数类型 查找方法
但是还是不行，发现参数名打印出来竟然是intent,限制成 v 以后可以了。但是MineActivity没有插入代码
比较后发现MineActivity onClick方法中发现 有startActivity等涉及Intent的调用，会不会是查找参数名称的代码有问题，排解
onClick方法中不得有私有变量，否则查找参数名称的方法会有问题（待解决）

更多问题请关注 微信公众号：按空格
有文章：app无埋点SDK设计思路前瞻及源码实现