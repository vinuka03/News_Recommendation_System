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

import java.io.IOException;

public class AdminLogin extends BaseController {

    public Button adminLginBtn;
    @FXML
    private Button backtouser;
    @FXML
    private TextField txtUserNameAdmin;
    @FXML
    private TextField txtPasswordAdmin;
    @FXML
    private Button adminLoginBtn;

    @Override
    protected void initializeDatabaseCollections() {
        try {
            // Initialize MongoDB collection for user details and login details
            userDetailsCollection = DatabaseHandler.getCollection("User_Details");
            userLoginDetailsCollection = DatabaseHandler.getCollection("User_Login_Details");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }
    @FXML
    public void initialize() {
        // Ensure the database collection is initialized
        initializeDatabaseCollections();
    }

    @FXML
    public void goBackToUserLogin(ActionEvent event) throws IOException {
        // Load the User Login page FXML
        loadScene("Login.fxml", "User Login Page", event);
    }



    // Validate login fields
    private boolean validateLoginFields(String username, String password) {
        return !(username.isEmpty() || password.isEmpty());
    }

    // Check if the user is an admin
    private boolean checkAdminCredentials(String username, String password) {
        Document admin = userDetailsCollection.find(new Document("username", username)
                .append("password", password)
                .append("role", "admin")).first();
        return admin != null;
    }

    // Load a scene for the given FXML file and display it
    private void loadScene(String fxmlFile, String title, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();

        // Close the current stage (admin login page)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void goToAdminDashBoard(ActionEvent event) throws IOException {
        String username = txtUserNameAdmin.getText();
        String password = txtPasswordAdmin.getText();

        // Validate the inputs before checking credentials
        if (validateLoginFields(username, password)) {
            if (checkAdminCredentials(username, password)) {
                // Save login details for the admin
                saveLoginDetails(username);

                showAlert(Alert.AlertType.INFORMATION, "Welcome", "Welcome Admin!");

                // Load Admin Dashboard page after successful login
                loadScene("AdminMainPage.fxml", "Admin Main Page", event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password, or you are not an admin.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Username or Password cannot be empty.");
        }
    }
}
