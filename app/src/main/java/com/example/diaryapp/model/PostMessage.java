package com.example.diaryapp.model;

public class PostMessage implements Comparable {
     private String uid;
     private String title;
     private String content;
     private int color;
     private long timestamp;

    public PostMessage() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public PostMessage(String title, String content, int color, long timestamp) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.timestamp = timestamp;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int compareTo(Object o) {
        PostMessage postMessage = (PostMessage) o;
        if (this.timestamp > postMessage.getTimestamp()){
            return -1;
        }
        if (this.timestamp == postMessage.getTimestamp()){
            return 0;
        }
        return 1;
    }
}
