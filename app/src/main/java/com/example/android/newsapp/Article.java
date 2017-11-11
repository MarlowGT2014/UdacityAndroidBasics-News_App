package com.example.android.newsapp;

/**
 * Created by joshua on 7/4/17.
 */

public class Article {

    private String mTitle;
    private String mTag;
    private String mPublish;
    private String mURL;

    //Constructor
    public Article(String title, String tag, String publishDate, String URL) {
        mTitle = title;
        mTag = tag;
        mPublish = publishDate;
        mURL = URL;
    }

    //Get Title
    public String getTitle() { return mTitle; }

    //Get Author
    public String getTag() { return mTag; }

    //Get Published Date
    public String getDatePublished() { return mPublish; }

    //Get URL
    public String getURL() { return mURL; }

}
