package ZipFiles;

import java.io.*;
import java.util.*;
import java.util.zip.*;

//<editor-fold defaultstate="collapsed" desc="Zipper Class">
/**
 * This is a class used to zip up files or extract all files from a zip file.
 *
 * @version 1.0
 * @author Kevin
 */
public class Zipper {

        /**
         * This is an object to hold the files to be zipped up into one Zip
         * file.
         */
        public final File[] files;

        //<editor-fold defaultstate="collapsed" desc="Code for Zipping up files into one file.">
        /**
         * This is a constructor for zipping up files. It takes an array of
         * files and initialises a private file object. Only use this
         * constructor if you want to zip up files into one zip file.
         *
         * @param files an array of files to be put in a zip file.
         * @throws IOException if the setSettings method throws an exception.
         *
         */
        public Zipper(File[] files) throws IOException {
                this.zipFile = null;
                this.files = files;
                System.out.println("Initializing default folder location.");
                setSettings();
        }

        /**
         * This method zips up the files initialised in the constructor into one
         * zip file. It then returns the zip file. The zip file is called
         * sendFiles.zip.
         *
         * @return The zip File. ie sendFiles.zip
         * @throws IOException incase an input/output error occurs during
         * zipping up.
         * @throws NullPointerException if the files object is empty.
         */
        public File zipUp() throws IOException {
                //Create file locarion incase it was not created by the time this method was called.
                System.out.println("Creating default folder location.");
                createFilePath();
                //Initialize the zip file.
                System.out.println("Initializing the zip file to contain the files.");
                File zip = new File(fileLocation + "/sendFiles.zip");

                System.out.println("Initializing output streams.");
                try ( //OutputStreams for writing to the file.
                        FileOutputStream output = new FileOutputStream(zip); ZipOutputStream zipper = new ZipOutputStream(output)) {
                        //Write into the file.
                        System.out.println("Zipping up: ");
                        for (File file : files) {
                                //Reader to read from the external file into the zip file.
                                FileInputStream reader;
                                if (file.isDirectory()) {
                                        System.out.println("\t" + file.getName() + ": Directory");
                                        //Get all inner files and folders recursively incase external file is a folder.
                                        System.out.println("\t\tGetting all inner files and folders...");
                                        ArrayList<String> directoryFiles = getAllInnerFiles(file);
                                        System.out.println("\t\tCompressing...");
                                        for (String directoryFile : directoryFiles) {
                                                File dFile = new File(directoryFile);
                                                if (dFile.isFile()) {
                                                        //Write files to the zip file.
                                                        reader = new FileInputStream(directoryFile);
                                                        String entryName = directoryFile.substring(directoryFile.indexOf(file.getName()));
                                                        ZipEntry dEntry = new ZipEntry(entryName);
                                                        zipper.putNextEntry(dEntry);

                                                        int read;
                                                        do {
                                                                read = reader.read(BUFFER, 0, BUFFER.length);
                                                                if (read > -1) {
                                                                        zipper.write(BUFFER, 0, read);
                                                                }
                                                        } while (read > -1);
                                                        reader.close();
                                                        zipper.closeEntry();
                                                } else {
                                                        //Put an empty folder into the zip file.
                                                        String entryName = directoryFile.substring(directoryFile.indexOf(file.getName())) + "/";
                                                        ZipEntry dEntry = new ZipEntry(entryName);
                                                        zipper.putNextEntry(dEntry);
                                                }
                                        }
                                        continue;
                                }
                                //Code is executed only if external file is a file and not a folder.
                                System.out.println("\t" + file.getName() + ": File");
                                System.out.println("\t\tPutting the entry in the zip file.");
                                reader = new FileInputStream(file);
                                ZipEntry entry = new ZipEntry(file.getName());
                                zipper.putNextEntry(entry);

                                System.out.println("\t\tCompressing");
                                int read;
                                do {
                                        read = reader.read(BUFFER, 0, BUFFER.length);
                                        if (read > -1) {
                                                zipper.write(BUFFER, 0, read);
                                        }
                                } while (read > -1);
                                reader.close();
                                zipper.closeEntry();
                        }
                }
                System.out.println("Compression done.");
                return zip;
        }

