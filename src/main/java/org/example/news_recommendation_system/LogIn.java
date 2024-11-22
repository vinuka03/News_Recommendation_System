package org.example.news_recommendation_system;

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
import org.example.news_recommendation_system.BaseController;
import org.example.news_recommendation_system.DatabaseHandler;
import org.example.news_recommendation_system.MainPageController;

import java.io.IOException;


public class LogIn extends BaseController {

    @FXML
    public Button loginOnClick;
    @FXML
    public Button SignupPage;
    public Button adminLgbtn;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPassword;

    @Override
    protected void initializeDatabaseCollections() {
        // Initialize both the User_Details and UserLogin collections
        userDetailsCollection = DatabaseHandler.getCollection("User_Details");
        userLoginDetailsCollection = DatabaseHandler.getCollection("User_Login_Details");  // Ensure the collection is initialized
        if (userDetailsCollection == null || userLoginDetailsCollection == null) {
            System.out.println("Error: Could not initialize user or user login collection.");
        }
    }

    @FXML
    public void initialize() {
        // Ensure the database collection is initialized
        initializeDatabaseCollections();
    }


    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        String username = txtUserName.getText().trim();  // Trim the username to avoid leading/trailing spaces
        String password = txtPassword.getText().trim();  // Trim the password as well

        // Validate credentials using the User_Details collection
        if (checkCredentials(username, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Login", "Welcome " + username);

            // Save the login details to the User_Login_Details collection
            saveLoginDetails(username);  // Saves both admin and user login details in the same collection

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent mainPageRoot = fxmlLoader.load();

            // Pass the username to the MainPageController for further use (e.g., for personalized content)
            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.initializeWithData(username);

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


    @FXML
    private void openSignupPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Signup.fxml"));
        Parent signupRoot = fxmlLoader.load();

        Stage signupStage = new Stage();
        signupStage.setTitle("Signup Page");
        signupStage.setScene(new Scene(signupRoot));
        signupStage.show();

        // Close the current login window
        Stage currentStage = (Stage) SignupPage.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void goToAdminLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
        Parent adminLoginRoot = fxmlLoader.load();

        Stage adminLoginStage = new Stage();
        adminLoginStage.setTitle("Admin Login Page");
        adminLoginStage.setScene(new Scene(adminLoginRoot));
        adminLoginStage.show();

        // Close the current login window
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    // Check if credentials are valid
    @Override
    protected boolean checkCredentials(String username, String password) {
        try {
            if (userDetailsCollection == null) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Database connection is not established.");
                return false;
            }

            // Log the username being checked
            System.out.println("Checking credentials for username: " + username);

            Document user = userDetailsCollection.find(new Document("username", username)
                    .append("password", password)).first();

            if (user != null) {
                System.out.println("User found: " + user.toJson());
                return true;
            } else {
                System.out.println("User not found or password incorrect.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while verifying credentials.");
        }
        return false;
    }

    @Override
    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
