package com.intelmix.newzrobot.android.data;

/**
 * Created by mahdi on 12/8/15.
 */
public class NewsItem {
    private String link;
    private String title;
    private String source;
    private long timestamp;

    public NewsItem() {
    }

    public NewsItem(String link, String title, String source, long timestamp) {
        this.link = link;
        this.title = title;
        this.source = source;
        this.timestamp = timestamp;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