        /**
         * This method gets all files inside a file folder recursively and
         * returns an ArrayList of file paths.
         *
         * @param folder The folder to be checked.
         * @return an ArrayList of file paths.
         */
        private ArrayList<String> getAllInnerFiles(File folder) {
                //Initialize the arraylist.
                ArrayList<String> list = new ArrayList<>();
                //Get all the inner files and directories and add them to the arraylist.
                for (File file : folder.listFiles()) {
                        list.add(file.getPath());
                        if (file.isDirectory()) {
                                getAllInnerFiles(file).forEach((inner) -> {
                                        list.add(inner);
                                });
                        }
                }
                return list;
        }

        //</editor-fold>
        
        
        
        /**
         * This file is the .zip file to be unzipped.
         */
        public final ZipFile zipFile;

        //<editor-fold defaultstate="collapsed" desc="Code for decompressing a zip file into the default folder location.">
        /**
         * This is the constructor for moving files from a zipped file to the
         * default folder location. It takes a file object and the file has to
         * be a zip file.<br>
         * Note: The zip file will be deleted after all files have been
         * extracted from it.
         *
         * @param file The zip file to be unzipped.
         * @throws IOException if the setSettings method throws an exception.
         * @throws ZipException if the file is not a .zip file or if another Zip
         * format error occurs.
         */
        public Zipper(File file) throws IOException {
                this.files = null;
                System.out.println("Initializing default folder location.");
                setSettings();
                //Check if file is a .zip file
                System.out.println("Checking if file is a zip file.");
                if (file.getName().endsWith(".zip")) {
                        zipFile = new ZipFile(file, ZipFile.OPEN_READ | ZipFile.OPEN_DELETE);
                } else {
                        zipFile = null;
                        throw new ZipException("File does not refer to a .zip file.");
                }
        }

        /**
         * This method moves files from the zip file object into the default
         * folder location.<br>
         * NB: The zip file is deleted after extraction is finished. The method
         * returns an arraylist of files that have been decompressed from the
         * zip file.
         *
         * @param folderName The name of the folder to store the unzipped files.
         * This folder will be inside the default folder.
         *
         * @return An arraylist of files decompressed from the zip file.
         *
         * @throws IOException if an i/o error occurs during extract.
         * @throws NullPointerException if the zipFile object is null
         */
        public ArrayList<File> unzip(String folderName) throws IOException {
                System.out.println("Creating an array list of files to store all unzipped files");
                //The arraylist object to be returned containing all decompressed files.
                ArrayList<File> allFiles = new ArrayList<>();
                //An enumeration of all files in the zipFile object.
                System.out.println("Getting all entries in the zipfile.");
                Enumeration<ZipEntry> temp = (Enumeration<ZipEntry>) zipFile.entries();
                System.out.println("Decompressing files: ");
                for (int i = 0; temp.hasMoreElements(); i++) {
                        ZipEntry entry = temp.nextElement();
                        System.out.println("\t" + entry.getName());
                        String temp1 = (entry.getName().contains("/")) ? entry.getName().substring(0, entry.getName().lastIndexOf("/")).replaceAll("/", "\\\\") : "";
                        String temp2 = (entry.getName().contains("\\") && temp1.isEmpty()) ? entry.getName().substring(0, entry.getName().lastIndexOf("\\")) : "";
                        String entryName = (temp1.isEmpty()) ? temp2 : temp1;
                        if (i == 0)//code is executed at the beginning of file extract alone. This creates all necessary parent directories in the system path.
                        {
                                new File(fileLocation + "\\" + folderName + "\\" + entryName).mkdirs();
                        }
                        if (entry.isDirectory()) {
                                //This code is used to create all folders in the enumeration.
                                new File(fileLocation + "\\" + folderName + "\\" + entry.getName().replaceAll("/", "\\\\")).mkdir();
                                continue;
                        }
                        try (InputStream is = zipFile.getInputStream(entry); DataInputStream in = new DataInputStream(is);//Streams for reading from the zip file
                                FileOutputStream fis = new FileOutputStream(new File(fileLocation + "\\" + folderName + "\\" + entry.getName().replaceAll("/", "\\\\")));
                                DataOutputStream out = new DataOutputStream(fis)) {//Write to the specified folder in the default folder location.
                                int read;
                                do {
                                        read = in.read(BUFFER, 0, BUFFER.length);
                                        if (read > -1) {
                                                out.write(BUFFER, 0, read);
                                        }
                                } while (read > -1);
                                allFiles.add(new File(fileLocation + "\\" + folderName + "\\" + entry.getName().replaceAll("/", "\\\\")));
                        }
                }
                System.out.println("Decompession done.");
                return allFiles;
        }

