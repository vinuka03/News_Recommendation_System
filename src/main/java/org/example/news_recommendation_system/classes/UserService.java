package org.example.news_recommendation_system.classes;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.example.news_recommendation_system.classes.DatabaseHandler;

import java.util.List;

public class UserService {

    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> userHistoryCollection;
    DatabaseHandler db=new DatabaseHandler();


    private MongoDatabase mongoDatabase;

    public UserService() {
        // Initialize the MongoDB connection and collections
        mongoDatabase = db.getDatabase();  // Assuming DatabaseHandler.getDatabase() returns the MongoDatabase instance
        this.userCollection = mongoDatabase.getCollection("User_Details");  // 'User_Details' collection for user data
        this.userHistoryCollection = mongoDatabase.getCollection("User_Preferences");

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


    // Method to update user preferences in the 'User_Preferences' collection
    public boolean updateUserPreferenceScore(String username, String category, int points) {
        try {
            MongoCollection<Document> preferenceCollection = mongoDatabase.getCollection("User_Preferences");

            // Update the specific category score for the user
            preferenceCollection.updateOne(
                    Filters.eq("username", username), // Find the user by username
                    Updates.inc("preferences." + category, points) // Increment the score for the category
            );
            System.out.println("Updated preference score for user: " + username + ", category: " + category + ", points: " + points);
            return true; // Indicate successful update
        } catch (Exception e) {
            System.err.println("Error updating preference score: " + e.getMessage());
            return false; // Indicate failure
        }
    }


    // Method to save article category score to the User_Preference collection when user views the article
    public void saveArticleCategoryToPreferences(String username, String category) {
        if (userHistoryCollection != null && username != null) {
            // Query the User_Preference collection to find the user's preference document
            Document userPreference = userHistoryCollection.find(new Document("username", username)).first();

            if (userPreference != null) {
                // Retrieve the current preferences (embedded document) from the user document
                Document preferences = (Document) userPreference.get("preferences");

                // Check if the category exists in the preferences, if not set it to 0
                int currentScore = preferences.containsKey(category) ? preferences.getInteger(category) : 0;

                // Increment the score for the relevant category
                int newScore = currentScore + 1;

                // Update the User_Preference document with the new score for the category
                userHistoryCollection.updateOne(
                        new Document("username", username),
                        new Document("$set", new Document("preferences." + category, newScore))
                );
            } else {
                throw new RuntimeException("User preferences not found.");
            }
        } else {
            throw new RuntimeException("Invalid inputs for saving preferences.");
        }
    }
}
