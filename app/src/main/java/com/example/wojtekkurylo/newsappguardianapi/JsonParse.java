package com.example.wojtekkurylo.newsappguardianapi;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by wojtekkurylo on 01.07.2017.
 */

public final class JsonParse {

    private JsonParse() {
        throw new AssertionError("No JsonParse Instances for you!");
    }

    public static List<News> extractNews(String jsonResponse) {

        // If the JSON string is empty or null, then return early.
        if (jsonResponse.isEmpty()) {
            return null;
        }

        List<News> newsArray = new ArrayList<News>();
        try {

            JSONObject rootObject = new JSONObject(jsonResponse);

            JSONObject responseObject = rootObject.getJSONObject("response");

            JSONArray resultsArray = responseObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentObject = resultsArray.getJSONObject(i);

                String sectionName = currentObject.getString("sectionName");
                String title = currentObject.getString("webTitle");
                String url = currentObject.getString("webUrl");
                String date = currentObject.getString("webPublicationDate");
                // change the date format
                String dateUpdated = formatDate(date);
                Log.v("JsonParse","Date dateUpdated : " + dateUpdated);


                // Create News Object
                News news = new News(sectionName, title, dateUpdated, url);
                // Add created Objects to ArrayList<News> @ i-th position
                newsArray.add(i, news);
            }

        } catch (JSONException e) {
            // If any of above Object || Array isEmpty then print Log && exit without crashing
            Log.e("JsonParse", "Error in extractNews method in JsonParse Class", e);
        }

        return newsArray;
    }

    private static String formatDate(String dateOld){
        String dateNew = "";

        // 2014-02-17T12:05:47Z

        SimpleDateFormat sdfOld = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
        Date d = sdfOld.parse(dateOld);
            sdfOld.applyPattern("yyyy-MM-dd");
            dateNew = sdfOld.format(d);
        }catch (ParseException e){
            Log.e("JsonParse","Error",e);
        }

        return dateNew;
    }
}
