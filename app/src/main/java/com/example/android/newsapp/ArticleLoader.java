package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by joshua on 7/9/17.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private static final String LOG_TAG = ArticleLoader.class.getName();
    private String mURL;

    public ArticleLoader(Context context,String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (mURL == null) { return null; }

        //Perform Network Request
        List<Article> articles = QueryUtils.fetchArticleData(mURL);
        return articles;
    }

}
