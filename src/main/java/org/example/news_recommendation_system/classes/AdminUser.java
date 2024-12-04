package org.example.news_recommendation_system.classes;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class AdminUser {
    private final MongoCollection<Document> userDetailsCollection;
    private final MongoCollection<Document> articlesCollection;

    private DatabaseHandler db = new DatabaseHandler();

    public AdminUser(MongoCollection<Document> userDetailsCollection, MongoCollection<Document> articlesCollection) {
        this.userDetailsCollection = userDetailsCollection;
        this.articlesCollection = articlesCollection;
        MongoDatabase database = db.getDatabase();
    }

    // Method to delete a user
    public boolean deleteUser(String username) {
        try {
            Document query = new Document("username", username);
            long deletedCount = userDetailsCollection.deleteOne(query).getDeletedCount();
            return deletedCount > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete an article
    public boolean deleteArticle(String headline) {
        try {
            Document query = new Document("headline", headline);
            long deletedCount = articlesCollection.deleteOne(query).getDeletedCount();
            return deletedCount > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
