package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
public class Signup {
    @FXML
    public Button signup;
    @FXML
    public Button goBackLogin;
    public TextField usernameField;

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField retypePasswordField;
    @FXML
    private CheckBox techCheckbox;
    @FXML
    private CheckBox sportsCheckbox;
    @FXML
    private CheckBox healthCheckbox;
    @FXML
    private CheckBox aiCheckbox;
    @FXML
    private CheckBox politicalCheckbox;
    @FXML
    private CheckBox religiousCheckbox;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> userDetailsCollection;

    public void initialize() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("News_Recommendation");
            userDetailsCollection = database.getCollection("User_Details");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    @FXML
    private void signUpOnAction(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String retypePassword = retypePasswordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                username.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "All fields must be filled.");
            return;
        }


        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailPattern)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Please enter a valid email address.");
            return;
        }


        // Check if password meets minimum length
        if (password.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Password must be at least 8 characters long.");
            return;
        }


        if (!password.equals(retypePassword)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Passwords do not match.");
            return;
        }

        // Check if username or email already exists
        Document existingUser = userDetailsCollection.find(
                new Document("$or", List.of(
                        new Document("username", username),
                        new Document("email", email)
                ))
        ).first();

        if (existingUser != null) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Username or Email already exists.");
            return;
        }

        List<String> categories = new ArrayList<>();
        if (techCheckbox.isSelected()) categories.add("Technology");
        if (sportsCheckbox.isSelected()) categories.add("Sports");
        if (healthCheckbox.isSelected()) categories.add("Health");
        if (aiCheckbox.isSelected()) categories.add("AI");
        if (politicalCheckbox.isSelected()) categories.add("Political");
        if (religiousCheckbox.isSelected()) categories.add("Religious");


        //atleast two categories are selected
        if (categories.size() < 2) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Please select at least two categories.");
            return;
        }

        Document newUser = new Document("firstName", firstName)
                .append("lastName", lastName)
                .append("email", email)
                .append("username", username)
                .append("password", password)
                .append("categories", categories);

        try {
            userDetailsCollection.insertOne(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "Account created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Could not save user data.");
        }
        clearFields();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    @FXML
    private void goBackToLogIN(ActionEvent event) throws IOException {
        // Load the LogIn.fxml file (assuming it is the login page)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
        Parent loginRoot = fxmlLoader.load();

        // Create a new stage for the login page
        Stage loginStage = new Stage();
        loginStage.setTitle("Login Page");
        loginStage.setScene(new Scene(loginRoot));

        // Show the login stage
        loginStage.show();

        // Close the current signup stage
        Stage currentStage = (Stage) goBackLogin.getScene().getWindow();
        currentStage.close();
    }


    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        usernameField.clear();
        passwordField.clear();
        retypePasswordField.clear();

        // Deselect checkboxes
        techCheckbox.setSelected(false);
        sportsCheckbox.setSelected(false);
        healthCheckbox.setSelected(false);
        aiCheckbox.setSelected(false);
        politicalCheckbox.setSelected(false);
        religiousCheckbox.setSelected(false);
    }
}
