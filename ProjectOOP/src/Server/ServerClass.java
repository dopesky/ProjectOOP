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
import javax.swing.*;

/**
 *
 * @author Kevin
 */
public class ServerClass {
        /**The server variable*/
        private ServerSocket socket;
        
        /**Socket to be used once the server accepts a connection*/
        private Socket client;
        
       
    
        /**The constructor.*/
        public ServerClass()throws IOException,SecurityException,IllegalBlockingModeException,AWTException{
                socket=new ServerSocket(15132);
                client=socket.accept();
                if(SystemTray.isSupported()){
                        sendNotification();
                }else{
                        JOptionPane.showMessageDialog(null,"\tError\nSystem tray not supported thus server cannot receive files.","File Share",JOptionPane.ERROR_MESSAGE);
                }
                client.close();
                socket.close();
        }
        
        /**Send a notification from the server*/
        private void sendNotification()throws AWTException{
                ImageIcon getImage=new ImageIcon(getClass().getResource("/Resources/lolo0073.png"));
                Image imageIcon = getImage.getImage();
                
                Notifications notify = new Notifications(imageIcon,"Accept the file being sent!");
                notify.addActionListener();
                notify.setPopupMenu();
                notify.displayTray("File is Being Sent to you","Click me to receive file", TrayIcon.MessageType.INFO);
                new Thread(){
                        @Override
                        public void run(){
                                int counter=1;
                                try{
                                        while(true){
                                                Thread.sleep(1000);
                                                counter++;
                                                if(counter>=120){
                                                        System.exit(0);
                                                }
                                                if(notify.selected!=-1){
                                                        this.interrupt();
                                                }
                                        }
                                }catch(InterruptedException ie){
                                        System.out.println("I was here");
                                        SystemTray.getSystemTray().remove(notify);
                                }
                        }
                }.start();
        }
        
        /**The main function.*/
        public static void main(String args[])throws IOException,SocketException,IllegalBlockingModeException, AWTException{
                try{
                        UIManager.setLookAndFeel(new DarculaLaf());
                }catch(UnsupportedLookAndFeelException ulaf){}
                new ServerClass();
        }
}
