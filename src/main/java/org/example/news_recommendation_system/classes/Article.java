package org.example.news_recommendation_system.classes;

import com.mongodb.client.MongoCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;

public class Article {
    private String headline;
    private String shortDescription;
    private String date;
    private String category;
    private String link;

    // Constructor
    public Article(String headline, String shortDescription, String date, String category, String link) {
        this.headline = headline;
        this.shortDescription = shortDescription;
        this.date = date;
        this.category = category;
        this.link = link;
    }

    // Getters and setters
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Static method to load articles from MongoDB
    public static ObservableList<Article> loadArticlesFromDatabase(MongoCollection<Document> collection) {
        ObservableList<Article> articlesList = FXCollections.observableArrayList();

        if (collection != null) {
            for (Document doc : collection.find()) {
                Article article = new Article(
                        doc.getString("headline"),
                        doc.getString("short_description"),
                        doc.getString("date"),
                        doc.getString("category"),
                        doc.getString("link")
                );
                articlesList.add(article);
            }
        }
        return articlesList;
    }
}
