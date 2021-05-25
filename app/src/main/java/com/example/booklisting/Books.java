package com.example.booklisting;

import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Books {
    private String mTitle;
    private String mAuthor;
    private Drawable mImage;
    String mWebReaderLink;

    public Books(String title, String author, Drawable image,String webReaderLink) {
        mTitle = title;
        mAuthor = author;
        mImage = image;
        mWebReaderLink=webReaderLink;
    }

    public String getBookTitle() {
        return mTitle;
    }

    public String getBookAuthor() {
        return mAuthor;
    }

    public Drawable getImage() {
        return mImage;
    }
    String getWebReaderLink(){
        return mWebReaderLink;
    }
}
