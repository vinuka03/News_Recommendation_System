package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import com.mongodb.client.MongoCollection;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.news_recommendation_system.classes.DatabaseHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public abstract class BaseController {

    protected MongoCollection<Document> userDetailsCollection;
    protected MongoCollection<Document> userLoginDetailsCollection;
    private DatabaseHandler db = new DatabaseHandler();

    // Abstract method to enforce specific implementations in child classes
    protected void initializeDatabaseCollections() {
        userDetailsCollection = db.getCollection("User_Details");
        userLoginDetailsCollection = db.getCollection("User_Login_Details");
    }

    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Abstract method for checking credentials, to be implemented by subclasses
    protected abstract boolean checkCredentials(String username, String password);


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

    protected void loadScene(String fxmlFile, String title, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();

        // Close the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    protected boolean validateLoginFields(String username, String password) {
        return !(username.isEmpty() || password.isEmpty());
    }




}
