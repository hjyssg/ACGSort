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

public class MangaFileTable extends Hashtable<String, AuthorInfo> {

    public boolean oneLevel = false;
    public boolean fileOn = true;
    public boolean folderOn = true;
    public boolean compressedFileOnly = false;
    public boolean getStringFromBracketsForFolder = false;

    public ArrayList<File> allIteratedFiles;

    public MangaFileTable() {
        allIteratedFiles = new ArrayList<File>();
    }

    /**
     * @brief let the table iterate multiple paths and add files to the table
     *
     * @param folderPaths the folder will be iterated
     */
    public void addFolders(String[] folderPaths) {
        //read one by one
        for (String path : folderPaths) {
            addFolder(path.trim());
            System.out.println("iterated: " + path);
        }
    }

    /**
     * @brief let the table iterate a path and add files to the table
     *
     * @param folderPath the folder will be iterated
     */
    public void addFolder(String folderPath) {
        iterate(new File(folderPath));
        mergeSameKeys();
    }

    /**
     * @brief add a file with its author name to the table
     *
     * @param authorName
     * @param f the file
     *
     * @return fail or success
     */
    private boolean add(String authorName, File f) {
        //System.out.println(authorName+"  " + f);

        //author can not be that long
        if (authorName.length() >= 50) {
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

    /**
     * @brief choose the longer string
     *
     * @return the longer string
     */
    String longerString(String s1, String s2) {
        if (s1.length() >= s2.length()) {
            return s1;
        } else {
            return s2;
        }
    }

    /**
     * @brief choose the shorter string
     *
     * @return the shorter string
     */
    String shorterString(String s1, String s2) {
        if (s1.length() <= s2.length()) {
            return s1;
        } else {
            return s2;
        }
    }

    /**
     * find the same key and and merge
     */
    public void mergeSameKeys() {
        //get the author of unsorted files     
        ArrayList<String> keys = new ArrayList(keySet());
        HashSet<String> goingToRemove = new HashSet<String>();

        //sort them will increase latter performance
        Collections.sort(keys);

        for (int ii = 0; ii < keys.size(); ii++) {

            String key1 = keys.get(ii);

            if (goingToRemove.contains(key1)) {
                continue;
            }
            ArrayList<String> n1s = this.get(key1).names;

            for (int jj = ii + 1; jj < keys.size(); jj++) {
                String key2 = keys.get(jj);
                if (goingToRemove.contains(key2)) {
                    continue;
                }

                ArrayList<String> n2s = this.get(key2).names;
                if (NameParser.isTwoNamesEqual(n1s, n2s, false)) {
                    String ss = shorterString(key1, key2);
                    String ls = longerString(key1, key2);
                    //keep the longer one
                    goingToRemove.add(ss);

                    //merge files 
                    this.get(ls).files.addAll(this.get(ss).files);

                    if (ss.equals(key1)) {
                        break;
                    }
                }
            }
        }
        //remove 
        for (String rm : goingToRemove) {
            this.remove(rm);
        }
    }

    private void iterate(File dir) {

        if (dir == null) {
            return;
        }

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

                    if (compressedFileOnly && NameParser.isAllowedFile(extension) == false) {
                        continue;
                    }

                    String authorName = NameParser.getStringFromBrackets(fileName);

                    if (authorName != null ) {
                        add(authorName, f);
                    }
                    allIteratedFiles.add(f);

                } else if (f.isDirectory()) {
                    if (folderOn) {
                        //String authorName = NameParser.getStringFromBrackets(fileName);
                        //System.out.println(authorName);
                        String tempFn = null;
                        if (getStringFromBracketsForFolder) {
                            tempFn = NameParser.getStringFromBrackets(fileName);
                            //System.out.println(tempFn + " "+ fileName);
                        } else {
                            tempFn = fileName;
                        }

                        // System.out.println(tempFn + " " + fileName );
                        if (tempFn != null) {
                            add(tempFn, f);
                        }
                        allIteratedFiles.add(f);
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
