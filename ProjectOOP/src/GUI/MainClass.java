/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.*;

public class MainClass {

        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) throws Throwable {
                File temp = new File("test/test.zip");
                File fff = new File("extract");
                fff.mkdir();
                
                ZipFiles.Zipper temporarys = new ZipFiles.Zipper(temp);
                temporarys.unzip("extract");
                
                temporarys = new ZipFiles.Zipper(new File[]{fff});
                temporarys.zipUp();
        }
}
