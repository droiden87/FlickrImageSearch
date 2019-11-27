package com.scalefocus.flickr;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scalefocus.flickr.adapters.ImageAdapter;
import com.scalefocus.flickr.model.Photo;
import com.scalefocus.flickr.network.FlickrJsonParser;
import com.scalefocus.flickr.network.FlickrNetworkRequests;
import com.scalefocus.flickr.widget.AutoCompleteSearchView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>  {

    private static final int NUM_RESULT_IMAGES = 100;
    private static final int NUM_GRID_COLUMN = 3;
    private static final int FLICKR_PHOTO_SEARCH_LOADER = 11;
    private static final String FLICKR_PHOTO_SEARCH_URL = "search_url";

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageAdapter imgdapter;
    private RecyclerView recyclerImagesList;
    private TextView searchHintTextView;
    private ProgressBar loadingIndicator;
    private AutoCompleteSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        searchHintTextView = findViewById(R.id.tv_flickr_search_hint);
        loadingIndicator =  findViewById(R.id.pb_loading_indicator);
        recyclerImagesList =  findViewById(R.id.rv_images);

        recyclerImagesList.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_GRID_COLUMN);
        recyclerImagesList.setLayoutManager(layoutManager);
        imgdapter = new ImageAdapter();
        recyclerImagesList.setAdapter(imgdapter);

        getSupportLoaderManager().initLoader(FLICKR_PHOTO_SEARCH_LOADER, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (AutoCompleteSearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.saveKeywordsHistory(query);
                imgdapter.clear();
                makeFlickrSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }


    private void makeFlickrSearchQuery(String keyword) {
        URL flickrSearchURL = FlickrNetworkRequests.buildURLWithPhotoSearchQuery(keyword, NUM_RESULT_IMAGES);
        Bundle queryBundle = new Bundle();
        if (flickrSearchURL == null || flickrSearchURL.toString().equals("")) {
            Log.e(TAG, "makeFlickrSearchQuery: Empty flickrSearchURL");
            return;
        }
        queryBundle.putString(FLICKR_PHOTO_SEARCH_URL, flickrSearchURL.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> flickSearch = loaderManager.getLoader(FLICKR_PHOTO_SEARCH_LOADER);
        if (flickSearch == null) {
            loaderManager.initLoader(FLICKR_PHOTO_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(FLICKR_PHOTO_SEARCH_LOADER, queryBundle, this);
        }

    }

    @Override
    public  Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mResultCache = null;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                loadingIndicator.setVisibility(View.VISIBLE);
                searchHintTextView.setVisibility(View.INVISIBLE);
                if (mResultCache == null) {
                    forceLoad();
                } else {
                    deliverResult(mResultCache);
                }
            }

            @Override
            public String loadInBackground() {
                String searchURL = args.getString(FLICKR_PHOTO_SEARCH_URL);
                if (searchURL == null || searchURL.equals("")) {
                    return null;
                }
                try {
                    URL url = new URL(searchURL);
                    return FlickrNetworkRequests.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mResultCache = data;
                super.deliverResult(mResultCache);
            }
        };
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Error, please try again...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerImagesList.setVisibility(View.VISIBLE);
        searchHintTextView.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {
            try {
                Photo[] images = FlickrJsonParser.getPhotosFromJson(data);
                imgdapter.setImageListData(images);
                imgdapter.setFlickrJsonResults(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}

