package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {

    public StackPane contentStackPane;
    // Sidebar Buttons
    @FXML
    private Button homeButton;
    @FXML
    private Button recommendedButton;
    @FXML
    private Button categoriesButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button rateButton;
    @FXML
    private Button logoutButton;

    // Content Panes
    @FXML
    private Pane homePane;
    @FXML
    private Pane recommendedPane;
    @FXML
    private Pane categoriesPane;
    @FXML
    private Pane profilePane;
    @FXML
    private Pane ratePane;

    // Method to display only the selected pane
    private void showPane(Pane pane) {
        // Hide all panes initially
        homePane.setVisible(false);
        recommendedPane.setVisible(false);
        categoriesPane.setVisible(false);
        profilePane.setVisible(false);
        ratePane.setVisible(false);

        // Show the selected pane
        pane.setVisible(true);
    }

    @FXML
    public void initialize() {
        showPane(homePane); // Display home pane by default
    }

    @FXML
    private void showHomePane(ActionEvent event) {
        showPane(homePane);
    }

    @FXML
    private void showRecommendedPane(ActionEvent event) {
        showPane(recommendedPane);
    }

    @FXML
    private void showCategoriesPane(ActionEvent event) {
        showPane(categoriesPane);
    }

    @FXML
    private void showProfilePane(ActionEvent event) {
        showPane(profilePane);
    }

    @FXML
    private void showRatePane(ActionEvent event) {
        showPane(ratePane);
    }

    @FXML
    private void logOutOnAction(ActionEvent event) throws IOException {
        // Load the login page FXML and switch scenes to log out
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
