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
    private DatabaseHandler db = new DatabaseHandler();
    private MongoDatabase mongoDatabase;

    public UserService() {
        mongoDatabase = db.getDatabase();
        this.userCollection = mongoDatabase.getCollection("User_Details");  // Collection for user data
        this.userHistoryCollection = mongoDatabase.getCollection("User_Preferences"); // Collection for user preferences
    }

    // Load user profile by username, returning a User object
    public User loadUserProfile(String username) {
        Document userDocument = userCollection.find(new Document("username", username)).first();
        if (userDocument != null) {
            String userName = userDocument.getString("username");
            String email = userDocument.getString("email");
            String firstName = userDocument.getString("firstName");
            String lastName = userDocument.getString("lastName");
            String password = userDocument.getString("password");

            // Create and return a User object with the retrieved data
            User user = new User(userName, email, firstName, lastName, password);
            return user;
        }
        return null; // Return null if the user is not found
    }

    // Update user details in the database using the User class's getters and setters
    public String updateUserDetails(User updatedUser) {
        // Get the current username from the User object
        String currentUsername = updatedUser.getUsername();

        // Check if the email exists for another user
        Document existingEmailUser = userCollection.find(new Document("email", updatedUser.getEmail())).first();
        if (existingEmailUser != null && !existingEmailUser.getString("username").equals(currentUsername)) {
            return "The email is already taken by another user.";
        }

        // Check if the username exists
        Document existingUser = userCollection.find(new Document("username", updatedUser.getUsername())).first();
        if (existingUser != null && !updatedUser.getUsername().equals(currentUsername)) {
            return "The new username is already taken.";
        }

        // Update user details in MongoDB using the User object
        Document updatedDetails = new Document("username", updatedUser.getUsername())
                .append("email", updatedUser.getEmail())
                .append("firstName", updatedUser.getFirstName())
                .append("lastName", updatedUser.getLastName())
                .append("password", updatedUser.getPassword());

        userCollection.updateOne(new Document("username", currentUsername), new Document("$set", updatedDetails));
        return null; // Return null if no errors
    }


    // Validate email format using a simple regex
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    // Validate password strength (at least 8 characters in this example)
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

    // Create a new user in the MongoDB collection using the User class
    public boolean createUser(User newUser) {
        try {
            Document userDocument = new Document("username", newUser.getUsername())
                    .append("email", newUser.getEmail())
                    .append("firstName", newUser.getFirstName())
                    .append("lastName", newUser.getLastName())
                    .append("password", newUser.getPassword())
                    .append("role", "user");
            userCollection.insertOne(userDocument);
            return true;  // Return true if the user is successfully created
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if an error occurs during insertion
        }
    }

    // Save user preferences to the 'User_Preferences' collection
    public boolean saveUserPreferences(Document userPreferences) {
        try {
            MongoCollection<Document> preferenceCollection = mongoDatabase.getCollection("User_Preferences");
            preferenceCollection.insertOne(userPreferences);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving user preferences: " + e.getMessage());
            return false;
        }
    }

    // Update user preference score
    public boolean updateUserPreferenceScore(String username, String category, int points) {
        try {
            MongoCollection<Document> preferenceCollection = mongoDatabase.getCollection("User_Preferences");
            preferenceCollection.updateOne(
                    Filters.eq("username", username),
                    Updates.inc("preferences." + category, points)
            );
            System.out.println("Updated preference score for user: " + username + ", category: " + category + ", points: " + points);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating preference score: " + e.getMessage());
            return false;
        }
    }

    // Save article category score to the User_Preference collection when user views the article
    public void saveArticleCategoryToPreferences(String username, String category) {
        if (userHistoryCollection != null && username != null) {
            Document userPreference = userHistoryCollection.find(new Document("username", username)).first();
            if (userPreference != null) {
                Document preferences = (Document) userPreference.get("preferences");
                int currentScore = preferences.containsKey(category) ? preferences.getInteger(category) : 0;
                int newScore = currentScore + 1;
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
