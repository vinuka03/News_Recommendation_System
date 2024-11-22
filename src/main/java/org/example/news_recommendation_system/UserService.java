package org.example.news_recommendation_system;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;

public class UserService {

    private MongoCollection<Document> userCollection;

    public UserService() {
        // Initialize the MongoDB collection
        this.userCollection = DatabaseHandler.getCollection("User_Details");  // Assuming 'User_Details' is your MongoDB collection
    }

    // Method to validate email format using a simple regex (you can refine this if needed)
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    // Method to validate password strength (at least 8 characters in this example)
    public boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }

    // Check if a user with the given username or email already exists
    public boolean isUserExists(String username, String email) {
        Document existingUser = userCollection.find(new Document("$or",
                List.of(
                        new Document("username", username),
                        new Document("email", email)
                ))).first();
        return existingUser != null;
    }


    // Method to create a new user in the MongoDB collection
    public boolean createUser(Document newUser) {
        try {
            // Directly insert the plain password (no hashing)
            userCollection.insertOne(newUser);
            return true;  // Return true if the user is successfully created
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if an error occurs during insertion
        }
    }

}
