package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.bson.Document;

import java.io.IOException;

public class AdminLogin extends BaseController {

    @FXML
    private Button adminLoginBtn;
    @FXML
    private Button backtouser;
    @FXML
    private TextField txtUserNameAdmin;
    @FXML
    private TextField txtPasswordAdmin;



    @FXML
    public void initialize() {
        initializeDatabaseCollections();
    }



    public boolean checkCredentials(String username, String password) {
        try {
            Document query = new Document("username", username)
                    .append("password", password)
                    .append("role", "admin");

            Document user = userDetailsCollection.find(query).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while verifying credentials.");
            return false;
        }
    }

    @FXML
    public void goToAdminDashBoard(ActionEvent event) throws IOException {
        String username = txtUserNameAdmin.getText();
        String password = txtPasswordAdmin.getText();

        if (!validateLoginFields(username, password)) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Username or Password cannot be empty.");
            return;
        }

        if (checkCredentials(username, password)) {
            saveLoginDetails(username);
            showAlert(Alert.AlertType.INFORMATION, "Welcome", "Welcome Admin!");
            loadScene("AdminMainPage.fxml", "Admin Main Page", event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password, or you are not an admin.");
        }
    }

    @FXML
    public void goBackToUserLogin(ActionEvent event) throws IOException {
        loadScene("Login.fxml", "User Login Page", event);
    }


}