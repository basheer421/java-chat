package com.basheer.os;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
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
    private BufferedReader in;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Client");

        chatArea = new TextArea();
        chatArea.getStyleClass().add("text-area");
        chatArea.setEditable(false);

        messageField = new TextField();
        messageField.getStyleClass().add("text-field");
        messageField.setPromptText("Enter your message here");
        messageField.setOnAction(e -> sendMessage());

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, messageField, sendButton);
        HBox.setHgrow(messageField, Priority.ALWAYS);
        HBox.setHgrow(sendButton, Priority.ALWAYS);
        VBox vbox = new VBox(10, chatArea, inputBox);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/com/basheer/os/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send client name to the server
            out.println("JavaFXClient");

            // Start a new thread to read messages from the server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        String finalMessage = message;
                        Platform.runLater(() -> chatArea.appendText(finalMessage + "\n"));
                    }
                } catch (IOException ex) {
                    System.out.println("Error reading messages: " + ex.getMessage());
                } finally {
                    closeConnections();
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
            chatArea.appendText("You: " + message + "\n");
            messageField.clear();
        }
    }

    private Socket socket;

    private void closeConnections() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing connections: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
