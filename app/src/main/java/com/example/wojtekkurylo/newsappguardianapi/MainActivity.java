package com.example.wojtekkurylo.newsappguardianapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.tag;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Basic URL
     */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    /**
     * Instance of NewsAdapter Custom Class
     */
    private NewsAdapter mNewsCustomAdapter;

    /**
     * Constant value for the News loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * NetworkInfo
     */
    //private NetworkInfo activeNetwork;

    //@BindView == ListView newsListView = (ListView) - automatically casting
    @BindView(R.id.list)
    ListView newsListView;
    @BindView(R.id.loading_spinner)
    ProgressBar spinnerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_internet)
    TextView noInternet;
    @BindView(R.id.no_news)
    TextView noNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife == findViewById() - performing the action for @BindViews
        ButterKnife.bind(MainActivity.this);

        // Instance required to create onItemClick Listener
        // newsList is empty (place holder) and will be replaced at the end of onFinishLoader method
        final List<News> newsList = new ArrayList<News>();

        // Create a new {@link newsCustomAdapter} of news
        mNewsCustomAdapter = new NewsAdapter(MainActivity.this, newsList);

        // Set the adapter on the {@link newsListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mNewsCustomAdapter);

        // handling Event Listener - using mNewsListView
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News clickedObject = newsList.get(position);
                String urlString = clickedObject.getLinkUrl();
                //Go to website
                Uri webpageUri = Uri.parse(urlString);
                //Implicit Intent
                Intent goWebsite = new Intent(Intent.ACTION_VIEW, webpageUri);

                //  check if there is an activity able to launch your intent before you try to launch it
                if (goWebsite.resolveActivity(getPackageManager()) != null) {
                    startActivity(goWebsite);
                }
            }
        });

        // Checking the internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Get a reference to the LoaderManager, in order to interact with loaders.
        final LoaderManager loaderManager = getSupportLoaderManager();

        // set the text to display in stead of ListView while no internet
        noInternet.setText(R.string.no_internet);

        if (isConnected) {
            noInternet.setVisibility(View.GONE);
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
        } else {
            mNewsCustomAdapter.clear();
            noInternet.setVisibility(View.VISIBLE);
            spinnerView.setVisibility(View.GONE);
        }
        // refresh the newsList on screen swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Checking the internet connection after Event
                ConnectivityManager cmSwipe = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkSwipe = cmSwipe.getActiveNetworkInfo();
                boolean isConnectedSwipe = activeNetworkSwipe != null && activeNetworkSwipe.isConnectedOrConnecting();

                if (isConnectedSwipe) {
                    mNewsCustomAdapter.clear();
                    noInternet.setVisibility(View.GONE);
                    spinnerView.setVisibility(View.VISIBLE);
                    loaderManager.restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                } else {
                    mNewsCustomAdapter.clear();
                    swipeRefreshLayout.setRefreshing(false);
                    noInternet.setVisibility(View.VISIBLE);
                    spinnerView.setVisibility(View.GONE);
                }
            }
        });

    }

    // method to send Long-time consuming Network Request from Main Thread to background thread
    // LoaderManager in charge og calling this method
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        String finalUrlRequest = uriConstructor(GUARDIAN_REQUEST_URL);

        Log.v("MainActivity", "URL result:" + finalUrlRequest);

        NewsAsyncTaskLoader instanceNews = new NewsAsyncTaskLoader(MainActivity.this, finalUrlRequest);
        return instanceNews;
    }

    // when onCreateLoader return the Loader<List<News>>, results are sent to onLoadFinish method
    // to display result on UI
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        swipeRefreshLayout.setRefreshing(false);
        spinnerView.setVisibility(View.GONE);
        mNewsCustomAdapter.clear();

        if (!data.isEmpty()) {
            // add the List at the last position of mNewsCustomAdapter constructor
            mNewsCustomAdapter.addAll(data);
        } else {
            noNews.setText(R.string.no_news);
        }
    }

    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsCustomAdapter.clear();
    }

    private String uriConstructor(String urlString) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String contentDefault = sharedPrefs.getString(
                getString(R.string.settings_content_key),
                getString(R.string.settings_content_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String userTag = sharedPrefs.getString(
                getString(R.string.settings_tag_key),
                getString(R.string.settings_tag_default)
        );

        Uri baseUri = Uri.parse(urlString);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String checkTag = "null";
        uriBuilder.appendQueryParameter("q", contentDefault);
        if (!userTag.equals(checkTag)) {
            uriBuilder.appendQueryParameter("tag", userTag);
            // technology, technology/android , education, politics , null
        }
        uriBuilder.appendQueryParameter("order-by", orderBy);
        // newest , oldest , relevance
        uriBuilder.appendQueryParameter("api-key", "test");

        return uriBuilder.toString();
    }

    // Inflate the menu, and respond when users click on our menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
