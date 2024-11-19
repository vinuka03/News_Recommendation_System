package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;

public class MainPageController {

    // StackPane and profile fields
    public StackPane contentStackPane;
    public ImageView profileImageView;
    @FXML private TextField profileUsername;
    @FXML private TextField profileEmail;
    @FXML private TextField profileFirstName;
    @FXML private TextField profileLastName;
    @FXML private TextField profilePassword;
    @FXML private Button updateProfileButton;

    // Sidebar Buttons
    @FXML private Button homeButton;
    @FXML private Button recommendedButton;
    @FXML private Button categoriesButton;
    @FXML private Button profileButton;
    @FXML private Button rateButton;
    @FXML private Button logoutButton;

    // Content Panes
    @FXML private Pane homePane;
    @FXML private Pane recommendedPane;
    @FXML private Pane categoriesPane;
    @FXML private Pane profilePane;
    @FXML private Pane ratePane;

    // MongoDB collections and current user data
    private MongoCollection<Document> userDetailsCollection;
    private String currentUsername;

    // Method to initialize the controller with MongoDB and user data
    public void initializeWithData(String username) {
        this.userDetailsCollection = DatabaseHandler.getCollection("User_Details");
        this.currentUsername = username;
    }

    @FXML
    public void initialize() {
        showPane(homePane); // Display the home pane by default
    }

    // Method to display only the selected pane
    private void showPane(Pane pane) {
        homePane.setVisible(false);
        recommendedPane.setVisible(false);
        categoriesPane.setVisible(false);
        profilePane.setVisible(false);
        ratePane.setVisible(false);

        pane.setVisible(true);
        if (pane == profilePane) {
            loadUserProfile();  // Load profile details when showing the Profile pane
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

    // Pane navigation methods
    @FXML
    private void showHomePane(ActionEvent event) { showPane(homePane); }
    @FXML
    private void showRecommendedPane(ActionEvent event) { showPane(recommendedPane); }
    @FXML
    private void showCategoriesPane(ActionEvent event) { showPane(categoriesPane); }
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
