package org.example.news_recommendation_system;

import javafx.collections.FXCollections;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;


public class MainPageController {


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
    @FXML private TextField searchHeadlineField;
    @FXML private TextField searchCategoryField;
    @FXML private TableView<Article> searchResultsTable;
    @FXML private TableColumn<Article, String> resultHeadlineColumn;
    @FXML private TableColumn<Article, String> resultCategoryColumn;
    @FXML private TableColumn<Article, String> resultDateColumn;
    @FXML private TextField ratingField;


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
    @FXML private Pane ratePane;

    // MongoDB collections and current user data
    private MongoCollection<Document> userDetailsCollection;
    private MongoCollection<Document> articlesCollection;

    private MongoCollection<Document> userHistoryCollection;
    private MongoCollection<Document> userRatingCollection;

    private String currentUsername;

    // Method to initialize the controller with MongoDB and user data
    public void initializeWithData(String username) {
        this.userDetailsCollection = DatabaseHandler.getCollection("User_Details");
        this.articlesCollection = DatabaseHandler.getCollection("NewsArticles");
        this.articlesCollection = DatabaseHandler.getCollection("articles");
        this.userHistoryCollection = DatabaseHandler.getCollection("User_History");
        this.userRatingCollection = DatabaseHandler.getCollection("User_Rating");
        setupTextWrapping();
        this.currentUsername = username;
    }

    @FXML
    public void initialize() {
        showPane(homePane); // Display the home pane by default
        initializeTableColumns();// Initialize the table columns
        addTableRowClickListener();  // Add the listener for row clicks
        initializeTableColumns();
        initializeSearchResultsTable();

    }
    private void initializeSearchResultsTable() {
        resultHeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("headline"));
        resultCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        resultDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
    //save article history
    private void saveArticleToHistory(Article article) {
        if (userHistoryCollection != null && currentUsername != null) {
            Document userHistory = new Document()
                    .append("username", currentUsername)
                    .append("headline", article.getHeadline())
                    .append("category", article.getCategory())
                    .append("timestamp", System.currentTimeMillis()); // Add timestamp for tracking

            userHistoryCollection.insertOne(userHistory);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to save to user history.");
        }
    }




