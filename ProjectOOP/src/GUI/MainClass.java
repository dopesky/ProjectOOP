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
                File temp = new File("test/test.zip");
                File fff=new File("extract");
                fff.mkdir();
                ZipFile zip = new ZipFile(temp);
                Enumeration<ZipEntry> files = (Enumeration<ZipEntry>) zip.entries();
                int count=0;
                while (files.hasMoreElements()) {
                        count++;
                        ZipEntry entry = files.nextElement();
                        System.out.println(entry.getName());
                        if(count==1)new File("extract/"+entry.getName().substring(0,entry.getName().lastIndexOf("/"))).mkdirs();
                        if (entry.isDirectory()){new File("extract/"+entry.getName()).mkdir();continue;}
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
                ZipFiles.Zipper temporary=new ZipFiles.Zipper(new File[]{fff});
                temporary.zipUp();
        }
}
