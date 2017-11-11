package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua on 7/9/17.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    //Constructor
    private QueryUtils() {

    }

    //Query the Guardian Webpage
    public static List<Article> fetchArticleData(String requestUrl) {
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform the HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP Request", e);
        }

        //Extract the requested fields from the JSON response and create a list of articles
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        //Return the list of articles
        return articles;
    }

    //Return the URL object from the given string url
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    //Make an HTTP Request to the given URL and return String as a response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Guardian JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Read from the Stream
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Extract the Features from the JSON File
    private static List<Article> extractFeatureFromJson(String articleJSON) {
        //Return early if the JSON string is empty
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        //Create an empty array to add articles
        List<Article> articles = new ArrayList<>();

        //Attempt to parse the JSON
        try {
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            //Extract the JSON associated with the keys
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = responseObject.getJSONArray("results");

            //For each article item in the array, create a link object
            for (int i = 0; i < resultsArray.length(); i++) {
                //Get a single article position i within the list of articles
                JSONObject articleInfo = resultsArray.getJSONObject(i);

                //Extract the Article Title
                String title = articleInfo.getString("webTitle");

                //Extract the Tag/Category
                String tag = articleInfo.getString("sectionName");
                tag = "Tag: " + tag;

                //Extract the Date Published
                String datePublished = articleInfo.getString("webPublicationDate");

                String[] parts = datePublished.split("T");
                String date = parts[0];
                String time = parts[1];

                String cutTime = time.substring(0, (time.length() - 4));
                int subTime = Integer.valueOf(cutTime.substring(0,1));

                StringBuilder formattedDate = new StringBuilder();
                formattedDate.append(date.substring(5,7) + "/" + date.substring(8,10) + "/" + date.substring(0,4));

                if (subTime > 12) {
                    subTime = subTime - 12;
                    time = Integer.toString(subTime) + cutTime.substring(2, cutTime.length()-1) + " PM";
                }
                else {
                    time = cutTime + " AM";
                }

                String dateTimeSignature = "Date: " + formattedDate.toString() + "\nTime: " + time;

                //Extract the article link URL
                String url = articleInfo.getString("webUrl");

                Article article = new Article(title, tag, dateTimeSignature, url);
                articles.add(article);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results.", e);
        }

        //Return the list of articles
        return articles;
    }
}
