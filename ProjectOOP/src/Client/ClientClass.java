/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ZipFiles.Zipper;
import java.io.*;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;

/**
 *
 * @author Kevin
 */
public class ClientClass {

        Socket socket, recepient;
        File transferFile, infoFile;
        int fileSize;
        ServerSocket sendingServer;
        String server;

        public ClientClass(String server,String user, String file) throws IOException, UnknownHostException, SocketException{
                this.server = server;
                InetAddress serverAddress = InetAddress.getByName(server);
                System.out.println("Connecting to server " + server + " on port 15132");
                socket = new Socket(serverAddress, 15132);
                System.out.println("Connection Successful.\nInitializing file to send.");
                transferFile = new File(file);
                fileSize = (int) transferFile.length();

                getInfoFile(user);
                sendInfoFile();
                sendFile();
        }

        private void getInfoFile(String username) throws IOException {
                System.out.println("Creating an Information file send.txt.");
                infoFile = new File("send.txt");

                try (PrintWriter sendTextWriter = new PrintWriter(infoFile)) {
                        sendTextWriter.write(String.valueOf(fileSize));
                        sendTextWriter.println();
                        sendTextWriter.write(transferFile.getName());
                        sendTextWriter.println();
                        sendTextWriter.write(Inet4Address.getLocalHost().getHostAddress());
                        sendTextWriter.println();
                        sendTextWriter.write(username);
                        sendTextWriter.println();
                }
        }

        private void sendInfoFile() throws IOException, SocketException {
                System.out.println("Reading info file (send.txt) into a byte array.");
                Zipper.BUFFER = new byte[(int) infoFile.length()];
                FileInputStream fin = new FileInputStream(infoFile);
                BufferedInputStream bin = new BufferedInputStream(fin);
                bin.read(Zipper.BUFFER, 0, Zipper.BUFFER.length);
                OutputStream os = socket.getOutputStream();
                System.out.println("Sending Info file...");
                os.write(Zipper.BUFFER, 0, Zipper.BUFFER.length);
                os.flush();
                socket.close();
                fin.close();
                bin.close();
                System.out.println("File transfer complete!!!\nDeleting send.txt.");
                infoFile.delete();
        }

        private void waitForRecepient() throws IOException, SocketException, SocketTimeoutException, IllegalBlockingModeException {
                System.out.println("Binding a serverSocket to port 15133.");
                sendingServer = new ServerSocket(15133);
                System.out.println("Waiting for recepient");
                sendingServer.setSoTimeout(150000);
                recepient = sendingServer.accept();
        }

        private void sendFile() throws IOException, IllegalBlockingModeException {
                try {
                        waitForRecepient();
                        System.out.println("Connection accepted: " + recepient);
                        if (fileSize > 691000000) {
                                sendLargeFiles();
                                return;
                        }
                        Zipper.BUFFER = new byte[(int) transferFile.length()];
                        FileInputStream fin = new FileInputStream(transferFile);
                        BufferedInputStream bin = new BufferedInputStream(fin);
                        int bytesRead=bin.read(Zipper.BUFFER, 0, Zipper.BUFFER.length);
                        System.out.println(bytesRead+" bytes read from stream.");
                        OutputStream os = recepient.getOutputStream();
                        System.out.println("Preparing to Send File " + transferFile.getName() + " to " + server + "...");
                        os.write(Zipper.BUFFER, 0, Math.min(Zipper.BUFFER.length,bytesRead));
                        os.flush();
                        recepient.close();
                        fin.close();
                        bin.close();
                        sendingServer.close();
                        System.out.println("File transfer complete!!");
                        System.out.println("Thankyou");
                        transferFile.delete();
                } catch (SocketTimeoutException stoe) {
                        System.out.println("Recepient took too long to respond. Connection terminated.");
                } catch (SocketException se) {
                        System.out.println("Recepient refused to receive the file.");
                }
        }

        private void sendLargeFiles() throws IOException, IllegalBlockingModeException, SocketException {
                int bytesRead;
                sendingServer.setSoTimeout(0);
                Zipper.BUFFER = new byte[691000000];
                FileInputStream fin = new FileInputStream(transferFile);
                BufferedInputStream bin = new BufferedInputStream(fin);
                OutputStream os = recepient.getOutputStream();
                System.out.println("Preparing to Send File " + transferFile.getName() + " to " + server + "...");

                do {
                        bytesRead = bin.read(Zipper.BUFFER, 0, Zipper.BUFFER.length);
                        System.out.println(bytesRead + " bytes read.");
                        if (bytesRead > -1) {
                                os.write(Zipper.BUFFER, 0, bytesRead);
                        }
                } while (bytesRead > -1);
                os.flush();
                recepient.close();
                fin.close();
                bin.close();
                sendingServer.close();
                transferFile.delete();
                System.out.println("File transfer complete!!");
                System.out.println("Thankyou");
        }
}
