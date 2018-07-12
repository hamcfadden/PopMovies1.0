package com.udacity.heather.popmoviesstage1final;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;




public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ListItemClickListener {

    @BindView(R.id.rv_poster_grid)
    RecyclerView mPosterGridRecyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    private GridLayoutManager mGridLayoutManager;
    private MovieRecyclerViewAdapter mMovieAdapter;

    private ArrayList<Movie> mMovies;

    static final String STATE_SORT_CRITERIA = "SORT_CRITERIA";
    static final String STATE_SCROLL_INDEX = "SCROLL_INDEX";
    static final String STATE_SCROLL_TOP = "SCROLL_TOP";

    static final int SORT_POPULARITY = 0;
    static final int SORT_BEST_RATED = 1;
    private int mSortCriteria = SORT_POPULARITY;

    private static final int GRID_NUMBER_OF_COLUMNS_PORTRAIT = 2;
    private static final int GRID_NUMBER_OF_COLUMNS_LANDSCAPE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutManager = new GridLayoutManager(this, GRID_NUMBER_OF_COLUMNS_LANDSCAPE);
        } else {
            mGridLayoutManager = new GridLayoutManager(this, GRID_NUMBER_OF_COLUMNS_PORTRAIT);
        }

        mPosterGridRecyclerView.setLayoutManager(mGridLayoutManager);

        mMovies = new ArrayList<>();

        mMovieAdapter = new MovieRecyclerViewAdapter(mMovies, this);
        mPosterGridRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSortCriteria = prefs.getInt(STATE_SORT_CRITERIA, SORT_POPULARITY);

        fetchMovies();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putInt(STATE_SORT_CRITERIA, mSortCriteria);

        int index = mGridLayoutManager.findFirstVisibleItemPosition();
        View v = mPosterGridRecyclerView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - mPosterGridRecyclerView.getPaddingTop());
        edit.putInt(STATE_SCROLL_INDEX, index);
        edit.putInt(STATE_SCROLL_TOP, top);

        edit.apply();
    }

    private void setScrollPosition() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int index = prefs.getInt(STATE_SCROLL_INDEX, -1);
        int top = prefs.getInt(STATE_SCROLL_TOP, -1);
        if (index != -1) {
            mGridLayoutManager.scrollToPositionWithOffset(index, top);
        }
    }

    private void fetchMovies() {
        URL url;
        if (mSortCriteria == SORT_POPULARITY) {
            url = Utils.buildPopularMoviesQuery(this);
            new tmdbQueryTask().execute(url);
        } else if (mSortCriteria == SORT_BEST_RATED) {
            url = Utils.buildTopRatedMoviesUrl(this);
            new tmdbQueryTask().execute(url);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {


        if (clickedItemIndex >= mMovies.size()) {
            Toast.makeText(this, getString(R.string.movie_not_found), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        Movie movie = mMovies.get(clickedItemIndex);

        intent.putExtra("movie", movie);

        startActivity(intent);
    }

    public class tmdbQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = Utils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (searchResults != null && !searchResults.equals("")) {
                try {
                    JSONObject jsonResults = new JSONObject(searchResults);

                    JSONArray movieResults = jsonResults.getJSONArray("results");

                    mMovies.clear();

                    for (int i = 0; i < movieResults.length(); i++) {
                        JSONObject movieResult = movieResults.getJSONObject(i);

                        String id = movieResult.getString("id");

                        String title = movieResult.getString("title");
                        String posterPath = Utils.getImageBaseUrl() + movieResult.getString("poster_path");
                        String overview = movieResult.getString("overview");
                        double rating = movieResult.getDouble("vote_average");
                        int voteCount = movieResult.getInt("vote_count");
                        String releaseDate = movieResult.getString("release_date");

                        Movie movie = new Movie(id, title, Uri.parse(posterPath), overview, rating, voteCount, releaseDate);

                        mMovies.add(movie);
                    }

                    mMovieAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, getString(R.string.invalid_response), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.no_response), Toast.LENGTH_SHORT).show();
            }
            setScrollPosition();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_sort_popularity:
                mSortCriteria = SORT_POPULARITY;
                fetchMovies();
                return true;

            case R.id.action_sort_best_rated:
                mSortCriteria = SORT_BEST_RATED;
                fetchMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


   }
