package org.example.news_recommendation_system.classes;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {


    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "NEWS";  // Update with your actual database name

    private static MongoDatabase database = null;

    // Singleton pattern to ensure only one MongoDB connection
    public  MongoDatabase getDatabase() {
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

    public  MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }


    public static void closeConnection() {
    }

}



