package org.example.news_recommendation_system;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.news_recommendation_system.classes.*;
import org.example.news_recommendation_system.classes.UserService;


import java.io.IOException;


public class MainPageController {

    @FXML
    private TableView recommendedArticlesTable;
    @FXML
    private TableColumn<Article, String> recommendedHeadlineColumn;
    @FXML
    private TableColumn<Article, String> recommendedShortDescriptionColumn;
    @FXML
    private TableColumn<Article, String> recommendedDateColumn;
    @FXML
    private TableColumn<Article, String> recommendedCategoryColumn;
    @FXML
    private TableColumn<Article, String> recommendedLinkColumn;

    @FXML private VBox articlesVBox;
    @FXML private Button refreshButton;
    
    // StackPane and profile fields
    @FXML
    private StackPane contentStackPane;
    @FXML
    private ImageView profileImageView;
    @FXML
    private TableColumn<Article, String> headlineColumn;
    @FXML
    private TableColumn<Article, String> shortDescriptionColumn;
    @FXML
    private TableColumn<Article, String> dateColumn;
    @FXML
    private TableColumn<Article, String> categoryColumn;
    @FXML
    private TableColumn linkColumn;
    @FXML
    private TableView<Article> articlesTable;

    @FXML private TextField profileUsername;
    @FXML private TextField profileEmail;
    @FXML private TextField profileFirstName;
    @FXML private TextField profileLastName;
    @FXML private TextField profilePassword;
    @FXML private Button updateProfileButton;



    // Sidebar Buttons
    @FXML private Button homeButton;
    @FXML private Button recommendedButton;
    @FXML private Button ViewButton;
    @FXML private Button profileButton;
    @FXML private Button rateButton;
    @FXML private Button logoutButton;

    // Content Panes
    @FXML private Pane homePane;
    @FXML private Pane recommendedPane;
    @FXML private Pane viewPane;
    @FXML private Pane profilePane;

    private String loggedInUsername;


    // MongoDB collections and current user data
    private MongoCollection<Document> userDetailsCollection;
    private MongoCollection<Document> articlesCollection;

    private MongoCollection<Document> userHistoryCollection;
    private RecommendEngine recommendEngine;
    private UserService userService;



    private String currentUsername;
private DatabaseHandler db = new DatabaseHandler();
    // Method to initialize the controller with MongoDB and user data
    public void initializeWithData(String username) {
        this.userDetailsCollection = db.getCollection("User_Details");
        this.articlesCollection = db.getCollection("NewsArticles");
        this.articlesCollection = db.getCollection("articles");
        this.userHistoryCollection = db.getCollection("User_Preferences");

        setupTextWrapping();
        this.currentUsername = username;
        this.recommendEngine = new RecommendEngine(db);
        userService = new UserService();


    }







    @FXML
    public void initialize() {
        showPane(homePane); // Display the home pane by default
        initializeTableColumns();// Initialize the table columns
        addTableRowClickListener();  // Add the listener for row clicks
        initializeTableColumns();



    }



// Method to open article in a new scene
private void openArticleDetailsWindow(Article article) {
    try {
        // Load the ArticleDetails.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ArticleDetails.fxml"));
        Parent articleDetailsRoot = fxmlLoader.load();

        // Get the ArticleDetailsController instance from the FXMLLo
        ArticleDetailsController articleDetailsController = fxmlLoader.getController();

        // Pass the logged-in username to the ArticleDetailsController
        articleDetailsController.setUsername(this.currentUsername);  // 'this.currentUsername' holds the logged-in user's username

        // Initialize the article details in the controller
        articleDetailsController.initializeWithArticle(article);

        // Create a new Stage for the article details window
        Stage articleDetailsStage = new Stage();
        articleDetailsStage.setTitle("Article Details");
        articleDetailsStage.setScene(new Scene(articleDetailsRoot));
        articleDetailsStage.show();  // Show the article detail window


        // Optionally, save article view to preferences (or history)
        userService.saveArticleCategoryToPreferences(currentUsername, article.getCategory());  // This method updates the user preferences based on the article viewed
    } catch (IOException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", "Failed to open article details.");
    }
}



        // Add event listener to handle row click
        private void addTableRowClickListener() {
            // Handle clicks on both tables
            for (TableView<?> table : new TableView<?>[]{articlesTable, recommendedArticlesTable}) {
                table.setOnMouseClicked((MouseEvent event) -> {
                    if (event.getClickCount() == 1) {
                        Article selectedArticle = (Article) table.getSelectionModel().getSelectedItem();
                        if (selectedArticle != null) {
                            openArticleDetailsWindow(selectedArticle);
                        }
                    }
                });
            }
        }






