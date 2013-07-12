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
import java.io.*;
import java.util.*;
import java.util.Collections;


public class MangaManageTool
{
    //the first argurment is where sorted folders are
    //the second is where unsorted files are
     public static void main(String[] args) throws Exception
    {
        ArrayList authorFolderURLs = new ArrayList<URL>();

        System.out.println(args[0]+" "+args[1]);
 
        SortedFileTable authorList = new SortedFileTable(args[0].trim());
        //authorList.debugDisplay();


     
 
       UnsortedFileTable unsortedFileList = new UnsortedFileTable(args[1].trim());
       //unsortedFileList.debugDisplay();


  
            moveFileAndMkdir(authorList, unsortedFileList);
    }
    
    /**
     * find matched files between SortedFileTable and UnsortedFileTable
     * print out instruction about how to move file from unsortedFolder to sortedFolder
     * You need to move file manually to make sure no any problem
     * @param existingAuthorList
     * @param unsortedFileList
     * @throws Exception 
     */
    public static void moveFileAndMkdir(SortedFileTable existingAuthorList, UnsortedFileTable unsortedFileList) 
    {
        try
        {
         
        //get the author of unsorted files     
        ArrayList<String> arr = new ArrayList(unsortedFileList.table.keySet());
        //sort them for more readable output
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
            //check if there already exist a folder for the file
            URL folderURL = existingAuthorList.getAuthorFolderURL(s);
            if (folderURL != null)
            {
                mvbuiBuilder.append(s).append("\n");

                //tell user to move the file
                ArrayList<File> urls = unsortedFileList.table.get(s);
                for (File tempUrl : urls)
                {
                    //mvMacCommand.append("mv "+"\""+tempUrl.getPath()+"\" "+"\""+folderURL.getPath()+"\"\n");
                    mvWinCommand.append("move " + "\"" + tempUrl.getPath() + "\" " + "\"" + folderURL.getPath() + "\"\n");
                    mvWinCommand.append("move ").append(tempUrl.getPath()).append(folderURL.getPath()).append("\n");
                    //mvMacCommand.append("mv ").append(tempUrl.getPath()).append(folderURL.getPath()).append("\n");
                }

                //move src dest
                mvCounter++;
            }
             else if (unsortedFileList.table.get(s).size() >= 2) 
            {
                //if folder  does not exist and this author have more than two book
                //tell user to create one
                
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
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        
    }
}
