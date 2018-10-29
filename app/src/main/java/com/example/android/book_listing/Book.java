package com.example.android.book_listing;

public class Book {

    private String mTitle;
    private String mAuthors;
    private String mUrl;


    public Book(String title, String authors) {
        mTitle = title;
        mAuthors = authors;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getUrl() {
        return mUrl;
    }
}
