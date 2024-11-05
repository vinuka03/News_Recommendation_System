package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LogIn {

    @FXML
    public Button loginOnClick;
    @FXML
    public Button SignupPage;

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
    // Method to open the main page after a successful login
    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        // Load the main page FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
        Parent mainPageRoot = fxmlLoader.load();

        // Create and show a new stage for the main page
        Stage mainStage = new Stage();
        mainStage.setTitle("Main Page");
        mainStage.setScene(new Scene(mainPageRoot));
        mainStage.show();

        // Close the current login window
        Stage currentStage = (Stage) loginOnClick.getScene().getWindow();
        currentStage.close();
    }


}




