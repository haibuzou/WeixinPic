# WeixinPic
高仿微信图片选择模块

主界面为GalleryActivity,通过设置intent的参数来选择选择图片，还是单图的裁剪

图片的加载选择的是 universal-image-loader库
可以根据需求更换，具体的实现类在GridViewAdapter中

1.选择多张图片

Intent intent = new Intent(this, GalleryActivity.class); 
intent.putExtra(Constant.TYPE, Constant.SEND_PIC); intent.putExtra(Constant.SEND_NUM, 9); 

可以限制可选的图片个数 Constant.SEND_NUM

2.单张图片选择裁剪

Intent intent = new Intent(this, GalleryActivity.class); 
intent.putExtra(Constant.TYPE, Constant.UPLOAD_PHOTO); 

裁剪的界面也与微信的裁剪界面一致、

3.图片选择之后的压缩操作已经完成好大小控制在 300k以内 


![](https://github.com/haibuzou/WeixinPic/raw/master/art/Screenshot_2015-12-31-16-48-45.png)
![](https://github.com/haibuzou/WeixinPic/raw/master/art/Screenshot_2015-12-31-16-48-50.png)
![](https://github.com/haibuzou/WeixinPic/raw/master/art/Screenshot_2015-12-31-16-49-06.png)
![](https://github.com/haibuzou/WeixinPic/raw/master/art/Screenshot_2015-12-31-15-59-56.png)
