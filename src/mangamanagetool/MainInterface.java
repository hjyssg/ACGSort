/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mangamanagetool;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import javax.swing.*;
import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.json.simple.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

/**
 *
 * @author hjyssg
 */
public class MainInterface extends javax.swing.JFrame {

    private final String UNSORTED_FOLDER_PATH_PROPERTY = "unsorted folder path";
    private final String SORTED_FOLDER_PATH_PROPERTY = "sort folder path";
    private final String SETTING_FILE = "property.xml";
    private final String ANIME_KEYWORD_FILENAME = "keywords";
    private final String UNSORTED_FOLDER_SEARCH_ONE_LEVEL_ONLY_PROPERTY = "unsorted folder search one level only property";
    private final String UNSORTED_FOLDER_SEARCH_FILE_ON_PROPERTY = "unsorted folder search file on property";
    private final String UNSORTED_FOLDER_SEARCH_FOLDER_ON_PROPERTY = "unsorted folder search foldere on property";
    private final String NUMBER_BEFORE_MKDIR_PROPERTY = "number before mkdir property";
    private final String BLUR_SEARCH_PROPETY = "blur search proprty";
   

    //sorted -> str
    private Hashtable<String, String> keywords_table;
    private Hashtable<String, Pattern> keyword_patthen_table;
    private Hashtable<String, String> keyword_destination_table;
    private ArrayList<String> destinations;
    private MangaFileTable srtFiles;
    private MangaFileTable usrtFiles;
    

    private Logger LOG = Logger.getLogger(MainInterface.class.getName());
    private FileHandler fileText;

    private String launchTime;
    
    private String mvword ;

