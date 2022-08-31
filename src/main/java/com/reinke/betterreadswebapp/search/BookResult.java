package com.reinke.betterreadswebapp.search;

import java.util.List;

public class BookResult {

    private String key;
    private String title;
    private String first_publish_year;
    private String cover_i;
    private List<String> author_name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst_publish_year() {
        return first_publish_year;
    }

    public void setFirst_publish_year(String first_publish_year) {
        this.first_publish_year = first_publish_year;
    }

    public String getCover_i() {
        return cover_i;
    }

    public void setCover_i(String cover_i) {
        this.cover_i = cover_i;
    }

    public List<String> getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(List<String> author_name) {
        this.author_name = author_name;
    }

    @Override
    public String toString() {
        return "BookResult{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", first_publish_year='" + first_publish_year + '\'' +
                ", cover_i='" + cover_i + '\'' +
                ", author_name=" + author_name +
                '}';
    }
}
