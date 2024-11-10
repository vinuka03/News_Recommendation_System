package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController {


    @FXML
    public Button backtouser;
    @FXML
    public TextField txtUserNameAdmin;
    @FXML
    public TextField txtPasswordAdmin;
    public Button adminLginBtn;


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
    private void  goToAdminDashBoard (ActionEvent event) throws IOException {
        // Load the AdminMainPage.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminMainPage.fxml"));
        Parent adminMainPageRoot = fxmlLoader.load();

        // Create a new stage for the Admin Main Page
        Stage adminMainPageStage = new Stage();
        adminMainPageStage.setTitle("Admin Main Page");
        adminMainPageStage.setScene(new Scene(adminMainPageRoot));

        // Show the Admin Main Page stage
        adminMainPageStage.show();

        // Close the current admin login stage
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }





}
