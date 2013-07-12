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
                // System.out.println(f);
                
                if (f.isFile())
                {
                    String fileName = f.getName();

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
                }
                else
               {
                        iterateAllSubfolderAndFindCompressedFile(f);
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
