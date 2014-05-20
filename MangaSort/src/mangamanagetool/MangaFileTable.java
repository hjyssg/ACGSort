/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

/**
 *
 * @author hjyssg
 */
import java.io.File;
import java.util.*;

/**
 * input an author name string and return an array of author names e.g
 * "生徒会室(あきもと大)" will return [生徒会室, あきもと大] abgrund (さいかわゆさ) will return
 * [abgrund,さいかわゆさ]
 *
 */
public class MangaFileTable extends Hashtable<String, AuthorInfo> {
    //default is recursive 

    public boolean oneLevel = false;
    public boolean fileOn = true;
    public boolean folderOn = true;
    public boolean compressedFileOnly = false;

    public void addFolders(String[] pathes) {
        //read one by one
        for (String path : pathes) {
            System.out.println(path);
            addFolder(path.trim());
        }
    }

    public void addFolder(String folderPath) {
        iterate(new File(folderPath));
        mergeSameKeys();
    }

    public boolean add(String authorName, File f) {
        //System.out.println(authorName+"  " + f);
        
        //author can not be that long
        if (authorName.length() >= 45)
        {
            return false;
        }
        
        if (containsKey(authorName)) {
            get(authorName).files.add(f);
            return true;
        } else {
            AuthorInfo entry = new AuthorInfo(authorName, f);
            put(authorName, entry);
             return true;
        }
    }
    

    String longerString(String s1, String s2)
    {
        if (s1.length()>=s2.length())
        {
            return s1;
        }else
        {
            return s2;
        }
    }


     String shorterString(String s1, String s2)
    {
        if (s1.length()<=s2.length())
        {
            return s1;
        }else
        {
            return s2;
        }
    }

    /**
     * find the same key and and merge
     */
    public void mergeSameKeys()
    {
           //get the author of unsorted files     
        ArrayList<String> keys = new ArrayList(keySet());
        HashSet<String> goingToRemove = new HashSet<String>();
        
         //sort them for more readable output
        Collections.sort(keys);
        
        for (int ii = 0; ii < keys.size();ii++) {
            
            String key1 = keys.get(ii);
            
            if (goingToRemove.contains(key1)){continue;}
            ArrayList<String> n1s = this.get(key1).names;
          
            for (int jj = ii+1; jj <  keys.size(); jj++) {
                 String key2 = keys.get(jj);
                 if (goingToRemove.contains(key2)){continue;}
                 
                  ArrayList<String> n2s = this.get(key2).names;
                 if (NameParser.isTwoNamesEqual(n1s, n2s, false))
                 {
                     //goingToRemove.add(key2);
                     
                     String ss = shorterString(key1, key2);
                     String ls = longerString(key1, key2);
                     
                     goingToRemove.add(ss);
                     
                     //merge files 
                     this.get(ls).files.addAll(this.get(ss).files);

                     if(ss.equals(key1)){break;}
                 }
            }
        }
        
        
        //remove 
        for (String rm : goingToRemove) {
            this.remove(rm);
        }
        
    }

    private void iterate(File dir) {
        try {
            File[] files = dir.listFiles();

            if (files == null) {
                return;
            }

            for (File f : files) {
                String fileName = f.getName();

                //skip hidden file and folder
                if (f.isHidden()) {
                    continue;
                }

                if (f.isFile() && fileOn) {
                    //get file extension
                    String extension = NameParser.getFileExtension(fileName);

                    if (compressedFileOnly && NameParser.isCompressionFile(extension) == false) {
                        continue;
                    }

                    String authorName = NameParser.getStringFromBrackets(fileName);

                    if (authorName != null) {
                        add(authorName, f);
                    }

                } else if (f.isDirectory()) {
                    if (folderOn) {
                        //String authorName = NameParser.getStringFromBrackets(fileName);
                        //System.out.println(authorName);
                        add(fileName, f);
                        // return;
                    }
                    //file hirachy travesal
                    if (!this.oneLevel) {
                        // System.out.println("__GOING TO "+ f.getName());
                        iterate(f);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void debugDisplay() {
        ArrayList<String> arr = new ArrayList();
        for (String s : keySet()) {
            arr.add(s);
        }

        System.out.println("number of keys " + arr.size());

        Collections.sort(arr);
        for (String s : arr) {
            System.out.println("|" + s + "|     " + get(s).names + " | " + get(s).files);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int fileNum = 0;

        ArrayList<String> arr = new ArrayList();
        for (String s : keySet()) {
            arr.add(s);
            fileNum += get(s).files.size();
        }
        
        str.append("oneLevel=").append(oneLevel).append(", fileOn=").append(fileOn).append(", folderOn=").append(folderOn).append(", compressedFileOnly=").append(compressedFileOnly).append('\n');
        str.append("number of files ").append(fileNum).append("\n\r");
        str.append("number of keys ").append(arr.size()).append("\n\r");

        Collections.sort(arr);
        for (String s : arr) {
            str.append("|").append(s).append("|:     ").append(get(s).names).append(" | ").append(get(s).files).append("\n\r");
        }
        return str.toString();
    }
}
