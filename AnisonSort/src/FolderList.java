
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author junyang_huang
 */
import java.io.*;
import java.util.*;

import java.net.*;
import javax.swing.tree.ExpandVetoException;

public class FolderList {


    public Hashtable<String, File> table = new Hashtable<String, File>();

    public FolderList() {
    }
    

    public FolderList(String FolderPath) throws Exception {
    
        this.iterateAllSubfolder(new File(FolderPath));
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
                    
                    this.table.put(directoryName, f);
                }
            }
        } catch (Exception e) {
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
            System.out.println(s + "     " + table.get(s).toString());
        }
    }
}
