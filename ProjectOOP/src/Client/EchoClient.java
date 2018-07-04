/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author mathe
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class EchoClient {

    private DatagramSocket socket;
    private InetAddress address;
    Scanner input = new Scanner(System.in);
    private byte[] buf;

    public EchoClient() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.out.println("Enter the message you want to broadcast:");
        String message = "";

        // keep reading until "Over" is input
        while (!message.equals("Over")) {
            try {
                message = input.nextLine();
                sendEcho(message);
            } catch (Exception i) {
                System.out.println(i);
            }
        }

    }

    public String sendEcho(String msg) {
        try {
            buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(
                    packet.getData(), 0, packet.getLength());
            return received;
        } catch (Exception ex) {
            System.out.println(ex);
            return "Error";
        }

    }

    public void close() {
        socket.close();
    }

    public static void main(String[] args) {
        EchoClient client = new EchoClient();
    }
}
