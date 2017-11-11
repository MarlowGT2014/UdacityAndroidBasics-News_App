package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua on 7/4/17.
 */

public class ArticleActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>>  {

    private static final String LOG_TAG = ArticleActivity.class.getName();
    private TextView mNoArticles;
    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleAdapter mAdapter;

    //Generic URL to Query Guardian Page
    //sample: https://content.guardianapis.com/search?q=debates
    String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView articleListView = (ListView) findViewById(R.id.list);

        //Create Adapter
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        //Set the adapter on the {@link ListView} so it can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        //Start building URL?
        String apiKey = "&api-key=d945dd7b-7952-455f-87b2-78c84a85ceab";

        Bundle bundle = getIntent().getExtras();
        String userText = bundle.getString("userText");

        GUARDIAN_REQUEST_URL = GUARDIAN_REQUEST_URL + userText + apiKey;

        EditText userSearch = (EditText) findViewById(R.id.userSearch);
        userSearch.setHint("Results for: " + userText);

        mNoArticles = (TextView) findViewById(R.id.emptyView);
        articleListView.setEmptyView(mNoArticles);

        //Set item click listener on the ListView, which sends intent to the web browser to open
        //a website with more information about the selected article
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find current earthquake that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                //Convert the String URL into a URI object that is then passed to Intent Constructor
                Uri articleUri = Uri.parse(currentArticle.getURL());

                //Create a new intent to view the article Uri
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                //Send the intent to launch a new activity
                startActivity(websiteIntent);
            }

        });

        //Checking internet connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //Connection Present -> Load the URL
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        }
        else {
            View loadIndicator = findViewById(R.id.loadingIndicator);
            loadIndicator.setVisibility(View.GONE);

            TextView internetCheckNote = (TextView) findViewById(R.id.internetCheckNote);
            internetCheckNote.setText("Internet is not currently available.");
            Log.e(LOG_TAG, "Internet is not currently available.");
        }
    }


    //vvv Code for the Loader vvv
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        //Create a new loader for the given URL
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        View loadingIndicator = findViewById(R.id.loadingIndicator);
        loadingIndicator.setVisibility(View.GONE);

        mNoArticles.setText(R.string.noResults);

        //Clear the adapter of previous articles
        mAdapter.clear();

        //If there is a valid list of articles, add them to the adapter's data set
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        //Loader reset, so it can clear out the existing data
        mAdapter.clear();
    }

    //Submitting an updated search after the original search has been initiated
    public void submitSearch(View view) {
        EditText userSearch = (EditText) findViewById(R.id.userSearch);
        String userText = userSearch.getText().toString();

        Intent intent = new Intent(ArticleActivity.this, ArticleActivity.class);
        intent.putExtra("userText", userText);
        startActivity(intent);
    }

}
