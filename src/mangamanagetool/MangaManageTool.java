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

       // System.out.println(args[0]+" "+args[1]);
 
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
        ArrayList<String> unsortedAuthorNames = new ArrayList(unsortedFileList.table.keySet());
        //sort them for more readable output00
        Collections.sort(unsortedAuthorNames); 

        int mvCounter = 0;
        int mkdirCounter = 0;

        
        StringBuilder mvbuiBuilder = new StringBuilder();
        
        //command to make new folder 
        //windows and mac both use "mkdir" to make new directory
        StringBuilder mkdrirBuilder = new StringBuilder();

        //command to move files one windows
        StringBuilder mvWinCommand = new StringBuilder();
        
        //command to move file on mac and linux
        StringBuilder mvMacCommand = new StringBuilder();

        StringBuilder mkdirCommand = new StringBuilder();

        for (String unsortedAuthorName : unsortedAuthorNames)
        {
            //check if there already exist a folder for the file
            File destFolder = getAuthorFolder(existingAuthorList, unsortedAuthorName, unsortedFileList.table.get(unsortedAuthorName).names);
            if (destFolder != null)
            {
                mvbuiBuilder.append(unsortedAuthorName).append("\n");

                //tell user to move the file
                ArrayList<File> sourceFiles = unsortedFileList.table.get(unsortedAuthorName).files;
                for (File sourceFile : sourceFiles)
                {
                    mvMacCommand.append("mv "+"\""+sourceFile.getPath()+"\" "+"\""+destFolder.getPath()+"\"\n");
                    mvWinCommand.append("move " + "\"" + sourceFile.getPath() + "\" " + "\"" + destFolder.getPath() + "\"\n");

                }

                //move src dest
                mvCounter++;
            }
             else if (unsortedFileList.table.get(unsortedAuthorName).files.size() >= 2) 
            {
                //if folder  does not exist and this author have more than two book
                //tell user to create one
                
                mkdrirBuilder.append(unsortedAuthorName).append("\n");

                //mkdir folder  
                mkdirCommand.append("mkdir \"" + unsortedAuthorName + "\"\n");
                mkdirCounter++;
            }
        }


        System.out.println("移动" + mvCounter + "次");
        System.out.print(mvbuiBuilder.toString());
        System.out.println(mvWinCommand);

        System.out.println("\n\n\n\n\n\n\n\n\n");
        System.out.println("建立新文件" + mkdirCounter + "次");
        System.out.print(mkdrirBuilder.toString());
        System.out.println(mkdirCommand);    
        System.out.println(mvMacCommand);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    
     //@param s: author name
    //@return the folder url, null if no exitence
    public static File getAuthorFolder(SortedFileTable sortedtable,String sourceName, ArrayList<String> sourceNames)
    {
        //if we can find directly, nice
        if (sortedtable.table.contains(sourceName))
        {
            return sortedtable.table.get(sourceName).directory;
        }

        //if not, compare all authors names
  
        for (AuthorInfo entry : sortedtable.table.values())
        {
            for (String name : entry.names)
            {
                for (String name2 : sourceNames)
                {
                    if (NameParser.stringDistance(name2, name) < 2)
                    {
                        return entry.directory;
                    }
                }
            }

        }
        return null;
    }
}
