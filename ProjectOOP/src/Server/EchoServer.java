/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import java.net.*;
import java.io.*;

/**
 *
 * @author mathe
 */
public class EchoServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public EchoServer() {
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

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
                buf = new byte[256];

                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                socket.send(packet);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        socket.close();
    }

    public static void main(String[] args) {
        EchoServer server = new EchoServer();
        server.start();
    }
}