        //</editor-fold>
        
        
        
        /**
         * This variable holds the absolute file path for all files created by
         * this class. Its default value is
         * <code>C:\ users \ %username% \ documents \ File sharing</code>
         */
        private String fileLocation = "C:/Users/" + System.getProperty("user.name") + "/Documents/File Sharing";

        /**
         * This is the file that holds settings for this class.
         */
        public File settingsFile = new File("Settings.txt");

        /**
         * The BUFFER for writing or reading purposes.
         */
        public static byte[] BUFFER = new byte[691000000];

        //<editor-fold defaultstate="collapsed" desc="Universal code">
        /**
         * This method sets the default file location. By default it is the
         * documents folder. This means that all unzipped/zipped files will be
         * in the documents folder under the folder File Sharing. This method
         * changes that setting and sets the default folder to another location.
         *
         * @param location This is the path to the user defined location of the
         * unzipped/zipped files.
         * @throws IOException if the specified folder exists but it was not
         * created by this program or if another i/o error occurs like if the
         * String does not refer to a folder that is writable.
         */
        public void setLocation(String location) throws IOException {
                if (!location.equals(fileLocation)) {
                        fileLocation = location;
                        System.out.println("Creating the user Defined file location.");
                        createFilePath();
                }
        }

        /**
         * This method gets the file Location for unzipped/zipped files;
         *
         * @return The absolute file path for unzipped files.
         */
        public String getLocation() {
                return fileLocation;
        }

        /**
         * This method is called to create the default folder destination if it
         * does not already exist.
         *
         * @throws IOException if the file exists but is not a folder, if the
         * file does not exist and cannot be created or if another i/o error
         * occurs.
         */
        private void createFilePath() throws IOException {
                if (!new File(fileLocation).exists()) {
                        System.out.println("Creating the file path.");
                        if (!new File(fileLocation).mkdirs()) {
                                setSettings();
                                throw new IOException("Access to create the folder denied or File does not refer to a folder. Default location reset i.e documents folder.");
                        }
                } else {
                        System.out.println("Checking if file path is a directory.");
                        if (!new File(fileLocation).isDirectory()) {
                                setSettings();
                                throw new IOException("Default file location does not refer to a directory. Default location reset i.e documents folder.");
                        }
                }
                System.out.println("Writing the settings to the settings file");
                settingsFile.createNewFile();
                try (PrintWriter writer = new PrintWriter(settingsFile)) {
                        writer.write(fileLocation);
                        writer.println();
                }
                System.out.println("Done..");
        }

        /**
         * This method reads the settings file and sets the default Folder
         * location to the user defined folder.
         *
         * @throws IOException if the settingsFile was not found.
         * @throws NoSuchElementException if the settingsFile is empty.
         */
        private void setSettings() throws IOException {
                if (settingsFile.exists()) {
                        System.out.println("Reading the settings file.");
                        try (FileInputStream fis = new FileInputStream(settingsFile); Scanner in = new Scanner(fis)) {
                                if (in.hasNext()) {
                                        System.out.println("Setting the default file location.");
                                        fileLocation = in.nextLine();
                                }
                        }
                } else {
                        System.out.println("Setting default file location.");
                        fileLocation = "C:/Users/" + System.getProperty("user.name") + "/Documents/File Sharing";
                }
        }

        //</editor-fold>
}

//</editor-fold>
