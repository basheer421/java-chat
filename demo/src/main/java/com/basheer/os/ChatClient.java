package com.basheer.os;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Scanner scanner = null;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Send client name to the server
            scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String clientName = scanner.nextLine();
            out.println(clientName);

            // Start a new thread to read messages from the server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading messages: " + e.getMessage());
                }
            }).start();

            // Read messages from the user and send them to the server
            String userInput;
            while (scanner.hasNextLine()) {
                userInput = scanner.nextLine();
                out.println(userInput);
            }
        } catch (IOException ex) {
            System.out.println("Client exception: " + ex.getMessage());
        } finally {
            if (scanner != null)
                scanner.close();
            System.out.println("Closing the connection");
        }
    }
}
