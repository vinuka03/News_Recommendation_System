package org.example.news_recommendation_system;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsFetcher {

    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines";
    private static final String API_KEY = "750c2a0d90a6468a8d3b5f33e820c665"; // Replace with your News API key
    private static final String COUNTRY = "us"; // Specify country
    private static final String DB_COLLECTION = "news_articles";

    // List of categories to fetch articles from
    private static final List<String> CATEGORIES = List.of("technology", "politics", "sports", "health");

    // Fetch articles from the News API for all categories and return a list of Article objects
    public static List<Article> fetchArticlesFromAPI() throws IOException {
        List<Article> allArticles = new ArrayList<>();

        // Loop through all categories and fetch articles
        for (String category : CATEGORIES) {
            List<Article> articles = fetchArticlesFromAPIForCategory(category);

            if (!articles.isEmpty()) {
                System.out.println("Fetched " + articles.size() + " articles for category: " + category);
                allArticles.addAll(articles);  // Add articles for this category to the list
            } else {
                System.out.println("No articles found for category: " + category);
            }
        }

        return allArticles;  // Return the full list of articles from all categories
    }

    // Fetch articles for a specific category
    public static List<Article> fetchArticlesFromAPIForCategory(String category) throws IOException {
        List<Article> articlesList = new ArrayList<>();

        // Build API request URL for the specified category
        String urlString = String.format("%s?country=%s&category=%s&apiKey=%s", NEWS_API_URL, COUNTRY, category, API_KEY);

        // Send HTTP GET request to News API and parse the response as JSON
        String response = sendHttpRequest(urlString);
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray articlesArray = jsonResponse.getJSONArray("articles");

        // Extract articles from the response and create Article objects
        for (int i = 0; i < articlesArray.length(); i++) {
            JSONObject articleJson = articlesArray.getJSONObject(i);

            // Safely get the 'author' field, defaulting to "Unknown" if not present or not a string
            String author = articleJson.has("author") && articleJson.get("author") instanceof String
                    ? articleJson.getString("author")
                    : "Unknown";

            // Safely get the 'description' field, defaulting to an empty string if it's not a string
            String description = articleJson.has("description") && articleJson.get("description") instanceof String
                    ? articleJson.getString("description")
                    : "";  // Default to an empty string if description is missing or not a string

            // Safely get the 'content' field, defaulting to an empty string if it's not a string
            String content = articleJson.has("content") && articleJson.get("content") instanceof String
                    ? articleJson.getString("content")
                    : "";  // Default to an empty string if content is missing or not a string

            // Safely get the 'title' field, defaulting to an empty string if it's not a string
            String title = articleJson.has("title") && articleJson.get("title") instanceof String
                    ? articleJson.getString("title")
                    : "";  // Default to an empty string if title is missing or not a string

            Article article = new Article(
                    title,        // Use the safely obtained title
                    description,  // Use the safely obtained description
                    content,      // Use the safely obtained content
                    author,       // Use the safely obtained author
                    articleJson.getString("url"),
                    articleJson.getString("publishedAt"),
                    articleJson.getJSONObject("source").getString("name")
            );
            articlesList.add(article);
        }

        return articlesList;
    }

    // Send HTTP request and return the response as a string
    private static String sendHttpRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Read the response from the input stream
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    // Save fetched articles to the MongoDB database (without category field)
    private static void saveArticlesToDatabase(List<Article> articles) {
        try {
            MongoDatabase database = DatabaseHandler.getDatabase();
            MongoCollection<Document> collection = database.getCollection(DB_COLLECTION);

            List<Document> documents = new ArrayList<>();
            for (Article article : articles) {
                Document doc = new Document()
                        .append("title", article.getTitle())
                        .append("description", article.getDescription())
                        .append("content", article.getContent())
                        .append("author", article.getAuthor())
                        .append("url", article.getUrl())
                        .append("publishedAt", article.getPublishedAt())
                        .append("source", article.getSource());
                documents.add(doc);
            }

            collection.insertMany(documents);  // Insert all the documents at once
            System.out.println(articles.size() + " articles have been saved to the database.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving articles to MongoDB.");
        }
    }
}
