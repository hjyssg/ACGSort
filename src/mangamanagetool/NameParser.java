/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.*;

/**
 *
 * @author hjyssg
 * @purpose: get a list of author names from managa files
 *
 */
public class NameParser {

    /**
     * input an author name string and return an array of author names e.g
     * "生徒会室(あきもと大)" will return [生徒会室, あきもと大] abgrund (さいかわゆさ) will return
     * [abgrund,さいかわゆさ]
     *
     * @param s
     * @return an array of author names
     */
    public static ArrayList<String> getAuthorNameEntry(String s) {
        String[] names = s.split("\\[|\\]|\\(|\\)|,|、|&");
        ArrayList<String> result = new ArrayList<String>(2);
        for (String tempS : names) {

            tempS = tempS.trim();

            if (tempS.length() < 1) {
                continue;
            }

            if (containWrongWord(tempS)) {
                continue;
            }
            //trim and only allow one space internally
            result.add(tempS.replaceAll(" {2,}", " "));
        }
        return result;
    }

    final static Pattern brktPattern = Pattern.compile("\\[(.*?)\\]");

    /**
     * "[sds]dssad" : sds by [] should just in front . "dsad[digital].zip will
     * return nothing"
     *
     *
     * @param fn fileName
     * @return author name
     */
    public static String getStringFromBrackets(String fn) {
        // System.out.println(fn);

        Matcher matcher = brktPattern.matcher(fn);
        while (matcher.find()) {
            String ss = matcher.group();
            //remove [ and ]

            int offset = matcher.end() + 1;

            //the next char could not be .
            if (offset + 1 <= ss.length() && ss.charAt(offset + 1) == '.') {
                continue;
            }

            ss = ss.replaceAll("\\[", "").replaceAll("\\]", "");
                // System.out.println(ss.charAt(0));
            if (!NameParser.containWrongWord(ss)) {
            return ss;
            }
            
        }
        return null;
    }

    /**
     * http://stackoverflow.com/questions/1515437/java-function-for-arrays-like-phps-join
     *
     * @param s
     * @param delimiter
     * @return
     */
    public static String join(Collection s, String delimiter) {
        StringBuilder buffer = new StringBuilder();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    //exclude non-author name 
    private static final String[] wrongWords = {
        // "雑誌", "杂志",
        "成年コミック", "コミック", "一般コミック", "COMIC",
        "Doujinshi", "Doujin", "同人", "同人", "doujin","同人CG","同人誌","同人志",
         "18禁ゲーム",
        "Anthology", "アンソロジー", "Various", "よろず", "original", "オリジナル",
        "Artist", "アーティスト",
        "DL版",
        "Artbook", "画集",
        "COMIC1☆", "サンクリ",
         "汉化", "漢化", "English", "Chinese", "Korean", "中文", "한국",
        "アニメ", "anime", "Anime",
    };
    


     //comiket (e.g c79, c82) is not an authour name
    final static Pattern wp = Pattern.compile("[Cc][0-9]{2}|[0-9]+|[0-9\\-]+|[Rr][Jj][0-9]+");

    //exclude certain string
    public static boolean containWrongWord(String s) {
       

        //pure number is not an anthour e.g 101012
        if (s == null) {
            return true;
        }
        
        if (wp.matcher(s).matches())
        {
           // System.out.println(s);
            return true;
        }
        
       for (String w : wrongWords) {
            if (s.equalsIgnoreCase(w)) {
                return true;
            }
        }
   
        return false;
    }
    
    
    /**
     * only allow following file type in sorting
     */
    private static final String[] CompressionType = {
        "zip", "rar", "7zip", "pdf", 
         "mp4", "mov", "rmvb", "wmv"
    };

    /**
     * decide if file is compressed file based on its extension
     *
     * @param fileExtension
     */
    public static boolean isAllowedFile(String fileExtension) {
        for (String etx : CompressionType) {
            if (fileExtension.equalsIgnoreCase(etx)) {
                return true;
            }
        }
        return false;
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    /**
     * compare two strings and calculate their string distance
     *
     * @param s1
     * @param s2
     * @return the string distance
     */
    public static int stringDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    public static boolean isTwoNamesEqual(ArrayList<String> l1, ArrayList<String> l2, boolean blur) {

        if (!blur) {
            for (String n1 : l1) {
                for (String n2 : l2) {
                    //igonore case
                    if (n1.equalsIgnoreCase(n2) ){
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (String n1 : l1) {
                for (String n2 : l2) {
                    // System.out.println(n2 + "  "+ name);
                    int strDistance = NameParser.stringDistance(n2, n1);

                    if (strDistance == 0 || n1.equals(n2)) {

                        return true;
                    } else if (strDistance == 1 && n2.length() > 2 && n1.length() > 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
