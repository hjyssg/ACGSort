/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

import java.io.*;
import java.util.*;

/**
 *
 * @author junyang_huang
 */
public class UnsortedFileTable {
    //the key is the author name 
    //the value is the array of file of his/her manga and doujinshi

    public Hashtable<String, AuthorInfo> table;

    //create the authotList based on the text file from Everthing
    public UnsortedFileTable(String fileFolderPath) throws Exception {
        table = new Hashtable<String, AuthorInfo>();

        this.iterateAllSubfolderAndFindCompressedFile(new File(fileFolderPath));
    }

    
    public UnsortedFileTable() {
        table = new Hashtable<String, AuthorInfo>();
    }
    
    public void addFolder(String folderPath)
    {
        iterateAllSubfolderAndFindCompressedFile(new File(folderPath));
    }
    
    private void iterateAllSubfolderAndFindCompressedFile(File dir) {
        try {
            File[] files = dir.listFiles();

            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        String fileName = f.getName();

                       //System.out.println(fileName);
                        
                        //get file extension
                        String extension = NameParser.getFileExtension(fileName);

                        if (!f.isHidden() && NameParser.isCompressionFile(extension)) {
                            
                            String authorName = NameParser.getAuthorName(fileName);

                            if (authorName != null) {
                                //System.out.println(authorName+"  " + fileName);

                                if (table.containsKey(authorName)) {
                                    table.get(authorName).files.add(f);
                                } else {

                                    AuthorInfo entry = new AuthorInfo();
                                    ArrayList fileEntry = new ArrayList<File>();
                                    fileEntry.add(f);
                                    entry.files = fileEntry;
                                    entry.names = NameParser.getAuthorNameEntry(authorName);

                                    table.put(authorName, entry);

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
            System.out.println(s+ "     " + table.get(s).files);
        }
    }
}
