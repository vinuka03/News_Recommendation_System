package org.example.news_recommendation_system;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import org.bson.Document;

public class DatabaseHandler {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "NEWS";  // Update with your actual database name

    private static MongoDatabase database = null;

    // Singleton pattern to ensure only one MongoDB connection
    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                // Create MongoDB connection string
                ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);

                // Create MongoClientSettings object using the connection string
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .build();

                // Create MongoClient using the settings
                var mongoClient = MongoClients.create(settings);

                // Get the database using the MongoClient
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
        // MongoClient.close() is not needed if using MongoClients.create() as shown above.
    }




}
