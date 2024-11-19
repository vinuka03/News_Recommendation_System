package org.example.news_recommendation_system;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.List;

public class DatabaseHandler {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "News_Recommendation";

    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    // Singleton pattern to ensure only one MongoDB connection
    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DATABASE_NAME);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to connect to MongoDB: " + e.getMessage());
            }
        }
        return database;
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }


    // Insert a list of articles into the MongoDB collection
    public static void saveArticlesToDatabase(List<Article> articles) {
        MongoCollection<Document> collection = getCollection("news_articles");

        for (Article article : articles) {
            Document doc = new Document("title", article.getTitle())
                    .append("description", article.getDescription())
                    .append("content", article.getContent())
                    .append("author", article.getAuthor())
                    .append("url", article.getUrl())
                    .append("publishedAt", article.getPublishedAt())
                    .append("source", article.getSource());

            collection.insertOne(doc);
        }
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
