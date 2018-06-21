/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.net.*;

/**
 *
 * @author Kevin
 */
public class Test {
        private static NetworkInterface ni;
        
        
        public Test(){
                
        }
        
        public static void main(String[] args) throws UnknownHostException,SocketException{
                String ipv4=Inet4Address.getLocalHost().getHostAddress();
                System.out.println(ipv4);
                System.out.println();
                
                String host=Inet4Address.getLocalHost().getCanonicalHostName();
                System.out.println(host);
                System.out.println();
                
                String loopback=Inet4Address.getLoopbackAddress().getHostAddress();
                System.out.println(loopback);
                System.out.println();
                
                String name=Inet4Address.getByAddress(new byte[]{-64,-88,0,14}).getHostName();
                System.out.println(name);
                System.out.println();
                
                InetAddress temp=InetAddress.getLocalHost();
                ni=NetworkInterface.getByInetAddress(temp);
                byte[] mac=ni.getHardwareAddress();
                for(int i=0;i<mac.length;i++){
                        System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
                }
                System.out.println();
                System.out.println();
        }
}
