package org.example.news_recommendation_system;

public class Article {
    private String title;
    private String description;
    private String content;
    private String author;
    private String url;
    private String publishedAt;
    private String source;

    // Constructor
    public Article(String title, String description, String content, String author, String url, String publishedAt, String source) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.author = author;
        this.url = url;
        this.publishedAt = publishedAt;
        this.source = source;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