    static {
        try {
            InputStream inputStream = new FileInputStream("log.properties");
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Creates new form MainInterface
     */
    public MainInterface() {
        initComponents();
        loadUserSetting();

        DateFormat dateFormat = new SimpleDateFormat("MMM dd HH_mm");
        Date date = new Date();
        //launchTime = dateFormat.format(date);
        launchTime = "";
        
         if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
             mvword = "move";
    } else {
             mvword = "mv";
        // everything else
    } 
        
        try {
            //set up logger
            fileText = new FileHandler(launchTime + "_dev.log");
            LOG.addHandler(fileText);

            String tt = LogManager.getLogManager().getProperty(".level");
            Level l = Level.parse(tt);

            //set it to the global log level
            LOG.setLevel(l);

        } catch (IOException ex) {
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     private String temp_destination_for_keywords = "__UNDEFINED_FOLDER__";
    boolean loadKeywordsData() {
        try {
            keywords_table = new Hashtable();
            keyword_patthen_table = new Hashtable();
            keyword_destination_table = new Hashtable<String, String>();
            destinations = new ArrayList<String>();
            BufferedReader in =  new BufferedReader(  new InputStreamReader(  new FileInputStream(ANIME_KEYWORD_FILENAME), "UTF8"));
                    //new BufferedReader(new FileReader(ANIME_KEYWORD_FILENAME));

            boolean regex_on = false;

            while (in.ready()) {

                String s = in.readLine();

                //must be longer than 4
                if (s.length() < 4) {
                    continue;
                } else if (s.charAt(0) == '#' || (s.charAt(0) == '\\' && s.charAt(1) == '\\')) {
                    //support # or // as comment
                    continue;
                } else if (s.indexOf("__DESTINATION_FOLDER__:") >= 0) {
                    //__DESTINATION_FOLDER__:  setting
                    temp_destination_for_keywords = s.replaceAll("__DESTINATION_FOLDER__:", "").trim();
                    continue;
                } else if (s.contains("__ENABLE_REGEX__")) {
                    regex_on = true;//for regex region
                    continue;
                } else if (s.contains("__DISABLE_REGEX__")) {
                    regex_on = false;
                    continue;
                }

                //split and get token array
                ArrayList<String> tokens = new ArrayList();
                for (String temp : s.split("\"|,")) {
                    if (temp.length() > 1) {
                        tokens.add(temp.trim());
                    }
                }

                String key;
                if (regex_on) {
                    key = tokens.get(0);
                } else {
                    key = tokens.get(0).toLowerCase();
                }

                //do not overwrite the existing fn
                if (keywords_table.containsKey(key)) {
                    continue;
                }

                if (tokens.size() == 1) {
                    keywords_table.put(key, tokens.get(0));
                } else if (tokens.size() == 2) {
                    keywords_table.put(key, tokens.get(1));
                }

                keyword_destination_table.put(key, temp_destination_for_keywords);
                if (!destinations.contains(temp_destination_for_keywords)){destinations.add(temp_destination_for_keywords);}
                
                if (regex_on) {
                    LOG.info(key + "   " + tokens.get(1));
                    keyword_patthen_table.put(key, Pattern.compile(key, Pattern.CASE_INSENSITIVE));
                }
            }
            in.close();

        } catch (Exception e) {
            Logger.getLogger(MainInterface.class.getName()).severe("Fail to load keyword data");
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    /*
     * load User Setting into UI
     */
    private void loadUserSetting() {
        try {
            FileInputStream in = new FileInputStream(SETTING_FILE);
            Properties userSetting = new Properties();
            userSetting.loadFromXML(in);

            //xml parser
            XStream xstream = new XStream(new DomDriver());

            this.scanFileBox.setSelected(userSetting.getProperty(UNSORTED_FOLDER_SEARCH_FILE_ON_PROPERTY).equals("yes") ? true : false);
            this.scanFolderBox.setSelected(userSetting.getProperty(UNSORTED_FOLDER_SEARCH_FOLDER_ON_PROPERTY).equals("yes") ? true : false);
            this.oneLevelCheckBox.setSelected(userSetting.getProperty(UNSORTED_FOLDER_SEARCH_ONE_LEVEL_ONLY_PROPERTY).equals("yes") ? true : false);
            this.blurMatchingCheckBox.setSelected(userSetting.getProperty(BLUR_SEARCH_PROPETY).equals("yes") ? true : false);

            Integer tempI = Integer.parseInt(userSetting.getProperty(NUMBER_BEFORE_MKDIR_PROPERTY).toString());

            this.createDirSpinner.setValue(tempI);

            //get path 
            if (userSetting.getProperty(UNSORTED_FOLDER_PATH_PROPERTY) != null) {
                Object obj = JSONValue.parse(userSetting.getProperty(UNSORTED_FOLDER_PATH_PROPERTY));
                JSONArray array = (JSONArray) obj;

                //and add to jlist ui
                for (Object o : array) {
                    String s = (String) o;
                    if (s != null) {
                        getDefaultListModel(unsortedFolderList).addElement(s);
                    }
                }
            }

            //get path a
            if (userSetting.getProperty(SORTED_FOLDER_PATH_PROPERTY) != null) {
                Object obj = JSONValue.parse(userSetting.getProperty(SORTED_FOLDER_PATH_PROPERTY));
                JSONArray array = (JSONArray) obj;
                //and nd add to jlist
                for (Object o : array) {
                    String s = (String) o;
                    if (s != null) {
                        getDefaultListModel(sortedFolderList).addElement(s);
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            Logger.getLogger(MainInterface.class.getName()).severe("Fail to load user setting");
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * save User Setting from UI
     */
    private void saveUserSetting() {
        try {
            String unsortedPathString = "";
            String sortedPathString = "";
            Properties userSetting = null;

            LinkedList unsortedPathlist = new LinkedList();
            LinkedList sortedPathlist = new LinkedList();

            if (this.rememberBox.isSelected()) {

                Object[] tempUnsortedPathArr = getDefaultListModel(unsortedFolderList).toArray();
                String[] unsortedPathArr = Arrays.copyOf(tempUnsortedPathArr, tempUnsortedPathArr.length, String[].class);

                Object[] tempSortedArr = getDefaultListModel(sortedFolderList).toArray();
                String[] sortedPathArr = Arrays.copyOf(tempSortedArr, tempSortedArr.length, String[].class);

                for (String s : unsortedPathArr) {
                    unsortedPathlist.add(s);
                }
                for (String s : sortedPathArr) {
                    sortedPathlist.add(s);
                }
            }
            userSetting = new Properties();
            userSetting.setProperty(UNSORTED_FOLDER_PATH_PROPERTY, JSONValue.toJSONString(unsortedPathlist));
            userSetting.setProperty(SORTED_FOLDER_PATH_PROPERTY, JSONValue.toJSONString(sortedPathlist));

            userSetting.setProperty(UNSORTED_FOLDER_SEARCH_FILE_ON_PROPERTY, (this.scanFileBox.isSelected() ? "yes" : "no"));
            userSetting.setProperty(UNSORTED_FOLDER_SEARCH_FOLDER_ON_PROPERTY, (this.scanFolderBox.isSelected() ? "yes" : "no"));
            userSetting.setProperty(UNSORTED_FOLDER_SEARCH_ONE_LEVEL_ONLY_PROPERTY, (this.oneLevelCheckBox.isSelected() ? "yes" : "no"));
            userSetting.setProperty(BLUR_SEARCH_PROPETY, this.blurMatchingCheckBox.isSelected() ? "yes" : "no");
            userSetting.setProperty(NUMBER_BEFORE_MKDIR_PROPERTY, this.createDirSpinner.getValue().toString());

            FileOutputStream out = new FileOutputStream(SETTING_FILE);
            userSetting.storeToXML(out, "Setting of user's folders");

            out.close();
        } catch (Exception e) {
            Logger.getLogger(MainInterface.class.getName()).severe("Fail to save user setting");
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        runButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        rememberBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        unsortedFolderList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        sortedFolderList = new javax.swing.JList();
        blurMatchingCheckBox = new javax.swing.JCheckBox();
        oneLevelCheckBox = new javax.swing.JCheckBox();
        scanFileBox = new javax.swing.JCheckBox();
        createDirSpinter = new javax.swing.JSpinner();
        createDirSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        scanFolderBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Manga Sorter"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        runButton.setText("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonClicked(evt);
            }
        });

        jButton1.setText("Add Folders Where Unsorted Files Are (Required)");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseWhereUnsortedFilesAre(evt);
            }
        });

        jButton2.setText("Add Folders Where Sorted Folders Are (Opitional)");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseWhereSortedFIlesAre(evt);
            }
        });

        rememberBox.setSelected(true);
        rememberBox.setText("Remember Setting");
        rememberBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rememberBoxActionPerformed(evt);
            }
        });

