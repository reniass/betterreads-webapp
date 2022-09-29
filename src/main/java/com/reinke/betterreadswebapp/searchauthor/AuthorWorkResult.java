package com.reinke.betterreadswebapp.searchauthor;

import java.util.List;

class AuthorWorkResult {

    private String title;
    private String key;
    private String cover_i;
    private List<String> author_key;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCover_i() {
        return cover_i;
    }

    public void setCover_i(String cover_i) {
        this.cover_i = cover_i;
    }

    public List<String> getAuthor_key() {
        return author_key;
    }

    public void setAuthor_key(List<String> author_key) {
        this.author_key = author_key;
    }
}
