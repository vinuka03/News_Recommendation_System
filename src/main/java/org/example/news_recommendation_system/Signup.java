package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Signup {
    @FXML
    public Button signup;
    @FXML
    public Button goBackLogin;

    @FXML
    private void goBackToLogIN(ActionEvent event) throws IOException {
        // Load the LogIn.fxml file (assuming it is the login page)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
        Parent loginRoot = fxmlLoader.load();

        // Create a new stage for the login page
        Stage loginStage = new Stage();
        loginStage.setTitle("Login Page");
        loginStage.setScene(new Scene(loginRoot));

        // Show the login stage
        loginStage.show();

        // Close the current signup stage
        Stage currentStage = (Stage) goBackLogin.getScene().getWindow();
        currentStage.close();
    }
}
