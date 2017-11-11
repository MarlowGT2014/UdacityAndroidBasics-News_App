package com.example.android.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitSearch(View view) {
        EditText userSearch = (EditText) findViewById(R.id.userSearch);
        String userText = userSearch.getText().toString();

        Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
        intent.putExtra("userText", userText);
        startActivity(intent);
    }
}
