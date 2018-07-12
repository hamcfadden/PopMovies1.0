package com.udacity.heather.popmoviesstage1final;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Utils {
  /* 1. To run this Popular Movies app you will need to create an account at https://www.themoviedb.org/account/signup
     2. Request an API key through your account at https://www.themoviedb.org/settings/api
     3. Copy and paste your API key into the the string below to replace "copy API Key here". */

        private static String API_KEY = "copy API key here";


        private static String API_KEY_PARAM = "api_key";

        private static String IMAGE_SMALL_SIZE = "w500";

        private static String BASE_URL = "http://api.themoviedb.org/3/movie/";
        static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

        private static String POPULAR_MOVIES_PATH = "popular";
        private static String TOP_RATED_MOVIES_PATH = "top_rated";

        public static URL buildPopularMoviesQuery (Context context){
            String baseUrl = BASE_URL + POPULAR_MOVIES_PATH;
            return buildUrl(baseUrl, context);
        }

        public static URL buildTopRatedMoviesUrl (Context context){
            String baseUrl = BASE_URL + TOP_RATED_MOVIES_PATH;
            return buildUrl(baseUrl, context);
        }
        private static URL buildUrl (String baseUrl, Context context){
            Uri builtUri = Uri.parse(baseUrl).buildUpon().appendQueryParameter(API_KEY_PARAM, API_KEY).build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return url;
        }
        public static String getImageBaseUrl () {
            return IMAGE_BASE_URL + IMAGE_SMALL_SIZE;
        }

        public static String getResponseFromHttpUrl (URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
    }










