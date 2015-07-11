package ACGSort;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.*;
import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import org.json.simple.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 *
 * @author hjyssg
 */
public class MainInterface extends javax.swing.JFrame {
    
       
    static {
        try {
            InputStream inputStream = new FileInputStream("log.properties");
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (Exception e) {
            System.err.println(e);
        }
    } 

    private final String UNSORTED_FOLDER_PROPERTY = "unsorted folder path";
    private final String SORTED_PATH_PROPERTY = "sort folder path";
    private final String ONE_LEVEL_ONLY_PROPERTY = "unsorted folder search one level only property";
    private final String FILE_ON_PROPERTY = "unsorted folder search file on property";
    private final String FOLDER_ON_PROPERTY = "unsorted folder search foldere on property";
    private final String NUMBER_BEFORE_MKDIR_PROPERTY = "number before mkdir property";
    private final String BLUR_MARCH_PROPETY = "blur search proprty";

    private String basePath = System.getProperty("user.dir") + "\\src\\resources\\";
    private final String SETTING_FILE = basePath + "setting.xml";
    private Controller controller;



    /**
     * Creates new form MainInterface
     */
    public MainInterface() {
        controller = new Controller();
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
            this.scanFileBox.setSelected(userSetting.getProperty(FILE_ON_PROPERTY).equals("yes"));
            this.scanFolderBox.setSelected(userSetting.getProperty(FOLDER_ON_PROPERTY).equals("yes") );
            this.oneLevelCheckBox.setSelected(userSetting.getProperty(ONE_LEVEL_ONLY_PROPERTY).equals("yes"));
            this.blurMatchingCheckBox.setSelected(userSetting.getProperty(BLUR_MARCH_PROPETY).equals("yes"));

            Integer tempI = Integer.parseInt(userSetting.getProperty(NUMBER_BEFORE_MKDIR_PROPERTY));
            this.createDirSpinner.setValue(tempI);

            //get path 
            if (userSetting.getProperty(UNSORTED_FOLDER_PROPERTY) != null) {
                Object obj = JSONValue.parse(userSetting.getProperty(UNSORTED_FOLDER_PROPERTY));
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
            if (userSetting.getProperty(SORTED_PATH_PROPERTY) != null) {
                Object obj = JSONValue.parse(userSetting.getProperty(SORTED_PATH_PROPERTY));
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

                unsortedPathlist.addAll(Arrays.asList(unsortedPathArr));
                sortedPathlist.addAll(Arrays.asList(sortedPathArr));
            }
            userSetting = new Properties();
            userSetting.setProperty(UNSORTED_FOLDER_PROPERTY, JSONValue.toJSONString(unsortedPathlist));
            userSetting.setProperty(SORTED_PATH_PROPERTY, JSONValue.toJSONString(sortedPathlist));

            userSetting.setProperty(FILE_ON_PROPERTY, (this.scanFileBox.isSelected() ? "yes" : "no"));
            userSetting.setProperty(FOLDER_ON_PROPERTY, (this.scanFolderBox.isSelected() ? "yes" : "no"));
            userSetting.setProperty(ONE_LEVEL_ONLY_PROPERTY, (this.oneLevelCheckBox.isSelected() ? "yes" : "no"));
            userSetting.setProperty(BLUR_MARCH_PROPETY, this.blurMatchingCheckBox.isSelected() ? "yes" : "no");
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
            System.out.println("You choose to open this file: " + chooser.getSelectedFile());
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
        controller.mainWrapper(getPaths(unsortedFolderList), getPaths(sortedFolderList),
                                oneLevelCheckBox.isSelected(), scanFolderBox.isSelected(), scanFileBox.isSelected(),
                                (Integer) this.createDirSpinner.getValue(), this.blurMatchingCheckBox.isSelected());
        this.runButton.setEnabled(true);
        try {
            //open the folder in the file exploer
            java.awt.Desktop.getDesktop().open(new File(controller.saveFolder));
        } catch (IOException ex) {
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
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
