/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.bulenkov.darcula.DarculaLaf;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Kevin
 */
public class ServerClass {

        /**
         * The server variable
         */
        private ServerSocket socket;

        /**
         * Socket to be used once the server accepts a connection
         */
        private Socket client;

        /**
         * The default max size of file to receive ie 658MB per unit time.
         * Depending on processor speed.
         */
        int filesize = 691000000;

        /**
         * variable to hold number of bytes read from the client side
         */
        int bytesRead;

        /**
         * A variable that keeps track of the number of bytes already read in a
         * loop. Used to set the offset in the next loop.
         */
        int currentTot = 0;

        /**
         * Buffer to store data read from the input stream.
         */
        byte[] bytearray = new byte[1024];

        /**
         * An array to store information about the file being received
         */
        String fileData[];

        /**
         * The constructor.
         */
        public ServerClass() throws IOException, SecurityException, IllegalBlockingModeException, AWTException {
                socket = new ServerSocket(15132);
                System.out.println("Waiting for connection... ");
                client = socket.accept();
                System.out.println("Accepted connection : " + client);
                receiveInfoByte();
                readInfoByte();

                if (SystemTray.isSupported()) {
                        sendNotification();
                } else {
                        JOptionPane.showMessageDialog(null, "\tError\nSystem tray not supported thus server cannot receive files.", "File Share", JOptionPane.ERROR_MESSAGE);
                }
                client.close();
                socket.close();
        }

        /**
         * Receive an info file from the client in form of a byte array which is
         * stored in <code>bytearray</code>
         */
        private void receiveInfoByte() throws IOException {
                InputStream is = client.getInputStream();
                bytesRead = is.read(bytearray, 0, bytearray.length);
                currentTot = bytesRead;
                do {
                        bytesRead = is.read(bytearray, currentTot, (bytearray.length - currentTot));
                        if (bytesRead >= 0) {
                                currentTot += bytesRead;
                        }
                } while (bytesRead > -1);
        }

        /**
         * Read the info <code>bytearray</code> into a string array
         */
        private void readInfoByte() throws IOException {
                FileOutputStream fos = new FileOutputStream("receive.txt");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(bytearray, 0, currentTot);
                bos.flush();
                bos.close();
                fos.close();
                client.close();

                readInfoFile();
        }

        /**
         * Reads the information file created into a string array
         */
        private void readInfoFile() throws IOException {
                File tempFile = new File("receive.txt");
                FileInputStream inputStream = new FileInputStream(tempFile);
                Scanner temp = new Scanner(inputStream);
                int rows;
                for (rows = 0; temp.hasNext(); rows++) {
                        temp.nextLine();
                }
                fileData = new String[rows];

                temp = new Scanner(new FileInputStream("receive.txt"));
                for (int i = 0; i < fileData.length; i++) {
                        fileData[i] = temp.nextLine();
                }
                temp.close();
                inputStream.close();
                tempFile.delete();
        }

        /**
         * Send a notification from the server
         */
        private void sendNotification() throws AWTException {
                ImageIcon getImage = new ImageIcon(getClass().getResource("/Resources/lolo0073.png"));
                Image imageIcon = getImage.getImage();

                Notifications notify = new Notifications(imageIcon, "Accept the file being sent!");
                notify.addActionListener();
                notify.setPopupMenu();
                notify.displayTray("File is Being Sent to you", "Click me to receive file", TrayIcon.MessageType.INFO);

                new Thread() {
                        @Override
                        public void run() {
                                int counter = 1;
                                try {
                                        while (true) {
                                                Thread.sleep(1000);
                                                counter++;
                                                if (counter >= 120) {
                                                        this.interrupt();
                                                }
                                                if (notify.selected != -1) {
                                                        this.interrupt();
                                                }
                                        }
                                } catch (InterruptedException ie) {
                                        SystemTray.getSystemTray().remove(notify);
                                        if(notify.selected!=-1) receiveFile(notify.selected);
                                }
                        }
                }.start();
        }

        /**
         * method to receive or refuse receiving of file
         */
        private void receiveFile(int answer) {
                try{
                        Socket receiveInfo = new Socket(InetAddress.getByName(fileData[2].trim()), 15133);
                        InputStream is = receiveInfo.getInputStream();
                        if (answer == 0) {
                                int fileSize = Integer.parseInt(fileData[0].trim());
                                bytearray = new byte[filesize];
                                bytesRead = is.read(bytearray, 0, bytearray.length);
                                currentTot = bytesRead;
                                do {
                                        bytesRead = is.read(bytearray, currentTot, (bytearray.length - currentTot));
                                        if (bytesRead >= 0) {
                                                currentTot += bytesRead;
                                        }
                                } while (bytesRead > -1);

                                FileOutputStream fos = new FileOutputStream(fileData[1]);
                                BufferedOutputStream bos = new BufferedOutputStream(fos);
                                bos.write(bytearray, 0, currentTot);
                                bos.flush();
                                bos.close();
                                fos.close();
                                receiveInfo.close();
                        } else if (answer == 1) {
                                receiveInfo.close();
                        }
                } catch (IOException ioe) {}
        }

        /**
         * The main function.
         */
        public static void main(String args[]) throws IOException, SocketException, IllegalBlockingModeException, AWTException {
                try {
                        UIManager.setLookAndFeel(new DarculaLaf());
                } catch (UnsupportedLookAndFeelException ulaf) {
                }
                new ServerClass();
        }
}
