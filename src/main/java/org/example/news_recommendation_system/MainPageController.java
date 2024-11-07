package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {

    public Button homeButton;
    public Button recommendedButton;
    public Button categoriesButton;
    public Button profileButton;

    public Button rateButton;
    @FXML
    private StackPane contentStackPane;
    @FXML
    private Button logoutButton;

    @FXML
    private void showHomePane(ActionEvent event) throws IOException {
        loadPane("Home.fxml");
    }

    @FXML
    private void showRecommendedPane(ActionEvent event) throws IOException {
        loadPane("Recommended.fxml");
    }

    @FXML
    private void showCategoriesPane(ActionEvent event) throws IOException {
        loadPane("Categories.fxml");
    }

    @FXML
    private void showProfilePane(ActionEvent event) throws IOException {
        loadPane("Profile.fxml");
    }

    @FXML
    private void showRatePane(ActionEvent event) throws IOException {
        loadPane("Rate.fxml");
    }

    private void loadPane(String fxmlFile) throws IOException {
        // Clear any existing content in the StackPane
        contentStackPane.getChildren().clear();

        // Load the specified FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent pane = fxmlLoader.load();

        // Add the loaded pane to the StackPane
        contentStackPane.getChildren().add(pane);
    }

    @FXML
    private void logOutOnAction(ActionEvent event) throws IOException {
        // Load the login page FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent loginRoot = fxmlLoader.load();

        // Create a new stage for the login page
        Stage loginStage = new Stage();
        loginStage.setTitle("Login Page");
        loginStage.setScene(new Scene(loginRoot));
        loginStage.show();

        // Close the current main window
        Stage currentStage = (Stage) logoutButton.getScene().getWindow();
        currentStage.close();
    }



}

