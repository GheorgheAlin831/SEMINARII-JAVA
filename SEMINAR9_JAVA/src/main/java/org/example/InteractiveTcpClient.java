package org.example;

import java.io.*;
import java.net.*;

public class InteractiveTcpClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5001);

        BufferedReader serverIn = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        PrintWriter serverOut = new PrintWriter(
                socket.getOutputStream(), true
        );

        BufferedReader userInput = new BufferedReader(
                new InputStreamReader(System.in)
        );

        String userMessage;

        while (true) {
            System.out.print("You: ");
            userMessage = userInput.readLine();

            if (userMessage.equalsIgnoreCase("exit")) {
                break;
            }

            serverOut.println(userMessage);

            String response = serverIn.readLine();
            System.out.println("Server: " + response);
        }

        socket.close();
    }
}