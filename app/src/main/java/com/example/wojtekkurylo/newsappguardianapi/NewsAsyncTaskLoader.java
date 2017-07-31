package com.example.wojtekkurylo.newsappguardianapi;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;


/**
 * Created by wojtekkurylo on 01.07.2017.
 */

public class NewsAsyncTaskLoader extends AsyncTaskLoader {

    /**
     * URL String Object from onLoadFinished() in MainActivity
     */
    private String mUrlString;

    private final String TAG = this.getClass().getSimpleName();

    public NewsAsyncTaskLoader(Context context, String urlString) {
        super(context);

        mUrlString = urlString;
    }

    /**
     * forceLoad(); is a required step to actually trigger the loadInBackground() method to execute.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This method runs on a background thread and performs the network request.
     * We should not update the UI from a background thread, so we return a list of
     * {@link News}s as the result.
     */
    @Override
    public List<News> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrlString == null) {
            return null;
        }
        // Create URL object
        URL url = createUrl(mUrlString);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Error in makeHttpRequest in loadInBackground", e);
        }

        // Take ArrayList from JsonParse method
        List<News> listWithNews = JsonParse.extractNews(jsonResponse);
        // return ArrayList to MainActivity onLoadFinished method
        return listWithNews;
    }

    private URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);

        } catch (MalformedURLException e) {
            Log.e("NewsAsyncTaskLoader", "Error in createUrl", e);
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If there is not url, return early
        if (url == null) {
            return jsonResponse;
        }

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            // check the HTTP response status code
            // If the request was successful(code 200)
            // read the input stream and parse response
            int httpResponseCode = urlConnection.getResponseCode();
            if (httpResponseCode == 200) {

                InputStream inputStream = urlConnection.getInputStream();
                try {
                    jsonResponse = readFromStream(inputStream);
                } catch (IOException e) {
                    Log.e(TAG, "Problem in makeHttpRequest method with readFromStream method", e);
                }

            } else {
                Log.e(TAG, "httpResponseCode:" + httpResponseCode);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error in makeHttpRequest", e);
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            try {
//                String line = reader.readLine();
//                while (line != null) {
//                    output.append(line); // dodajemy do naszego StringBuilder nowe linie, zamiast tworzyć line += line …
//                    line = reader.readLine();

                int length = inputStream.available();
                for (int i = 0; i <= length; i++) {
                    output.append(reader.readLine());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error with readLine in readFromStream ", e);
            }

        }

        return output.toString();
    }
}
