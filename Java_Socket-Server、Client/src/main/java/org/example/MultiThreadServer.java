package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//  Multithreaded Servers in Java
//  https://www.geeksforgeeks.org/multithreaded-servers-in-java/
public class MultiThreadServer {

    public static void main(String[] args) throws IOException{
        ServerSocket server = null;

        try {

            //  建立ServerSocket物件，並設定埠號5000
            server = new ServerSocket(5000);
            System.out.println("等待client");
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                //  收到Client請求 如果Socket匹配 兩Socket可互相通信
                Socket client = server.accept();
                System.out.println("已連線Client");
                // Displaying that new client is connected to server
                System.out.println("New client connected "
                        + client.getInetAddress()
                        .getHostAddress());
                System.out.println("--------------------------------Server--------------------------------");

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

                // This thread will handle the client separately
                new Thread(clientSock).start();

            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
