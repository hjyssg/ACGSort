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
public class UnsortedFileTable {
    //the key is the author name 
    //the value is the array of file of his/her manga and doujinshi

    Hashtable<String, ArrayList<File>> table;

    //create the authotList based on the text file from Everthing
    public UnsortedFileTable(String fileFolderPath) throws Exception {
        table = new Hashtable<String, ArrayList<File>>();

        this.iterateAllSubfolderAndFindCompressedFile(new File(fileFolderPath));
    }

    private void iterateAllSubfolderAndFindCompressedFile(File dir) {
        try {
            File[] files = dir.listFiles();

            if (files != null) {

                for (File f : files) {
                    if (f.isFile()) {
                        String fileName = f.getName();

                        //get file extension
                        String extension = NameParser.getFileExtension(fileName);

                        if (!f.isHidden() && NameParser.isCompressionFile(extension)) {
                            String authorName = NameParser.getAuthorName(f.getName());

                            if (authorName != null) {
                                //System.out.println(authorName+"  " + fileName);

                                if (table.containsKey(authorName)) {
                                    table.get(authorName).add(f);
                                } else {
                                    ArrayList urlEntry = new ArrayList<File>();
                                    urlEntry.add(f);
                                    table.put(authorName, urlEntry);
                                }
                            }
                        }
                    } else {
                        iterateAllSubfolderAndFindCompressedFile(f);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void debugDisplay() {
        ArrayList<String> arr = new ArrayList();
        for (String s : table.keySet()) {
            arr.add(s);
        }

        System.out.println("number of keys " + arr.size());

        Collections.sort(arr);
        for (String s : arr) {
            System.out.println(s);
        }
    }
}
