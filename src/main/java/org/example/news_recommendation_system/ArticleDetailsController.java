package org.example.news_recommendation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;

import java.awt.Desktop;


public class ArticleDetailsController {

    @FXML private Hyperlink articleLink;
    @FXML private Button closeButton;
    @FXML private TextField detailHeadline;
    @FXML private TextArea detailDescription;
    @FXML private TextField detailDate;
    @FXML private TextField detailCategory;
    @FXML private TextField detailLink;



    // Assuming you have a method that sets the article data
    public void initializeWithArticle(Article article) {
        detailHeadline.setText(article.getHeadline());
        detailDescription.setText(article.getShortDescription());  // Set the description text
        detailDate.setText(article.getDate());
        detailCategory.setText(article.getCategory());



        detailDescription.setEditable(false);
        detailHeadline.setEditable(false);
        detailDate.setEditable(false);





        String url = article.getLink();

        // Set the link text to the article's URL
        articleLink.setText(url);

        // Set the action to open the URL when clicked
        articleLink.setOnAction(event -> openLink(url));

    }

    // Method to open the link in the default browser
    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    // Close the details window
    @FXML
    private void closeDetails() {
        Stage stage = (Stage) detailHeadline.getScene().getWindow();
        stage.close();
    }


}
