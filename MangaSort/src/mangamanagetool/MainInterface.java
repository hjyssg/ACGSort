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
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author jhuang
 */
public class MainInterface extends javax.swing.JFrame {
    
    //private boolean LOG1 = false;
    private boolean LOG1 = true;

    private final String UNSORTED_FOLDER_PATH_PROPERTY = "unsorted folder path";
    private final String SORTED_FOLDER_PATH_PROPERTY = "sort folder path";
    private final String SETTING_FILE = "property.xml";
    private final String UNSORTED_FOLDER_SEARCH_ONE_LEVEL_ONLY_PROPERTY = "unsorted folder search one level only property";
    private final String UNSORTED_FOLDER_SEARCH_FILE_ONLY_PROPERTY = "unsorted folder search file only property";
    private final String NUMBER_BEFORE_MKDIR_PROPERTY = "number before mkdir property";
    private final String BLUR_SEARCH_PROPETY = "blur search proprty";

    //sorted -> str
    private MangaFileTable srtFiles;
    private MangaFileTable usrtFiles;

    /**
     * Creates new form MainInterface
     */
    public MainInterface() {

        initComponents();
        loadUserSetting();
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

            this.FileOnlyCheckBox.setSelected(userSetting.getProperty(UNSORTED_FOLDER_SEARCH_FILE_ONLY_PROPERTY).equals("yes") ? true : false);
            this.oneLevelCheckBox.setSelected(userSetting.getProperty(UNSORTED_FOLDER_SEARCH_ONE_LEVEL_ONLY_PROPERTY).equals("yes") ? true : false);
            this.blurMatchingCheckBox.setSelected(userSetting.getProperty(BLUR_SEARCH_PROPETY).equals("yes") ? true : false);

            Integer tempI = Integer.parseInt(userSetting.getProperty(NUMBER_BEFORE_MKDIR_PROPERTY).toString());

            this.createDirSpinner.setValue(tempI);

            if (userSetting.getProperty(UNSORTED_FOLDER_PATH_PROPERTY) != null) {
                String[] unsortedPathArr = (String[]) xstream.fromXML(userSetting.getProperty(UNSORTED_FOLDER_PATH_PROPERTY));
                for (String s : unsortedPathArr) {
                    if (s != null) {
                        getDefaultListModel(unsortedFolderList).addElement(s);
                    }
                }
            }

            if (userSetting.getProperty(SORTED_FOLDER_PATH_PROPERTY) != null) {
                String[] sortedPathArr = (String[]) xstream.fromXML(userSetting.getProperty(SORTED_FOLDER_PATH_PROPERTY));
                for (String s : sortedPathArr) {
                    if (s != null) {
                        getDefaultListModel(sortedFolderList).addElement(s);
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fail to load user setting");
        }
    }

    /**
     * save User Setting from UI
     */
    private void saveUserSetting() {
        try {
            String unsortedPath = "";
            String sortedPath = "";
            Properties userSetting = null;

            if (this.rememberBox.isSelected()) {
                Object[] tempUnsortedPathArr = getDefaultListModel(unsortedFolderList).toArray();
                String[] unsortedPathArr = Arrays.copyOf(tempUnsortedPathArr, tempUnsortedPathArr.length, String[].class);

                Object[] tempSortedArr = getDefaultListModel(sortedFolderList).toArray();
                String[] sortedPathArr = Arrays.copyOf(tempSortedArr, tempSortedArr.length, String[].class);

                //init xml generator
                XStream xstream = new XStream(new StaxDriver());

                unsortedPath = xstream.toXML(unsortedPathArr);
                sortedPath = xstream.toXML(sortedPathArr);
            }

            userSetting = new Properties();
            userSetting.setProperty(UNSORTED_FOLDER_PATH_PROPERTY, unsortedPath);
            userSetting.setProperty(SORTED_FOLDER_PATH_PROPERTY, sortedPath);

            userSetting.setProperty(UNSORTED_FOLDER_SEARCH_FILE_ONLY_PROPERTY, (this.FileOnlyCheckBox.isSelected() ? "yes" : "no"));
            userSetting.setProperty(UNSORTED_FOLDER_SEARCH_ONE_LEVEL_ONLY_PROPERTY, (this.oneLevelCheckBox.isSelected() ? "yes" : "no"));
            userSetting.setProperty(BLUR_SEARCH_PROPETY, this.blurMatchingCheckBox.isSelected() ? "yes" : "no");
            userSetting.setProperty(NUMBER_BEFORE_MKDIR_PROPERTY, this.createDirSpinner.getValue().toString());

            FileOutputStream out = new FileOutputStream(SETTING_FILE);
            userSetting.storeToXML(out, "Setting of user's folders");

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fail to save user setting" + e.getMessage());
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
        FileOnlyCheckBox = new javax.swing.JCheckBox();
        createDirSpinter = new javax.swing.JSpinner();
        createDirSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();

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
                run(evt);
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

        FileOnlyCheckBox.setSelected(true);
        FileOnlyCheckBox.setText("File Only");

        createDirSpinter.setPreferredSize(new java.awt.Dimension(1, 30));

        createDirSpinner.setFocusTraversalPolicyProvider(true);
        createDirSpinner.setMaximumSize(new java.awt.Dimension(20, 20));
        createDirSpinner.setMinimumSize(new java.awt.Dimension(0, 0));
        createDirSpinner.setName(""); // NOI18N
        createDirSpinner.setValue(2);

        jLabel1.setText("# of Files before mkdir");

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
                                        .addComponent(FileOnlyCheckBox))
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
                    .addComponent(FileOnlyCheckBox))
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

    /*
     * a method for file choosing
     */
    private File lastTimeChoice = null;   //a function-only local vairable for chooseFile

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
            //System.out.println("You chose to open this file: " + chooser.getSelectedFile());
            lastTimeChoice = chooser.getSelectedFile().getParentFile();
            return chooser.getSelectedFile().getPath();
        } else {
            return null;
        }
    }

