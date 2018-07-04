/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.*;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;

/**
 *
 * @author Kevin
 */
public class ClientClass {
        Socket socket,recepient;
        File transferFile, infoFile;
        int fileSize;
        byte[] buffer;
        ServerSocket sendingServer;
                 
        public ClientClass(String server,String file)throws IllegalBlockingModeException,IOException,UnknownHostException,SocketException,SecurityException,BindException,ConnectException,SocketTimeoutException{
                InetAddress serverAddress=InetAddress.getByName(server);
                socket = new Socket(serverAddress, 15132);

                transferFile = new File(file);
                fileSize = (int) transferFile.length();
                
                getInfoFile();
                sendInfoFile();
                sendFile();
        }
        
        private void getInfoFile()throws IOException{
                infoFile = new File("send.txt");

                PrintWriter sendTextWriter = new PrintWriter(infoFile);
                sendTextWriter.write(String.valueOf(fileSize));
                sendTextWriter.println();
                sendTextWriter.write(transferFile.getName());
                sendTextWriter.println();
                sendTextWriter.write(Inet4Address.getLocalHost().getHostAddress());
                sendTextWriter.println();
                sendTextWriter.close();
        }
        
        private void sendInfoFile()throws IOException,SocketException{
                buffer = new byte[(int)infoFile.length()];
                FileInputStream fin = new FileInputStream(infoFile);
                BufferedInputStream bin = new BufferedInputStream(fin);
                bin.read(buffer, 0, buffer.length);
                OutputStream os = socket.getOutputStream();
                System.out.println("Sending Files...");
                os.write(buffer, 0, buffer.length);
                os.flush();
                socket.close();
                fin.close();
                bin.close();
                System.out.println("File transfer complete");
                infoFile.delete();
        }
        
        private void waitForRecepient()throws IOException,SocketException,SocketTimeoutException,IllegalBlockingModeException{
                sendingServer = new ServerSocket(15133);
                System.out.println("Waiting for recepient");
                sendingServer.setSoTimeout(150000);
                recepient=sendingServer.accept();
        }
        
        private void sendFile()throws IOException,IllegalBlockingModeException{
                try {
                        waitForRecepient();
                        System.out.println("Connection accepted: " + recepient);
                        buffer = new byte[(int)transferFile.length()];
                        FileInputStream fin = new FileInputStream(transferFile);
                        BufferedInputStream bin = new BufferedInputStream(fin);
                        bin.read(buffer,0, buffer.length);
                        OutputStream os = recepient.getOutputStream();
                        System.out.println("Sending Files...");
                        os.write(buffer, 0, buffer.length);
                        os.flush();
                        recepient.close();
                        fin.close();
                        bin.close();
                        System.out.println("File transfer complete");
                } catch (SocketTimeoutException stoe) {
                        System.out.println("Recepient took too long to respond. Connection terminated.");
                } catch (SocketException se) {
                        System.out.println("Recepient refused to receive the file.");
                }
        }
}
