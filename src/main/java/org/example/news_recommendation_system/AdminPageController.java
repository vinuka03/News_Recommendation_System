package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPageController {

    public Button updateCategoryButton;
    public Button deleteUserButton;
    public Button deleteArticleButton;
    public Button addArticleButton;
    @FXML
    private VBox homePane;
    @FXML
    private VBox addArticlePane;
    @FXML
    private VBox deleteArticlePane;
    @FXML
    private VBox deleteUserPane;
    @FXML
    private VBox updateCategoryPane;
    @FXML
    private StackPane adminContentStackPane;

    @FXML
    private Button logoutButton;


    // Initialize method (optional, useful if you want to set default pane visibility)


    @FXML
    private void showHomePane() {
        setPaneVisibility(homePane);
    }

    @FXML
    private void showAddArticlePane() {
        setPaneVisibility(addArticlePane);
    }

    @FXML
    private void showDeleteArticlePane() {
        setPaneVisibility(deleteArticlePane);
    }

    @FXML
    private void showDeleteUserPane() {
        setPaneVisibility(deleteUserPane);
    }

    @FXML
    private void showUpdateCategoryPane() {
        setPaneVisibility(updateCategoryPane);
    }

    // Helper method to show only one pane at a time

    private void setPaneVisibility(VBox paneToShow) {
        addArticlePane.setVisible(paneToShow == addArticlePane);
        deleteArticlePane.setVisible(paneToShow == deleteArticlePane);
        deleteUserPane.setVisible(paneToShow == deleteUserPane);
        updateCategoryPane.setVisible(paneToShow == updateCategoryPane);
    }


    @FXML
    private void logOutOnAction(ActionEvent event) throws IOException {
        // Load the login page FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent loginRoot = loader.load();

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene with the login page
        stage.setScene(new Scene(loginRoot));
        stage.show();
    }
}
