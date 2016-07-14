最近项目中有个需求是仿微信的发送位置界面，我选择了使用腾讯地图，毕竟都是腾讯的应该好仿出来，之前用百度地图实现过，不过觉得百度有点麻烦。
[源码下载](http://download.csdn.net/detail/wushge11/9576061)
[github下载](https://github.com/wushge11/WeiXinSelectLocation)
> 先看效果图
 ![这里写图片描述](http://img.blog.csdn.net/20160714152433320)


没啥技术要点 ， 集成时要细心按步骤来，so库，key配置，使用的时候记得初始化之类的，这个里面我用的腾讯2d地图 ， 和检索 sdk 还有 定位的sdk
项目中的poi列表是根据屏幕中心坐标解析位置信息返回来的poilist 不是检索服务的poi，所以只有10条，有需要的朋友可以看一下检索sdk的api来获取更多的poilist
[源码下载](http://download.csdn.net/detail/wushge11/9576061)
[github下载](https://github.com/wushge11/WeiXinSelectLocation)