    // Add event listener to handle row click
    private void addTableRowClickListener() {
        articlesTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                Article selectedArticle = articlesTable.getSelectionModel().getSelectedItem();
                if (selectedArticle != null) {
                    openArticleDetailsWindow(selectedArticle);
                }
            }
        });
    }

    // Method to open a new window with the article details
    private void openArticleDetailsWindow(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleDetails.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the controller and pass the selected article
            ArticleDetailsController controller = loader.getController();
            controller.initializeWithArticle(article);

            Stage stage = new Stage();
            stage.setTitle("Article Details");
            stage.setScene(scene);
            stage.show();

            // Save interaction to User_History
            saveArticleToHistory(article);



        } catch (IOException e) {
            e.printStackTrace();
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

    }

    // Method to display only the selected pane
    private void showPane(Pane pane) {
        homePane.setVisible(false);
        recommendedPane.setVisible(false);
        viewPane.setVisible(false);
        profilePane.setVisible(false);
        ratePane.setVisible(false);

        pane.setVisible(true);
        if (pane == profilePane) {
            loadUserProfile();  // Load profile details when showing the Profile pane
        }
        if (pane == viewPane) {
            loadArticlesFromDatabase();// Load articles when showing the View pane
            customizeTableRowHeight(); // Customize row height
            setupTextWrapping();
        }
    }

    // Load user details into profile fields
    private void loadUserProfile() {
        if (userDetailsCollection != null && currentUsername != null) {
            Document user = userDetailsCollection.find(new Document("username", currentUsername)).first();
            if (user != null) {
                profileUsername.setText(user.getString("username"));
                profileEmail.setText(user.getString("email"));
                profileFirstName.setText(user.getString("firstName"));
                profileLastName.setText(user.getString("lastName"));
                profilePassword.setText(user.getString("password"));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "User data not found.");
            }
        }
    }

    // Handle the "Update" button click to save profile changes
    @FXML
    private void updateProfile(ActionEvent event) {
        String newUsername = profileUsername.getText();
        String newEmail = profileEmail.getText();
        String newFirstName = profileFirstName.getText();
        String newLastName = profileLastName.getText();
        String newPassword = profilePassword.getText();

        // Validate input before updating
        if (newEmail == null || newEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Email cannot be empty.");
            return;
        }
        if (newFirstName == null || newFirstName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "First Name cannot be empty.");
            return;
        }
        if (newLastName == null || newLastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Last Name cannot be empty.");
            return;
        }
        if (newPassword == null || newPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Password cannot be empty.");
            return;
        }
        if (newPassword.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Password must be at least 8 characters.");
            return;
        }

        // Check if the email already exists in the database for another user (excluding the current user)
        Document existingEmailUser = userDetailsCollection.find(new Document("email", newEmail)).first();
        if (existingEmailUser != null && !existingEmailUser.getString("username").equals(currentUsername)) {
            showAlert(Alert.AlertType.ERROR, "Email Taken", "The email is already taken by another user.");
            return;
        }

        // Check if the username already exists (excluding the current user)
        Document existingUser = userDetailsCollection.find(new Document("username", newUsername)).first();
        if (existingUser != null && !newUsername.equals(currentUsername)) {
            showAlert(Alert.AlertType.ERROR, "Username Taken", "The new username is already taken.");
            return;
        }

        // Create a document with updated details
        Document updatedDetails = new Document("email", newEmail)
                .append("firstName", newFirstName)
                .append("lastName", newLastName)
                .append("password", newPassword);

        // Update MongoDB
        userDetailsCollection.updateOne(new Document("username", currentUsername), new Document("$set", updatedDetails));
        currentUsername = newUsername;  // Update the current username if changed

        showAlert(Alert.AlertType.INFORMATION, "Profile Update", "Your profile has been updated successfully.");

        // Clear the fields after update
        clearProfileFields();
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

    // Load articles from the MongoDB database
    private void loadArticlesFromDatabase() {
        if (articlesCollection != null) {
            ObservableList<Article> articlesList = FXCollections.observableArrayList();
            for (Document doc : articlesCollection.find()) {
                Article article = new Article(
                        doc.getString("headline"),
                        doc.getString("short_description"),
                        doc.getString("date"),
                        doc.getString("category"),
                        doc.getString("link")
                );
                articlesList.add(article);
            }
            articlesTable.setItems(articlesList); // Set the list to the TableView
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load articles.");
        }
    }

    //search article by headline  and category
    @FXML
    private void searchArticles(ActionEvent event) {
        String searchHeadline = searchHeadlineField.getText().trim();
        String searchCategory = searchCategoryField.getText().trim();

        // Validate input
        if (searchHeadline.isEmpty() && searchCategory.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter at least a headline or category to search.");
            return;
        }

        // Prepare the search query
        Document query = new Document();
        if (!searchHeadline.isEmpty()) {
            query.append("headline", new Document("$regex", searchHeadline).append("$options", "i")); // Case-insensitive search
        }
        if (!searchCategory.isEmpty()) {
            query.append("category", new Document("$regex", searchCategory).append("$options", "i"));
        }

        // Execute the search in the database
        ObservableList<Article> searchResults = FXCollections.observableArrayList();
        for (Document doc : articlesCollection.find(query)) {
            Article article = new Article(
                    doc.getString("headline"),
                    doc.getString("short_description"),
                    doc.getString("date"),
                    doc.getString("category"),
                    doc.getString("link")
            );
            searchResults.add(article);
        }

        // Check if search results are empty
        if (searchResults.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Articles Found", "No articles found for the given headline and category.");
        } else {
            // Update the TableView with the search results
            searchResultsTable.setItems(searchResults);
        }
    }



    @FXML
    private void submitRating(ActionEvent event) {
        Article selectedArticle = searchResultsTable.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            showAlert(Alert.AlertType.ERROR, "No Selection", "Please select an article to rate.");
            return;
        }

        String ratingText = ratingField.getText();
        int rating;
        try {
            rating = Integer.parseInt(ratingText);
            if (rating < 1 || rating > 5) {
                throw new NumberFormatException("Out of range");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Rating", "Please enter a number between 1 and 5.");
            return;
        }

        Document ratingDoc = new Document("username", currentUsername)
                .append("headline", selectedArticle.getHeadline())
                .append("category", selectedArticle.getCategory())
                .append("rating", rating)
                .append("timestamp", System.currentTimeMillis());

        DatabaseHandler.getCollection("User_Rating").insertOne(ratingDoc);

        showAlert(Alert.AlertType.INFORMATION, "Rating Submitted", "Your rating has been saved successfully.");
        ratingField.clear();
        searchResultsTable.getSelectionModel().clearSelection();
        searchHeadlineField.clear();
        searchCategoryField.clear();
        searchResultsTable.getItems().clear();
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
    @FXML
    private void showRatePane(ActionEvent event) { showPane(ratePane); }

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
}