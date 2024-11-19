module org.example.news_recommendation_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires com.fasterxml.jackson.databind;
    requires org.json;


    opens org.example.news_recommendation_system to javafx.fxml;
    exports org.example.news_recommendation_system;
}