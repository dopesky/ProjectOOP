/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

/**
 *
 * @author Kevin
 */
import java.io.*;
import java.net.*;

public class Client {

        public static void main(String[] args) throws IOException {
                //reads over 658MB per loop
                int filesize = 691000000;
                int bytesRead;
                int currentTot = 0;
                Socket socket = new Socket("10.51.36.132", 15123);
                byte[] bytearray = new byte[filesize];
                InputStream is = socket.getInputStream();
                FileOutputStream fos = new FileOutputStream("copy.jpg");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = is.read(bytearray, 0, bytearray.length);
                currentTot = bytesRead;
                do {
                        bytesRead = is.read(bytearray, currentTot, (bytearray.length - currentTot));
                        if (bytesRead >= 0) {
                                currentTot += bytesRead;
                        }
                } while (bytesRead > -1);
                bos.write(bytearray, 0, currentTot);
                bos.flush();
                bos.close();
                socket.close();
        }
}
