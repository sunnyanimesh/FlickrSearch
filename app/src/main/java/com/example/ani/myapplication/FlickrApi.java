package com.example.ani.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class FlickrApi {

    public static final String FLICKRKEY ="cd4123dc8131a99ecb3b102027b09482";
    private static  final String URL ="https://api.flickr.com/services/rest/";
    public static final String FLICKR_SEARCH_METHOD ="flickr.photos.search";


    public static PostService postService =null;
    public static PostService getService(){
        if(postService ==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService = retrofit.create(PostService.class);
        }
        return postService;
    }

    public interface PostService{

        @GET("?")
        Call<Flickrsearch> getImages(@Query("method") String method, @Query("api_key") String api_key, @Query("text") String text, @Query("per_page") int per_page, @Query("page") int page, @Query("format") String format, @Query("nojsoncallback") int nojsoncallback);

    }

}
