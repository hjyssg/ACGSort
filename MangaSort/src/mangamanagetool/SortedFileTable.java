
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

/**
 *
 * @author junyang_huang
 */
import java.io.*;
import java.util.*;

import java.net.*;
import javax.swing.tree.ExpandVetoException;

public class SortedFileTable {

    /**
     * the key is the author name e.g 鳥居姫, 魚骨工造 (カポ), 怪奇日蝕(綾野なおと)
     *
     */
    public Hashtable<String, AuthorInfo> table;
    
    public SortedFileTable()
    {
        this.table = new Hashtable<String, AuthorInfo>();
    }

    public SortedFileTable(String sortedFolderPath) throws Exception {
        this.table = new Hashtable<String, AuthorInfo>();

        this.iterateAllSubfolder(new File(sortedFolderPath));
    }
    
    public void addFolder(String sortedFilePath)
    {
        this.iterateAllSubfolder(new File(sortedFilePath));
    }

    private void iterateAllSubfolder(File rootFolder) {
        try {
            
            //iterate through each author's folder
            for (File f : rootFolder.listFiles()) {
                
                //...and find author name, save into  ealist
                if (f.isDirectory() && (!f.isHidden())) {
                    String directoryName = f.getName();
                    
                    AuthorInfo entry = new AuthorInfo();
                    entry.names = NameParser.getAuthorNameEntry(directoryName);
                    entry.directory = f;

                    this.table.put(directoryName, entry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
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
            System.out.println("|"+s + "|     " + table.get(s).names );
        }
    }
}
