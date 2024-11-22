package org.example.news_recommendation_system;

import javafx.scene.control.Alert;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.time.LocalDateTime;

public abstract class BaseController {

    protected MongoCollection<Document> userDetailsCollection;
    protected MongoCollection<Document> userLoginDetailsCollection;

    // Abstract method to enforce specific implementations in child classes
    protected abstract void initializeDatabaseCollections();

    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected boolean checkCredentials(String username, String password) {
        try {
            Document user = userDetailsCollection.find(new Document("username", username)
                    .append("password", password)).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
            return false;
        }
    }

    // Updated saveLoginDetails method without User_type field
    protected void saveLoginDetails(String username) {
        try {
            // Save the login details to the "User_Login_Details" collection
            Document loginRecord = new Document("Username", username)
                    .append("Login_time", LocalDateTime.now().toString());

            // Save to the same collection "User_Login_Details"
            userLoginDetailsCollection.insertOne(loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

}
