package com.basheer.os;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat Server is listening on address " + serverSocket.getInetAddress().getHostAddress() + " and port " + PORT);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected");

                    ClientHandler clientHandler = new ClientHandler(socket);
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler).start();
                } catch (IOException ex) {
                    System.out.println("Error accepting client connection: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(message);
            }
        }
    }

    public static void removeClientHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            clientName = in.readLine();
            System.out.println(clientName + " has joined the chat");
        } catch (IOException e) {
            closeConnections();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                ChatServer.broadcastMessage(clientName + ": " + message, this);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            closeConnections();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeConnections() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing connections: " + e.getMessage());
        }
        ChatServer.removeClientHandler(this);
    }
}
