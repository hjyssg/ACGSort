/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.*;

/**
 *
 * @author junyang_huang
 * @purpose: get a list of author names from managa files
 *
 */
public class NameParser {

    /**
     * input an author name string and return an array of author names e.g
     * "生徒会室(あきもと大)" will return [生徒会室, あきもと大] abgrund (さいかわゆさ) will return
     * [abgrund,さいかわゆさ]
     *
     * @param the author name
     * @return an array of author names
     */
    public static ArrayList<String> getAuthorNameEntry(String s) {
        String[] names = s.split("\\[|\\]|\\(|\\)|,|、|&");
        //String[] names = s.split("\\[|\\]|\\(|\\)|,");
        ArrayList<String> result = new ArrayList<String>(names.length);
        for (String tempS : names) {

            if (tempS.length() < 1) {
                continue;
            }

            //trim and only allow one space internally
            result.add(tempS.trim().replaceAll(" {2,}", " "));
        }
        return result;
    }

    static Pattern brktPattern = Pattern.compile("\\[(.*?)\\]");

    /**
     * return author name from a file name e.g "(COMIC1☆7) [DUAL BEAT (柚木貴)]
     * LONESOME DUMMY (ザ·キング·オブ·ファイターズ).zip" will give "DUAL BEAT (柚木貴)"
     * "(サンクリ60) [abgrund (さいかわゆさ)] やばいと思ったがちー欲を抑えきれなかった・・・! (はたらく魔王さま!).zip"
     * will give "abgrund (さいかわゆさ)"
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
            ss = ss.replaceAll("\\[", "").replaceAll("\\]", "");

            if (!containWrongWord(ss)) {
                // System.out.println(ss.charAt(0));
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
        "汉化", "漢化", "English", "Chinese", "Korean", "中文", "한국",
        "アニメ", "anime", "Anime",
        "雑誌", "杂志",
        "Anthology", "アンソロジー", "Various", "よろず", "original", "オリジナル",
        "Artist", "アーティスト",
        "成年コミック", "コミック", "一般コミック", "COMIC",
        "Doujinshi", "Doujin", "同人", "同人", "doujin",
        "DL版",
        "Artbook", "画集",
        "COMIC1☆", "サンクリ", "[Digital]"
    };

    public static final Pattern wrongWordsPatten = Pattern.compile(NameParser.join(new ArrayList(Arrays.asList(wrongWords)), "|"));

    //exclude certain string
    public static boolean containWrongWord(String s) {
        //comiket (e.g c79, c82) is not an authour name

        //pure number is not an anthour e.g 101012
        if (s == null || (s.matches("[Cc][0-9]{2}|[0-9]+|[0-9\\-]+"))) {
            return true;
        }

//        Matcher m = wrongWordsPatten.matcher(s);
//        return m.matches();
        //should not contain any wrong word
        for (String w : wrongWords) {
            if (s.contains(w)) {
                return true;
            }
        }
        return false;

    }
    private static final String[] CompressionType = {
        "zip", "rar", "7zip", "pdf"
    };

    /**
     * decide if file is compressed file based on its extension
     */
    public static boolean isCompressionFile(String fileExtension) {
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
                    if (n1.equals(n2)) {
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
