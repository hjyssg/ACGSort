package namepicker;

import java.util.Hashtable;

public class NamePickerProj {

    public static void main(String[] args) {
        String basePath = System.getProperty("user.dir") + "\\src\\resources\\";

        String LASTNAMES_FN = basePath + "Jp_Family_Names.txt";
        String[] fns = {basePath + "AV_Atress_Names.txt",
            basePath + "Male_Seiyuu_Names.txt",
            basePath + "Female_Seiyuu_Names.txt",
            basePath + "Historic_People.txt"};

        NamePicker picker = new NamePicker(LASTNAMES_FN, fns);
        String lines = "REAL-535 上原亜衣のチ●ポしごきに耐えたら10万円差し上げます\n"
                + " ABP-266 冬月かえでの、いっぱいコスって萌えてイこう！\n"
                + " ABP-269  女子マネージャーは、僕達の性処理ペット。 003 翼みさき.mkv\n"
                + " abp109 おもてなし庵 純情小町 鈴村あいり.avi\n"
                + " ATFB-253 ゴスロリ痴女DOLL 3 佳苗るか\n"
                + " dap030ガチ催眠×有名レイヤー②　調教SEX編.mp  佳苗あいり  喜多村英梨　翼あいり  日向良平　櫻井浩美　　　　櫻井良平";

        Hashtable<String, Integer> result = picker.findNames(lines);
        for (String name : result.keySet()) {
            System.out.println(name + "  " + result.get(name));
        }
        
        assert result.containsKey("上原亜衣");
        assert result.containsKey("翼みさき");
        assert result.containsKey("佳苗るか");
        assert result.containsKey("佳苗あいり");
        assert result.containsKey("喜多村英梨");
        assert result.containsKey("翼あいり");
        assert result.containsKey("日向良平");
        
        String[] names = NamePicker.convertTableIntoSortedArray(result);
        assert names[0].equals("上原亜衣");
        assert names[1].equals("冬月かえで");
        
        
    }
}
