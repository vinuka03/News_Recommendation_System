module org.example.news_recommendation_system {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.news_recommendation_system to javafx.fxml;
    exports org.example.news_recommendation_system;
}