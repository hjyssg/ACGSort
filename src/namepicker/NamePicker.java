package namepicker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NamePicker {

    int FULLNAME_MIN_LEN = 2;
    int FULLNAME_MAX_LEN = 8;
    int FIRSTNAME_MIN_LEN = 1;
    int FIRSTNAME_MAX_LEN = 5;
    int LASTNAME_MIN_LEN = 1;
    int LASTNAME_MAX_LEN = 4;

    HashSet<String> names;
    HashSet<String> lastNames;
    HashSet<String> firstNames;

    Hashtable<Character, Integer> charCountTable;
    
    
    public static NamePicker getInstace(){
        String basePath = System.getProperty("user.dir") + "\\src\\resources\\";

        String LASTNAMES_FN = basePath + "Jp_Family_Names.txt";
        String[] fns = {basePath + "AV_Atress_Names.txt",
            basePath + "Male_Seiyuu_Names.txt",
            basePath + "Female_Seiyuu_Names.txt",
            basePath + "Historic_People.txt"};
         NamePicker picker = new NamePicker(LASTNAMES_FN, fns);
         return picker;
    }

    NamePicker(String lastNameFn, String[] nameFns) {
        names = new HashSet();
        lastNames = new HashSet();
        firstNames = new HashSet();
        charCountTable = new Hashtable();

        lastNames.addAll(readFileLines(lastNameFn, LASTNAME_MIN_LEN, LASTNAME_MAX_LEN));

        for (String fn : nameFns) {
            names.addAll(readFileLines(fn, FULLNAME_MIN_LEN, FULLNAME_MAX_LEN));
        }

        for (String name : names) {
            //init char count
            for (Character cc : name.toCharArray()) {
                if (charCountTable.containsKey(cc)) {
                    charCountTable.put(cc, charCountTable.get(cc) + 1);
                } else {
                    charCountTable.put(cc, 1);
                }
            }

            //init first name
            for (int ii = LASTNAME_MAX_LEN; ii > 0; ii--) {
                int b1 = min(ii, name.length());
                String sub = name.substring(0, b1);
                if (lastNames.contains(sub)) {
                    String fn = name.substring(b1, name.length());
                    if(fn.length() >= FIRSTNAME_MIN_LEN && fn.length() <= FIRSTNAME_MAX_LEN){
                        firstNames.add(fn);
                        break;
                    }
                }
            }
        }
//        for (String name : firstNames) {
//            System.out.println(name);
//        }
    }

    public HashSet<String> readFileLines(String fn, int minSize, int maxSize) {
        HashSet<String> result = new HashSet();
        FileInputStream fis;
        try {
            fis = new FileInputStream(fn);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NamePicker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(fn);
            return result;
        }

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line;
        try {
            while ((line = br.readLine()) != null) {
                line = line.split(" ")[0];
                line = line.trim();
                if (line.length() >= minSize && line.length() <= maxSize) {
                    result.add(line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NamePicker.class.getName()).log(Level.WARNING, null, ex);
        }
        return result;
    }

    private int min(int a, int b) {
        return Math.min(a, b);
    }

    public Hashtable<String, Integer> findNames(String text) {
        Hashtable<String, Integer> result = new Hashtable<>();

        int len = text.length();
        //using existing names
        for (int ii = 0; ii < len; ii++) {
            int found = -1;
            for (int jj = FULLNAME_MAX_LEN; jj >= FULLNAME_MIN_LEN; jj--) {
                int b1 = min(ii + jj, len);
                String sub = text.substring(ii, b1);
                if (names.contains(sub)) {
                    result.put(sub, text.indexOf(sub));
                    found = b1;
                }
            }
            if (found > ii) {
                ii = found;
            }
        }

        String[] bigTokens = text.split(" |、|\\n|。|\\.|[A-Za-z\\d]|／");

        for (String bigToken : bigTokens) {
            len = bigToken.length();
            if (len < FULLNAME_MIN_LEN) {
                continue;
            }

            for (int ii = 0; ii < len; ii++) {
                if (!charCountTable.containsKey(bigToken.charAt(ii))) {
                    continue;
                }
                int found = -1;
                //using last names and first combination
                for (int jj = LASTNAME_MAX_LEN; jj >= LASTNAME_MIN_LEN; jj--) {
                    int b1 = min(ii + jj, len);
                    String sub = bigToken.substring(ii, b1);
                    if (lastNames.contains(sub)) {
                        for (int kk = FIRSTNAME_MAX_LEN; kk >= FIRSTNAME_MIN_LEN; kk--) {
                            int b2 = min(b1 + kk, len);
                            String sub2 = bigToken.substring(b1, b2);
                            if (firstNames.contains(sub2)) {
                                String tempFullName = sub + sub2;
                                result.put(tempFullName, text.indexOf(tempFullName));
                                found = b2;
                                break;
                            }
                        }
                    }
                }
                if (found > ii) {
                    ii = found;
                }
            }
        }
        return result;
    }

    public static String[] convertTableIntoSortedArray(Hashtable<String, Integer>  table){
        Object[] objects = table.keySet().toArray();
        String[] keys = Arrays.copyOf(objects, objects.length, String[].class);
        
        //LONG TIME NO SEE, BUBBLE SORT
        for(int ii = 0; ii < keys.length-1; ii++){  
            int minIndex = ii;
            int minValue = table.get(keys[ii]);
            for(int jj = ii+1; jj < keys.length; jj++){
                int value = table.get(keys[jj]);
                if(value < minValue || (value == minValue && keys[jj].length() > keys[minIndex].length() )){
                    String temp = keys[minIndex];
                    keys[minIndex] = keys[jj];
                    keys[jj] = temp;
                }
            }
        }
        return keys;
    }
}
