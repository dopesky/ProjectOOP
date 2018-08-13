/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import AppPackage.AnimationClass;
import Class.*;
import ZipFiles.Zipper;
import com.bulenkov.darcula.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javazoom.jl.player.*;

/**
 *
 * @author Kevin
 */
public class UserInterface extends javax.swing.JFrame {

        final AnimationClass anime = new AnimationClass();
        private boolean capsIsOn;
        private boolean fileNotFound = false;
        private Point compCords = null, frameCords = null;
        Player audio;
        private boolean isCompleted = true;
        private boolean musicFound = true;
        private Scanner About;
        private String aboutContent = "";
        DBUtils database;
        ResultSet set;
        String username, password;
        Server.ServerClass server;
        private static int notifications = 1;
        private final String currentDir = "c:\\Users\\" + System.getProperty("user.name") + "\\Desktop";
        private Process process=null;

        Runnable serverThread = new Runnable() {
                @Override
                public void run() {
                        try {
                                ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd.exe", "/c", "cd c:\\users\\"+System.getProperty("user.name")+"\\desktop\\javaapps\\projectoop\\projectoop\\build\\classes && java -cp c:\\users\\"+System.getProperty("user.name")+"\\desktop\\javaapps\\projectoop\\projectoop\\dist\\lib\\darcula.jar; Server.ServerClass " + notifications});
                                builder.redirectErrorStream(true);
                                process = builder.start();
                                Scanner in = new Scanner(process.getInputStream());
                                while (in.hasNextLine()) {
                                        System.out.println(in.nextLine());
                                }
                        } catch (Throwable th) {
                                displayJOptionPane(th.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        btnServer.setSelected(false);
                        btnServer.setText("Server is Off");
                        startServer = new Thread(serverThread);
                }
        };

        Thread startServer = new Thread(serverThread);
        
        Runnable clientThread=new Runnable() {
            @Override
            public void run(){
                int choice = showOpenDialog();
                File[] files = chooser.getSelectedFiles();
                if (files.length < 1 || choice != JFileChooser.APPROVE_OPTION) {
                        return;
                }
                try {
                        Zipper zipper=new Zipper(files);
                        Client.ClientClass sendFile=new Client.ClientClass("192.168.43.27",username,zipper.zipUp().getAbsolutePath());
                } catch (IOException | SecurityException | IllegalBlockingModeException th) {
                        displayJOptionPane(th.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                startClient=new Thread(clientThread);
            }
        };
        
        Thread startClient=new Thread(clientThread);

        /**
         * Creates new form UserInterface
         */
        public UserInterface() {
                connectToDatabase();
                ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource("/Resources/lolo0073.png")).getImage());
                initComponents();

                findAboutFile();
                music(getClass().getResource("/Resources/music/music.mp3"));

                resize(getClass().getResource("/Resources/lolo0073.png"), btnIcon);
                resize(getClass().getResource("/Resources/lolo0073.png"), btnIconAbout);
                resize(getClass().getResource("/Resources/lolo0073.png"), btnIconCreate);
                resize(getClass().getResource("/Resources/lolo0073.png"), btnIconMain);
                resize(getClass().getResource("/Resources/Images/eye.png"), btnshowhide);

                frameLogin.setIconImage(icon.getImage());
                frameAbout.setIconImage(icon.getImage());
                frameCreate.setIconImage(icon.getImage());
                setIconImage(icon.getImage());

                setTextAreaToTransparent(txtAreaAbout, scrlAbout);

                resize(getClass().getResource("/Resources/images/wall.jpg"), lblAboutBackground);
                resize(getClass().getResource("/Resources/images/back7.jpg"), lblBackGround1);
                resize(getClass().getResource("/Resources/images/cloudy.jpg"), lblCreate);
                resize(getClass().getResource("/Resources/images/main2.jpg"), lblBackground);

                setInitialProperties();

                lblCaps.setVisible(Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK));

                backGroundSlideShow();
                btnshowhide.setVisible(false);
                frameLogin.setVisible(true);
        }

        private void connectToDatabase() {
                try {
                        UIManager.setLookAndFeel(new DarculaLaf());
                        database = new DBUtils("jdbc:mysql://localhost/filesharingoop", "root", "biggie5941");
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (Throwable th) {
                        database = new DBUtils("jdbc:mysql://localhost/filesharingoop", "root", "biggie5941");
                }
                if (!database.isConnectionSuccessful) {
                        System.exit(0);
                }
        }

        private void setInitialProperties() {
                btnExit.setFocusable(false);
                btnExitAbout.setFocusable(false);
                btnMinimize.setFocusable(false);
                btnIcon.setFocusable(false);
                btnIconAbout.setFocusable(false);
                btnIconCreate.setFocusable(false);
                btnForgotPassword.setFocusable(false);
                btnReplay.setFocusable(false);
                btnMinimizeCreate.setFocusable(false);
                btnExitCreate.setFocusable(false);
                frameLogin.setUndecorated(true);
                frameAbout.setUndecorated(true);
                frameLogin.getRootPane().setBorder(new BevelBorder(BevelBorder.LOWERED, Color.YELLOW.darker().darker(), Color.RED.darker().darker().darker().darker(), Color.YELLOW.darker().darker(), Color.RED.darker().darker().darker().darker()));
                frameAbout.getRootPane().setBorder(new BevelBorder(BevelBorder.LOWERED, Color.YELLOW.darker().darker(), Color.RED.darker().darker().darker().darker(), Color.YELLOW.darker().darker(), Color.RED.darker().darker().darker().darker()));
                frameCreate.getRootPane().setBorder(new BevelBorder(BevelBorder.LOWERED, Color.YELLOW.darker().darker(), Color.RED.darker().darker().darker().darker(), Color.YELLOW.darker().darker(), Color.RED.darker().darker().darker().darker()));
                getRootPane().setBorder(new BevelBorder(BevelBorder.LOWERED, Color.YELLOW.darker().darker(), Color.RED.darker().darker().darker().darker(), Color.YELLOW.darker().darker(), Color.RED.darker().darker().darker().darker()));
                frameLogin.setLocationRelativeTo(null);
                frameAbout.setLocationRelativeTo(null);
                frameCreate.setLocationRelativeTo(null);
                setLocationRelativeTo(null);
        }

        private void setlblCapsVisibility(KeyEvent event) {
                capsIsOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
                lblCaps.setVisible(capsIsOn);
        }

        private void movePane() {
                new Thread() {
                        @Override
                        public void run() {
                                try {
                                        Thread.sleep(10);
                                        new Thread() {
                                                @Override
                                                public void run() {
                                                        try {
                                                                if (musicFound && isCompleted) {
                                                                        audio.play();
                                                                        audio.close();
                                                                }
                                                        } catch (Exception e) {
                                                        }
                                                }
                                        }.start();
                                        btnExitAbout.setVisible(false);
                                        btnReplay.setVisible(false);
                                        anime.jTextAreaYUp(309, -935, 60, 1, scrlAbout);
                                        Thread.sleep(78500);
                                        btnReplay.setVisible(true);
                                        btnExitAbout.setVisible(true);
                                } catch (Exception e) {
                                        displayJOptionPane("Sorry, Cannot do this at this time", "Error", JOptionPane.ERROR_MESSAGE);
                                        frameAbout.setVisible(false);
                                        frameLogin.setVisible(true);
                                }
                        }
                }.start();
        }

        private void music(URL url) {
                try {
                        audio = new Player(new FileInputStream(new File(url.toURI())));
                } catch (Exception e) {
                        musicFound = false;
                }
        }

        private void findAboutFile() {
                try {
                        int count = 0;
                        About = new Scanner(new FileInputStream(new File(getClass().getResource("/Resources/Docs/about.txt").toURI()))).useDelimiter("\n");
                        while (About.hasNext()) {
                                count++;
                                if (count == 1) {
                                        aboutContent = About.next();
                                } else {
                                        aboutContent += "\n" + About.next();
                                }
                        }
                        txtAreaAbout.setText(aboutContent);
                        txtAreaAbout.append("\n\n\n\n\n\n\n\n\t        THE END...");
                } catch (FileNotFoundException | URISyntaxException fnfe) {
                        fileNotFound = true;
                }
        }

        private void resize(URL url, JButton jbutton) {
                ImageIcon one = new ImageIcon(url);
                Image two = one.getImage();
                Image three = two.getScaledInstance(jbutton.getWidth(), jbutton.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon four = new ImageIcon(three);
                jbutton.setIcon(four);
        }

        private void resize(URL url, JLabel jlabel) {
                ImageIcon one = new ImageIcon(url);
                Image two = one.getImage();
                Image three = two.getScaledInstance(jlabel.getWidth(), jlabel.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon four = new ImageIcon(three);
                jlabel.setIcon(four);
        }

        private void setTextAreaToTransparent(JTextArea textArea, JScrollPane scrollPane) {
                scrollPane.setOpaque(false);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.setViewportBorder(null);
                textArea.setBorder(null);
                textArea.setBackground(new Color(0, 0, 0, 64));
        }

        private void backGroundSlideShow() {
                new Thread() {
                        int count = 0;
                        boolean passed = false;

                        @Override
                        public void run() {
                                try {
                                        while (true) {
                                                switch (count) {
                                                        case 0:
                                                                if (!passed) {
                                                                        Thread.sleep(10000);
                                                                        passed = true;
                                                                } else {
                                                                        Thread.sleep(13000);
                                                                }
                                                                resize(getClass().getResource("/Resources/images/back8.jpg"), lblBackGround2);
                                                                anime.jLabelXLeft(0, -685, 20, 5, lblBackGround1);
                                                                anime.jLabelXLeft(685, 0, 20, 5, lblBackGround2);
                                                                count = 1;
                                                                break;
                                                        case 1:
                                                                Thread.sleep(13000);
                                                                resize(getClass().getResource("/Resources/images/back9.jpg"), lblBackGround1);
                                                                anime.jLabelXRight(-685, 0, 20, 5, lblBackGround1);
                                                                anime.jLabelXRight(0, 685, 20, 5, lblBackGround2);
                                                                count = 2;
                                                                break;
                                                        case 2:
                                                                Thread.sleep(13000);
                                                                resize(getClass().getResource("/Resources/images/back10.jpg"), lblBackGround2);
                                                                anime.jLabelXLeft(0, -685, 20, 5, lblBackGround1);
                                                                anime.jLabelXLeft(685, 0, 20, 5, lblBackGround2);
                                                                count = 3;
                                                                break;
                                                        case 3:
                                                                Thread.sleep(13000);
                                                                resize(getClass().getResource("/Resources/images/back11.jpg"), lblBackGround1);
                                                                anime.jLabelXRight(-685, 0, 20, 5, lblBackGround1);
                                                                anime.jLabelXRight(0, 685, 20, 5, lblBackGround2);
                                                                count = 4;
                                                                break;
                                                        case 4:
                                                                Thread.sleep(13000);
                                                                resize(getClass().getResource("/Resources/images/back13.jpg"), lblBackGround2);
                                                                anime.jLabelXLeft(0, -685, 20, 5, lblBackGround1);
                                                                anime.jLabelXLeft(685, 0, 20, 5, lblBackGround2);
                                                                count = 5;
                                                                break;
                                                        case 5:
                                                                Thread.sleep(13000);
                                                                resize(getClass().getResource("/Resources/images/back7.jpg"), lblBackGround1);
                                                                anime.jLabelXRight(-685, 0, 20, 5, lblBackGround1);
                                                                anime.jLabelXRight(0, 685, 20, 5, lblBackGround2);
                                                                count = 0;
                                                                break;
                                                }
                                        }
                                } catch (Exception e) {
                                        PrintWriter writer;
                                        try {
                                                writer = new PrintWriter("BackgroundErrorLog.txt");
                                                e.printStackTrace(writer);
                                        } catch (FileNotFoundException ex) {
                                        }
                                }
                        }
                }.start();
        }

        private void displayJOptionPane(String message, String header, int type) {
                try {
                        UIManager.setLookAndFeel(new DarculaLaf());
                        JOptionPane.showMessageDialog(null, message, header, type);
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (Throwable th) {
                }
        }

        private boolean checkUser(String user) {
                try {
                        set = database.getResultSet("select * from users where username='" + user + "'");
                        set.next();
                        String temp = set.getObject(1).toString();
                        if (!temp.isEmpty()) {
                                return true;
                        }
                } catch (SQLException sqle) {
                }
                return false;
        }

        private String getMacAddress() throws SocketException,UnknownHostException {
                InetAddress localhost = InetAddress.getLocalHost();
                NetworkInterface Interface = NetworkInterface.getByInetAddress(localhost);
                byte[] mac = Interface.getHardwareAddress();
                String macString = "";
                for (int i = 0; i < mac.length; i++) {
                        macString += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
                }
                return macString;
        }

        private int showOpenDialog() {
                chooser.setFileFilter(chooser.getAcceptAllFileFilter());
                chooser.setCurrentDirectory(new File(currentDir));
                chooser.setSelectedFile(new File(""));
                return chooser.showOpenDialog(null);
        }

        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                frameLogin = new javax.swing.JFrame();
                txtUserName = new javax.swing.JTextField();
                btnshowhide = new javax.swing.JButton();
                txtPassword = new javax.swing.JPasswordField();
                btnLogin = new javax.swing.JButton();
                btnCancel = new javax.swing.JButton();
                btnForgotPassword = new javax.swing.JButton();
                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                btnMinimize = new javax.swing.JButton();
                btnExit = new javax.swing.JButton();
                lblWelcome = new javax.swing.JLabel();
                btnIcon = new javax.swing.JButton();
                panelTop = new javax.swing.JPanel();
                lblCaps = new javax.swing.JLabel();
                lblBackGround1 = new javax.swing.JLabel();
                lblBackGround2 = new javax.swing.JLabel();
                jLabel4 = new javax.swing.JLabel();
                frameAbout = new javax.swing.JFrame();
                btnReplay = new javax.swing.JButton();
                btnExitAbout = new javax.swing.JButton();
                btnIconAbout = new javax.swing.JButton();
                panelAbout = new javax.swing.JPanel();
                scrlAbout = new javax.swing.JScrollPane();
                txtAreaAbout = new javax.swing.JTextArea();
                lblAboutBackground = new javax.swing.JLabel();
                frameCreate = new javax.swing.JFrame();
                jLabel3 = new javax.swing.JLabel();
                jLabel5 = new javax.swing.JLabel();
                jLabel6 = new javax.swing.JLabel();
                txtUname = new javax.swing.JTextField();
                txtPass = new javax.swing.JTextField();
                txtPassRe = new javax.swing.JTextField();
                btnCancelCreate = new javax.swing.JButton();
                btnBackCreate = new javax.swing.JButton();
                btnCreate = new javax.swing.JButton();
                btnIconCreate = new javax.swing.JButton();
                btnExitCreate = new javax.swing.JButton();
                btnMinimizeCreate = new javax.swing.JButton();
                panelCreate = new javax.swing.JPanel();
                lblCreate = new javax.swing.JLabel();
                chooser = new javax.swing.JFileChooser();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTextArea1 = new javax.swing.JTextArea();
                btnMin = new javax.swing.JButton();
                jButton2 = new javax.swing.JButton();
                btnIconMain = new javax.swing.JButton();
                panelMain = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                btnSlide = new javax.swing.JButton();
                btnChange = new javax.swing.JButton();
                btnLogout = new javax.swing.JButton();
                btnServer = new javax.swing.JToggleButton();
                btnBroadcast = new javax.swing.JToggleButton();
                btnSend = new javax.swing.JToggleButton();
                lblBackground = new javax.swing.JLabel();

                frameLogin.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                frameLogin.setTitle("User Login");
                frameLogin.setBackground(new java.awt.Color(0, 0, 0));
                frameLogin.setResizable(false);
                frameLogin.setSize(new java.awt.Dimension(683, 309));
                frameLogin.getContentPane().setLayout(null);

                txtUserName.setBackground(new java.awt.Color(0, 0, 0));
                txtUserName.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
                txtUserName.setForeground(new java.awt.Color(0, 204, 0));
                txtUserName.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                txtUserNameKeyReleased(evt);
                        }
                });
                frameLogin.getContentPane().add(txtUserName);
                txtUserName.setBounds(240, 79, 330, 30);

                btnshowhide.setBackground(new Color(0,0,0,64));
                btnshowhide.setForeground(new java.awt.Color(255, 255, 255));
                btnshowhide.setBorder(null);
                btnshowhide.setBorderPainted(false);
                btnshowhide.setContentAreaFilled(false);
                btnshowhide.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnshowhide.setFocusable(false);
                btnshowhide.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnshowhideActionPerformed(evt);
                        }
                });
                frameLogin.getContentPane().add(btnshowhide);
                btnshowhide.setBounds(535, 133, 30, 20);

                txtPassword.setBackground(new java.awt.Color(0, 0, 0));
                txtPassword.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
                txtPassword.setForeground(new java.awt.Color(0, 204, 0));
                txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                txtUserNameKeyReleased(evt);
                        }
                });
                frameLogin.getContentPane().add(txtPassword);
                txtPassword.setBounds(240, 129, 330, 30);

                btnLogin.setBackground(new java.awt.Color(0, 0, 0));
                btnLogin.setForeground(new java.awt.Color(0, 204, 0));
                btnLogin.setText("Login");
                btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnLogin.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnLoginActionPerformed(evt);
                        }
                });
                btnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                txtUserNameKeyReleased(evt);
                        }
                });
                frameLogin.getContentPane().add(btnLogin);
                btnLogin.setBounds(240, 180, 90, 30);

                btnCancel.setBackground(new java.awt.Color(0, 0, 0));
                btnCancel.setForeground(new java.awt.Color(0, 204, 0));
                btnCancel.setText("Cancel");
                btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnCancel.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnCancelActionPerformed(evt);
                        }
                });
                btnCancel.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                                btnCancelKeyPressed(evt);
                        }
                });
                frameLogin.getContentPane().add(btnCancel);
                btnCancel.setBounds(480, 180, 90, 30);

                btnForgotPassword.setBackground(new java.awt.Color(0, 0, 0));
                btnForgotPassword.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
                btnForgotPassword.setForeground(new java.awt.Color(255, 0, 0));
                btnForgotPassword.setText("Forgot Password?");
                btnForgotPassword.setContentAreaFilled(false);
                btnForgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnForgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                btnForgotPasswordMouseEntered(evt);
                        }
                        public void mouseExited(java.awt.event.MouseEvent evt) {
                                btnForgotPasswordMouseExited(evt);
                        }
                });
                btnForgotPassword.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnForgotPasswordActionPerformed(evt);
                        }
                });
                frameLogin.getContentPane().add(btnForgotPassword);
                btnForgotPassword.setBounds(333, 230, 142, 30);

                jLabel1.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(255, 255, 255));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("User Name");
                frameLogin.getContentPane().add(jLabel1);
                jLabel1.setBounds(110, 80, 120, 30);

                jLabel2.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
                jLabel2.setForeground(new java.awt.Color(255, 255, 255));
                jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel2.setText("Password");
                frameLogin.getContentPane().add(jLabel2);
                jLabel2.setBounds(110, 125, 120, 30);

                btnMinimize.setBackground(new java.awt.Color(0, 0, 0));
                btnMinimize.setFont(new java.awt.Font("Palatino Linotype", 3, 12)); // NOI18N
                btnMinimize.setForeground(new java.awt.Color(0, 204, 0));
                btnMinimize.setText("_");
                btnMinimize.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnMinimize.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                btnMinimize.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnMinimizeActionPerformed(evt);
                        }
                });
                frameLogin.getContentPane().add(btnMinimize);
                btnMinimize.setBounds(590, 7, 40, 20);

                btnExit.setBackground(new java.awt.Color(0, 0, 0));
                btnExit.setFont(new java.awt.Font("Palatino Linotype", 1, 12)); // NOI18N
                btnExit.setForeground(new java.awt.Color(0, 204, 0));
                btnExit.setText("x");
                btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnExit.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnExitActionPerformed(evt);
                        }
                });
                frameLogin.getContentPane().add(btnExit);
                btnExit.setBounds(632, 7, 40, 20);

                lblWelcome.setBackground(new java.awt.Color(102, 102, 102));
                lblWelcome.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
                lblWelcome.setForeground(new java.awt.Color(206, 252, 188));
                lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblWelcome.setText("WELCOME");
                frameLogin.getContentPane().add(lblWelcome);
                lblWelcome.setBounds(240, 0, 220, 50);

                btnIcon.setBackground(new Color(0,0,0,64));
                btnIcon.setForeground(new Color(0,0,0,64));
                btnIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnIcon.setOpaque(false);
                btnIcon.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnIconActionPerformed(evt);
                        }
                });
                frameLogin.getContentPane().add(btnIcon);
                btnIcon.setBounds(0, 0, 30, 30);

                panelTop.setBackground(new java.awt.Color(0, 0, 0));
                panelTop.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(102, 0, 0), new java.awt.Color(0, 153, 0), new java.awt.Color(102, 0, 0), new java.awt.Color(0, 204, 0)), "User Login", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Monotype Corsiva", 1, 14), new java.awt.Color(0, 204, 0))); // NOI18N
                panelTop.setForeground(new java.awt.Color(51, 51, 55));
                panelTop.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                panelTop.setOpaque(false);
                panelTop.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                        public void mouseDragged(java.awt.event.MouseEvent evt) {
                                panelTopMouseDragged(evt);
                        }
                });
                panelTop.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                                panelTopMousePressed(evt);
                        }
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                                panelTopMouseReleased(evt);
                        }
                });

                javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
                panelTop.setLayout(panelTopLayout);
                panelTopLayout.setHorizontalGroup(
                        panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                );
                panelTopLayout.setVerticalGroup(
                        panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                );

                frameLogin.getContentPane().add(panelTop);
                panelTop.setBounds(30, 0, 640, 30);

                lblCaps.setForeground(new java.awt.Color(204, 0, 0));
                lblCaps.setText("Caps Lock Is On!!!");
                frameLogin.getContentPane().add(lblCaps);
                lblCaps.setBounds(570, 45, 100, 20);
                frameLogin.getContentPane().add(lblBackGround1);
                lblBackGround1.setBounds(0, 0, 685, 309);
                frameLogin.getContentPane().add(lblBackGround2);
                lblBackGround2.setBounds(685, 0, 685, 309);

                jLabel4.setBackground(new java.awt.Color(0, 0, 0));
                jLabel4.setOpaque(true);
                frameLogin.getContentPane().add(jLabel4);
                jLabel4.setBounds(-20, -5, 750, 320);

                frameAbout.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                frameAbout.setTitle("About");
                frameAbout.setResizable(false);
                frameAbout.setSize(new java.awt.Dimension(683, 309));
                frameAbout.getContentPane().setLayout(null);

                btnReplay.setBackground(new java.awt.Color(0, 0, 0));
                btnReplay.setForeground(new java.awt.Color(204, 0, 204));
                btnReplay.setText("Replay");
                btnReplay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnReplay.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnReplayActionPerformed(evt);
                        }
                });
                frameAbout.getContentPane().add(btnReplay);
                btnReplay.setBounds(10, 270, 68, 32);

                btnExitAbout.setBackground(new java.awt.Color(0, 0, 0));
                btnExitAbout.setFont(new java.awt.Font("Palatino Linotype", 1, 13)); // NOI18N
                btnExitAbout.setForeground(new java.awt.Color(255, 0, 255));
                btnExitAbout.setText("x");
                btnExitAbout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnExitAbout.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnExitAboutActionPerformed(evt);
                        }
                });
                frameAbout.getContentPane().add(btnExitAbout);
                btnExitAbout.setBounds(640, 5, 40, 20);

                btnIconAbout.setBackground(new Color(0,0,0,64));
                frameAbout.getContentPane().add(btnIconAbout);
                btnIconAbout.setBounds(0, 0, 30, 30);

                panelAbout.setBackground(new Color(0,0,0,64));
                panelAbout.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 0, 0), new java.awt.Color(153, 0, 153), new java.awt.Color(51, 0, 0), new java.awt.Color(255, 0, 255)), "About", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Monotype Corsiva", 1, 14), new java.awt.Color(255, 51, 153))); // NOI18N
                panelAbout.setForeground(new Color(0,0,0,64));
                panelAbout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                panelAbout.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                        public void mouseDragged(java.awt.event.MouseEvent evt) {
                                panelAboutMouseDragged(evt);
                        }
                });
                panelAbout.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                                panelAboutMousePressed(evt);
                        }
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                                panelAboutMouseReleased(evt);
                        }
                });

                javax.swing.GroupLayout panelAboutLayout = new javax.swing.GroupLayout(panelAbout);
                panelAbout.setLayout(panelAboutLayout);
                panelAboutLayout.setHorizontalGroup(
                        panelAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 628, Short.MAX_VALUE)
                );
                panelAboutLayout.setVerticalGroup(
                        panelAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 5, Short.MAX_VALUE)
                );

                frameAbout.getContentPane().add(panelAbout);
                panelAbout.setBounds(30, 0, 640, 30);

                txtAreaAbout.setEditable(false);
                txtAreaAbout.setColumns(20);
                txtAreaAbout.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
                txtAreaAbout.setForeground(new java.awt.Color(255, 255, 255));
                txtAreaAbout.setRows(5);
                scrlAbout.setViewportView(txtAreaAbout);

                frameAbout.getContentPane().add(scrlAbout);
                scrlAbout.setBounds(100, 309, 500, 2000);
                frameAbout.getContentPane().add(lblAboutBackground);
                lblAboutBackground.setBounds(0, 0, 683, 309);

                frameCreate.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                frameCreate.setUndecorated(true);
                frameCreate.setResizable(false);
                frameCreate.setSize(new java.awt.Dimension(840, 334));
                frameCreate.getContentPane().setLayout(null);

                jLabel3.setFont(new java.awt.Font("Monotype Corsiva", 3, 18)); // NOI18N
                jLabel3.setForeground(new java.awt.Color(255, 255, 255));
                jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel3.setText("Repeat Password");
                jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                frameCreate.getContentPane().add(jLabel3);
                jLabel3.setBounds(100, 180, 140, 30);

                jLabel5.setFont(new java.awt.Font("Monotype Corsiva", 3, 18)); // NOI18N
                jLabel5.setForeground(new java.awt.Color(255, 255, 255));
                jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel5.setText("UserName");
                jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                frameCreate.getContentPane().add(jLabel5);
                jLabel5.setBounds(100, 60, 140, 30);

                jLabel6.setFont(new java.awt.Font("Monotype Corsiva", 3, 18)); // NOI18N
                jLabel6.setForeground(new java.awt.Color(255, 255, 255));
                jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel6.setText("Password");
                jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                frameCreate.getContentPane().add(jLabel6);
                jLabel6.setBounds(100, 120, 140, 30);

                txtUname.setBackground(new java.awt.Color(0, 0, 0));
                txtUname.setFont(new java.awt.Font("Courier New", 3, 16)); // NOI18N
                txtUname.setForeground(new java.awt.Color(102, 102, 255));
                txtUname.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                btnCreateKeyReleased(evt);
                        }
                });
                frameCreate.getContentPane().add(txtUname);
                txtUname.setBounds(260, 60, 490, 30);

                txtPass.setBackground(new java.awt.Color(0, 0, 0));
                txtPass.setFont(new java.awt.Font("Courier New", 3, 16)); // NOI18N
                txtPass.setForeground(new java.awt.Color(102, 102, 255));
                txtPass.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                btnCreateKeyReleased(evt);
                        }
                });
                frameCreate.getContentPane().add(txtPass);
                txtPass.setBounds(260, 120, 490, 30);

                txtPassRe.setBackground(new java.awt.Color(0, 0, 0));
                txtPassRe.setFont(new java.awt.Font("Courier New", 3, 16)); // NOI18N
                txtPassRe.setForeground(new java.awt.Color(102, 102, 255));
                txtPassRe.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                btnCreateKeyReleased(evt);
                        }
                });
                frameCreate.getContentPane().add(txtPassRe);
                txtPassRe.setBounds(260, 180, 490, 30);

                btnCancelCreate.setBackground(new java.awt.Color(0, 0, 0));
                btnCancelCreate.setFont(new java.awt.Font("Courier New", 3, 18)); // NOI18N
                btnCancelCreate.setForeground(new java.awt.Color(102, 102, 255));
                btnCancelCreate.setText("Cancel");
                btnCancelCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnCancelCreate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnCancelCreateActionPerformed(evt);
                        }
                });
                btnCancelCreate.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                btnCancelCreateKeyReleased(evt);
                        }
                });
                frameCreate.getContentPane().add(btnCancelCreate);
                btnCancelCreate.setBounds(640, 240, 110, 37);

                btnBackCreate.setBackground(new java.awt.Color(0, 0, 0));
                btnBackCreate.setFont(new java.awt.Font("Courier New", 3, 18)); // NOI18N
                btnBackCreate.setForeground(new java.awt.Color(102, 102, 255));
                btnBackCreate.setText("Back");
                btnBackCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnBackCreate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnBackCreateActionPerformed(evt);
                        }
                });
                btnBackCreate.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                btnBackCreateKeyReleased(evt);
                        }
                });
                frameCreate.getContentPane().add(btnBackCreate);
                btnBackCreate.setBounds(210, 240, 110, 37);

                btnCreate.setBackground(new java.awt.Color(0, 0, 0));
                btnCreate.setFont(new java.awt.Font("Courier New", 3, 18)); // NOI18N
                btnCreate.setForeground(new java.awt.Color(102, 102, 255));
                btnCreate.setText("Create");
                btnCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnCreate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnCreateActionPerformed(evt);
                        }
                });
                btnCreate.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                btnCreateKeyReleased(evt);
                        }
                });
                frameCreate.getContentPane().add(btnCreate);
                btnCreate.setBounds(420, 240, 110, 37);

                btnIconCreate.setBackground(new java.awt.Color(0, 0, 0));
                frameCreate.getContentPane().add(btnIconCreate);
                btnIconCreate.setBounds(0, 0, 30, 30);

                btnExitCreate.setBackground(new java.awt.Color(0, 0, 0));
                btnExitCreate.setFont(new java.awt.Font("Palatino Linotype", 3, 12)); // NOI18N
                btnExitCreate.setForeground(new java.awt.Color(102, 102, 255));
                btnExitCreate.setText("x");
                btnExitCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnExitCreate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnExitActionPerformed(evt);
                        }
                });
                frameCreate.getContentPane().add(btnExitCreate);
                btnExitCreate.setBounds(790, 7, 40, 20);

                btnMinimizeCreate.setBackground(new java.awt.Color(0, 0, 0));
                btnMinimizeCreate.setFont(new java.awt.Font("Palatino Linotype", 3, 12)); // NOI18N
                btnMinimizeCreate.setForeground(new java.awt.Color(102, 102, 255));
                btnMinimizeCreate.setText("_");
                btnMinimizeCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnMinimizeCreate.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                btnMinimizeCreate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnMinimizeCreateActionPerformed(evt);
                        }
                });
                frameCreate.getContentPane().add(btnMinimizeCreate);
                btnMinimizeCreate.setBounds(750, 7, 40, 20);

                panelCreate.setBackground(new Color(0,0,0,64));
                panelCreate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(0, 51, 204), new java.awt.Color(102, 0, 0), new java.awt.Color(153, 0, 0), new java.awt.Color(0, 0, 102)), "Create Account", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Monotype Corsiva", 1, 13), new java.awt.Color(0, 102, 255))); // NOI18N
                panelCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                panelCreate.setOpaque(false);
                panelCreate.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                        public void mouseDragged(java.awt.event.MouseEvent evt) {
                                panelCreateMouseDragged(evt);
                        }
                });
                panelCreate.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                                panelCreateMousePressed(evt);
                        }
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                                panelCreateMouseReleased(evt);
                        }
                });
                panelCreate.setLayout(null);
                frameCreate.getContentPane().add(panelCreate);
                panelCreate.setBounds(30, 0, 780, 30);

                lblCreate.setBackground(new java.awt.Color(0, 0, 0));
                lblCreate.setOpaque(true);
                frameCreate.getContentPane().add(lblCreate);
                lblCreate.setBounds(0, 0, 840, 330);

                chooser.setApproveButtonText("Send");
                chooser.setApproveButtonToolTipText("Click to send selected files.");
                chooser.setCurrentDirectory(new java.io.File("C:\\Users"));
                chooser.setDialogTitle("Select files to Send");
                chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setFont(new java.awt.Font("Palatino Linotype", 3, 16)); // NOI18N
                chooser.setMultiSelectionEnabled(true);

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setUndecorated(true);
                setResizable(false);
                setSize(new java.awt.Dimension(915, 380));
                getContentPane().setLayout(null);

                jTextArea1.setColumns(20);
                jTextArea1.setRows(5);
                jScrollPane1.setViewportView(jTextArea1);

                getContentPane().add(jScrollPane1);
                jScrollPane1.setBounds(220, 30, 680, 330);

                btnMin.setBackground(new java.awt.Color(0, 0, 0));
                btnMin.setFont(new java.awt.Font("Palatino Linotype", 3, 12)); // NOI18N
                btnMin.setForeground(new java.awt.Color(204, 0, 0));
                btnMin.setText("_");
                btnMin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnMin.setFocusable(false);
                btnMin.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
                btnMin.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnMinActionPerformed(evt);
                        }
                });
                getContentPane().add(btnMin);
                btnMin.setBounds(820, 7, 40, 20);

                jButton2.setBackground(new java.awt.Color(0, 0, 0));
                jButton2.setFont(new java.awt.Font("Palatino Linotype", 3, 12)); // NOI18N
                jButton2.setForeground(new java.awt.Color(204, 0, 0));
                jButton2.setText("x");
                jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                jButton2.setFocusable(false);
                jButton2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnExitActionPerformed(evt);
                        }
                });
                getContentPane().add(jButton2);
                jButton2.setBounds(860, 7, 40, 20);

                btnIconMain.setBackground(new java.awt.Color(0, 0, 0));
                btnIconMain.setFocusable(false);
                getContentPane().add(btnIconMain);
                btnIconMain.setBounds(0, 0, 30, 30);

                panelMain.setBackground(new Color(0,0,0,64));
                panelMain.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(153, 0, 0), new java.awt.Color(153, 153, 153), new java.awt.Color(102, 0, 0), new java.awt.Color(204, 204, 204)), "Main", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Monotype Corsiva", 1, 13), new java.awt.Color(153, 0, 0))); // NOI18N
                panelMain.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                panelMain.setOpaque(false);
                panelMain.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                        public void mouseDragged(java.awt.event.MouseEvent evt) {
                                panelMainMouseDragged(evt);
                        }
                });
                panelMain.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                                panelMainMousePressed(evt);
                        }
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                                panelMainMouseReleased(evt);
                        }
                });

                javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
                panelMain.setLayout(panelMainLayout);
                panelMainLayout.setHorizontalGroup(
                        panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 838, Short.MAX_VALUE)
                );
                panelMainLayout.setVerticalGroup(
                        panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                );

                getContentPane().add(panelMain);
                panelMain.setBounds(36, 0, 850, 30);

                jPanel2.setBackground(new java.awt.Color(51, 51, 51));
                jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 0, 0), new java.awt.Color(102, 102, 102), new java.awt.Color(153, 0, 0), new java.awt.Color(153, 153, 153)));

                btnSlide.setBackground(new java.awt.Color(0, 0, 0));
                btnSlide.setFont(new java.awt.Font("Palatino Linotype", 3, 16)); // NOI18N
                btnSlide.setForeground(new java.awt.Color(153, 0, 0));
                btnSlide.setText("Background");
                btnSlide.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnSlide.setFocusable(false);

                btnChange.setBackground(new java.awt.Color(0, 0, 0));
                btnChange.setFont(new java.awt.Font("Palatino Linotype", 3, 16)); // NOI18N
                btnChange.setForeground(new java.awt.Color(153, 0, 0));
                btnChange.setText("Change Password");
                btnChange.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnChange.setFocusable(false);

                btnLogout.setBackground(new java.awt.Color(0, 0, 0));
                btnLogout.setFont(new java.awt.Font("Palatino Linotype", 3, 16)); // NOI18N
                btnLogout.setForeground(new java.awt.Color(153, 0, 0));
                btnLogout.setText("Log Out");
                btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnLogout.setFocusable(false);
                btnLogout.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnLogoutActionPerformed(evt);
                        }
                });

                btnServer.setBackground(new java.awt.Color(0, 0, 0));
                btnServer.setFont(new java.awt.Font("Palatino Linotype", 3, 16)); // NOI18N
                btnServer.setForeground(new java.awt.Color(153, 0, 0));
                btnServer.setText("Server is Off");
                btnServer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnServer.setFocusable(false);
                btnServer.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnServerActionPerformed(evt);
                        }
                });

                btnBroadcast.setBackground(new java.awt.Color(0, 0, 0));
                btnBroadcast.setFont(new java.awt.Font("Palatino Linotype", 3, 16)); // NOI18N
                btnBroadcast.setForeground(new java.awt.Color(153, 0, 0));
                btnBroadcast.setText("Broadcast is Off");
                btnBroadcast.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnBroadcast.setFocusable(false);

                btnSend.setBackground(new java.awt.Color(0, 0, 0));
                btnSend.setFont(new java.awt.Font("Palatino Linotype", 3, 16)); // NOI18N
                btnSend.setForeground(new java.awt.Color(153, 0, 0));
                btnSend.setText("Send");
                btnSend.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnSendActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnSlide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnChange, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                        .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnBroadcast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                );
                jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSlide)
                                .addGap(18, 18, 18)
                                .addComponent(btnChange)
                                .addGap(18, 18, 18)
                                .addComponent(btnServer)
                                .addGap(18, 18, 18)
                                .addComponent(btnBroadcast)
                                .addGap(19, 19, 19)
                                .addComponent(btnLogout)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                getContentPane().add(jPanel2);
                jPanel2.setBounds(0, 33, 210, 325);

                lblBackground.setBackground(new java.awt.Color(0, 0, 0));
                lblBackground.setOpaque(true);
                getContentPane().add(lblBackground);
                lblBackground.setBounds(0, 0, 915, 380);
        }// </editor-fold>//GEN-END:initComponents

        private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
                username = txtUserName.getText().trim();
                password = txtPassword.getText().trim();
                if (username.isEmpty() || password.isEmpty()) {
                        displayJOptionPane("No field can be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }
                try {
                        database.prepareStatement("select * from users where username=?");
                        database.setObjects(new Object[]{username});
                        set = database.executeQueryPst();
                        if (set.next()) {
                                String temp = set.getObject(3).toString();
                                if (temp.equals(password)) {
//                                        database.execute("update users set macaddress='" + getMacAddress() + "' where username='" + username + "'");
                                        txtUserName.setText("");
                                        txtPassword.setText("");
                                        frameLogin.setVisible(false);
                                        setVisible(true);
                                } else {
                                        displayJOptionPane("Wrong Password", "Error", JOptionPane.ERROR_MESSAGE);
                                        txtPassword.setText("");
                                        txtPassword.requestFocus();
                                }
                        } else {
                                displayJOptionPane("Wrong Username", "Error", JOptionPane.ERROR_MESSAGE);
                                txtUserName.setText("");
                                txtPassword.setText("");
                                txtUserName.requestFocus();
                        }
                } catch (NullPointerException npe) {
                        displayJOptionPane("You are not connected to the internet.\nExiting...", "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                } catch (Exception sqle) {
                        displayJOptionPane(sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
        }//GEN-LAST:event_btnLoginActionPerformed

        private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
                txtUserName.setText("");
                txtPassword.setText("");
                txtUserName.requestFocus();
        }//GEN-LAST:event_btnCancelActionPerformed

        private void btnCancelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelKeyPressed
                switch (evt.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                                btnCancel.doClick();
                                break;
                        case KeyEvent.VK_ESCAPE:
                                System.exit(0);
                        default:
                                setlblCapsVisibility(evt);
                                break;
                }
        }//GEN-LAST:event_btnCancelKeyPressed

        private void btnForgotPasswordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnForgotPasswordMouseEntered
                btnForgotPassword.setForeground(Color.ORANGE);
        }//GEN-LAST:event_btnForgotPasswordMouseEntered

        private void btnForgotPasswordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnForgotPasswordMouseExited
                btnForgotPassword.setForeground(Color.RED);
        }//GEN-LAST:event_btnForgotPasswordMouseExited

        private void btnForgotPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForgotPasswordActionPerformed
                displayJOptionPane("Please see Administrator to change password", "Information", JOptionPane.INFORMATION_MESSAGE);
        }//GEN-LAST:event_btnForgotPasswordActionPerformed

        private void btnMinimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimizeActionPerformed
                frameLogin.setState(JFrame.ICONIFIED);
        }//GEN-LAST:event_btnMinimizeActionPerformed

        private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
                System.exit(0);
        }//GEN-LAST:event_btnExitActionPerformed

        private void btnIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIconActionPerformed
                JPopupMenu icon = new JPopupMenu();
                JMenuItem create = new JMenuItem("Create Account");
                create.setFont(new Font("courier new", 4, 14));
                JMenuItem about = new JMenuItem("About");
                about.setFont(new Font("courier new", 4, 14));
                JMenuItem aob = new JMenuItem("...");
                aob.setFont(new Font("courier new", 4, 14));
                icon.add(create);
                icon.add(about);
                icon.add(aob);
                create.setCursor(new Cursor(Cursor.HAND_CURSOR));
                about.setCursor(create.getCursor());
                aob.setCursor(create.getCursor());
                create.addActionListener((ActionEvent ae) -> {
                        frameLogin.setVisible(false);
                        frameCreate.setVisible(true);
                        txtUname.requestFocus();
                });
                about.addActionListener((ActionEvent e) -> {
                        if (!fileNotFound) {
                                frameLogin.setVisible(false);
                                frameAbout.setVisible(true);
                                movePane();
                        } else {
                                displayJOptionPane("\"About\" file cannot be found", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                });
                aob.addActionListener((ActionEvent e) -> {
                        if (Desktop.isDesktopSupported()) {
                                try {
                                        Desktop.getDesktop().browse(new URI("www.localhost/phpmyadmin/index.php"));
                                } catch (IOException | URISyntaxException exe) {}
                        }
                });
                icon.show(btnIcon, 5, btnIcon.getHeight());
        }//GEN-LAST:event_btnIconActionPerformed

        private void panelTopMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopMouseDragged
                Point currentCords = new Point(evt.getXOnScreen(), evt.getYOnScreen());
                frameLogin.setLocation(currentCords.x - compCords.x + frameCords.x, currentCords.y - compCords.y + frameCords.y);
        }//GEN-LAST:event_panelTopMouseDragged

        private void panelTopMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopMousePressed
                compCords = new Point(evt.getXOnScreen(), evt.getYOnScreen());
                frameCords = new Point(frameLogin.getX(), frameLogin.getY());
        }//GEN-LAST:event_panelTopMousePressed

        private void panelTopMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelTopMouseReleased
                compCords = null;
                frameCords = null;
        }//GEN-LAST:event_panelTopMouseReleased

        private void btnReplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplayActionPerformed
                scrlAbout.setLocation(100, 309);
                if (!audio.isComplete()) {
                        isCompleted = false;
                } else {
                        audio.close();
                        music(getClass().getResource("/Resources/music/music.mp3"));
                        isCompleted = true;
                }
                movePane();
        }//GEN-LAST:event_btnReplayActionPerformed

        private void btnExitAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitAboutActionPerformed
                scrlAbout.setLocation(100, 309);
                audio.close();
                music(getClass().getResource("/Resources/music/music1.mp3"));
                isCompleted = true;
                frameAbout.setVisible(false);
                frameLogin.setVisible(true);
                txtUserName.setText("");
                txtPassword.setText("");
                txtUserName.requestFocus();
        }//GEN-LAST:event_btnExitAboutActionPerformed

        private void panelAboutMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAboutMouseDragged
                Point currentCords = evt.getLocationOnScreen();
                frameAbout.setLocation(currentCords.x - compCords.x + frameCords.x, currentCords.y - compCords.y + frameCords.y);
        }//GEN-LAST:event_panelAboutMouseDragged

        private void panelAboutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAboutMousePressed
                compCords = evt.getLocationOnScreen();
                frameCords = new Point(frameAbout.getX(), frameAbout.getY());
        }//GEN-LAST:event_panelAboutMousePressed

        private void panelAboutMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAboutMouseReleased
                compCords = null;
                frameCords = null;
        }//GEN-LAST:event_panelAboutMouseReleased

        private void btnMinimizeCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimizeCreateActionPerformed
                frameCreate.setState(ICONIFIED);
        }//GEN-LAST:event_btnMinimizeCreateActionPerformed

        private void btnBackCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackCreateActionPerformed
                frameCreate.setVisible(false);
                txtUname.setText("");
                txtPass.setText("");
                txtPassRe.setText("");
                frameLogin.setVisible(true);
                txtUserName.setText("");
                txtPassword.setText("");
                txtUserName.requestFocus();
        }//GEN-LAST:event_btnBackCreateActionPerformed

        private void btnBackCreateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBackCreateKeyReleased
                switch (evt.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                                btnBackCreate.doClick();
                                break;
                        case KeyEvent.VK_ESCAPE:
                                System.exit(0);
                        default:
                                setlblCapsVisibility(evt);
                                break;
                }
        }//GEN-LAST:event_btnBackCreateKeyReleased

        private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
                String user = txtUname.getText().trim();
                String pass = txtPass.getText().trim();
                String passRe = txtPassRe.getText().trim();
                if (user.isEmpty() || pass.isEmpty() || passRe.isEmpty()) {
                        displayJOptionPane("No field can be empty", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                        if (user.length() < 3) {
                                displayJOptionPane("Username too short", "Error", JOptionPane.ERROR_MESSAGE);
                                txtUname.setText("");
                                txtUname.requestFocus();
                                return;
                        }
                        if (pass.length() < 8) {
                                displayJOptionPane("Password too short", "Error", JOptionPane.ERROR_MESSAGE);
                                txtPass.setText("");
                                txtPassRe.setText("");
                                txtPass.requestFocus();
                                return;
                        }
                        if (!pass.equals(passRe)) {
                                displayJOptionPane("Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
                                txtPassRe.setText("");
                                txtPassRe.requestFocus();
                                return;
                        }
                        if (checkUser(user)) {
                                displayJOptionPane("User already exists", "Error", JOptionPane.ERROR_MESSAGE);
                                txtUname.setText("");
                                txtUname.requestFocus();
                                return;
                        }
                        try {
                                String macAddress = getMacAddress();
                                database.prepareStatement("insert into users(username,macaddress,password) values (?,?,?)");
                                database.setObjects(new Object[]{user, macAddress, pass});
                                database.executePst();
                                displayJOptionPane("SUCCESS", "Information", JOptionPane.INFORMATION_MESSAGE);
                                txtUname.setText("");
                                txtPass.setText("");
                                txtPassRe.setText("");
                                frameCreate.setVisible(false);
                                frameLogin.setVisible(true);
                                txtUserName.setText(user);
                                txtPassword.requestFocus();
                        } catch (SocketException | UnknownHostException | SQLException e) {
                                displayJOptionPane(e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                }
        }//GEN-LAST:event_btnCreateActionPerformed

        private void btnCreateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCreateKeyReleased
                switch (evt.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                                btnCreate.doClick();
                                break;
                        case KeyEvent.VK_ESCAPE:
                                System.exit(0);
                        default:
                                setlblCapsVisibility(evt);
                                break;
                }
        }//GEN-LAST:event_btnCreateKeyReleased

        private void btnCancelCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelCreateActionPerformed
                txtUname.setText("");
                txtPass.setText("");
                txtPassRe.setText("");
                txtUname.requestFocus();
        }//GEN-LAST:event_btnCancelCreateActionPerformed

        private void btnCancelCreateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelCreateKeyReleased
                switch (evt.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                                btnCancelCreate.doClick();
                                break;
                        case KeyEvent.VK_ESCAPE:
                                System.exit(0);
                        default:
                                setlblCapsVisibility(evt);
                                break;
                }
        }//GEN-LAST:event_btnCancelCreateKeyReleased

        private void btnshowhideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnshowhideActionPerformed
                if (txtPassword.echoCharIsSet()) {
                        txtPassword.setEchoChar((char) 0);
                        resize(getClass().getResource("/Resources/images/eye-slash.png"), btnshowhide);
                } else {
                        txtPassword.setEchoChar('*');
                        resize(getClass().getResource("/Resources/images/eye.png"), btnshowhide);
                }
        }//GEN-LAST:event_btnshowhideActionPerformed

        private void txtUserNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyReleased
                switch (evt.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                                btnLogin.doClick();
                                break;
                        case KeyEvent.VK_ESCAPE:
                                System.exit(0);
                        default:
                                setlblCapsVisibility(evt);
                                break;
                }
                if (evt.getSource().equals(txtPassword)) {
                        if (!txtPassword.getText().trim().equals("")) {
                                btnshowhide.setVisible(true);
                        } else {
                                btnshowhide.setVisible(false);
                        }
                }
        }//GEN-LAST:event_txtUserNameKeyReleased

        private void btnMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinActionPerformed
                setState(ICONIFIED);
        }//GEN-LAST:event_btnMinActionPerformed

        private void btnServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServerActionPerformed
                try {
                        if (btnServer.isSelected()) {
                                btnServer.setText("Turning Server on");
                                new Thread() {
                                        @Override
                                        public void run() {
                                                startServer.start();
                                        }
                                }.start();
                        } else {
                                btnServer.setSelected(true);
                        }
                } catch (Throwable th) {
                        displayJOptionPane(th.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
        }//GEN-LAST:event_btnServerActionPerformed

        private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
                setVisible(false);
                frameLogin.setVisible(true);
                txtUserName.requestFocus();
        }//GEN-LAST:event_btnLogoutActionPerformed

        private void panelCreateMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelCreateMouseDragged
                Point currentCords = evt.getLocationOnScreen();
                frameCreate.setLocation(currentCords.x - compCords.x + frameCords.x, currentCords.y - compCords.y + frameCords.y);
        }//GEN-LAST:event_panelCreateMouseDragged

        private void panelCreateMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelCreateMousePressed
                compCords = evt.getLocationOnScreen();
                frameCords = new Point(frameCreate.getX(), frameCreate.getY());
        }//GEN-LAST:event_panelCreateMousePressed

        private void panelCreateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelCreateMouseReleased
                compCords = null;
                frameCords = null;
        }//GEN-LAST:event_panelCreateMouseReleased

        private void panelMainMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMainMouseDragged
                Point currentCords = evt.getLocationOnScreen();
                setLocation(currentCords.x - compCords.x + frameCords.x, currentCords.y - compCords.y + frameCords.y);
        }//GEN-LAST:event_panelMainMouseDragged

        private void panelMainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMainMousePressed
                compCords = evt.getLocationOnScreen();
                frameCords = new Point(getX(), getY());
        }//GEN-LAST:event_panelMainMousePressed

        private void panelMainMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMainMouseReleased
                compCords = null;
                frameCords = null;
        }//GEN-LAST:event_panelMainMouseReleased

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        if(btnSend.isSelected()){
            btnSend.setText("Sending...");
            new Thread(){
                    @Override
                    public void run(){
                            startClient.start();
                    }
            }.start();
        }else{
            btnSend.setSelected(true);
        }
    }//GEN-LAST:event_btnSendActionPerformed

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
                /* Set the Nimbus look and feel */
                //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
                /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
                 */
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                }
                //</editor-fold>

                /* Create and display the form */
                javax.swing.SwingUtilities.invokeLater(() -> {
                        UserInterface temp = new UserInterface();
                });
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btnBackCreate;
        private javax.swing.JToggleButton btnBroadcast;
        private javax.swing.JButton btnCancel;
        private javax.swing.JButton btnCancelCreate;
        private javax.swing.JButton btnChange;
        private javax.swing.JButton btnCreate;
        private javax.swing.JButton btnExit;
        private javax.swing.JButton btnExitAbout;
        private javax.swing.JButton btnExitCreate;
        private javax.swing.JButton btnForgotPassword;
        private javax.swing.JButton btnIcon;
        private javax.swing.JButton btnIconAbout;
        private javax.swing.JButton btnIconCreate;
        private javax.swing.JButton btnIconMain;
        private javax.swing.JButton btnLogin;
        private javax.swing.JButton btnLogout;
        private javax.swing.JButton btnMin;
        private javax.swing.JButton btnMinimize;
        private javax.swing.JButton btnMinimizeCreate;
        private javax.swing.JButton btnReplay;
        private javax.swing.JToggleButton btnSend;
        public static javax.swing.JToggleButton btnServer;
        private javax.swing.JButton btnSlide;
        private javax.swing.JButton btnshowhide;
        private javax.swing.JFileChooser chooser;
        private javax.swing.JFrame frameAbout;
        private javax.swing.JFrame frameCreate;
        private javax.swing.JFrame frameLogin;
        private javax.swing.JButton jButton2;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTextArea jTextArea1;
        private javax.swing.JLabel lblAboutBackground;
        private javax.swing.JLabel lblBackGround1;
        private javax.swing.JLabel lblBackGround2;
        private javax.swing.JLabel lblBackground;
        private javax.swing.JLabel lblCaps;
        private javax.swing.JLabel lblCreate;
        private javax.swing.JLabel lblWelcome;
        private javax.swing.JPanel panelAbout;
        private javax.swing.JPanel panelCreate;
        private javax.swing.JPanel panelMain;
        private javax.swing.JPanel panelTop;
        private javax.swing.JScrollPane scrlAbout;
        private javax.swing.JTextArea txtAreaAbout;
        private javax.swing.JTextField txtPass;
        private javax.swing.JTextField txtPassRe;
        private javax.swing.JPasswordField txtPassword;
        private javax.swing.JTextField txtUname;
        private javax.swing.JTextField txtUserName;
        // End of variables declaration//GEN-END:variables
}
