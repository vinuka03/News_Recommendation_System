package org.example.news_recommendation_system;

public class Article {
    private String headline;
    private String shortDescription;
    private String date;
    private String category;
    private String link;

    // Constructor
    public Article(String headline, String shortDescription, String date, String category, String link) {
        this.headline = headline;
        this.shortDescription = shortDescription;
        this.date = date;
        this.category = category;
        this.link = link;
    }

    // Getters and setters
    public String getHeadline() {
        return headline;
    }


    public String getShortDescription() {
        return shortDescription;
    }


    public  String  getLink(){return link;}
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

}
