package com.udacity.heather.popmoviesstage1final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_poster)ImageView mMoviePosterImageView;
    @BindView(R.id.tv_original_title) TextView mOriginalTitleTextView;
    @BindView(R.id.tv_movie_overview) TextView mOverviewTextView;
    @BindView(R.id.tv_movie_vote_average) TextView mAverageVoteTextView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;

    private Movie mMovie;

    private Menu mMenu;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mOriginalTitleTextView.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();

        if (intent.hasExtra("movie")) {

            mMovie = intent.getParcelableExtra("movie");

            String posterPathUri = mMovie.getPosterPathUri().toString();
            Picasso.with(mMoviePosterImageView.getContext())
                    .load(posterPathUri)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.movies_icon)
                    .into(mMoviePosterImageView);

            mOriginalTitleTextView.setText(mMovie.getTitle());
            mOverviewTextView.setText(mMovie.getOverview());

            String rating = String.valueOf(mMovie.getRating()) + "/10";
            mAverageVoteTextView.setText(rating);

            if (mMovie.getReleaseDate().length() >= 4) {
                String year = mMovie.getReleaseDate().substring(0, 4);
                mReleaseDateTextView.setText(year);
            } else {
                mReleaseDateTextView.setText("-");
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            /* This ID represents the Home or Up button. In the case of this
             activity, the Up button is shown. Use NavUtils to allow users
             to navigate up one level in the application structure.See the Navigation pattern
             on Android Design:
            http://developer.android.com/design/patterns/navigation.html#up-vs-back
           */
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}
