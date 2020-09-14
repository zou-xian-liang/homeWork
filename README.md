# homeWork
仅供ThoughtWorks面试所用

> 项目github地址：https://github.com/zou-xian-liang/homeWork

## 产品需求

- 1、仿微信朋友圈实现个人朋友圈
- 2、过滤接口返回的脏数据
- 3、图片采用9图网格形式排列
- 4、将所有数据一次请求加载至内存中
- 5、客户端实现下拉刷新，和上拉加载功能。每次加载5条
- 6、自定义图片加载器，将图片缓存至本地
- 7、需要做不同设备的适配



## 功能实现

- 1、主体采用Kotlin语言进行功能开发，采用mvvm架构
- 2、引入jetpack相关viewmodel和liveData
- 3、将网络图片缓存到私有缓存目录中
- 4、图片的下载及存储采用协程的方式完成
- 5、数据请求采用Retrofit+liveData+kotlin协程进行



## bug

1、CoordinatorLayout+RecyclerView由于RecyclerView中嵌套多个RecyclerView引起滑动问题的处理

2、异常数据处理，json数据中变量名含有空格等问题



## 未解决的问题

1、用户头像以及朋友圈中含有的部分图片无法加载，尝试采用其他的图片加载库，仍然失败。

