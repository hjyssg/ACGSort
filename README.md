MangaSort
===============


###这是啥？
一个阿宅写来整理漫画和同人志的Java小程序 。

###怎么整理的？
根据文件名。比如，

	"[谏山创]進撃の巨人第04巻.zip” 放到一个叫 "谏山创" 的文件夹里面。  
	"[同人志][アレマテオレマ（小林由高] GARIGARI 30 （东方).zip” 放到一个叫 "アレマテオレマ（小林由高）" 的文件夹里面。
  
###有什么我必须知道？漫画文件命名规则
本程序没有什么高级数据库或者机器学习，也不会去读取文件内容。单纯是找[]中间的文字作为作者名。
文件名请按照如下规则：
	
    单行本：[作者名]作品名    
    同人志：（发行的展会*）[社团名(作者名1,作者名2)]作品名(原作品名*)(漫画的语言*) 

目前大部分网上漫画命名都是按照这个格式。基本上，漫画、同人志下载下来不要改名字就对了。如果你的文件名没有用"[]"中括号包围作者名的话，程序没办法认出作者名的，自动跳过该文件。
含*可选的项目都是，仅是我个人建议，程序并不会去处理。


 
###怎么打开？
* 下载解压打开[MangaManageTool.jar](https://sourceforge.net/projects/mangasort/)。
*  或者打开来用Netbeans的运行。这是一个NetBeans project。

###怎么用？
起始界面  
![screenshot1](./screenshots/1.png?raw=true)   


上面的按钮是选择你没整理文件所在位置。
下面的按钮是选择你要整理好文件所放位置。  
![screenshot1](./screenshots/2.png?raw=true)  


点run运行。 会生成三个文件。  
打开看，里面是用来整理的命令。自己去复制粘贴到cmd就可以了。  
![screenshot1](./screenshots/3.png?raw=true)  



我提供几个小选项。

* **one-level treversal** 决定程序扫描你的文件的时候是所有子文件夹都查，还是就查第一层。
* **scan file** 扫描文件。
* **scan foldeer** 扫描文件夹。
* **blur martch** 模糊配对。比如 "apple" 和 "appll"在这个模式下就会被视为一样。
* **remember setting** 退出时，记录设定和地址。


### keywords 文件
![screenshot1](./screenshots/4.png?raw=true)   
本程序也读取keywords文件来进行判断。你可以修改它来支持更多自定义整理。  


###建议
所有漫画文件用压缩文件保存，不要解压成一个图文件夹。这样利於复製传输。同样大小，文件越多越慢。
很多软件支持无需解压直接看压缩文件内的图片。比如，haoZip，iOS上面的Bookman。
我推荐用[DirSyncPro](http://www.dirsyncpro.org/)备份文件，非常好用。

###更新
* 2013年8月13日 现在也支持按公司名整理erogame ，只要公司名像漫画作者名一样在中括号[]里面。
* 2014年5月19日 如果你发现英文说明和中文不一样,不要惊慌不要恐惧，我当时吃饱撑写的。


===========English============  
#####Update 08.13.2013   Supporting game sorting now!

###What is this?
A tool to sort Manga and Doujinsin  

###How to sort?
Put manga files into two folders. One is for sorted files, the other is for unsorted.
You create folders for each author, and put his/her manga into corresponding folders.
For example:

    ......./_Manga/Sorted_By_Author$  ls - 1   
    5年目の放課後  
	谷川ニコ  
	竜騎士07
    (.............)


###What will program the do?
It will scan your folder, calculate and then generate shell commands telling you how to move files and create new folders.

###How to open the program?
* Click and run the jar file. 
* Or Open the whole project from Netbeans. This is a Netbeans project.

###Manga File Naming Convention
The program’s algorithm is based on files’ name. It will not read file’s content.
The filename needs to follow following convention in order to get correct output.

    Tankobon: [author_name]manga_name(language)
    doujinshi: (the_evetnt_it_release*)[group_name(author)]manga_name(parody*)(language*)

I did not "invent” this convention. Most manga files on the internet follow this convention.  "[]”square brackets is where the program find author's’ name. "*” is just my recommendation, the program will just ignore them.

###keywords file
You can also modify keywords file to support more filename.

Even you don't know programming, you still make contribution to this project by updating keyword file to support new anime music sorting

###small suggestion
For Windows, I recommend using [DirSyncPro](http://www.dirsyncpro.org/) to bakcup files.



