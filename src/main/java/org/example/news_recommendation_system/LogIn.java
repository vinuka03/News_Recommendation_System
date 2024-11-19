package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import org.bson.Document;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class LogIn implements Initializable {

    @FXML
    public Button loginOnClick;
    @FXML
    public Button SignupPage;
    public Button adminLgbtn;


    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPassword;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> userDetailsCollection;
    private MongoCollection<Document> userLoginDetailsCollection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            userDetailsCollection = DatabaseHandler.getCollection("User_Details");
            userLoginDetailsCollection = DatabaseHandler.getCollection("User_Login");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    private boolean checkCredentials(String username, String password) {
        try {
            // Find user with matching username and password
            Document user = userDetailsCollection.find(new Document("username", username)
                    .append("password", password)).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
        }
        return false;
    }

    private void saveLoginDetails(String username) {
        try {
            // Insert login record
            Document loginRecord = new Document("Username", username)
                    .append("Login_time", LocalDateTime.now().toString());
            userLoginDetailsCollection.insertOne(loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void openSignupPage(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Signup.fxml"));
            Parent signupRoot = fxmlLoader.load();

            Stage signupStage = new Stage();
            signupStage.setTitle("Signup Page");
            signupStage.setScene(new Scene(signupRoot));

            signupStage.show();

            Stage currentStage = (Stage) SignupPage.getScene().getWindow();
            currentStage.close();

    }



    public void goToAdminLogin(ActionEvent event) throws IOException {
        // Load the Admin Login page FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
        Parent adminLoginRoot = fxmlLoader.load();

        // Create a new stage for the Admin Login page
        Stage adminLoginStage = new Stage();
        adminLoginStage.setTitle("Admin Login Page");
        adminLoginStage.setScene(new Scene(adminLoginRoot));

        // Show the Admin Login page
        adminLoginStage.show();

        // Close the current (User Login) stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
    // Method to open the main page after a successful login

    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        String username = txtUserName.getText();
        String password = txtPassword.getText();

        if (checkCredentials(username, password)) {
            saveLoginDetails(username);
            showAlert(Alert.AlertType.INFORMATION, "Login", "Welcome " + username);

            // Load the main page FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent mainPageRoot = fxmlLoader.load();

            // Get the controller and initialize it with the username
            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.initializeWithData(username);

            // Create and show a new stage for the main page
            Stage mainStage = new Stage();
            mainStage.setTitle("Main Page");
            mainStage.setScene(new Scene(mainPageRoot));
            mainStage.show();

            // Close the current login window
            Stage currentStage = (Stage) loginOnClick.getScene().getWindow();
            currentStage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login", "Incorrect username or password");
        }
    }




}




