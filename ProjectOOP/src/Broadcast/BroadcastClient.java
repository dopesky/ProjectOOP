/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Broadcast;

/**
 *
 * @author mathe
 */
import java.net.*;

public class BroadcastClient extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private String received;

    public BroadcastClient() {
        try {
            socket = new DatagramSocket(4445);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        running = true;
        System.out.println("Waiting for a broadcast...");

        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                System.out.println("1");
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                this.received = received;

                System.out.println(received);
//                ImageIcon getImage = new ImageIcon(getClass().getResource("/Resources/lolo0073.png"));
//                Image imageIcon = getImage.getImage();
//                Notifications notify = new Notifications(imageIcon, "Notification");
//                notify.addActionListener();
//                notify.setPopupMenu();
//                notify.displayTray("Notice! Notice!", received, TrayIcon.MessageType.NONE);
                buf = new byte[256];

                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                socket.send(packet);
            } catch (Exception ex) {
                //System.out.println(ex);
            }
        }
        socket.close();
    }

    public String getReceived() {
        return this.received;
    }

    public static void main(String[] args) {
        BroadcastClient client = new BroadcastClient();
        client.start();
    }
}
