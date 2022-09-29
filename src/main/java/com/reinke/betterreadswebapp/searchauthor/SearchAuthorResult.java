package com.reinke.betterreadswebapp.searchauthor;

import java.util.List;

class SearchAuthorResult {

    private int numFound;
    private List<AuthorWorkResult> docs;

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public List<AuthorWorkResult> getDocs() {
        return docs;
    }

    public void setDocs(List<AuthorWorkResult> docs) {
        this.docs = docs;
    }
}
