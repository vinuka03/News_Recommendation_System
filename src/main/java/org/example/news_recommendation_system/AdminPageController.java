package org.example.news_recommendation_system;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.bson.Document;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;

import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.util.List;

public class AdminPageController {

    // TableView for displaying users
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Button> deleteColumn;

    // Pane elements
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

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> userDetailsCollection;

    // Initialize method
    @FXML
    public void initialize() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("News_Recommendation");
            userDetailsCollection = database.getCollection("User_Details");

            // Setup Table columns
            usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
            emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

            // Set delete button on each row
            deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(createDeleteButton(cellData.getValue())));

            // Load users from the database
            loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    // Load all users (excluding admins) from the database into the TableView
    private void loadUsers() {
        List<Document> users = userDetailsCollection.find(new Document("role", "user")).into(new java.util.ArrayList<>());
        for (Document userDoc : users) {
            String username = userDoc.getString("username");
            String email = userDoc.getString("email");

            // Add user to TableView
            userTableView.getItems().add(new User(username, email));
        }
    }

    // Create a delete button for each user
    private Button createDeleteButton(User user) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteUser(user));
        return deleteButton;
    }

    // Delete user from the system and update the database
    private void deleteUser(User user) {
        // Construct a query document to find the user by username
        Document query = new Document("username", user.getUsername()); // Adjust field name as needed

        // Attempt to delete the user from the database
        long deletedCount = userDetailsCollection.deleteOne(query).getDeletedCount();

        // Check if a record was deleted
        if (deletedCount > 0) {
            // Remove the user from the TableView if deletion was successful
            userTableView.getItems().remove(user);
            showAlert(Alert.AlertType.INFORMATION, "User Deleted", "The user has been deleted successfully.");
        } else {
            // If no record was deleted, show an error alert
            showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete user from the database.");
        }
    }

    // Set visibility for different admin panes
    private void setPaneVisibility(Pane paneToShow) {
        // Set all panes to invisible
        addArticlePane.setVisible(false);
        deleteArticlePane.setVisible(false);
        deleteUserPane.setVisible(false);
        updateCategoryPane.setVisible(false);

        // Set the chosen pane to visible
        paneToShow.setVisible(true);
    }

    // Show the Add Article Pane
    @FXML
    private void showAddArticlePane() {
        setPaneVisibility(addArticlePane);
    }

    // Show the Delete Article Pane
    @FXML
    private void showDeleteArticlePane() {
        setPaneVisibility(deleteArticlePane);
    }

    // Show the Delete User Pane
    @FXML
    private void showDeleteUserPane() {
        setPaneVisibility(deleteUserPane);
    }

    // Show the Update Category Pane
    @FXML
    private void showUpdateCategoryPane() {
        setPaneVisibility(updateCategoryPane);
    }

    // Log out and go back to the login page
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

    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
