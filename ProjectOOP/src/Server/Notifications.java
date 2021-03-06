/**
 * There exists no licence for this product. One may do with it 
 * as they please.
 */
package Server;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is a Notifications Class used to setup and display notifications to the
 * User on the system tray. It is a subclass of the TrayIcon class. A TrayIcon 
 * object represents a tray icon that can be added to the system tray. A TrayIcon 
 * can have a tooltip (text), an image, a pop up menu, and a set of listeners 
 * associated with it. A TrayIcon can generate various MouseEvents and supports
 * adding corresponding listeners to receive notification of these events. TrayIcon 
 * processes some of the events by itself. For example, by default, when the right-mouse
 * click is performed on the TrayIcon it displays the specified pop up menu. When the 
 * mouse hovers over the TrayIcon the tooltip is displayed.<br>Note: When the MouseEvent
 * is dispatched to its registered listeners its component property will be set to null. 
 * (See ComponentEvent.getComponent()) The source property will be set to this TrayIcon.
 * (See EventObject.getSource()). This class adds a mouseClicked event and a pop up menu
 *  automatically by calling its relevant methods. It also adds this TrayIcon object to the 
 * system tray automatically with a call to displayTray method If a SecurityManager is installed,  
 * the AWTPermission accessSystemTray must be granted in order to create a TrayIcon. 
 * Otherwise the constructor will throw a SecurityException.<br>
 * See the SystemTray class overview for an example on how to use the TrayIcon API.
 * @version 1.0
 * @author Kevin
 */
public class Notifications extends TrayIcon {
        
         /**Value selected by user from the confirm dialog box.<br>
          <b>Warning:</b> Do not modify.*/
        public int selected=-1;
        
        /**
         * The constructor that initialises the tray icon with
         * an Image <code>image</code> and a tooltip text <code>text</code>.
         * Also sets the image auto resized feature to true.
         * @param image This is the image to be displayed 
         * by this tray icon in the system tray.
         * @param text This is the tool tip text displayed
         * when the mouse hovers on the tray icon on the 
         * system tray
         */
        public Notifications(Image image,String text){
                super(image,text);
                this.setImageAutoSize(true);
        }
        
        /**
         * This method initialises this tray icon with a mouse listener to
         * listen to and handle mouse clicked events only. When the mouse
         * is clicked and the mouse button clicked is not the default 
         * pop up trigger button, a <code>JOptionPane</code> is displayed to the user.
         */
        public void addActionListener(){
                this.addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent ae) {
                                if(!ae.isPopupTrigger()){
                                        selected=JOptionPane.showConfirmDialog(null,"Accept the file being sent?","File Share",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                }
                        }
                });
        }
        
        /**
         * This method creates a pop up menu and initialises it with 2
         *  menu items: a <code>reject</code> item and an <code>accept</code> item.
         * Both items affect the global variable, <code>selected</code>, depending on what
         * the user selects.
         */
        public void setPopupMenu(){
                PopupMenu trayPopup=new PopupMenu();
                trayPopup.setFont(new Font("palatino linotype",4,14));
                
                MenuItem accept=new MenuItem("     Accept File");
                accept.addActionListener((ActionEvent ae) -> {
                        selected=0;
                });
                
                MenuItem reject = new MenuItem("     Reject File");
                reject.addActionListener((ActionEvent ae) -> {
                        selected=1;
                });
                
                trayPopup.add(accept);
                trayPopup.addSeparator();
                trayPopup.add(reject);
                
                this.setPopupMenu(trayPopup);
        }
        
        /**
         * This method displays the tray icon on the system tray. It also displays a 
         * message along with the tray.<br>
         * <b>WARNING:</b><br> Once this method is called, The JVM will not exit
         * until a call to SystemTray.getSystemTray().remove(this) to remove the added 
         * tray icon from the system tray is made. This is because this method adds the
         * tray icon to the system tray.
         * <br><b>Note:</b> Some platforms may not support showing a message.
         * @param heading The heading to be displayed on top of the tray icon once a 
         * notification is fired.
         * @param message The message to be contained on the tray icon once it is displayed.
         * @param type The type of message as defined in class MessageType.  an enum 
         * indicating the message type.
         * @throws NullPointerException if both the message and the heading are null.
         * @throws AWTException if the desktop system tray is missing
         * @throws IllegalArgumentException if the same instance of a TrayIcon is added more than once
         */
        public void displayTray(String heading,String message,MessageType type)throws AWTException,IllegalArgumentException,NullPointerException{
                SystemTray.getSystemTray().add(this);
                this.displayMessage(heading,message, type);
        }
}
