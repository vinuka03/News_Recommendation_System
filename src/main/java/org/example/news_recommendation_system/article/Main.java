package org.example.news_recommendation_system.article;

import org.example.news_recommendation_system.classes.DatabaseHandler;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Step 1: Fetch articles
        ArticleFetcher fetcher = new ArticleFetcher("src/main/article.json", 5000); // Provide file path and max articles
        List<Map<String, Object>> articles = fetcher.fetchArticles();

        // Step 2: Categorize articles
        ArticleCategorizer categorizer = new ArticleCategorizer();
        for (Map<String, Object> article : articles) {
            String category = categorizer.categorizeArticle(article);
            article.put("category", category); // Update article with its category
        }

        // Step 3: Save categorized articles to the database
        ArticleSaver saver = new ArticleSaver("articles"); // Provide collection name
        saver.saveArticles(articles);
        DatabaseHandler.closeConnection();

        System.out.println("Article processing completed successfully.");
    }
}
