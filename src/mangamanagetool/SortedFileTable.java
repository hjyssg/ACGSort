
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


public class SortedFileTable
{
    /*
     * the folders that contain author's folder
     */
    public URL sortedFileFolder;
   

    /*
     * sorted author list
     * the first element is the folder urL
     * the rest are the author's name. each authot may have mutilpe names
     * e.g 鳥居姫, 魚骨工造 (カポ), 怪奇日蝕(綾野なおと)
     */
    private ArrayList<ArrayList<Object>> eaList;
    
    public SortedFileTable(URL sortedFolderURL)
    {
        this.eaList = new ArrayList<ArrayList<Object>>();
        this.sortedFileFolder = sortedFolderURL;
        this.updateForSingleURL(sortedFolderURL);
    }
    
    
    private void updateForSingleURL(URL url)
    {
        try
        {
            File rootFolder = new File(url.toURI());

            //iterate through each author's folder
            for (File sf : rootFolder.listFiles())
            {
                //...and find author name, save into  ealist
                if (sf.isDirectory() && (!sf.isHidden()))
                {
                    ArrayList<Object> entry = new ArrayList<Object>(2);
                    URL tempUrl = sf.toURI().toURL();
                    
                    entry.add(tempUrl);
                    
                    entry.addAll(NameParser.getAuthorNameEntry(sf.getName()));
                    eaList.add(entry);
                }
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
    


    //@param s: author name
    //@return the folder url, null if no exitence
    public URL getAuthorFolderURL(String s)
    {
        for (int ii = 0; ii < eaList.size(); ii++)
        {
            ArrayList<Object> entry = eaList.get(ii);
            for (int jj = 1; jj < entry.size(); jj++)
            {
                String tempS = (String) entry.get(jj);
                // will modify here later
                int editDistance = NameParser.stringDistance(tempS, s);
                if (editDistance < 2)
                {
                    return (URL) entry.get(0);
                }
            }
        }
        return null;
    }
    
    public void debugDisplay()
    {
        for (ArrayList<Object> entry : eaList)
        {            
            for (Object object : entry)
            {
                System.out.print(object.toString() + "   ");
            }
            System.out.println();
        }
    }
}
