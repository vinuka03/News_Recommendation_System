package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.news_recommendation_system.classes.Article;
import org.example.news_recommendation_system.classes.UserService;

import java.awt.Desktop;
import java.net.URI;

public class ArticleDetailsController {

    @FXML
    private Button dislikeButton;
    @FXML
    private Button likeButton;
    @FXML
    private Hyperlink articleLink;
    @FXML
    private Button closeButton;
    @FXML
    private TextField detailHeadline;
    @FXML
    private TextArea detailDescription;
    @FXML
    private TextField detailDate;
    @FXML
    private TextField detailCategory;

    private Article currentArticle; // Declare the currentArticle variable
    private String userRating = ""; // To keep track of the current rating ("like" or "dislike")
    private String username; // To store the dynamically set username

    private final UserService userService = new UserService(); // Create an instance of UserService

    // Method to set article data
    public void initializeWithArticle(Article article) {
        currentArticle = article; // Assign the passed article to currentArticle

        detailHeadline.setText(article.getHeadline());
        detailDescription.setText(article.getShortDescription());
        detailDate.setText(article.getDate());
        detailCategory.setText(article.getCategory());

        detailDescription.setEditable(false);
        detailHeadline.setEditable(false);
        detailDate.setEditable(false);

        String url = article.getLink();
        articleLink.setText(url);
        articleLink.setOnAction(event -> openLink(url));
    }

    // Add this setter method
    public void setUsername(String username) {
        this.username = username;
        System.out.println("Username set to: " + username); // Optional: To verify if it's being set correctly
    }

    // Open the link in the default browser
    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Submit a like for the article
    @FXML
    private void submitLike(ActionEvent event) {
        if (username == null || currentArticle == null) {
            System.err.println("Error: Missing username or article data.");
            return;
        }

        String category = currentArticle.getCategory();
        if ("like".equals(userRating)) {
            System.out.println("User removed the like for the article: " + currentArticle.getHeadline());
            userRating = ""; // Reset the rating
            likeButton.setStyle(null); // Reset button style
            if (!userService.updateUserPreferenceScore(username, category, -2)) {
                System.err.println("Failed to update user preference score.");
            }
        } else {
            System.out.println("User liked the article: " + currentArticle.getHeadline());
            userRating = "like";
            likeButton.setStyle("-fx-background-color: #4caf50;"); // Highlight the button
            dislikeButton.setStyle(null); // Reset the other button
            if (!userService.updateUserPreferenceScore(username, category, 2)) {
                System.err.println("Failed to update user preference score.");
            }
        }
    }

    // Submit a dislike for the article
    @FXML
    private void submitDislike(ActionEvent event) {
        if (username == null || currentArticle == null) {
            System.err.println("Error: Missing username or article data.");
            return;
        }

        String category = currentArticle.getCategory();
        if ("dislike".equals(userRating)) {
            System.out.println("User removed the dislike for the article: " + currentArticle.getHeadline());
            userRating = ""; // Reset the rating
            dislikeButton.setStyle(null); // Reset button style
            if (!userService.updateUserPreferenceScore(username, category, 2)) {
                System.err.println("Failed to update user preference score.");
            }
        } else {
            System.out.println("User disliked the article: " + currentArticle.getHeadline());
            userRating = "dislike";
            dislikeButton.setStyle("-fx-background-color: #f44336;"); // Highlight the button
            likeButton.setStyle(null); // Reset the other button
            if (!userService.updateUserPreferenceScore(username, category, -2)) {
                System.err.println("Failed to update user preference score.");
            }
        }
    }

    // Close the details window
    @FXML
    private void closeDetails(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();  // Close the article details window
    }
}