    // Customize Table Row Height
    private void customizeTableRowHeight() {
        articlesTable.setRowFactory(tv -> {
            TableRow<Article> row = new TableRow<>() {
                @Override
                protected void updateItem(Article item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        this.setPrefHeight(60); // Set preferred row height
                    }
                }
            };
            return row;
        });
    }


    // Enable Text Wrapping for Specific Columns
    private void setupTextWrapping() {
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
    }


    // Method to initialize the TableView columns
    private void initializeTableColumns() {
        headlineColumn.setCellValueFactory(new PropertyValueFactory<>("headline"));
        shortDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("shortDescription"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        linkColumn.setCellValueFactory(new PropertyValueFactory<>("link"));
        recommendedHeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("headline"));
        recommendedShortDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("shortDescription"));
        recommendedDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        recommendedCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        recommendedLinkColumn.setCellValueFactory(new PropertyValueFactory<>("link"));



    }

    // Method to display only the selected pane
    private void showPane(Pane pane) {
        homePane.setVisible(false);
        recommendedPane.setVisible(false);
        viewPane.setVisible(false);
        profilePane.setVisible(false);


        pane.setVisible(true);
        if (pane == profilePane) {
            loadUserProfile();  // Load profile details when showing the Profile pane
        }
        if (pane == viewPane) {
            ObservableList<Article> articlesList = Article.loadArticlesFromDatabase(articlesCollection);
            articlesTable.setItems(articlesList);
            customizeTableRowHeight(); // Customize row height
            setupTextWrapping();
        }
    }
    public void showRecommendedPane() {
        recommendedPane.setVisible(true);
        // Fetch recommended articles using the method from RecommendEngine
        ObservableList<Article> recommendedArticles = recommendEngine.fetchRecommendedArticles(currentUsername);
        recommendedArticlesTable.setItems(recommendedArticles);
    }


    // Load user details into profile fields
    private void loadUserProfile() {
        if (userService != null && currentUsername != null) {
            User user = userService.loadUserProfile(currentUsername);
            if (user != null) {
                profileUsername.setText(user.getUsername());
                profileEmail.setText(user.getEmail());
                profileFirstName.setText(user.getFirstName());
                profileLastName.setText(user.getLastName());
                profilePassword.setText(user.getPassword());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "User data not found.");
            }
        }
    }


    @FXML
    private void updateProfile(ActionEvent event) {
        String newUsername = profileUsername.getText();
        String newEmail = profileEmail.getText();
        String newFirstName = profileFirstName.getText();
        String newLastName = profileLastName.getText();
        String newPassword = profilePassword.getText();

        // Validate inputs
        if (newEmail == null || newEmail.isEmpty() || newFirstName == null ||
                newFirstName.isEmpty() || newLastName == null || newLastName.isEmpty() ||
                newPassword == null || newPassword.isEmpty() || newPassword.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill all fields correctly.");
            return;
        }

        // Create a User object to pass to the updateUserDetails method
        User updatedUser = new User(newUsername, newEmail, newFirstName, newLastName, newPassword);

        // Call the updateUserDetails method
        String errorMessage = userService.updateUserDetails(updatedUser);
        if (errorMessage != null) {
            showAlert(Alert.AlertType.ERROR, "Error", errorMessage);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Profile Update", "Your profile has been updated successfully.");
            currentUsername = newUsername; // Update current username
            clearProfileFields(); // Optional: Clear fields
        }
    }



    // Method to clear fields in the profile
    private void clearProfileFields() {
        profileUsername.clear();
        profileEmail.clear();
        profileFirstName.clear();
        profileLastName.clear();
        profilePassword.clear();
    }

    // Show an alert message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    // Pane navigation methods
    @FXML
    private void showHomePane(ActionEvent event) { showPane(homePane); }
    @FXML
    private void showRecommendedPane(ActionEvent event) { showPane(recommendedPane); }
    @FXML
    private void showViewPane(ActionEvent event) { showPane(viewPane); }
    @FXML
    private void showProfilePane(ActionEvent event) { showPane(profilePane); }
    


    // Log out action to return to the login page
    @FXML
    private void logOutOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent loginRoot = fxmlLoader.load();

        Stage loginStage = new Stage();
        loginStage.setTitle("Login Page");
        loginStage.setScene(new Scene(loginRoot));
        loginStage.show();

        Stage currentStage = (Stage) logoutButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void refreshArticles() {
        showRecommendedPane();
    }

    // Method to fetch and display recommended articles for the logged-in user



    private void showError(String message) {
        System.err.println(message);
    }
}

