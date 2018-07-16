/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

public class MainClass {

        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) throws Throwable {
                byte[] reader = new byte[1024];
                File temp = new File("test/Zip.zip");
                File fff=new File("extract");
                fff.mkdir();
                ZipFile zip = new ZipFile(temp);
                Enumeration<ZipEntry> files = (Enumeration<ZipEntry>) zip.entries();
                while (files.hasMoreElements()) {
                        ZipEntry entry = files.nextElement();
                        InputStream stream = zip.getInputStream(entry);
                        DataInputStream in = new DataInputStream(stream);
                        System.out.println(entry.getName());
                        FileOutputStream stream2 = new FileOutputStream("extract/"+entry.getName());
                        DataOutputStream out = new DataOutputStream(stream2);
                        int read = 0;
                        do {
                                read = in.read(reader, 0, reader.length);
                                if (read > -1) {
                                        out.write(reader, 0, read);
                                }
                        } while (read > -1);
                }
                
                
                File zipped = new File("Zip.zip");
                File unzipped=new File("extract");
                File[] toZip = unzipped.listFiles();
                FileOutputStream outer = new FileOutputStream(zipped);
                ZipOutputStream oss = new ZipOutputStream(outer);
                for (File f : toZip) {
                        FileInputStream fis = new FileInputStream(f);
                        ZipEntry ent = new ZipEntry(f.getName());
                        oss.putNextEntry(ent);
                        int read = 0;
                        do {
                                read = fis.read(reader, 0, reader.length);
                                if (read > -1) {
                                        oss.write(reader, 0, read);
                                }
                        } while (read > -1);
                        fis.close();
                        oss.closeEntry();
                }
                oss.close();
                outer.close();

                //Client.ClientClass client = new Client.ClientClass("127.0.0.1", "test/Zip.zip");
                //passMany();
        }

}
