/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;
import java.net.*; 
import java.io.*; 
/**
 *
 * @author mathe
 */
public class Clientmock {
    public static void main(String[] args) throws IOException {
        int filesize = 691000000;
        int bytesRead;
        int currentTot = 0;
        Socket socket1 = new Socket("localhost", 15123);
        System.out.println("Accepted connection : " + socket1);
        File transferFile = new File("send.txt");
        byte[] bytearray = new byte[1024];
        FileInputStream fin = new FileInputStream(transferFile);
        BufferedInputStream bin = new BufferedInputStream(fin);

        bin.read(bytearray, 0, bytearray.length);
        OutputStream os = socket1.getOutputStream();
        System.out.println("Sending Files...");
        os.write(bytearray, 0, bytearray.length);
        os.flush();
        socket1.close();
        System.out.println("File transfer complete");
    }
}
