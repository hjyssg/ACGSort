/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

/**
 *
 * @author junyang
 */
import java.util.*;
import java.io.*;

public class AuthorInfo 
{
    /*
     * Each author may have mutlipe names, especially for doujin sakka
     * e.g e.g 有葉と愉快な仲間たち (有葉), Fatalpulse (朝凪)
     * We use hashSet because we do not need the order of name 
     */
    public ArrayList<String> names;
    
    /*
     * Each author may have mutiple books
     * We use hashSet because we do not need the order of name 
     * and increase potential performance
     */
    public ArrayList<File> files;
    
    
    /*
     * Where you put the author's file
     */
     public File directory;

    @Override
    public String toString() {
        return "AuthorInfo{" + "names=" + names + ", files=" + files + ", directory=" + directory + '}';
    }
     
     
     
     
}
