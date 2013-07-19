MangaManageTool
===============

如果你是一名阿宅，你擁有超過超多的漫畫、同人誌。你一定會想整理他們。 
  
根據我個人實踐，最好的整理辦法就是按作者名分類。最好的整理辦法就是按作者名分類。這個很重要，這裡要說兩次。
因為實用的作者總是很使用，難用的作者一直都那麼難用。
並且一般漫畫文件都有標作者，分起來比較方便。

同人作品有人可以按題材分。但是同人作品一般水平差別很多，魚龍混雜。都放在一起會導致有時你想到一個本子，但找不到。
作者也有水平好壞，你可以實用的作者放一個文件夾，不實用的作者放一個文件夾。

###具體整理方法
所有漫畫文件用壓縮文件保存，不要解壓成一個圖片文件夾。這樣利於複製傳輸。
比如，你有500本漫畫。A組是全部解壓的，B組是壓縮成zip。你嘗試從一個移動硬盤移動到另一個。B組會明顯快於A組。  
現在越來越多軟件支持無需解壓直接看壓縮文件內的圖片。比如，大陸的haoZip，ios上面的Bookman。
壓縮文件直接拿來看，實在不必去解壓，浪費硬盤空間。 
 
文件分到兩個文件夾，一個是整理好的，一個沒整理過的。
整理的文件夾是這樣的： 按一個一個作者創建文件，把對應的文件放進去。

    ......./_Manga/Sorted_By_Author$  ls - 1   
    ドウガネブイブイ(あぶりだしざくろ)   
    5年目の放課後  
    にびなも凸面体(アイソトニクス)  
    Heaven's Gate (安藤智也)  
    メメ50  
    LEYMEI      
    (.............)

你可以按這樣手動整理。但這樣的做法的實際問題是：  
1.隨著文件數量增加，一個一個文件找相應作者文件夾很困難.  
2.當新作者出現的時候，憑記憶沒法及時創建該作者相對應的文件.  
3.非常非常費時費力。整理一次漫畫要30多個小時。   


###關於本程序
綜上原因，我自己寫了一個java程序幫助整理漫畫。
你打開選好沒整理的壓縮文件所在的文件夾。和選在已經整理好文件夾所在的根目錄。最後點執行。
這個程序會自動讀取兩個文件夾的所有文件的文件名，配對計算，最後在生成的shell command文件
告訴你要移動哪些文件，要新建哪些文件夾。你需自己用terminal去執行shell command或者根據此手動整理。

之所以不直接由這個程序執行移動和創建文件夾操作是因為:  
1.移動文件是一件很risky的事。丟失、損壞都有可能發生。  
2.這個程序的利用文件名來進行匹配，不是100%正確的，需要人工判斷。   
3.手動移動才有樂趣，程序一下跑完就沒意思了.  
4.作為軟件工程師，隨便動別人電腦內部文件是一件不道德的事情。  

###打開方式
1.右鍵打開dist/MangaManageTool.jar文件。或者用terminal運行java -jar MangaManageTool.jar。  
2.用Netbeans打開來run。這是一個Netbeans Project。 

###漫畫文件命名規則
因為本程序是基於是文件名比較的找出作者名進行計算的，不會讀取壓縮文件的內容。
文件名需按照如下規則

    单行本：[作者名]作品名    
    同人志：（發行的展會*）[社團名(作者名)]作品名(原作品名*)(漫畫的語言*) 

目前大部分網上漫畫命名都是按照這個格式。如果你的文件名沒有用"[]"中括號包圍作者名的話，程序沒辦法認出作者名的。
含*的項目都是optional，僅是我個人建議，程序並不會去處理。  

漫画作者文件夹请不要用“[]”中括號包围,可能会导致错误。像我上文那样命名肯定没问题，而且也相对美观。

對於Windows，我推薦使用everything來幫助移動文件。我推荐DirSync Pro用备份文件, 非常好用。

如果你有任何好的主意或者發現bug，請pull request。同时欢迎fork这个project。   
    
我被迫又写了一个英文说明。

===========English============  
If you are an Otaku and have a big manga collection, you may want to sort them.

Based on my own practice, the best way is to sort manga files based on the author.  Again, based on the author. It is so important, so I repeat.

###How?
All the manga should keep as compressed files(zip, rar, etc) rather than  having folders that hold picture files. By doing so, transferring files is much faster.
Meanwhile, there are more and more software that support read compressed file’s content without unziping them. e.g HaoZip, Bookman on iOS. You really don't need to unzip them.

Put manga files into two folders. One is for sorted files, the other is for unsorted.
You create folders for each author, and put his/her manga into corresponding folders.
For example:

    ......./_Manga/Sorted_By_Author$  ls - 1  
    ドウガネブイブイ(あぶりだしざくろ)   
    5年目の放課後  
    にびなも凸面体(アイソトニクス)  
    Heaven's Gate (安藤智也)  
    メメ50  
    LEYMEI      
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

I did not “invent” this convention. Most manga files on the internet follow this convention.  “[]”square brackets is where the program find author's’ name. “*” is just my recommendation, the program will just ignore them.

For Windows, I recommend using Everthing to move files.

If you have any good idea and find a bug, pleae pull a request. Also, forking this project is welcome.
 

