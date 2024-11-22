package org.example.news_recommendation_system;

import com.mongodb.client.MongoCollection;
import javafx.beans.property.SimpleObjectProperty;
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

import java.io.IOException;
import java.util.List;

public class AdminPageController {

    public Button deleteArticleButton;
    public Button deleteUserButton;
    public Button updateCategoryButton;
    public Button addArticleButton;
    // UI components
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Button> deleteColumn;

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

    private MongoCollection<Document> userDetailsCollection;

    // Initialize the Admin Page Controller
    @FXML
    public void initialize() {
        setupDatabaseConnection();
        setupTableColumns();
        loadUsers();
    }

    // Setup the MongoDB connection
    private void setupDatabaseConnection() {
        try {
            userDetailsCollection = DatabaseHandler.getCollection("User_Details");

            if (userDetailsCollection == null) {
                throw new IllegalStateException("Collection 'User_Details' does not exist in the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    // Setup TableView columns
    private void setupTableColumns() {
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(createDeleteButton(cellData.getValue())));
    }

    // Load users from the database
    private void loadUsers() {
        try {
            List<Document> users = userDetailsCollection.find(new Document("role", "user")).into(new java.util.ArrayList<>());
            users.forEach(userDoc -> {
                String username = userDoc.getString("username");
                String email = userDoc.getString("email");
                userTableView.getItems().add(new User(username, email));
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Data Loading Error", "Unable to fetch users from the database.");
        }
    }

    // Create a delete button for each user
    private Button createDeleteButton(User user) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteUser(user));
        return deleteButton;
    }

    // Delete a user from the database
    private void deleteUser(User user) {
        try {
            Document query = new Document("username", user.getUsername());
            long deletedCount = userDetailsCollection.deleteOne(query).getDeletedCount();

            if (deletedCount > 0) {
                userTableView.getItems().remove(user);
                showAlert(Alert.AlertType.INFORMATION, "User Deleted", "The user has been deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete user from the database.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Delete Error", "An error occurred while deleting the user.");
        }
    }

    // Set visibility for admin panes
    private void showPane(Pane paneToShow) {
        addArticlePane.setVisible(false);
        deleteArticlePane.setVisible(false);
        deleteUserPane.setVisible(false);
        updateCategoryPane.setVisible(false);

        paneToShow.setVisible(true);
    }

    // Show Add Article Pane
    @FXML
    private void showAddArticlePane() {
        showPane(addArticlePane);
    }

    // Show Delete Article Pane
    @FXML
    private void showDeleteArticlePane() {
        showPane(deleteArticlePane);
    }

    // Show Delete User Pane
    @FXML
    private void showDeleteUserPane() {
        showPane(deleteUserPane);
    }

    // Show Update Category Pane
    @FXML
    private void showUpdateCategoryPane() {
        showPane(updateCategoryPane);
    }

    // Log out and navigate to the login page
    @FXML
    private void logOutOnAction(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the login page.");
        }
    }

    // Display alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
