package com.udacity.heather.popmoviesstage1final;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private ArrayList<Movie> mMovies;

    private final ListItemClickListener mItemClickListener;

    public interface ListItemClickListener {

        void onListItemClick(int clickedPosition);
    }

    MovieRecyclerViewAdapter(ArrayList<Movie> movies, ListItemClickListener listener) {
        mMovies = movies;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movieItem = mMovies.get(position);
        holder.bindMovie(movieItem);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private ImageView mImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

           mImageView = itemView.findViewById(R.id.iv_movie_poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mItemClickListener.onListItemClick(clickedPosition);
        }

        void bindMovie(Movie movieItem) {
                    Picasso.with(mImageView.getContext())
                            .load(movieItem.getPosterPathUri())
                            .fit()
                            .placeholder(R.drawable.movies_icon)
                            .into(mImageView);


        }
    }
    }




