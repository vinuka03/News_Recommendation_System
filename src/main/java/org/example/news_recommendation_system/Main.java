package org.example.news_recommendation_system;



import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Fetch news articles using the NewsFetcher class
            List<Article> articles = NewsFetcher.fetchArticlesFromAPI();  // Now this method correctly returns a List<Article>

            if (articles.isEmpty()) {
                System.out.println("No articles fetched.");
            } else {
                System.out.println("Fetched " + articles.size() + " articles.");

                // Save articles to the database using the DatabaseHandler class
                DatabaseHandler.saveArticlesToDatabase(articles);  // Assuming this method is static
                System.out.println("Articles saved to the database.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
