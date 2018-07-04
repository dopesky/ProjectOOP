/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

/**
 *
 * @author Kevin
 */
public class MainClass {

        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) throws Throwable {
                Client.ClientClass client = new Client.ClientClass("127.0.0.1", "test/testing.mp3");

        }

}
