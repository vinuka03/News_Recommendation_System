package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.MongoClients;

import java.net.URI;
import java.awt.Desktop;

public class ArticleDetailsController {

    @FXML private Button dislikeButton;
    @FXML private Button likeButton;
    @FXML private Hyperlink articleLink;
    @FXML private Button closeButton;
    @FXML private TextField detailHeadline;
    @FXML private TextArea detailDescription;
    @FXML private TextField detailDate;
    @FXML private TextField detailCategory;

    private Article currentArticle; // Declare the currentArticle variable
    private String userRating = ""; // To keep track of the current rating ("like" or "dislike")
    private String username; // To store the dynamically set username
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
        if ("like".equals(userRating)) {
            System.out.println("User removed the like for the article: " + currentArticle.getHeadline());
            userRating = ""; // Reset the rating
            likeButton.setStyle(null); // Reset button style
            updateUserPreferenceScore(-2); // Deduct 2 marks from the category
        } else {
            System.out.println("User liked the article: " + currentArticle.getHeadline());
            userRating = "like";
            likeButton.setStyle("-fx-background-color: #4caf50;"); // Highlight the button
            dislikeButton.setStyle(null); // Reset the other button
            updateUserPreferenceScore(2); // Add 2 marks to the category
        }
    }

    // Submit a dislike for the article
    @FXML
    private void submitDislike(ActionEvent event) {
        if ("dislike".equals(userRating)) {
            System.out.println("User removed the dislike for the article: " + currentArticle.getHeadline());
            userRating = ""; // Reset the rating
            dislikeButton.setStyle(null); // Reset button style
            updateUserPreferenceScore(2); // Add 2 marks to the category
        } else {
            System.out.println("User disliked the article: " + currentArticle.getHeadline());
            userRating = "dislike";
            dislikeButton.setStyle("-fx-background-color: #f44336;"); // Highlight the button
            likeButton.setStyle(null); // Reset the other button
            updateUserPreferenceScore(-2); // Deduct 2 marks from the category
        }
    }

    // Close the details window
    @FXML
    private void closeDetails() {
        Stage stage = (Stage) detailHeadline.getScene().getWindow();
        stage.close();
    }

    // Method to update the user's preferences in the database
    private void updateUserPreferenceScore(int points) {
        if (username == null) {
            System.out.println("Error: No logged-in user.");
            return;
        }

        String category = currentArticle.getCategory(); // Get the category of the current article
        MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("NEWS");
        MongoCollection<Document> userHistoryCollection = database.getCollection("User_Preferences");

        System.out.println("Updating preferences for user: " + username); // Debugging statement

        // Update the specific category score for the current user
        userHistoryCollection.updateOne(
                Filters.eq("username", username), // Find the user by the actual logged-in username
                Updates.inc("preferences." + category, points) // Increment the score for the category
        );
    }


}
