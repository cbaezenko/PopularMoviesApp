package com.example.baeza.popularmoviesapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by baeza on 16.02.2018.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIE_URL =
            "https://api.themoviedb.org/3/movie/";

    private static final String IMAGE_MOVIE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_BASE_URL = STATIC_MOVIE_URL;

    private static final String MOVIE_POPULAR = "popular";
    private static final String MOVIE_TOP_RATED = "top_rated";

    private static final String API_KEY_FORMAT = "?api_key=";


    private static final String RECOMMENDED_SIZE_MOBILE = "w185/";
    private static final String format = "json";

    private static String URL_BASE_FOR_IMAGE_MOVIE = IMAGE_MOVIE_BASE_URL + RECOMMENDED_SIZE_MOBILE;


    public static URL buildUrl(String path, String key){
        if(path.equals("popular")) {
            path = MOVIE_POPULAR;
            }
        else if (path.equals("top_rated")){
            path = MOVIE_TOP_RATED;
        }

        Uri builtUir = Uri.parse(MOVIE_BASE_URL +
                path +
                API_KEY_FORMAT + key)
                .buildUpon()
                .build();

        URL url = null;
        try{
            url = new URL(builtUir.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d(TAG, "Built URI " + url);

        return url;
    }

    public static String getUrlBaseForImageMovie() {
        return URL_BASE_FOR_IMAGE_MOVIE;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
