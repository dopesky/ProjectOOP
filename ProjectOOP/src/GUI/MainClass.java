/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Class.DBUtils;
import Network.Network;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class MainClass {

        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) throws Throwable {
                DBUtils db = new DBUtils("jdbc:mysql://localhost/filesharingoop", "root", "biggie5941");
                Network temp = new Network(db);
                System.out.println(temp.getMacAddress("dopesky"));
                System.out.println();
                System.out.println();
                System.out.println(temp.getMacAddress((Inet4Address)Inet4Address.getLocalHost()));
                System.out.println();
                System.out.println();
                System.out.println(temp.getMacAddress((NetworkInterface.getByInetAddress(InetAddress.getByName("10.51.40.228"))).getHardwareAddress()));
                System.out.println();
                System.out.println();
                for(byte Byte:temp.getMacByte("3E-95-FE-48-5C-1B"))
                        System.out.print(Byte);
                System.out.println();
                 for(byte Byte:NetworkInterface.getByInetAddress(InetAddress.getByName("10.51.40.228")).getHardwareAddress())
                        System.out.print(Byte);
                System.out.println();
                System.out.println();
        }
}
