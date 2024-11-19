package org.example.news_recommendation_system;

import com.mongodb.client.*;
import org.bson.Document;

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

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
