package ACGSort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import namepicker.NamePicker;

public class Controller {

    public final String resPath = System.getProperty("user.dir") + "\\src\\resources\\";
    public final String saveFolder = System.getProperty("user.dir") + "\\output\\";

    private final String SETTING_FILE = resPath + "setting.xml";
    private final String ANIME_KEYWORD_FILENAME = resPath + "keywords.txt";

    private final String LOG_FP = saveFolder + "_dev.log";
    private final String SRT_LOG_FP = saveFolder + "_iterated_sorted_files.log";
    private final String UNSRT_LOG_FP = saveFolder + "_iterated_sorted_files.log";
    private final String SORTER_FILE_LOG_FP = saveFolder + "_sorted_files.log";

    private final String CMD_BY_KEYWORD_FP = saveFolder + "cmd_by_keywords.txt";
    private final String CMD_BY_AUTHOR_FP = saveFolder + "cmd_by_author.txt";
    private final String CMD_BY_NAME_FP = saveFolder + "cmd_by_names.txt";

    //sorted -> str
    private Hashtable<String, String> keywords_table;
    private Hashtable<String, Pattern> keyword_patthen_table;
    private Hashtable<String, String> keyword_destination_table;
    private ArrayList<String> destinations;
    private MangaFileTable sortedFiles;
    private MangaFileTable unsortedFiles;

    private Logger LOG = Logger.getLogger("Controller");
    private FileHandler fileText;
    private String mvword;
    private NamePicker namePicker;

    String sortedPath = null;
    private String temp_destination_for_keywords = "__UNDEFINED_FOLDER__";

