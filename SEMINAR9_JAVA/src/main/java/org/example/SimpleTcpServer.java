package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTcpServer {
    public static void main(String[] args) throws IOException {
        int port = 5001;

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept(); // waits for a client
            System.out.println("Client connected");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true
            );

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client says: " + message);
                out.println("Server received: " + message);
            }

            clientSocket.close();
        }
    }
}