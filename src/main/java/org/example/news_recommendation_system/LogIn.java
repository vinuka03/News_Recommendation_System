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



    @FXML
    public void initialize() {
        initializeDatabaseCollections();
    }

    // Override the checkCredentials method to perform user-specific logic
    @Override
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



    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        String username = txtUserName.getText().trim();
        String password = txtPassword.getText().trim();

        if (checkCredentials(username, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Login", "Welcome " + username);
            saveLoginDetails(username); // Save login details using BaseController method

            // Use BaseController's loadScene method to load the Main Page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent root = fxmlLoader.load();

            // Initialize MainPageController with the logged-in user's data
            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.initializeWithData(username);

            // Call the BaseController's loadScene method to switch scenes
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Page");
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login", "Incorrect username or password.");
        }
    }



    @FXML
    private void openSignupPage(ActionEvent event) throws IOException {
        loadScene("Signup.fxml", "Signup Page", event);
    }

    @FXML
    public void goToAdminLogin(ActionEvent event) throws IOException {
        loadScene("AdminLogin.fxml", "Admin Login Page", event);
    }
}
