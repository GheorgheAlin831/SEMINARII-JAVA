package org.example;

import java.io.*;
import java.net.*;

public class SimpleTcpClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5001);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        out.println("Hello Georgel");

        String response = in.readLine();
        System.out.println(response);

        socket.close();
    }
}