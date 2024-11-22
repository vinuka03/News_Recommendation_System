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
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.news_recommendation_system.article.ArticleCategorizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @FXML
    private TextField headlineTextField;
    @FXML
    private TextField shortDescriptionTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField linkTextField;
    @FXML
    private Button submitArticleButton;


    private MongoCollection<Document> userDetailsCollection;
    private MongoCollection<Document> articlesCollection; // Add this for the articles collection
    private ArticleCategorizer articleCategorizer;

    // Initialize the Admin Page Controller
    @FXML
    public void initialize() {
        setupDatabaseConnection();
        setupTableColumns();
        loadUsers();
        articleCategorizer = new ArticleCategorizer();

    }

    // Setup the MongoDB connection
    private void setupDatabaseConnection() {
        try {
            userDetailsCollection = DatabaseHandler.getCollection("User_Details");
            articlesCollection = DatabaseHandler.getCollection("articles"); // Initialize articles collection

            if (userDetailsCollection == null || articlesCollection == null) {
                throw new IllegalStateException("Required collections do not exist in the database.");
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

    // Handle submitting a new article
    @FXML
    private void handleSubmitArticle() {
        String headline = headlineTextField.getText();
        String shortDescription = shortDescriptionTextField.getText();
        String date = dateTextField.getText();
        String link = linkTextField.getText();

        // Check if any fields are empty
        if (headline.isEmpty() || shortDescription.isEmpty() || date.isEmpty() || link.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill out all fields.");
            return;
        }

        // Prepare article data in a Map
        Map<String, Object> article = new HashMap<>();
        article.put("headline", headline);
        article.put("short_description", shortDescription);
        article.put("date", date);
        article.put("link", link);

        // Categorize the article
        String category = articleCategorizer.categorizeArticle(article);

        // Create a MongoDB document for the article, including the category
        Document articleDoc = new Document("headline", headline)
                .append("short_description", shortDescription)
                .append("date", date)
                .append("link", link)
                .append("category", category);

        // Save the article to MongoDB with its category
        try {
            articlesCollection.insertOne(articleDoc);
            showAlert(Alert.AlertType.INFORMATION, "Article Submitted", "The article has been added successfully.");

            headlineTextField.clear();
            shortDescriptionTextField.clear();
            dateTextField.clear();
            linkTextField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Article Submission Error", "An error occurred while submitting the article.");
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
