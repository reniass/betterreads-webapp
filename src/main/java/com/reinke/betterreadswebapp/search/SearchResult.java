package com.reinke.betterreadswebapp.search;

import java.util.List;

public class SearchResult {

    private int numFound;
    private List<BookResult> docs;


    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public List<BookResult> getDocs() {
        return docs;
    }

    public void setDocs(List<BookResult> docs) {
        this.docs = docs;
    }
}
