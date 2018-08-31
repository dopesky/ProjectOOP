/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import ZipFiles.Zipper;
import com.bulenkov.darcula.DarculaLaf;
import java.awt.*;
import java.io.*;
import java.net.*;
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
        private final ServerSocket socket;

        /**
         * Socket to be used once the server accepts a connection.
         */
        private final Socket client;

        /**
         * The default max size of file to receive ie 658MB per unit time.
         * Depending on processor speed.
         */
        int filesize = 691000000;

        /**
         * Variable to hold number of bytes read from the client side
         */
        int bytesRead;

        /**
         * A variable that keeps track of the number of bytes already read in a
         * loop. Used to set the offset in the next loop.
         */
        int currentTot = 0;

        /**
         * An array to store information about the file being received
         */
        String fileData[];

        /**
         * A variable to store final status of the final transfer.<br>
         * Warning: Do not modify.
         */
        public int finalStatus = -1;


        /**
         * The constructor.
         */
        public ServerClass(int skip) throws IOException, SocketException, AWTException {
                socket = new ServerSocket(15132);
                System.out.println("Waiting for connection... ");
                client = socket.accept();
                System.out.println("Accepted connection : " + client);
                receiveInfoByte();
                readInfoByte();
                if (skip == 1) {
                        if (SystemTray.isSupported()) {
                                sendNotification();
                        } else {
                                JOptionPane.showMessageDialog(null, "\tError\nSystem tray not supported thus server cannot receive files.", "File Share", JOptionPane.ERROR_MESSAGE);
                        }
                } else if (skip == 0) {
                        finalStatus = receiveFile(0);
                }
                client.close();
                socket.close();

                if (finalStatus != -1) {
                        System.out.println("Return Status: " + finalStatus);
                }
        }

        /**
         * Receive an info file from the client in form of a byte array which is
         * stored in <code>bytearray</code>
         */
        private void receiveInfoByte() throws IOException, SocketException {
                InputStream is = client.getInputStream();
                bytesRead = is.read(Zipper.BUFFER, 0, Zipper.BUFFER.length);
                currentTot = bytesRead;
                System.out.println("Receiving info byte.");
                do {
                        bytesRead = is.read(Zipper.BUFFER, currentTot, (Zipper.BUFFER.length - currentTot));
                        if (bytesRead >= 0) {
                                currentTot += bytesRead;
                        }
                } while (bytesRead > -1);
        }

        /**
         * Read the info <code>bytearray</code> into a string array
         */
        private void readInfoByte() throws IOException, SocketException {
                FileOutputStream fos = new FileOutputStream("receive.txt");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                System.out.println("Reading info byte into temporary receive file.");
                bos.write(Zipper.BUFFER, 0, currentTot);
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
                try (FileInputStream inputStream = new FileInputStream(tempFile)) {
                        Scanner temp = new Scanner(inputStream);
                        int rows;
                        for (rows = 0; temp.hasNextLine(); rows++) {
                                temp.nextLine();
                        }
                        System.out.println("Reading receive.txt file into fileData array.");
                        fileData = new String[rows];
                        
                        temp = new Scanner(new FileInputStream("receive.txt"));
                        for (int i = 0; i < fileData.length; i++) {
                                fileData[i] = temp.nextLine();
                        }
                        temp.close();
                }
                System.out.println("Deleting temporary file.");
                tempFile.delete();
        }

        /**
         * Send a notification from the server
         */
        private void sendNotification() throws AWTException {
                System.out.println("Sending Notification.");
                ImageIcon getImage = new ImageIcon(getClass().getResource("/Resources/lolo0073.png"));
                Image imageIcon = getImage.getImage();

                Notifications notify = new Notifications(imageIcon, "Accept the file being sent!");
                notify.addActionListener();
                notify.setPopupMenu();
                notify.displayTray("File is Being Sent to you." , "Click me to receive file from\n"+fileData[3].trim(), TrayIcon.MessageType.INFO);

                new Thread() {
                        @Override
                        public void run() {
                                int counter = 1;
                                try {
                                        System.out.println("Waiting for user to deny or accept file.");
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
                                        if (notify.selected != -1) {
                                                finalStatus = receiveFile(notify.selected);
                                        } else {
                                                System.out.println("Took too long to respond. Closing connection.");
                                                finalStatus = 1;
                                        }

                                        if (finalStatus != -1) {
                                                System.out.println("Return Status: " + finalStatus);
                                        }
                                }
                        }
                }.start();
        }

        /**
         * method to receive or refuse receiving of file
         */
        private int receiveFile(int answer) {
                try {
                        System.out.println("Connecting to client " + fileData[2].trim() + " to receive file " + fileData[1] + " on port 15133.");
                        Socket receiveInfo = new Socket(InetAddress.getByName(fileData[2].trim()), 15133);
                        InputStream is = receiveInfo.getInputStream();
                        if (answer == 0) {
                                System.out.println("File Accepted.\nReceiving file...");
                                int fileSize = Integer.parseInt(fileData[0].trim());
                                if (fileSize > 691000000) {
                                        int temp= receiveLargeFiles(is, receiveInfo);
                                        extractFiles();
                                        return temp;
                                }
                                bytesRead = is.read(Zipper.BUFFER, 0, Zipper.BUFFER.length);
                                currentTot = bytesRead;
                                do {
                                        bytesRead = is.read(Zipper.BUFFER, currentTot, (Zipper.BUFFER.length - currentTot));
                                        if (bytesRead >= 0) {
                                                currentTot += bytesRead;
                                        }
                                        System.out.println(currentTot + " bytes read in total.");
                                } while (bytesRead > -1);

                                try (FileOutputStream fos = new FileOutputStream(fileData[1]); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                                        bos.write(Zipper.BUFFER, 0, currentTot);
                                        bos.flush();
                                }
                                receiveInfo.close();
                                System.out.println("File receipt complete.");
                                extractFiles();
                                return 0;
                        } else if (answer == 1) {
                                System.out.println("File Rejected. Closing connection");
                                receiveInfo.close();
                        }
                } catch (Throwable ioe) {
                        JOptionPane.showMessageDialog(null, ioe.getMessage(), "Error in Transfer of File", JOptionPane.ERROR_MESSAGE);
                        ioe.printStackTrace();
                }
                return 1;
        }

        private int receiveLargeFiles(InputStream socket, Socket receiver) throws IOException, SocketException {
                try (FileOutputStream fos = new FileOutputStream(fileData[1]); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                        
                        do {
                                bytesRead = socket.read(Zipper.BUFFER, 0, Zipper.BUFFER.length);
                                currentTot += bytesRead;
                                System.out.println(currentTot + " bytes read in total.");
                                if (bytesRead > -1) {
                                        bos.write(Zipper.BUFFER, 0, bytesRead);
                                }
                        } while (bytesRead > -1);
                        bos.flush();
                }
                receiver.close();
                System.out.println("File receipt complete.");
                return 0;
        }
        
        private void extractFiles() throws IOException{
                System.out.println("Beginning copying of files");
                Zipper zipper=new Zipper(new File(fileData[1]));
                System.out.println("Unzipping...");
                zipper.unzip(fileData[3]);
                System.out.println("It is finished. Check the default folder for your files.");
        }

        /**
         * The main function.
         */
        public static void main(String args[]) {
                try {
                        UIManager.setLookAndFeel(new DarculaLaf());
                } catch (UnsupportedLookAndFeelException ulaf) {
                        ulaf.printStackTrace();
                }
                try {
                        new ServerClass(Integer.parseInt(args[0]));
                } catch (Throwable th) {
                        try {
                                th.printStackTrace();
                                if (SystemTray.isSupported()) {
                                        ImageIcon getImage = new ImageIcon("");
                                        Image imageIcon = getImage.getImage();

                                        Notifications notify = new Notifications(imageIcon, "Errors");
                                        notify.displayTray("Error", th.toString(), TrayIcon.MessageType.ERROR);
                                        new Thread() {
                                                @Override
                                                public void run() {
                                                        int counter = 1;
                                                        try {
                                                                while (true) {
                                                                        Thread.sleep(1000);
                                                                        counter++;
                                                                        if (counter >= 10) {
                                                                                this.interrupt();
                                                                        }
                                                                }
                                                        } catch (InterruptedException ie) {
                                                                SystemTray.getSystemTray().remove(notify);
                                                        }
                                                }
                                        }.start();
                                }
                        } catch (Throwable e) {
                                e.printStackTrace();
                        }
                }
        }
}
