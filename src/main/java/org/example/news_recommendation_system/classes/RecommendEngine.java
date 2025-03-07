package org.example.news_recommendation_system.classes;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.bson.Document;
import org.example.news_recommendation_system.classes.Article;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendEngine {

    private MongoCollection<Document> userHistoryCollection;
    private MongoCollection<Document> articlesCollection;


    DatabaseHandler db;

    // Constructor to initialize the collections
    public RecommendEngine(DatabaseHandler database) {
        this.db = database;
        this.userHistoryCollection = db.getCollection("User_Preferences");
        this.articlesCollection = db.getCollection("articles");
    }

    // Method to fetch recommended articles for a given user
    public ObservableList<Article> fetchRecommendedArticles(String currentUsername) {
        try {
            // Fetch user preferences for the logged-in user
            if (currentUsername == null || currentUsername.isEmpty()) {
                throw new IllegalArgumentException("No user is currently logged in.");
            }

            Document userPreferences = userHistoryCollection.find(new Document("username", currentUsername)).first();
            if (userPreferences == null) {
                throw new IllegalArgumentException("User preferences not found for the current user.");
            }

            // Get top categories based on scores
            Document scores = (Document) userPreferences.get("preferences");
            if (scores == null || scores.isEmpty()) {
                throw new IllegalArgumentException("No preferences found for the current user.");
            }

            // Sort categories by score and select the top 3
            List<Map.Entry<String, Integer>> sortedCategories = scores.entrySet()
                    .stream()
                    .map(entry -> Map.entry(entry.getKey(), (Integer) entry.getValue()))
                    .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                    .collect(Collectors.toList());

            List<String> topCategories = sortedCategories.stream()
                    .limit(3)  // Limit to top 3 categories
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Fetch articles from these categories
            List<Document> articles = new ArrayList<>();
            for (String category : topCategories) {
                MongoCursor<Document> cursor = articlesCollection.find(new Document("category", category)).iterator();
                while (cursor.hasNext()) {
                    articles.add(cursor.next());
                }
            }

            // Shuffle and limit to 20 articles
            Collections.shuffle(articles);
            articles = articles.subList(0, Math.min(20, articles.size()));

            // Convert articles to Article objects using getters and setters
            ObservableList<Article> articlesList = FXCollections.observableArrayList();
            for (Document article : articles) {
                // Create a new Article object
                Article articleObj = new Article("", "", "", "", "");

                // Use setters to set the article's properties
                articleObj.setHeadline(article.getString("headline"));
                articleObj.setShortDescription(article.getString("short_description"));
                articleObj.setDate(article.getString("date"));
                articleObj.setCategory(article.getString("category"));
                articleObj.setLink(article.getString("link"));

                // Add the article to the list
                articlesList.add(articleObj);
            }

            return articlesList;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching recommended articles: " + e.getMessage(), e);
        }
    }

}
