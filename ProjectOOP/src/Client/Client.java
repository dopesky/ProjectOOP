/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathe
 */
public class Client {

    public static void main(String[] args) throws IOException {
        try {
            InetAddress ip = InetAddress.getLocalHost();

            System.out.println("Host Name: " + ip.getHostName());
            System.out.println("IP Address: " + ip.getHostAddress());
        } catch (Exception e) {
            System.out.println(e);
        }
        int filesize = 691000000;
        int bytesRead;
        int currentTot = 0;
        Socket socket = new Socket();
        try {
            socket = new Socket("127.0.0.1", 15123);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytearray = new byte[filesize];
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream("send.txt");
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
