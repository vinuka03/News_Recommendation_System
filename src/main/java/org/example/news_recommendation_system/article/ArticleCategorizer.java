package org.example.news_recommendation_system.article;

import java.util.*;

public class ArticleCategorizer {

    private static final Map<String, Set<String>> categoryKeywords = new HashMap<>();

    static {
        categoryKeywords.put("Health", new HashSet<>(Arrays.asList(
                "health", "medicine", "fitness", "wellness", "sick", "dengue", "treatment", "disease",
                "doctor", "clinic", "hospital", "symptoms", "cure", "vaccine", "nutrition", "exercise",
                "mental health", "immunity", "illness", "therapy", "chronic", "prevention", "recovery",
                "diabetes", "obesity", "cardiology", "blood pressure", "cancer", "weight loss", "surgery",
                "healthcare", "patients", "remedy", "medication", "health tips", "well-being", "physical therapy",
                "mental wellness", "pharmacy", "stress relief"
        )));

        categoryKeywords.put("Sports", new HashSet<>(Arrays.asList(
                "sports", "game", "team", "athlete", "match", "score", "competition", "player", "champion",
                "coach", "training", "stadium", "olympics", "football", "basketball", "soccer", "volleyball",
                "baseball", "rugby", "cricket", "swimming", "cycling", "hockey", "golf", "boxing", "wrestling",
                "tournament", "medal", "sportsmanship", "fitness", "running", "exercise", "competition", "goal",
                "endurance", "performance", "athletic", "track", "field"
        )));

        categoryKeywords.put("Politics", new HashSet<>(Arrays.asList(
                "government", "election", "politics", "policy", "president", "senate", "congress", "parliament",
                "voting", "campaign", "democracy", "reform", "law", "justice", "presidential", "governmental",
                "debate", "legislation", "policy", "voter", "politician", "party", "diplomacy", "corruption",
                "transparency", "national", "state", "representative", "electoral", "supreme court", "candidate",
                "manifesto", "constitution", "public policy", "governance", "rights", "liberties", "international relations"
        )));

        categoryKeywords.put("Comedy", new HashSet<>(Arrays.asList(
                "comedy", "funny", "humor", "joke", "laughter", "parody", "satire", "stand-up", "skit",
                "improv", "laugh", "pun", "comedian", "ridicule", "absurd", "irony", "meme", "hilarious",
                "quirky", "witty", "sarcasm", "prank", "entertainment", "comedic", "gag", "spoof", "fun",
                "silly", "laugh-out-loud", "punny", "funniest", "sketch", "roast", "humorous", "humorist",
                "laughs", "absurdity", "slapstick", "satirical"
        )));



        categoryKeywords.put("Religious", new HashSet<>(Arrays.asList(
                "religion", "faith", "spiritual", "church", "bible", "god", "prayer", "jesus", "christianity",
                "muslim", "hindu", "buddhism", "judaism", "worship", "temple", "holy", "devotion", "sacred",
                "belief", "religious", "godly", "morality", "sin", "atonement", "karma", "divine", "preacher",
                "holy scripture", "priest", "meditation", "soul", "sermon", "theology", "spirituality", "holy spirit",
                "rituals", "faithfulness", "believer", "prophet"
        )));

        categoryKeywords.put("Technology", new HashSet<>(Arrays.asList(
                "technology", "tech", "AI", "automation", "machine learning", "data", "software", "app", "innovation",
                "robotics", "future", "internet", "smart", "IoT", "gadgets", "cloud", "programming", "cybersecurity",
                "big data", "virtual reality", "augmented reality", "blockchain", "digital", "startup", "app development",
                "tech trends", "electronic", "computing", "computers", "database", "network", "AI research",
                "cloud computing", "5G", "smartphone", "wearables", "cyber attack", "quantum computing", "tech companies"
        )));
    }

    public String categorizeArticle(Map<String, Object> article) {
        String headline = ((String) article.get("headline")).toLowerCase();
        String description = ((String) article.get("short_description")).toLowerCase();

        for (Map.Entry<String, Set<String>> entry : categoryKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (headline.contains(keyword) || description.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }
        return "Uncategorized"; // Default if no keywords match
    }
}
