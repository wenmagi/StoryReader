# 饭团小说
开源小说阅读app，切勿用于不当用途。造成一切后果与本人无关。

求个缺Android开发的团队收留 --> yzd11254@gmail.com

[下载应用](http://yourbuffslonnol.com)


### [百度小说源](https://github.com/loveliu/BaiduBookSuorce) 还未加入app，有空再加。

## 食用配料
Java + Kotlin + okgo(okhttp) + RxJava + GreenDB + GreenDaoUpgradeHelper

## 小说源
两种方式获取小说内容，一种追书神器api，一种爬取盗版小说站。

### 追书神器api
在项目的api文件里有详尽的接口说明。来源于网络。

### 爬虫
重点说说爬虫的实现。考虑到旧版追书的api（新版的都用https）随时都可能会关闭。刚开始只是实现爬虫的，
本着学习的心态（人话：主要还是考虑到请求速度还有质量）。

## 对爬虫的要求：  
   *  不升级app，能修改爬虫。  
   *  不升级app，能增加盗版站的爬虫。
   #### 使用方案是，把单个盗版小说站爬虫打包成一个jar包，然后通过动态加载jar包反射调用里面的方法，获取小说内容。通过与服务器的通信进行jar包增删改操作。

#### 制作爬虫步骤：  

  * 0.参考module包里面的 BooFactory_xxxxx类。
  
  * 1.继承 com.anonymouser.book.module.IBookLoadFactory  
  
  * 2.实现 getZhangjie 方法，实现爬取章节标题和章节的链接。
  
  * 3.实现 getBook 主体内容的爬虫。
  
  * 4.实现 getVersion 该爬虫的版本号，考虑到网站会改变。
  
  * 5.打包类为jar包，如何打包成jar包（略），打包后参考assets/jar，也可以放在服务器，实现更新爬虫，添加爬虫。
  
#### 字体修改
  
  * 0.字体文件放在assets/fonts。
  * 1.解除注释 com.anonymouser.book.bean.PaintInfo$58 。
  * 2.修改 com.anonymouser.book.bean.PaintInfo$61 修改成字体文件路径。
  * 3.解除注释 com.anonymouser.book.widget.BookPageFactory$61 。
  * 4.需要实现多字体选择请自行修改代码。

#### 提示
  * 内有Google analytics 和 Google adword。请自行修改。
   
## 成型
![image](https://github.com/loveliu/FanTuan/blob/master/readme/0.png)  
![image](https://github.com/loveliu/FanTuan/blob/master/readme/1.png)  
![image](https://github.com/loveliu/FanTuan/blob/master/readme/2.png)  
![image](https://github.com/loveliu/FanTuan/blob/master/readme/3.png)  
![image](https://github.com/loveliu/FanTuan/blob/master/readme/4.png)




#License

	饭团小说	
    Copyright 2017 Yang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.































