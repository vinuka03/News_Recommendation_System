package org.example.news_recommendation_system.article;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.news_recommendation_system.classes.DatabaseHandler;

import java.util.List;
import java.util.Map;

public class ArticleSaver {

    private final String collectionName;
    private DatabaseHandler db = new DatabaseHandler();

    public ArticleSaver(String collectionName) {
        this.collectionName = collectionName;
    }

    public void saveArticles(List<Map<String, Object>> articles) {
        MongoCollection<Document> collection = db.getCollection(collectionName);

        for (Map<String, Object> article : articles) {
            if (!"Uncategorized".equals(article.get("category"))) {
                Document doc = new Document(article);
                collection.insertOne(doc);
            }
        }
        System.out.println("Articles with valid categories have been saved to the database.");
    }
}
