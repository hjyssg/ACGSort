/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namepicker;

import java.util.Hashtable;
import junit.framework.TestCase;

/**
 *
 * @author pc
 */
public class NamePickerTest extends TestCase {
    
    NamePicker picker;
    
    public NamePickerTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        picker =  NamePicker.getInstace();
        assertTrue(picker.lastNames.isEmpty() == false);
        assertTrue(picker.names.isEmpty() == false);
        assertTrue(picker.firstNames.isEmpty() == false);

        for (String name: picker.names) {
            assertTrue( name.length() <= picker.FULLNAME_MAX_LEN);
            assertTrue( name.length() >= picker.FULLNAME_MIN_LEN);
        }

        for (String name: picker.firstNames) {
            assertTrue( name.length() <= picker.FIRSTNAME_MAX_LEN);
            assertTrue( name.length() >= picker.FIRSTNAME_MIN_LEN);
        }

        for (String name: picker.lastNames) {
            assertTrue( name.length() <= picker.LASTNAME_MAX_LEN);
            assertTrue( name.length() >= picker.LASTNAME_MIN_LEN);
        }
    }
    

    public void testFindNames() {

        String text = "上原亜衣のきに耐えたら10万円差し上げます 冬月かえでの、いっぱいコスって萌えてイこう！\n"
                + " 女子マネージャーは、僕達の 003 翼みさき.mkv 鈴村あいり.avi ゴスロリ痴女DOLL 3 佳苗るか\n"
                + " dap030ガチ催眠×有名レイヤー②　調教SEX編.mp  佳苗あいり  喜多村英梨　翼あいり  日向良平　櫻井浩美　　　　櫻井良平";

        Hashtable<String, Integer> result = picker.findNames(text);
        
        for (String name : result.keySet()) {
            System.out.println(name + "  " + result.get(name));
        }
        
        assertTrue(result != null);
         assertTrue(result.containsKey("上原亜衣"));
         assertTrue(result.containsKey("翼みさき"));
         assertTrue(result.containsKey("佳苗るか") );
         assertTrue(result.containsKey("佳苗あいり") );
         assertTrue(result.containsKey("喜多村英梨"));
         assertTrue(result.containsKey("翼あいり"));
         assertTrue(result.containsKey("日向良平") );
         assertTrue(result.containsKey("櫻井浩美"));
         assertTrue(result.containsKey("櫻井良平"));
    }

    public void testConvertTableIntoSortedArray() {
    }
    
}
