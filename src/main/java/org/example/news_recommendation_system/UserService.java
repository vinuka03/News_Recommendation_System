package org.example.news_recommendation_system;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class UserService {

    private MongoCollection<Document> userCollection;
    private MongoDatabase mongoDatabase;

    public UserService() {
        // Initialize the MongoDB connection and collections
        mongoDatabase = DatabaseHandler.getDatabase();  // Assuming DatabaseHandler.getDatabase() returns the MongoDatabase instance
        this.userCollection = mongoDatabase.getCollection("User_Details");  // 'User_Details' collection for user data
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

    // Method to save user preferences to the 'User_Preferences' collection (updated collection name)
    public boolean saveUserPreferences(Document userPreferences) {
        try {
            // Get the MongoDB collection for user preferences (corrected collection name)
            MongoCollection<Document> preferenceCollection = mongoDatabase.getCollection("User_Preferences");

            // Insert the preferences document
            preferenceCollection.insertOne(userPreferences);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving user preferences: " + e.getMessage());
            return false;
        }
    }
}
