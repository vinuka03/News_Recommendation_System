package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignupController {

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
    private CheckBox ComedyCheckbox;
    @FXML
    private CheckBox politicalCheckbox;
    @FXML
    private CheckBox religiousCheckbox;

    private UserService userService;

    public SignupController() {
        // Dependency Injection for the UserService
        this.userService = new UserService();
    }

    @FXML
    private void signUpOnAction(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String retypePassword = retypePasswordField.getText();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                username.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "All fields must be filled.");
            return;
        }

        if (!userService.isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Please enter a valid email address.");
            return;
        }

        if (!userService.isPasswordValid(password)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Password must be at least 8 characters long.");
            return;
        }

        if (!password.equals(retypePassword)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Passwords do not match.");
            return;
        }

        if (userService.isUserExists(username, email)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Username or Email already exists.");
            return;
        }

        List<String> categories = getSelectedCategories();
        if (categories.size() < 2) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Please select at least two categories.");
            return;
        }

        // Create new user document
        Document newUser = new Document("firstName", firstName)
                .append("lastName", lastName)
                .append("email", email)
                .append("username", username)
                .append("password", password)
                .append("categories", categories)
                .append("role", "user");  // Assign 'user' role

        // Insert user into the database
        boolean userCreated = userService.createUser(newUser);
        if (userCreated) {
            showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "Account created successfully!");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Could not save user data.");
        }
    }

    @FXML
    private void goBackToLogIN(ActionEvent event) throws IOException {
        // Load the login page (LogIn.fxml)
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

    private List<String> getSelectedCategories() {
        List<String> categories = new ArrayList<>();
        if (techCheckbox.isSelected()) categories.add("Technology");
        if (sportsCheckbox.isSelected()) categories.add("Sports");
        if (healthCheckbox.isSelected()) categories.add("Health");
        if (ComedyCheckbox.isSelected()) categories.add("AI");
        if (politicalCheckbox.isSelected()) categories.add("Political");
        if (religiousCheckbox.isSelected()) categories.add("Religious");
        return categories;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        ComedyCheckbox.setSelected(false);
        politicalCheckbox.setSelected(false);
        religiousCheckbox.setSelected(false);
    }
}
