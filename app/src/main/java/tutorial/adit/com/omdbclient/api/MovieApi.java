/*
 * Copyright (c) 2019. Created By Adit Chauhan
 */

package tutorial.adit.com.omdbclient.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tutorial.adit.com.omdbclient.model.MovieDetails;
import tutorial.adit.com.omdbclient.model.SearchResults;

public interface MovieApi {

//
//    public interface NetworkAPI {
//        @GET("/?apikey=475db1a5")
//        Observable<SearchResults> searchMovies(@Query("s") String query, @Query("type") String type, @Query("page") int page);
//        @GET("/?apikey=475db1a5")
//        Observable<MovieDetails> getMovieDetails(@Query("i") String imdbId);
//    }

    String BASE_URL = "http://www.omdbapi.com/";
    //String API_KEY = "3718fb6751f784124dfcc824555b4af83f19da0805b1b22977d31bacb14f036a";


    @GET("?apikey=f840b400")
    Call<SearchResults> searchMovies(@Query("s") String query, @Query("page") int page);

    @GET("?apikey=f840b400")
    Call<MovieDetails> getMovieDetails(@Query("i") String imdbId);



    class Client {
        private static MovieApi service;
        public static MovieApi getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(MovieApi.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
