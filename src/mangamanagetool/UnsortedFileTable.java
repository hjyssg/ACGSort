/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 *
 * @author junyang_huang
 */
public class UnsortedFileTable
{
    //the key is the author name 
    //the value is the array of url of his/her manga and doujinshi
    Hashtable<String, ArrayList> table;

    //create the authotList based on the text file from Everthing
    public UnsortedFileTable(File ZipFileListText) throws IOException
    {
        table = new Hashtable<String, ArrayList>();

        //take input from text file
        Scanner fileInScanner = new Scanner(ZipFileListText);
        while (fileInScanner.hasNextLine())
        {
            String line = fileInScanner.nextLine();
            URL tempUrl = new URL("file:///" + line);

            String name = NameParser.getAuthorName(tempUrl.getFile());
            if (name != null)
            {

                if (table.containsKey(name))
                {
                    table.get(name).add(tempUrl);
                }
                else
                {
                    ArrayList urlEntry = new ArrayList<URL>();
                    urlEntry.add(tempUrl);
                    table.put(name, urlEntry);
                }
                //System.out.println("put " + name + "   " + tempUrl.getFile());
            }
        }
        //close the file
        fileInScanner.close();
        ZipFileListText.exists();
    }

    //create the authotList based on the text file from Everthing
    public UnsortedFileTable(URL fileFolder) throws Exception
    {
        table = new Hashtable<String, ArrayList>();

        this.iterateAllSubfolderAndFindCompressedFile(new File(fileFolder.toURI()));
    }

    private void iterateAllSubfolderAndFindCompressedFile(File dir) 
    {
        try
        {
            File[] files = dir.listFiles();
            if (files != null)
        {
            for (File f : files)
            {
                if (f.isFile())
                {
                    String fileName = f.getName();

                    //System.out.println(fileName);

                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                    if (!f.isHidden() && NameParser.isCompressionFile(extension))
                    {
                        String authorName = NameParser.getAuthorName(f.getName());

                        if (authorName != null)
                        {
                            //System.out.println(authorName+"  " + fileName);
                            
                            if (table.containsKey(authorName))
                            {
                                table.get(authorName).add(f);
                            }
                            else
                            {
                                ArrayList urlEntry = new ArrayList<URL>();
                                urlEntry.add(f);
                                table.put(authorName, urlEntry);
                            }
                        }
                    }
                    else
                    {
                        iterateAllSubfolderAndFindCompressedFile(f);
                    }
                }
            }
        } 
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void debugDisplay()
    {
        ArrayList<String> arr = new ArrayList();
        for (String s : table.keySet())
        {
            arr.add(s);
        }

        System.out.println("number of keys " + arr.size());

        Collections.sort(arr);
        for (String s : arr)
        {
            System.out.println(s);
        }
    }
}