    String[] getPathes(JList l) {
        Object[] tempArr = getDefaultListModel(l).toArray();
        return Arrays.copyOf(tempArr, tempArr.length, String[].class);
    }





    /*
     * the most important method of this class
     * read folder and then do the matching
     */
    private void run(java.awt.event.ActionEvent evt)//GEN-FIRST:event_run
    {//GEN-HEADEREND:event_run

        this.runButton.setEnabled(false);
        //this.runButton.setText("Running...");

        scan_folders();
       

        //run the matching algo
        try {
            MatchAndPrintTheCommand();
            System.out.println("calculation: done");
            // JOptionPane.showMessageDialog(this, "Please check the project. Command files are created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e + "during matching calculation");
            JOptionPane.showMessageDialog(this, e + "during matching calculation");
        } finally {
            this.runButton.setEnabled(true);
        }
        
        
      
        
    }//GEN-LAST:event_run

    /*
     * choose folder for unsorted list
     */
    private void chooseWhereUnsortedFilesAre(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chooseWhereUnsortedFilesAre
    {//GEN-HEADEREND:event_chooseWhereUnsortedFilesAre
        String file = chooseFile();
        getDefaultListModel(unsortedFolderList).addElement(file);
    }//GEN-LAST:event_chooseWhereUnsortedFilesAre

    /*
     * choose folder for sorted list
     */
    private void ChooseWhereSortedFIlesAre(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseWhereSortedFIlesAre
        String file = chooseFile();
        getDefaultListModel(sortedFolderList).addElement(file);
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
        // TODO add your handling code here:
    }//GEN-LAST:event_blurMatchingCheckBoxActionPerformed

    private void oneLevelCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneLevelCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oneLevelCheckBoxActionPerformed

    /**
     * a simple utility to get the defaultListModel of JList
     *
     * @param list
     * @return DefaultListModel
     */
    private DefaultListModel getDefaultListModel(JList list) {
        return (DefaultListModel) list.getModel();
    }




    private void scan_folders()
    {
      
        usrtFiles = new MangaFileTable();
        usrtFiles.oneLevel = this.oneLevelCheckBox.isSelected();
        //usrtFiles.oneLevel = false;
        usrtFiles.fileOn = true;
        //usrtFiles.folderOn = false;
        usrtFiles.folderOn = !this.FileOnlyCheckBox.isSelected();
        usrtFiles.compressedFileOnly = true;
        
           srtFiles = new MangaFileTable();
        srtFiles.fileOn = false;
        srtFiles.folderOn = true;
        srtFiles.oneLevel = true;

        
        //scan unsorted folders
        try {
            String[] unsortedPathArr = getPathes(unsortedFolderList);
            usrtFiles.addFolders(unsortedPathArr);
            System.out.println("unsorted folders scanning: done");
            
               
            
              //write result into text file
           if (LOG1){
               //usrtFiles.debugDisplay();
                PrintWriter out;
                String saveFolder = System.getProperty("user.dir");
                out = new PrintWriter(saveFolder + "\\" + "usrtFiles_debug_log.txt");
                out.print(usrtFiles.toString());
                out.close();
            }

            String[] sortedPathArr = getPathes(sortedFolderList);
            srtFiles.addFolders(sortedPathArr);
            System.out.println("sorted folders scanning: done");
            
         
            
            if (LOG1){ 

                //srtFiles.debugDisplay();
                PrintWriter out;
                String saveFolder = System.getProperty("user.dir");
                out = new PrintWriter(saveFolder + "\\" + "srtFiles_debug_log.txt");
                out.print(srtFiles.toString());
                out.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getStackTrace() + " during scanning");
            JOptionPane.showMessageDialog(this, e + " during scanning");
            this.runButton.setEnabled(true);
            return;
        }
    }

    /**
     * find matched files between SortedFileTable and UnsortedFileTable、 print
     * out instruction about how to move file from unsortedFolder to
     * sortedFolder You need to move file manually to make sure no any problem
     *
     * @throws Exception
     */
    private void MatchAndPrintTheCommand() throws Exception {

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
        StringBuilder mvWinCmd = new StringBuilder();

        //command to move file on mac and linux
        StringBuilder mvMacCmd = new StringBuilder();
        StringBuilder mkdirCmd = new StringBuilder();

        //a non-shell version for human to read
        StringBuilder mvStr = new StringBuilder();

        int numBeforeMkdir = (Integer) this.createDirSpinner.getValue();

        HashSet<String> skip = new HashSet<String>();
        
        
         // System.out.println("hehre"+ unsortedAuthorNames.size());

        for (String unsortedAuthorName : unsortedAuthorNames) {

            if (skip.contains(unsortedAuthorName)) {continue; }

            
            
            
            //check if there already exist a folder for the file
            AuthorInfo entry = usrtFiles.get(unsortedAuthorName);
            if (entry == null) { 
                System.out.println("!!!"+unsortedAuthorName+" HAS NO ENTRY");
                continue; }

            ArrayList<String> names = entry.names;
            File destFolder = getAuthorFolder(unsortedAuthorName, names);

            if (destFolder != null) {

                //System.out.println("FOUND MATHCH"+ unsortedAuthorName + destFolder);

                //tell user to move the file
                ArrayList<File> sourceFiles = usrtFiles.get(unsortedAuthorName).files;
                for (File sourceFile : sourceFiles) {
                    StringBuilder line = new StringBuilder();
                    line.append(sourceFile.getPath()).append("\" \"").append(destFolder.getPath()).append("\"\n\r");
                    mvMacCmd.append("mv \"").append(line);  mvWinCmd.append("move \"").append(line);
                    mvStr.append(sourceFile.getName()).append("    ").append(destFolder.getName()).append("\n\r");
                    mvCounter++;
                }
                //continue;
            }

            //calculate the files the author has 
            int occurence = 0;
            for (String key : usrtFiles.keySet()) {
                AuthorInfo tempE = usrtFiles.get(key);
                if (isTwoNamesEqual(names, tempE.names)) {
                    occurence += entry.files.size();

                    //only counter once
                    skip.add(key);
                }
            }

            if (occurence >= numBeforeMkdir) {
                //if folder  does not exist and this author have more than two book
                //tell user to create one
                mkdirCmd.append("mkdir \"").append(unsortedAuthorName).append("\"\n\r");
                mkdirCounter++;
            }
        }

        //write result into text files
        PrintWriter out;

        String saveFolder = System.getProperty("user.dir");

        out = new PrintWriter(saveFolder + "\\" + "mv_win_cmd.txt");
        mvWinCmd.insert(0, "Need to move files " + mvCounter + " times\n\r\n\r");
        out.print(mvWinCmd.toString());
        out.close();

        out = new PrintWriter(saveFolder + "\\" + "mv_mac_cmd.txt");
        mvMacCmd.insert(0, "Need to move files " + mvCounter + " times\n\r\n\r");
        out.print(mvMacCmd.toString());
        out.close();

        out = new PrintWriter(saveFolder + "\\" + "mkdir_cmd.txt");
        mkdirCmd.insert(0, "#Need to create " + mkdirCounter + " new folders\n\r\n\r");
        out.print(mkdirCmd.toString());
        out.close();

        //open the folder in the file exploer
        java.awt.Desktop.getDesktop().open(new File(saveFolder));
    }


    private boolean isTwoNamesEqual(ArrayList<String> l1, ArrayList<String> l2) {
        for (String n1 : l1) {
            for (String n2 : l2) {
                if (n1.equals(n2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param sourceName
     * @param sourceNames
     * @brief [brief description]
     * @details [long description]
     * @return the folder url, null if no exitence
     */
    public File getAuthorFolder(String sourceName, ArrayList<String> sourceNames) {
        
       // System.out.println("hehre");
        
        //if we can find directly, nice
        if (srtFiles.containsKey(sourceName)) {
         // System.out.println("FOUND"+  srtFiles.get(sourceName));
            return srtFiles.get(sourceName).directory;
        }

        File result = null;
        
        
        

        //if not, compare all authors names
        for (AuthorInfo entry : srtFiles.values()) {
            for (String n1 : entry.names) {
                for (String n2 : sourceNames) {
                    
                    

                    // System.out.println(n2 + "  "+ name);
                    int strDistance = NameParser.stringDistance(n2, n1);

                    if (strDistance == 0 || n1.equals(n2)) {
                        result = entry.directory;
                        return result;
                    } else if (this.blurMatchingCheckBox.isSelected() && strDistance == 1 && n2.length() > 2 && n1.length() > 2) {
                        result = entry.directory;
                    }
                }
            }

        }
        return result;
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
            public void run() {
                new MainInterface().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox FileOnlyCheckBox;
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
    private javax.swing.JList sortedFolderList;
    private javax.swing.JList unsortedFolderList;
    // End of variables declaration//GEN-END:variables
}
