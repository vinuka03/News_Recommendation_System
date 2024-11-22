package org.example.news_recommendation_system.article;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class ArticleFetcher {

    private final String jsonFilePath;
    private final int maxArticles;

    public ArticleFetcher(String jsonFilePath, int maxArticles) {
        this.jsonFilePath = jsonFilePath;
        this.maxArticles = maxArticles;
    }

    public List<Map<String, Object>> fetchArticles() {
        List<Map<String, Object>> articles = new ArrayList<>();
        Gson gson = new Gson();

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonObject articleJson = gson.fromJson(line, JsonObject.class);
                articles.add(parseArticle(articleJson));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.shuffle(articles);
        return articles.size() > maxArticles ? articles.subList(0, maxArticles) : articles;
    }

    private Map<String, Object> parseArticle(JsonObject articleJson) {
        Map<String, Object> article = new HashMap<>();
        article.put("link", articleJson.get("link").getAsString());
        article.put("headline", articleJson.get("headline").getAsString());
        article.put("short_description", articleJson.get("short_description").getAsString());
        article.put("authors", articleJson.get("authors").getAsString());
        article.put("date", articleJson.get("date").getAsString());
        article.put("category", "Uncategorized"); // Default category
        return article;
    }
}
