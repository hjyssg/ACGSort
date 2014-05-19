MangaSort
===============


###这是啥？
一个阿宅写来整理漫画和同人志的Java小程序 。

###怎么整理的？
根据文件名。比如

	"[谏山创]進撃の巨人第04巻.zip” 放到一个叫 "谏山创" 的文件夹里面。  
	"[同人志][アレマテオレマ（小林由高] GARIGARI 30 （东方).zip” 放到一个叫 "アレマテオレマ（小林由高）" 的文件夹里面。
  
###有什么我必须知道？
####漫画文件命名规则
本程序没有什么高级数据库或者机器学习，也不会去读取文件内容。单纯是找[]中间的文字作为作者名。
文件名请按照如下规则
	
    单行本：[作者名]作品名    
    同人志：（發行的展會*）[社團名(作者名1,作者名2)]作品名(原作品名*)(漫畫的語言*) 

目前大部分网上漫画命名都是按照这个格式。基本上，漫画、同人志下载下来不要改名字就对了。如果你的文件名没有用"[]"中括号包围作者名的话，程序没办法认出作者名的，自动跳过该文件。
含*可选的项目都是，仅是我个人建议，程序并不会去处理。


 
###怎么打开？
1. 右键打开 MangaManageTool.jar文件，
1. 打开来用Netbeans的运行。这是一个NetBeans项目。

###怎么用？
![screenshot1](./screenshots/1.png?raw=true)   
打开界面


![screenshot1](./screenshots/2.png?raw=true)  
上面的按钮是选择你没整理文件所在位置。
下面的按钮是选择你要整理好文件所放位置。


![screenshot1](./screenshots/3.png?raw=true)  
点run运行。 会生成三个文件。  
打开看，里面是用来整理的命令。自己去复制粘贴到cmd就可以了。


我提供4个小选项。

* **one-level treversal** 决定程序扫描你的文件的时候是所有子文件夹都查，还是就查第一层。
* **file only** 只扫描文件，无视文件夹。
* **blur martch** 模糊配对。比如 "apple" 和 "appll"在这个模式下就会被视为一样。
* **rememver setting** 退出时，记录设定和地址


###建议
所有漫畫文件用壓縮文件保存，不要解壓成一個圖片文件夾。這樣利於複製傳輸。同样大小，文件越多越慢。
很多軟件支持無需解壓直接看壓縮文件內的圖片。比如，haoZip，ios上面的Bookman。
我推荐目录同步临用备份文件，非常好用。

###更新
* 2013年8月13日 现在也支持按公司名整理EROGAME ，只要公司名像漫画作者名一样在中括号[]里面。
* 2014年5月19日 如果你发现英文说明和中文完全不一样不要惊慌不要恐惧，我当时吃饱撑写的。


===========English============  
#####Update 08.13.2013   Supporting galgame sorting now!  
If you are an Otaku and have a big manga collection, you may want to sort them.

Based on my own practice, the best way is to sort manga files based on the author.  Again, based on the author. It is so important, so I repeat.

###How?
All the manga should keep as compressed files(zip, rar, etc) rather than  having folders that hold picture files. By doing so, transferring files is much faster.
Meanwhile, there are more and more software that support read compressed file’s content without unziping them. e.g HaoZip, Bookman on iOS. You really don't need to unzip them.

Put manga files into two folders. One is for sorted files, the other is for unsorted.
You create folders for each author, and put his/her manga into corresponding folders.
For example:

    ......./_Manga/Sorted_By_Author$  ls - 1   
    5年目の放課後  
	谷川ニコ  
	竜騎士07
    (.............)

It is very simple. You can totally do it manually. But:
1. As the number of files increasing, finding corresponding author’s folder will be difficult.
2. When a new author appear, you may not to able to create his/her folder timely based on memory. 
3. It is very tedious. Sorting 1000 manga files may took more than 10 hours.

###About this program
Due to above reason, I wrote this java program to help me sort manga files.
It will scan your folder, calculate and then generate shell commands telling you how to move files, which new folders need to be created.

You can run the shell command, or move files manually based on shell command.


###How to open the program
1. Click and run the jar file. Open the jar from terminal.
2. Open the whole project from Netbeans.

###Manga File Naming Convention
The program’s algorithm is based on files’ name. It will not read file’s content.
The filename  need to follow following convention in order to get correct output.

    Tankobon: [author_name]manga_name(language)
    doujinshi: (the_evetnt_it_release*)[group_name(author)]manga_name(parody*)(language*)

I did not "invent” this convention. Most manga files on the internet follow this convention.  "[]”square brackets is where the program find author's’ name. "*” is just my recommendation, the program will just ignore them.

For Windows, I recommend using Everthing to move files.

If you have any good idea and find a bug, pleae pull a request. Also, forking this project is welcome.
 

