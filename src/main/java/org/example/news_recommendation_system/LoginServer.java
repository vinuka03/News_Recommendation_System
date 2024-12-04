package org.example.news_recommendation_system;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.news_recommendation_system.classes.DatabaseHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginServer {
    private static final int PORT = 12345;
    private final MongoCollection<Document> userDetailsCollection;
    private DatabaseHandler db = new DatabaseHandler();

    // Constructor to initialize the userDetailsCollection using the DatabaseHandler
    public LoginServer() {
        this.userDetailsCollection = db.getCollection("users"); // Replace "users" with your actual collection name
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read credentials from the client
            String username = in.readLine();
            String password = in.readLine();

            // Validate credentials
            boolean isValid = validateCredentials(username, password);

            // Send the result to the client
            out.println(isValid ? "SUCCESS" : "FAILURE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateCredentials(String username, String password) {
        Document user = userDetailsCollection.find(new Document("username", username)
                .append("password", password)).first();
        return user != null;
    }

    public static void main(String[] args) {
        try {
            LoginServer server = new LoginServer();
            server.start(); // Start the server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
