package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPageController {

    @FXML
    private Button updateCategoryButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button deleteArticleButton;
    @FXML
    private Button addArticleButton;

    @FXML
    private Pane homePane;
    @FXML
    private Pane addArticlePane;
    @FXML
    private Pane deleteArticlePane;
    @FXML
    private Pane deleteUserPane;
    @FXML
    private Pane updateCategoryPane;
    @FXML
    private StackPane adminContentStackPane;

    @FXML
    private Button logoutButton;

    // Initialize method (optional, useful if you want to set default pane visibility)
    @FXML
    public void initialize() {
        // Set the homePane as the default visible pane
        setPaneVisibility(addArticlePane);
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
    private void setPaneVisibility(Pane paneToShow) {
        // Set all panes to invisible

        addArticlePane.setVisible(false);
        deleteArticlePane.setVisible(false);
        deleteUserPane.setVisible(false);
        updateCategoryPane.setVisible(false);

        // Set the chosen pane to visible
        paneToShow.setVisible(true);
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
