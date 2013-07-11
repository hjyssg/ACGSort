/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

/**
 *
 * @author jhuang
 */

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;

public class MangaManageTool
{
     public static void main(String[] args) throws Exception
    {
        ArrayList authorFolderURLs = new ArrayList<URL>();

        //"file:///F:\\_Happy%20Lesson\\_Manga\\按作者分类"
        SortedFileTable authorList = new SortedFileTable(new URL("file:///" + "C:\\Users\\jhuang\\Dropbox\\bbb"));
        authorList.debugDisplay();


        UnsortedFileTable unsortedFileList = new UnsortedFileTable(new URL("file:///" + "C:\\Users\\jhuang\\Dropbox\\aaa"));
        unsortedFileList.debugDisplay();


        try
        {
            moveFileAndMkdir(authorList, unsortedFileList);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
    
    /**
     * find matched files between SortedFileTable and UnsortedFileTable
     * print out instruction about how to move file from unsortedFolder to sortedFolder
     * You need to move file manually to make sure no any problem
     * @param existingAuthorList
     * @param unsortedFileList
     * @throws Exception 
     */
    public static void moveFileAndMkdir(SortedFileTable existingAuthorList, UnsortedFileTable unsortedFileList) throws Exception
    {
        ArrayList<String> arr = new ArrayList(unsortedFileList.table.keySet());
        

        Collections.sort(arr);

        int mvCounter = 0;
        int mkdirCounter = 0;

        StringBuilder mvbuiBuilder = new StringBuilder();
        StringBuilder mkdrirBuilder = new StringBuilder();

        StringBuilder mvWinCommand = new StringBuilder();
        StringBuilder mvMacCommand = new StringBuilder();

        StringBuilder mkdirCommand = new StringBuilder();

        for (String s : arr)
        {
            URL folderURL = existingAuthorList.getAuthorFolderURL(s);
            if (folderURL != null)
            {
                mvbuiBuilder.append(s).append("\n");

                ArrayList<URL> urls = unsortedFileList.table.get(s);
                for (URL tempUrl : urls)
                {
                    //mvMacCommand.append("mv "+"\""+tempUrl.getPath()+"\" "+"\""+folderURL.getPath()+"\"\n");
                    mvWinCommand.append("move " + "\"" + tempUrl.getPath() + "\" " + "\"" + folderURL.getPath() + "\"\n");
                    mvWinCommand.append("move ").append(tempUrl.getPath()).append(folderURL.getPath()).append("\n");
                    //mvMacCommand.append("mv ").append(tempUrl.getPath()).append(folderURL.getPath()).append("\n");
                }

                //move src dest
                mvCounter++;
                continue;
            }
            if (unsortedFileList.table.get(s).size() >= 2)
            {
                mkdrirBuilder.append(s).append("\n");

                //mkdir folder  
                mkdirCommand.append("mkdir \"" + s + "\"\n");
                mkdirCounter++;
            }
        }


        System.out.println("移动" + mvCounter + "次");
        System.out.print(mvbuiBuilder.toString());
        //System.out.println(mvWinCommand);

        System.out.println("\n\n\n\n\n\n\n\n\n");
        System.out.println("建立新文件" + mkdirCounter + "次");
        System.out.print(mkdrirBuilder.toString());
        //System.out.println(mkdirCommand);    
        // System.out.println(mvMacCommand);
    }
}
