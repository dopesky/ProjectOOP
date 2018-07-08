/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Kevin
 */
public class Notifications extends TrayIcon {
        
         /**Value selected by user from the confirm dialog box.
          Warning: Do not modify.*/
        public int selected=-1;
        
        public Notifications(Image image,String text){
                super(image,text);
                this.setImageAutoSize(true);
        }
        
        public void addActionListener(){
                this.addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseReleased(MouseEvent ae) {
                                if(!ae.isPopupTrigger()){
                                        selected=JOptionPane.showConfirmDialog(null,"Accept the file being sent?","File Share",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                }
                        }
                });
        }
        
        public void setPopupMenu(){
                PopupMenu trayPopup=new PopupMenu();
                trayPopup.setFont(new Font("courier new",4,14));
                
                MenuItem accept=new MenuItem("          Accept File");
                accept.addActionListener((ActionEvent ae) -> {
                        selected=0;
                });
                
                MenuItem reject = new MenuItem("          Reject File");
                reject.addActionListener((ActionEvent ae) -> {
                        selected=1;
                });
                
                trayPopup.add(accept);
                trayPopup.addSeparator();
                trayPopup.add(reject);
                
                this.setPopupMenu(trayPopup);
        }
        
        public void displayTray(String heading,String message,MessageType type)throws AWTException{
                SystemTray.getSystemTray().add(this);
                this.displayMessage(heading,message, type);
        }
        
        public static void main(String[] args)throws Throwable{
                Image temp=new ImageIcon("").getImage();
                Notifications notify=new Notifications(temp,"Receive File");
                notify.addActionListener();
                notify.setPopupMenu();
                notify.displayTray("I was here","I want to fuck", TrayIcon.MessageType.NONE);
        }
        
}
