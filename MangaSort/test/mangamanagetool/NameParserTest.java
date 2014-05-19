/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import junit.framework.TestCase;
/**
 *
 * @author junyang
 */
public class NameParserTest extends TestCase {
    
    public NameParserTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getStringFromBracketsEntry method, of class NameParser.
     */
    public void testGetAuthorNameEntry() {
        System.out.println("getAuthorNameEntry");
        String s = "apple[pear]";
        ArrayList expResult = new ArrayList();
        expResult.add("apple");
        expResult.add("pear");
        ArrayList result = NameParser.getAuthorNameEntry(s);
        assertEquals(expResult, result);
    }

    
    public void testContainWrongWord()
    {
         System.out.println("testcontainWrongWord");
         
         //System.out.println(NameParser.wrongWordsPatten);
         
          String s = "[成年コミック]komic eros";
         boolean  expResult = true;
          boolean result = NameParser.containWrongWord(s);
          assertEquals(expResult, result);
         
          s = "[doujin asdasfdaskomic eros";
          expResult = true;
          result = NameParser.containWrongWord(s);
          assertEquals(expResult, result);

          s = "10-10-1123";
          expResult = true;
          result = NameParser.containWrongWord(s);
          assertEquals(expResult, result);

            s = "104654564101123";
          expResult = true;
          result = NameParser.containWrongWord(s);
          assertEquals(expResult, result);
       

          s = "リリム";
          expResult = false;
          result = NameParser.containWrongWord(s);
          assertEquals(expResult, result);

    }
    
    public void testJoin()
    {
       System.out.println("join");
        String [] t = {"apple", "cake", "php"};
        ArrayList<String> ss = new ArrayList<String>(Arrays.asList(t));
        String delimeter = "|";
        String expResult = "apple|cake|php";
        String result = NameParser.join(ss, delimeter);
        
         assertEquals(expResult, result); 
    }


    /**
     * Test of getStringFromBrackets method, of class NameParser.
     */
    public void testgetStringFromBrackets() {
        System.out.println("getAuthorName");
        String fn = "[ホノカチャン]、]かわい";
        String expResult = "ホノカチャン";
        String result = NameParser.getStringFromBrackets(fn);
        assertEquals(expResult, result);


        fn = "[101023]、]かわい";
        expResult = null;
        result = NameParser.getStringFromBrackets(fn);
        assertEquals(expResult, result);

        fn = "[14-10-23]、]かわい";
        expResult = null;
        result = NameParser.getStringFromBrackets(fn);
        assertEquals(expResult, result);
    }

    /**
     * Test of isCompressionFile method, of class NameParser.
     */
    public void testIsCompressionFile() {
        System.out.println("isCompressionFile");
        String fileExtension = "docx";
        boolean expResult = false;
        boolean result = NameParser.isCompressionFile(fileExtension);
        assertEquals(expResult, result);

        fileExtension = "zip";
        expResult = true;
        result = NameParser.isCompressionFile(fileExtension);
        assertEquals(expResult, result);

        fileExtension = "rar";
        expResult = true;
        result = NameParser.isCompressionFile(fileExtension);
        assertEquals(expResult, result);

    }

    /**
     * Test of getFileExtension method, of class NameParser.
     */
    public void testGetFileExtension() {
        System.out.println("getFileExtension");
        String fileName = "NameParserTest.java";
        String expResult = "java";
        String result = NameParser.getFileExtension(fileName);
        assertEquals(expResult, result);

        fileName = " (蒲田鎮守府弐) [日々鳥々 (日鳥)] 大体こんな日常 (艦隊これくしょん -艦これ-).zip";
        expResult = "zip";
        result = NameParser.getFileExtension(fileName);
        assertEquals(expResult, result);

    }

    /**
     * Test of stringDistance method, of class NameParser.
     */
    public void testStringDistance() {
        System.out.println("stringDistance");
        String s1 = "54615616あpp";
        String s2 = "54115616あkp1";
        int expResult = 3;
        int result = NameParser.stringDistance(s1, s2);
        assertEquals(expResult, result);


        s1 = "54615616あpp";
        s2 = "54615616あkp";
        expResult = 1;
        result = NameParser.stringDistance(s1, s2);
        assertEquals(expResult, result);

        s1 = "らぶらいぶ";
        s2 = "らぶらいぶ";
        expResult = 0;
        result = NameParser.stringDistance(s1, s2);
        assertEquals(expResult, result);
    }

    /**
     * Test of isTwoNamesEqual method, of class NameParser.
     */
    public void testIsTwoNamesEqual() {
        System.out.println("isTwoNamesEqual");
        
        String[] a1 = {"a", "b"};
        String[] b2 = {"b", "1"};
         
        ArrayList<String> l1 = new ArrayList<String>(Arrays.asList(a1));
        ArrayList<String> l2 =new ArrayList<String>(Arrays.asList(b2));
        boolean blur = false;
        boolean expResult = true;
        boolean result = NameParser.isTwoNamesEqual(l1, l2, blur);
        assertEquals(expResult, result);

    }
}
