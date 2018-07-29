/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ZipFiles;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * This is a class used to zip up files or extract all files from a zip file.
 *
 * @version 1.0
 * @author Kevin
 */
public class Zipper {

        /**
         * This is a file path to ascertain that the program has been started at
         * least once. Its presence proofs that the program has been started at
         * least once.
         */
        public final String startFile = "lockFile.lock";

        /**
         * This variable holds the absolute file path for all files created by this class.
         * Its default value is
         * <code>C:\ users \ %username% \ documents \ File sharing</code>
         */
        private String fileLocation = "C:/Users/" + System.getProperty("user.name") + "/Documents/File Sharing";

        /**
         * This is an object to hold the files to be zipped up into one Zip file.
         */
        private final File[] files;

        /**
         * This method sets the default file location. By default it is the
         * documents folder. This means that all unzipped/zipped files will be in the
         * documents folder under the folder File Sharing. This method changes that
         * setting and sets the default folder to another location.
         *
         * @param location This is the path to the user defined location of the
         * unzipped/zipped files.
         * @throws IOException if the specified folder exists but it was not 
         * created by this program or if another i/o error occurs like if the String does
         * not refer to a folder that is writable.
         */
        public void setLocation(String location) throws IOException {
                fileLocation = location;
                createFilePath();
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
         * This is a constructor for zipping up files. It takes an array of
         * files and initialises a private file object. Only use this constructor 
         * if you want to zip up files into one zip file.
         *
         * @param files an array of files to be put in a zip file.
         */
        public Zipper(File[] files) {
                this.files = files;
        }

        /**
         * This method is called to create the default folder destination if it does not
         * already exist.
         *
         * @throws IOException if the default folder exists but it was not 
         * created by this program or if another i/o error occurs like if the folder
         * is not writable.
         */
        private void createFilePath() throws IOException {
                if (!new File(fileLocation).exists()) {
                        if(!new File(fileLocation).mkdirs()){
                                throw new IOException("Access to create the folder denied");
                        }else{
                                new File(startFile).createNewFile();
                        }
                } else {
                        if(!new File(fileLocation).isDirectory()){
                                throw new IOException("Default file location does not refer to a directory.");
                        }else if (!new File(startFile).exists()) {
                                throw new IOException("Folder "+fileLocation+" already exists.");
                        }
                }
        }

        /**
         * This method zips up the files initialised in the constructor into one
         * zip file. It then returns the zip file.
         *
         * @return The zip File.
         * @throws IOException incase an input output error occurs during
         * zipping up.
         */
        public File zipUp() throws IOException {
                //
                createFilePath();
                //Initialize the zip file.
                File zip = new File(fileLocation + "/sendFiles.zip");
                //The buffer for writing purposes.
                byte[] buffer = new byte[691000000];
                //OutputStreams for writing to the file.
                FileOutputStream output = new FileOutputStream(zip);
                ZipOutputStream zipper = new ZipOutputStream(output);
                //Write into the file.
                for (File file : files) {
                        //Reader to read from the external file into the zip file.
                        FileInputStream reader;
                        if (file.isDirectory()) {
                                //Get all inner files and folders recursively incase external file is a folder.
                                ArrayList<String> directoryFiles = getAllInnerFiles(file);
                                for (String directoryFile : directoryFiles) {
                                        File dFile = new File(directoryFile);
                                        if (dFile.isFile()) {
                                                //Write to the zip file.
                                                reader = new FileInputStream(directoryFile);
                                                String entryName = directoryFile.substring(directoryFile.indexOf(file.getName()));
                                                ZipEntry dEntry = new ZipEntry(entryName);
                                                zipper.putNextEntry(dEntry);

                                                int read;
                                                do {
                                                        read = reader.read(buffer, 0, buffer.length);
                                                        if (read > -1) {
                                                                zipper.write(buffer, 0, read);
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
                        reader = new FileInputStream(file);
                        ZipEntry entry = new ZipEntry(file.getName());
                        zipper.putNextEntry(entry);

                        int read;
                        do {
                                read = reader.read(buffer, 0, buffer.length);
                                if (read > -1) {
                                        zipper.write(buffer, 0, read);
                                }
                        } while (read > -1);
                        reader.close();
                        zipper.closeEntry();
                }
                zipper.close();
                output.close();
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
}
