/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Class.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Kevin
 */
public class Network {

        public Process process;

        private String broadcastAddress;

        private final DBUtils database;

        public Network(DBUtils database) {
                this.database = database;
        }

        private void refresh() throws IOException {
                ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd.exe", "/c", "ping " + broadcastAddress});
                builder.redirectErrorStream(true);
                process = builder.start();
                System.out.println("Refreshing..");
                Scanner in = new Scanner(process.getInputStream());
                while (in.hasNextLine()) {
                        System.out.println(in.nextLine());
                }
        }

        public String getMacAddress(String userName) throws SQLException {
                database.prepareStatement("Select MacAddress from users where username=?");
                database.setObjects(new Object[]{userName});
                ResultSet set = database.executeQueryPst();
                if (set.next()) {
                        return set.getObject(1).toString();
                } else {
                        throw new SQLException("The username is not in the database.");
                }
        }

        public String getMacAddress(Inet4Address address) throws SocketException {
                NetworkInterface ni = NetworkInterface.getByInetAddress(address);
                if (ni != null) {
                        byte[] macAddress = ni.getHardwareAddress();
                        if (macAddress == null) {
                                throw new NullPointerException("Unable to get Mac Address of the ip address specified.");
                        }
                        return getMacAddress(macAddress);
                } else {
                        throw new NullPointerException("No Network Interface is linked to the specified Inet4Address.");
                }
        }

        public String getMacAddress(byte[] macAddress) {
                String mac = "";
                for (int i = 0; i < macAddress.length; i++) {
                        mac += String.format("%02X%s", macAddress[i], (i < macAddress.length - 1) ? "-" : "");
                }
                return mac;
        }

        public byte[] getMacByte(String macAddress) {
                String[] splitMac = macAddress.split("-");
                byte[] macByte = new byte[splitMac.length];
                for (int i = 0; i < splitMac.length; i++) {
                        BigInteger converter = new BigInteger(splitMac[i], 16);
                        byte[] raw = converter.toByteArray();
                        macByte[i] = raw[raw.length - 1];
                }
                return macByte;
        }
}
