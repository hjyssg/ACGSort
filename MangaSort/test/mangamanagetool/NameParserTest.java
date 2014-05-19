/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

import java.util.ArrayList;
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
        String s = "";
        ArrayList expResult = null;
        ArrayList result = NameParser.getAuthorNameEntry(s);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStringFromBrackets method, of class NameParser.
     */
    public void testGetAuthorName() {
        System.out.println("getAuthorName");
        String fn = "";
        String expResult = "";
        String result = NameParser.getStringFromBrackets(fn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isCompressionFile method, of class NameParser.
     */
    public void testIsCompressionFile() {
        System.out.println("isCompressionFile");
        String fileExtension = "";
        boolean expResult = false;
        boolean result = NameParser.isCompressionFile(fileExtension);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileExtension method, of class NameParser.
     */
    public void testGetFileExtension() {
        System.out.println("getFileExtension");
        String fileName = "";
        String expResult = "";
        String result = NameParser.getFileExtension(fileName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stringDistance method, of class NameParser.
     */
    public void testStringDistance() {
        System.out.println("stringDistance");
        String s1 = "";
        String s2 = "";
        int expResult = 0;
        int result = NameParser.stringDistance(s1, s2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