    Controller() {
        namePicker = NamePicker.getInstace();
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            mvword = "move";
        } else {
            mvword = "mv";
        }
        try {
            //set up logger
            fileText = new FileHandler(LOG_FP);
            LOG.addHandler(fileText);
            String tt = LogManager.getLogManager().getProperty(".level");
            Level l = Level.parse(tt);
            //set it to the global log level
            LOG.setLevel(l);
        } catch (IOException ex) {
            Logger.getLogger("Controller").log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger("Controller").log(Level.SEVERE, null, ex);
        }
    }

    public void mainWrapper(String[] unsortedPathArr, String[] sortedPathArr, boolean oneLevel, boolean folderOn, boolean fileOn, int numBeforeMkdir, boolean blurMatching) {
        long t1, tt;
        t1 = System.nanoTime();
        scanFolders(unsortedPathArr, sortedPathArr, oneLevel, folderOn, fileOn);
        System.out.printf("%.4fS  to scan Folder\n", (System.nanoTime() - t1) / 1e+9);

        t1 = System.nanoTime();
        loadKeywordsData();
        System.out.printf("%.4fS  to load keywords\n", (System.nanoTime() - t1) / 1e+9);

        t1 = System.nanoTime();
        matchWithKeyword();
        System.out.printf("%.4fS  to match with keywords\n", (System.nanoTime() - t1) / 1e+9);

        t1 = System.nanoTime();
        matchByAuthorName(numBeforeMkdir, blurMatching);
        System.out.printf("%.4fS  to match with authorname\n", (System.nanoTime() - t1) / 1e+9);

        t1 = System.nanoTime();
        matchByName();
        System.out.printf("%.4fS  to match with names\n", (System.nanoTime() - t1) / 1e+9);
    }

    public boolean loadKeywordsData() {
        try {
            keywords_table = new Hashtable();
            keyword_patthen_table = new Hashtable();
            keyword_destination_table = new Hashtable<String, String>();
            destinations = new ArrayList<String>();
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(ANIME_KEYWORD_FILENAME), "UTF8"));

            boolean regex_on = false;

            while (in.ready()) {
                String s = in.readLine();
                //must be longer than 4
                if (s.length() < 4) {
                    continue;
                } else if (s.charAt(0) == '#' || (s.charAt(0) == '\\' && s.charAt(1) == '\\')) {
                    //support # or // as comment
                    continue;
                } else if (s.contains("__DESTINATION_FOLDER__:")) {
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
                if (!destinations.contains(temp_destination_for_keywords)) {
                    destinations.add(temp_destination_for_keywords);
                }

                if (regex_on) {
                    LOG.info(key + "   " + tokens.get(1));
                    keyword_patthen_table.put(key, Pattern.compile(key, Pattern.CASE_INSENSITIVE));
                }
            }
            in.close();
        } catch (Exception e) {
            Logger.getLogger("Controller").severe("Fail to load keyword data");
            Logger.getLogger("Controller").log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    private void writeToFile(String fileName, String text) throws Exception {
        PrintWriter out = new PrintWriter(fileName, "UTF-8");
        out.print(text);
        out.close();
    }

    public void scanFolders(String[] unsortedPathArr, String[] sortedPathArr, boolean oneLevel, boolean folderOn, boolean fileOn) {
        unsortedFiles = new MangaFileTable();
        unsortedFiles.oneLevel = oneLevel;
        unsortedFiles.folderOn = folderOn;
        unsortedFiles.fileOn = fileOn;
        unsortedFiles.compressedFileOnly = true;
        unsortedFiles.getStringFromBracketsForFolder = true;

        sortedFiles = new MangaFileTable();
        sortedFiles.fileOn = false;
        sortedFiles.folderOn = true;
        sortedFiles.oneLevel = true;
        sortedFiles.getStringFromBracketsForFolder = false;

        System.out.println("LOG Level: " + LOG.getLevel());
        boolean lg = LOG.getLevel().intValue() >= Level.INFO.intValue() || LOG.getLevel().intValue() == Level.ALL.intValue();
        lg = lg && LOG.getLevel().intValue() != Level.OFF.intValue();

        //scan unsorted folders
        try {
            unsortedFiles.addFolders(unsortedPathArr);
            System.out.println("unsorted folders scanning: done");
            LOG.info("unsorted folders scanning: done");

            //write result into text file
            //System.out.println((LOG.getLevel().intValue() + "  " + Level.INFO.intValue()));
            if (lg) {
                writeToFile(SRT_LOG_FP, unsortedFiles.toString());

                StringBuilder temp = new StringBuilder();
                for (File f : unsortedFiles.allIteratedFiles) {
                    temp.append(f.getPath()).append("\n");
                }
                writeToFile(UNSRT_LOG_FP, temp.toString());
            }
        } catch (Exception e) {
            Logger.getLogger("Controller").severe("error during scanning unsorted folder");
            Logger.getLogger("Controller").log(Level.SEVERE, null, e);
        }
        try {
            sortedFiles.addFolders(sortedPathArr);
            LOG.info("sorted folders scanning: done");
            System.out.println("sorted folders scanning: done");

            if (sortedPathArr.length > 0 && sortedPathArr[0] != null) {
                sortedPath = sortedPathArr[0];
            }

            if (lg) {
                //srtFiles.debugDisplay();
                writeToFile(SORTER_FILE_LOG_FP, sortedFiles.toString());
            }
        } catch (Exception e) {
            Logger.getLogger("Controller").severe("error during scanning unsorted folder");
            Logger.getLogger("Controller").log(Level.SEVERE, null, e);
        }
    }

    public void matchByName() {
        try {
            System.out.println("there are " + unsortedFiles.allIteratedFiles.size() + " files");
            StringBuffer mvStr = new StringBuffer();

            for (File file : unsortedFiles.allIteratedFiles) {
                String fn = file.getName().toLowerCase();
                String[] names = NamePicker.convertTableIntoSortedArray(namePicker.findNames(fn));
                if (names.length == 0) {
                    continue;
                }
                String name = names[0];
                String destDir = name;

                mvStr.append("mkdir \"").append(destDir).append("\" \n");
                mvStr.append(mvword).append(" \"").append(file.getPath()).append("\" \"").append(destDir).append("\"\n");
            }
            writeToFile(CMD_BY_NAME_FP, mvStr.toString());
        } catch (Exception e) {
            Logger.getLogger("Controller").severe("error during scanning calculation");
            Logger.getLogger("Controller").log(Level.SEVERE, null, e);
        }
    }

    public void matchWithKeyword() {
        //run the matching algo
        try {
            System.out.println("there are " + unsortedFiles.allIteratedFiles.size() + " files");

            Hashtable<String, StringBuilder> strBuffers = new Hashtable<String, StringBuilder>();
            for (String dest : destinations) {
                strBuffers.put(dest, new StringBuilder());
            }
            for (File file : unsortedFiles.allIteratedFiles) {
                String fn = file.getName().toLowerCase();
                //compare it with each keyword
                for (String key : keywords_table.keySet()) {
                    boolean regex_on = keyword_patthen_table.containsKey(key);
                    Matcher m = null;

                    if (regex_on) {
                        LOG.finest("going to use keyword:" + key + " to " + fn);
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
            PrintWriter out = new PrintWriter(CMD_BY_KEYWORD_FP, "UTF-8");
            //out.print(mvStr.toString());

            for (StringBuilder strb : strBuffers.values()) {
                out.print(strb.toString());
                out.print("\n\n\n\n");
            }
            out.close();
        } catch (Exception e) {
            Logger.getLogger("Controller").severe("error during scanning calculation");
            Logger.getLogger("Controller").log(Level.SEVERE, null, e);
        }
    }

    /**
     * @brief finding the author names from scanned file
     * @details finding the author names from scanned file. Match them and
     * output command file
     */
    public void matchByAuthorName(int numBeforeMkdir, boolean blurMatching) {
        try {
            //get the author of unsorted files     
            ArrayList<String> unsortedAuthorNames = new ArrayList(unsortedFiles.keySet());

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
            if (sortedPath == null) {
                sortedPath = "";
            }
            for (String unsortedAuthorName : unsortedAuthorNames) {
                //check if there already exist a folder for the file
                AuthorInfo entry = unsortedFiles.get(unsortedAuthorName);
                if (entry == null) {
                    LOG.warning("!!!" + unsortedAuthorName + " HAS NO ENTRY");
                    continue;
                }
                ArrayList<String> names = entry.names;
                File destFolder = getAuthorFolder(unsortedAuthorName, names, blurMatching);
                ArrayList<File> unsortedFiles = this.unsortedFiles.get(unsortedAuthorName).files;
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
            mkdirCmd.insert(0, "#Need to create " + mkdirCounter + " new folders\n\r");
            mvCmd.insert(0, "\n\r\n\r#Need to move files " + mvCounter + " times\n\r");

            PrintWriter out = new PrintWriter(CMD_BY_AUTHOR_FP, "UTF-8");
            out.print(mkdirCmd.toString());
            out.print(mvCmd.toString());
            out.close();
        } catch (Exception e) {
            Logger.getLogger("Controller").severe("Fail to load keyword data");
            Logger.getLogger("Controller").log(Level.SEVERE, null, e);
        }
    }

    /**
     * @param sourceName
     * @param sourceNames
     * @brief Find the author folder for the given name
     * @details [long description]
     * @return the folder url, null if no exitence
     */
    public File getAuthorFolder(String sourceName, ArrayList<String> sourceNames, boolean blurMatch) {
        File result = null;
        //if we can find directly, nice
        if (sortedFiles.containsKey(sourceName)) {
            LOG.finest("FOUND" + sortedFiles.get(sourceName));
            result = sortedFiles.get(sourceName).directory;
        } else {
            //if not, compare all authors names
            for (AuthorInfo entry : sortedFiles.values()) {
                if (NameParser.isTwoNamesEqual(entry.names, sourceNames, blurMatch)) {
                    LOG.finest(entry.names + "  " + sourceNames);
                    result = entry.directory;
                    break;
                }
            }
        }
        return result;
    }
}
