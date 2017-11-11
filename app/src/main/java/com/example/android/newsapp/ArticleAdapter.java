package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joshua on 7/4/17.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check for Reusable View
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item, parent, false);
        }

        //Find Article at Given Position
        Article currentArticle = getItem(position);

        //Find Article Name
        TextView titleView = (TextView) listItemView.findViewById(R.id.articleName);
        titleView.setText(currentArticle.getTitle());

        //Find Author Name
        TextView tagView = (TextView) listItemView.findViewById(R.id.tagName);
        tagView.setText(currentArticle.getTag());

        //Find Date Published
        TextView dateView = (TextView) listItemView.findViewById(R.id.datePublished);
        dateView.setText(currentArticle.getDatePublished());

        return listItemView;
    }

}
