package com.basheer.os;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import javafx.application.Platform;

public class App extends Application {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private TextArea chatArea;
    private TextField messageField;
    private PrintWriter out;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Client");

        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageField = new TextField();
        messageField.setPromptText("Enter your message here");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        VBox vbox = new VBox(chatArea, messageField, sendButton);
        Scene scene = new Scene(vbox, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();

        connectToServer();
    }

    private void connectToServer() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send client name to the server
            out.println("JavaFXClient");

            // Start a new thread to read messages from the server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        String finalMessage = message;
                        Platform.runLater(() -> {
                            chatArea.appendText(finalMessage + "\n");
                        });
                    }
                } catch (IOException ex) {
                    System.out.println("Error reading messages: " + ex.getMessage());
                }
            }).start();
        } catch (IOException ex) {
            System.out.println("Client exception: " + ex.getMessage());
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            messageField.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
