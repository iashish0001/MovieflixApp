package com.example.movie2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movie2.Model.MovieDetails;
import com.example.movie2.Model.MovieItems;
import com.example.movie2.Model.ResponseObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {
    private static final String TAG = "Utils";
    public static final String DATABASE_NAME = "fake_database";
    private Context context;
    private ArrayList<MovieItems> genreMovies;

    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    public Utils(Context context) {
        this.context = context;

    }

    public void initDataBase() {
        Log.d(TAG, "initDataBase: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<MovieItems>>() {
        }.getType(); //creating this type just to pass in gson
        ArrayList<MovieItems> possibleItems = gson.fromJson(sharedPreferences.getString("allItems", ""), type);

        initAllItems();


    }

    public void initAllItems() {
        Log.d(TAG, "initAllItems: created");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitClient retrofitClient = retrofit.create(RetrofitClient.class);
        Call<ResponseObject> getAllMovies = retrofitClient.getAllMovies();
        getAllMovies.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                Log.d(TAG, "onResponse: called onGettingAllMovies:" + response.code());
                ResponseObject responseObject = response.body();

                Gson gson = new Gson();

                SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                ArrayList<MovieItems> allItems = new ArrayList<>();
                allItems = responseObject.getResults();
                Log.d(TAG, "onResponse: called allitems" + allItems.toString());
                String finalString = gson.toJson(allItems);
                editor.putString("allItems", finalString);
                editor.commit();


            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.d(TAG, "onFailure: onGettingAllMovies : t" + t.getMessage());
            }
        });
        Call<ResponseObject> getTrendingMovies = retrofitClient.getTrendingMovies();
        getTrendingMovies.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                //Log.d(TAG, "onResponse: onGettingTrendingMovies :" + response.code());
                ResponseObject responseObject1 = response.body();

                Gson gson = new Gson();

                SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                ArrayList<MovieItems> allTrendingItems = new ArrayList<>();
                allTrendingItems = responseObject1.getResults();
                //Log.d(TAG, "onResponse: allTrendingMovies" + allTrendingItems.toString());
                String finalString1 = gson.toJson(allTrendingItems);
                editor.putString("allTrending", finalString1);
                editor.commit();


            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.d(TAG, "onFailure: onGettingTrendingMovies : t" + t.getMessage());
            }
        });
        Call<ResponseObject> getNewMovies = retrofitClient.getNewMovies();
        getNewMovies.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                Log.d(TAG, "onResponse: onGettingNewMovies :" + response.code());
                ResponseObject responseObject2 = response.body();

                Gson gson = new Gson();

                SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                ArrayList<MovieItems> allNewMovies = new ArrayList<>();
                allNewMovies = responseObject2.getResults();
                //Log.d(TAG, "onResponse: allNewMovies" + allNewMovies.toString());
                String finalString2 = gson.toJson(allNewMovies);
                editor.putString("allNewMovies", finalString2);
                editor.commit();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.d(TAG, "onFailure: onGettingNewMovies : t" + t.getMessage());
            }
        });


    }

    public ArrayList<MovieItems> getAllItems() {
        Log.d(TAG, "getAllItems: called");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<MovieItems>>() {
        }.getType();
        ArrayList<MovieItems> allItems = gson.fromJson(sharedPreferences.getString("allItems", null), type);
        //Log.d(TAG, "getAllItems: allItems"+ allItems.toString());
        return allItems;

    }


    public ArrayList<MovieItems> getNewItems() {
        Log.d(TAG, "getNewItems: called");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Log.d(TAG, "getNewItems: newItems ()" + sharedPreferences.getString("allNewMovies", null));
        Type type = new TypeToken<ArrayList<MovieItems>>() {
        }.getType();
        ArrayList<MovieItems> newItems = gson.fromJson(sharedPreferences.getString("allNewMovies", null), type);
        Log.d(TAG, "getNewItems: newItems" + sharedPreferences.getString("allNewMovies", null));
        return newItems;

    }

    public ArrayList<MovieItems> getTrendingItems() {
        Log.d(TAG, "getTrendingItems: called");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<MovieItems>>() {
        }.getType();
        ArrayList<MovieItems> trendingItems = gson.fromJson(sharedPreferences.getString("allTrending", null), type);
        //Log.d(TAG, "getTrendingItems: trendingItems" + trendingItems.toString());
        return trendingItems;

    }

    public void findMoviesDetails(final String id) {
        Log.d(TAG, "findMoviesDetails: called");

        final Gson gson = new Gson();
        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=12cd0a8a7f3fab830b272438df172ea8";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MovieDetails movieDetails = null;
                try {
                    movieDetails = gson.fromJson(response, MovieDetails.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: check " + movieDetails.toString());//24.673
                SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String finalString2 = gson.toJson(movieDetails);

                editor.putString("movieDetails", finalString2);
                editor.apply();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.start();

    }

    public MovieDetails getMovieDetails(String id) {
        Log.d(TAG, "getMovieDetails: called");
        findMoviesDetails(id);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<MovieDetails>() {
        }.getType();
        MovieDetails details = gson.fromJson(sharedPreferences.getString("movieDetails", null), MovieDetails.class);
        if (details != null) {
            Log.d(TAG, "getMovieDetails: movieDetails " + details.toString());
            return details;
        }
        return new MovieDetails();



    }

    public void searchMovies(String url) {
        Log.d(TAG, "searchMovies: called");

        final Gson gson = new Gson();
        final String str = url;

        //28%2C12

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ResponseObject responseObject = gson.fromJson(response, ResponseObject.class);
                Log.d(TAG, "onResponse called searchResults : responseObject" + responseObject.toString());

                SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                ArrayList<MovieItems> similarMovies = responseObject.getResults();
                String finalString2 = gson.toJson(similarMovies);
                editor.putString("searchResults" + str, finalString2);
                editor.commit();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.start();
    }

    public ArrayList<MovieItems> getSearchResults(String url) {
        Log.d(TAG, "getSearchResults: called");
        searchMovies(url);
        final String str = url;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<MovieItems>>() {
        }.getType();
        ArrayList<MovieItems> searchResults = gson.fromJson(sharedPreferences.getString("searchResults" + str, null), type);
        if (searchResults != null) {
            Log.d(TAG, "getSearchResults: called SearchResults" + searchResults.toString());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("searchResults" + str);
            editor.commit();
            return searchResults;
        } else {
            Log.d(TAG, "getSearchResults: SearchResults is null");
        }

        return new ArrayList<>();

    }

    public void setSignedIn(boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("signedIn", value);
        editor.commit();

    }

    public boolean isSignedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("signedIn", false);

    }
}