        unsortedFolderList.setModel(new DefaultListModel());
        unsortedFolderList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        unsortedFolderList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                unsortedFolderListKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(unsortedFolderList);

        sortedFolderList.setModel(new DefaultListModel());
        sortedFolderList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        sortedFolderList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sortedFolderListKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(sortedFolderList);

        blurMatchingCheckBox.setText("Blur Matching");
        blurMatchingCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blurMatchingCheckBoxActionPerformed(evt);
            }
        });

        oneLevelCheckBox.setText("One Level Travesal");
        oneLevelCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneLevelCheckBoxActionPerformed(evt);
            }
        });

        scanFileBox.setSelected(true);
        scanFileBox.setText("Scan File");
        scanFileBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanFileBoxActionPerformed(evt);
            }
        });

        createDirSpinter.setPreferredSize(new java.awt.Dimension(1, 30));

        createDirSpinner.setFocusTraversalPolicyProvider(true);
        createDirSpinner.setMaximumSize(new java.awt.Dimension(20, 20));
        createDirSpinner.setMinimumSize(new java.awt.Dimension(0, 0));
        createDirSpinner.setName(""); // NOI18N
        createDirSpinner.setValue(2);

        jLabel1.setText("# of Files before mkdir");

        scanFolderBox.setSelected(true);
        scanFolderBox.setText("Scan Folder");
        scanFolderBox.setToolTipText("");
        scanFolderBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanFolderBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton1)
                                        .addGap(18, 18, 18)
                                        .addComponent(oneLevelCheckBox)
                                        .addGap(41, 41, 41)
                                        .addComponent(scanFileBox)
                                        .addGap(34, 34, 34)
                                        .addComponent(scanFolderBox))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(createDirSpinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createDirSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123)
                        .addComponent(blurMatchingCheckBox)
                        .addGap(37, 37, 37)
                        .addComponent(rememberBox)
                        .addGap(60, 60, 60)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(runButton, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(oneLevelCheckBox)
                    .addComponent(scanFileBox)
                    .addComponent(scanFolderBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addGap(22, 22, 22)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(runButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rememberBox)
                            .addComponent(blurMatchingCheckBox)
                            .addComponent(createDirSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(createDirSpinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //a static vairable for chooseFile method
    private File lastTimeChoice = null;


    /*
     * a method for file choosing
     */
    private String chooseFile() {
        //Create a file chooser
        JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setApproveButtonText("choose");
        chooser.setCurrentDirectory(lastTimeChoice);

        //In response to a button click:
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            LOG.info("You choose to open this file: " + chooser.getSelectedFile());
            lastTimeChoice = chooser.getSelectedFile().getParentFile();
            return chooser.getSelectedFile().getPath();
        } else {
            return null;
        }
    }

    /**
     * @brief A small method to get string array from jlist
     *
     * @param JList l
     * @return string array
     */
    String[] getPaths(JList l) {
        Object[] tempArr = getDefaultListModel(l).toArray();
        return Arrays.copyOf(tempArr, tempArr.length, String[].class);
    }

    /*
     * the most important method of this class
     * read folder and then do the matching
     */
    private void runButtonClicked(java.awt.event.ActionEvent evt)//GEN-FIRST:event_runButtonClicked
    {
        //save user setting
        if (this.rememberBox.isSelected()) {
            this.saveUserSetting();
        }
        //disable ui during calculation
        this.runButton.setEnabled(false);
        
        long t1, t2, tt;

        t1 =  System.nanoTime();
        //scanning folder and save class member
        scanFolders();
        t2 = System.nanoTime(); tt = t2 - t1;
        System.out.printf( "%.4fS  to scan Folder\n" , tt/1e+9);
        
         t1 =  System.nanoTime();
        loadKeywordsData();
        t2 = System.nanoTime();  tt = t2 - t1;
        System.out.printf( "%.4fS  to load keywords\n" , tt/1e+9);
        
         t1 =  System.nanoTime();
        matchWithKeywordAndOutputFile();
          t2 = System.nanoTime();   tt = t2 - t1;
        System.out.printf( "%.4fS  to match with keywords\n" , tt/1e+9);

        t1 =  System.nanoTime();
        matchByFindingAuthorNameAndOutputFile();
         t2 = System.nanoTime();
        tt = t2 - t1;
        System.out.printf( "%.4fS  to match with authorname\n" , tt/1e+9);

        System.out.println("calculation: done");
        this.runButton.setEnabled(true);
    }//GEN-LAST:event_runButtonClicked

    /*
     * choose folder for unsorted list
     */
    private void chooseWhereUnsortedFilesAre(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chooseWhereUnsortedFilesAre
    {//GEN-HEADEREND:event_chooseWhereUnsortedFilesAre
        String file = chooseFile();
        getDefaultListModel(unsortedFolderList).addElement(file);
        if (this.rememberBox.isSelected()) {
            this.saveUserSetting();
        }
    }//GEN-LAST:event_chooseWhereUnsortedFilesAre

    /*
     * choose folder for sorted list
     */
    private void ChooseWhereSortedFIlesAre(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseWhereSortedFIlesAre
        String file = chooseFile();
        getDefaultListModel(sortedFolderList).addElement(file);
        if (this.rememberBox.isSelected()) {
            this.saveUserSetting();
        }
    }//GEN-LAST:event_ChooseWhereSortedFIlesAre

    /*
     * called when program closing
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.saveUserSetting();
    }//GEN-LAST:event_formWindowClosing

    /*
     *  delete list item from unsortedFolderList
     */
    private void unsortedFolderListKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_unsortedFolderListKeyReleased
    {//GEN-HEADEREND:event_unsortedFolderListKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int index = unsortedFolderList.getSelectedIndex();
            getDefaultListModel(unsortedFolderList).removeElementAt(index);
        }
    }//GEN-LAST:event_unsortedFolderListKeyReleased

    /*
     * delete list item from sortedFolderList
     */
    private void sortedFolderListKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_sortedFolderListKeyReleased
    {//GEN-HEADEREND:event_sortedFolderListKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int index = sortedFolderList.getSelectedIndex();
            getDefaultListModel(sortedFolderList).removeElementAt(index);
        }
    }//GEN-LAST:event_sortedFolderListKeyReleased

    private void blurMatchingCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blurMatchingCheckBoxActionPerformed
        if (this.rememberBox.isSelected()) {
            this.saveUserSetting();
        }
    }//GEN-LAST:event_blurMatchingCheckBoxActionPerformed

    private void oneLevelCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneLevelCheckBoxActionPerformed

        if (this.rememberBox.isSelected()) {
            this.saveUserSetting();
        }
    }//GEN-LAST:event_oneLevelCheckBoxActionPerformed

    private void scanFileBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanFileBoxActionPerformed
        if (this.rememberBox.isSelected()) {
            this.saveUserSetting();
        }
    }//GEN-LAST:event_scanFileBoxActionPerformed

    private void scanFolderBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanFolderBoxActionPerformed
        if (this.rememberBox.isSelected()) {
            this.saveUserSetting();
        }
    }//GEN-LAST:event_scanFolderBoxActionPerformed

    private void rememberBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rememberBoxActionPerformed
        if (this.rememberBox.isSelected()) {
            this.saveUserSetting();
        }
    }//GEN-LAST:event_rememberBoxActionPerformed

    /**
     * a simple utility to get the defaultListModel of JList
     *
     * @param list
     * @return DefaultListModel
     */
    private DefaultListModel getDefaultListModel(JList list) {
        return (DefaultListModel) list.getModel();
    }

    String sortedPath = null;

    private void scanFolders() {
        usrtFiles = new MangaFileTable();
        usrtFiles.oneLevel = this.oneLevelCheckBox.isSelected();
        usrtFiles.folderOn = this.scanFolderBox.isSelected();
        usrtFiles.fileOn = this.scanFileBox.isSelected();
        usrtFiles.compressedFileOnly = true;
        usrtFiles.getStringFromBracketsForFolder = true;

        srtFiles = new MangaFileTable();
        srtFiles.fileOn = false;
        srtFiles.folderOn = true;
        srtFiles.oneLevel = true;
        srtFiles.getStringFromBracketsForFolder = false;

        System.out.println("LOG Level: "+LOG.getLevel());
        boolean lg = LOG.getLevel().intValue() >= Level.INFO.intValue() || LOG.getLevel().intValue() == Level.ALL.intValue();
        lg = lg && LOG.getLevel().intValue() != Level.OFF.intValue();

        //scan unsorted folders
        try {
            String[] unsortedPathArr = getPaths(unsortedFolderList);
            usrtFiles.addFolders(unsortedPathArr);
            System.out.println("unsorted folders scanning: done");
            LOG.info("unsorted folders scanning: done");

            //write result into text file
            //System.out.println((LOG.getLevel().intValue() + "  " + Level.INFO.intValue()));
            if (lg) {
                String saveFolder = System.getProperty("user.dir");
                PrintWriter out = new PrintWriter(saveFolder + "\\" + launchTime + "_unsorted_files.log", "UTF-8");
                out.print(usrtFiles.toString());
                out.close();

                saveFolder = System.getProperty("user.dir");
                out = new PrintWriter(saveFolder + "\\" + launchTime + "_iterated_unsorted_files.log", "UTF-8");
                StringBuilder temp = new StringBuilder();
                for (File f : usrtFiles.allIteratedFiles) {
                    temp.append(f.getPath()).append("\n");
                }
                out.print(temp);
                out.close();
            }
        } catch (Exception e) {
            Logger.getLogger(MainInterface.class.getName()).severe("error during scanning unsorted folder");
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, e);
            this.runButton.setEnabled(true);
        }

        try {
            String[] sortedPathArr = getPaths(sortedFolderList);
            srtFiles.addFolders(sortedPathArr);
            LOG.info("sorted folders scanning: done");
            System.out.println("sorted folders scanning: done");

            if (sortedPathArr.length > 0 && sortedPathArr[0] != null) {
                sortedPath = sortedPathArr[0];
            }

            if (lg) {
                //srtFiles.debugDisplay();
                String saveFolder = System.getProperty("user.dir");
                PrintWriter out = new PrintWriter(saveFolder + "\\" + launchTime + "_sorted_file_log.log", "UTF-8");
                out.print(srtFiles.toString());
                out.close();
            }
        } catch (Exception e) {
            Logger.getLogger(MainInterface.class.getName()).severe("error during scanning unsorted folder");
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, e);
            this.runButton.setEnabled(true);
        }

    }
    


    private void matchWithKeywordAndOutputFile() {

        //run the matching algo
        try {
            
            System.out.println("there are " +usrtFiles.allIteratedFiles.size() + " files" );
            
            Hashtable<String,StringBuilder> strBuffers = new Hashtable<String, StringBuilder>();
            for(String dest : destinations)
            {
                  StringBuilder temp = new StringBuilder();
                  strBuffers.put(dest, temp);
            }
            
            
            for (File file : usrtFiles.allIteratedFiles) {
                String fn = file.getName().toLowerCase();
               
                //compare it with each keyword
                for (String key :  keywords_table.keySet()) {
                    boolean regex_on = keyword_patthen_table.containsKey(key);
                    Matcher m = null;

                    if (regex_on) {
                        LOG.finest("going to use keyword:" + key +" to " + fn);
                        Pattern p = keyword_patthen_table.get(key);
                        m = p.matcher(fn);
                    }

                    //mvStr.append("going to use keyword:" + key +" to " + fn).append("\n");
                    
                    //use string contains or regex to match
                    if ((regex_on == false && fn.contains(key) || (regex_on) && m != null && m.find())) {
                        
                        String destDir = keyword_destination_table.get(key);
                        String dest = keywords_table.get(key);
                       
                       // System.out.println(destDir);
                       StringBuilder mvStr = strBuffers.get(destDir);
                        
                        destDir = destDir + '\\' + dest;
                        
                        mvStr.append("mkdir \"").append(destDir).append("\" \n");
                        mvStr.append(mvword).append(" \"").append(file.getPath()).append("\" \"").append(destDir).append("\"\n");
                        break;
                    }
                }
            }

            //write result into text files
            String saveFolder = System.getProperty("user.dir");
            PrintWriter out = new PrintWriter(saveFolder + "\\" + "cmd_by_keywords.txt", "UTF-8");
            //out.print(mvStr.toString());
            
            for (StringBuilder strb : strBuffers.values()) {
                 out.print(strb.toString());
                 out.print("\n\n\n\n");
            }
            
            out.close();
        } catch (Exception e) {
            Logger.getLogger(MainInterface.class.getName()).severe("error during scanning calculation");
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * @brief finding the author names from scanned file
     * @details finding the author names from scanned file. Match them and
     * output command file
     */
    private void matchByFindingAuthorNameAndOutputFile() {

        try {

            //get the author of unsorted files     
            ArrayList<String> unsortedAuthorNames = new ArrayList(usrtFiles.keySet());

            //sort them for more readable output
            Collections.sort(unsortedAuthorNames);

            int mvCounter = 0;
            int mkdirCounter = 0;

            //command to make new folder 
            //windows and mac both use "mkdir" to make new directory
            StringBuilder mkdrirBuilder = new StringBuilder();

            //command to move files one windows
            StringBuilder mvCmd = new StringBuilder();

            //command to move file on mac and linux
            StringBuilder mkdirCmd = new StringBuilder();

            int numBeforeMkdir = (Integer) this.createDirSpinner.getValue();

            if (sortedPath == null) {
                sortedPath = "";
            }

            for (String unsortedAuthorName : unsortedAuthorNames) {
                //check if there already exist a folder for the file
                AuthorInfo entry = usrtFiles.get(unsortedAuthorName);
                if (entry == null) {
                    LOG.warning("!!!" + unsortedAuthorName + " HAS NO ENTRY");
                    continue;
                }

                ArrayList<String> names = entry.names;
                File destFolder = getAuthorFolder(unsortedAuthorName, names);
                ArrayList<File> unsortedFiles = usrtFiles.get(unsortedAuthorName).files;
                int occurence = unsortedFiles.size();

                //tell user to move the file if the sorted folder is existing
                if (destFolder != null) {

                    LOG.finest("FOUND MATHCH" + unsortedAuthorName + "   " + destFolder);
                    for (File sourceFile : unsortedFiles) {
                        StringBuilder line = new StringBuilder();
                        line.append(sourceFile.getPath()).append("\" \"").append(destFolder.getPath()).append("\"\n\r");
                        mvCmd.append(mvword).append(" \"").append(line);
                        mvCounter++;
                    }
                } else if (occurence >= numBeforeMkdir) {
                    //if folder  does not exist and this author have more than
                    //tell user to create one
                    mkdirCmd.append("mkdir \"").append(sortedPath).append("\\").append(unsortedAuthorName).append("\"\n\r");
                    mkdirCounter++;

                    for (File sourceFile : unsortedFiles) {
                        StringBuilder line = new StringBuilder();
                        line.append(sourceFile.getPath()).append("\" \"").append(sortedPath).append("\\").append(unsortedAuthorName).append("\"\n\r");
                        mvCmd.append(mvword).append(" \"").append(line);
                        mvCounter++;
                    }
                }
            }

            //write result into text files
            String saveFolder = System.getProperty("user.dir");

            mkdirCmd.insert(0, "#Need to create " + mkdirCounter + " new folders\n\r");
            mvCmd.insert(0, "\n\r\n\r#Need to move files " + mvCounter + " times\n\r");

            PrintWriter out = new PrintWriter(saveFolder + "\\" + "cmd_by_author_name.txt", "UTF-8");
            out.print(mkdirCmd.toString());
            out.print(mvCmd.toString());
            out.close();

            //open the folder in the file exploer
            java.awt.Desktop.getDesktop().open(new File(saveFolder));
        } catch (Exception e) {
            Logger.getLogger(MainInterface.class.getName()).severe("Fail to load keyword data");
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * @param sourceName
     * @param sourceNames
     * @brief Find the author folder for the given name
     * @details [long description]
     * @return the folder url, null if no exitence
     */
    public File getAuthorFolder(String sourceName, ArrayList<String> sourceNames) {

        //if we can find directly, nice
        if (srtFiles.containsKey(sourceName)) {
            LOG.finest("FOUND" + srtFiles.get(sourceName));
            return srtFiles.get(sourceName).directory;
        }

        //if not, compare all authors names
        for (AuthorInfo entry : srtFiles.values()) {
            if (NameParser.isTwoNamesEqual(entry.names, sourceNames, this.blurMatchingCheckBox.isSelected())) {
                LOG.finest(entry.names + "  " + sourceNames);
                return entry.directory;
            }
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainInterface().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox blurMatchingCheckBox;
    private javax.swing.JSpinner createDirSpinner;
    private javax.swing.JSpinner createDirSpinter;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox oneLevelCheckBox;
    private javax.swing.JCheckBox rememberBox;
    private javax.swing.JButton runButton;
    private javax.swing.JCheckBox scanFileBox;
    private javax.swing.JCheckBox scanFolderBox;
    private javax.swing.JList sortedFolderList;
    private javax.swing.JList unsortedFolderList;
    // End of variables declaration//GEN-END:variables
}
