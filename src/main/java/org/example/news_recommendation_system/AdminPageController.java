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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.news_recommendation_system.article.ArticleCategorizer;
import org.example.news_recommendation_system.classes.AdminUser;
import org.example.news_recommendation_system.classes.Article;
import org.example.news_recommendation_system.classes.DatabaseHandler;
import org.example.news_recommendation_system.classes.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminPageController {

    // Buttons for navigation
    public Button deleteArticleButton;
    public Button deleteUserButton;
    public Button updateCategoryButton;
    public Button addArticleButton;



    // UI components for user management
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Button> deleteColumn;

    // UI components for article management
    @FXML
    private TableView<Article> articleTableView;
    @FXML
    private TableColumn<Article, String> headlineColumn;
    @FXML
    private TableColumn<Article, String> descriptionColumn;
    @FXML
    private TableColumn<Article, String>categoryColumn;
    @FXML
    private TableColumn<Article, String> dateColumn;
    @FXML
    private TableColumn<Article, String> linkColumn;
    @FXML
    private TableColumn<Article, Button> deleteArticleColumn;

    // Admin panes
    @FXML
    private Pane homePane;
    @FXML
    private Pane addArticlePane;
    @FXML
    private Pane deleteArticlePane;
    @FXML
    private Pane deleteUserPane;
    @FXML
    private Pane AdminHomePane;
    @FXML
    private ImageView categoryImageView;

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
    private AdminUser adminUser;

    private DatabaseHandler db = new DatabaseHandler();
    // Initialize the Admin Page Controller
    @FXML
    public void initialize() {
        userDetailsCollection = db.getCollection("User_Details");
        articlesCollection = db.getCollection("articles");
        setupUserTableColumns();
        setupArticleTableColumns();
        loadUsers();
        loadArticles();
        setupTextWrapping();
        articleCategorizer = new ArticleCategorizer();
        loadCategoryImage();
        showPane(AdminHomePane);
        adminUser = new AdminUser(userDetailsCollection, articlesCollection);
    }


    @FXML
    private void loadCategoryImage() {
        try {
            // Replace "path/to/image.png" with the actual path to your image
            Image categoryImage = new Image(getClass().getResourceAsStream("/org/example/news_recommendation_system/images/CategoryImage.jpg"));
            categoryImageView.setImage(categoryImage);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Image Load Error", "Unable to load the category image.");
        }
    }


    // Setup TableView columns for users
    private void setupUserTableColumns() {
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(createDeleteButton(cellData.getValue())));
    }

    // Setup TableView columns for articles
    private void setupArticleTableColumns() {
        headlineColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeadline()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getShortDescription()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        linkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLink()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        deleteArticleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(createDeleteArticleButton(cellData.getValue())));
    }

    // Load users from the database
    private void loadUsers() {
        try {
            List<Document> users = userDetailsCollection.find(new Document("role", "user")).into(new java.util.ArrayList<>());
            users.forEach(userDoc -> {
                String username = userDoc.getString("username");
                String email = userDoc.getString("email");
                userTableView.getItems().add(new User(username, email,null,null,null));
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Data Loading Error", "Unable to fetch users from the database.");
        }
    }

    // Load articles from the database
    private void loadArticles() {
        try {
            List<Document> articles = articlesCollection.find().into(new java.util.ArrayList<>());
            articles.forEach(articleDoc -> {
                String headline = articleDoc.getString("headline");
                String description = articleDoc.getString("short_description");
                String date = articleDoc.getString("date");
                String link = articleDoc.getString("link");
                String category = articleDoc.getString("category");
                articleTableView.getItems().add(new Article(headline, description,category, date, link));
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Data Loading Error", "Unable to fetch articles from the database.");
        }
    }

    // Create a delete button for each user
    private Button createDeleteButton(User user) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteUser(user));
        return deleteButton;
    }

    // Create a delete button for each article
    private Button createDeleteArticleButton(Article article) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteArticle(article));
        return deleteButton;
    }

    // Delete a user from the database
    private void deleteUser(User user) {
        if (adminUser.deleteUser(user.getUsername())) {
            userTableView.getItems().remove(user);
            showAlert(Alert.AlertType.INFORMATION, "User Deleted", "The user has been deleted successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete user from the database.");
        }
    }


    // Delete an article from the database
    private void deleteArticle(Article article) {
        if (adminUser.deleteArticle(article.getHeadline())) {
            articleTableView.getItems().remove(article);
            showAlert(Alert.AlertType.INFORMATION, "Article Deleted", "The article has been deleted successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete article from the database.");
        }
    }


    // Set visibility for admin panes
    private void showPane(Pane paneToShow) {
        addArticlePane.setVisible(false);
        deleteArticlePane.setVisible(false);
        deleteUserPane.setVisible(false);
        AdminHomePane.setVisible(false);

        paneToShow.setVisible(true);
    }

    // Enable Text Wrapping for Specific Columns
    private void setupTextWrapping() {
        // Enable text wrapping for the headline column
        headlineColumn.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>() {
                private final Text text = new Text();

                {
                    text.wrappingWidthProperty().bind(headlineColumn.widthProperty());
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
            return cell;
        });

        // Enable text wrapping for the short description column
        descriptionColumn.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>() {
                private final Text text = new Text();

                {
                    text.wrappingWidthProperty().bind(descriptionColumn.widthProperty());
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
            return cell;
        });

        // Enable text wrapping for the link column
        linkColumn.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>() {
                private final Text text = new Text();

                {
                    text.wrappingWidthProperty().bind(linkColumn.widthProperty());
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
            return cell;
        });

        // Enable text wrapping for the category column
        categoryColumn.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>() {
                private final Text text = new Text();

                {
                    text.wrappingWidthProperty().bind(categoryColumn.widthProperty());
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
            return cell;
        });

        // Enable text wrapping for the date column
        dateColumn.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>() {
                private final Text text = new Text();

                {
                    text.wrappingWidthProperty().bind(dateColumn.widthProperty());
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
            return cell;
        });
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
        showPane(AdminHomePane);
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
