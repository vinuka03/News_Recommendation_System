package org.example.news_recommendation_system;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class AdminLoginController {


    @FXML
    public Button backtouser;
    @FXML
    public TextField txtUserNameAdmin;
    @FXML
    public TextField txtPasswordAdmin;
    public Button adminLginBtn;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> userDetailsCollection;

    public void initialize() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("News_Recommendation");
            userDetailsCollection = database.getCollection("User_Details");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }



    @FXML
    public void goBackToUserLogin(ActionEvent event) throws IOException {
        // Load the User Login page FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent userLoginRoot = fxmlLoader.load();

        // Create a new stage for the User Login page
        Stage userLoginStage = new Stage();
        userLoginStage.setTitle("User Login Page");
        userLoginStage.setScene(new Scene(userLoginRoot));

        // Show the User Login page
        userLoginStage.show();

        // Close the current (Admin Login) stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void goToAdminDashBoard(ActionEvent event) throws IOException {
        String username = txtUserNameAdmin.getText();
        String password = txtPasswordAdmin.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Username or Password cannot be empty.");
            return;
        }

        // Check if the username and password match an admin in the database
        Document admin = userDetailsCollection.find(new Document("userName", username)
                .append("password", password)
                .append("role", "admin")).first();

        if (admin != null) {
            showAlert(Alert.AlertType.INFORMATION, "Welcome", "Welcome Admin!");
            // Admin found, proceed to Admin Main Page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminMainPage.fxml"));
            Parent adminMainPageRoot = fxmlLoader.load();

            Stage adminMainPageStage = new Stage();
            adminMainPageStage.setTitle("Admin Main Page");
            adminMainPageStage.setScene(new Scene(adminMainPageRoot));

            adminMainPageStage.show();

            // Close the current admin login stage
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            // Invalid admin credentials
            showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password, or you are not an admin.");
        }
    }

    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}