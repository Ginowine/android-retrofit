package app.movie.tutorial.com.rest;

import app.movie.tutorial.com.Config;
import app.movie.tutorial.com.model.configuration.ConfigurationResponse;
import app.movie.tutorial.com.model.movie.MovieResponse;
import app.movie.tutorial.com.model.movie.images.ImagesResponse;

import app.movie.tutorial.com.model.movies.MoviesResponse;
import app.movie.tutorial.com.model.search.SearchMovieResponse;
import retrofit2.Call;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class API {

    public interface Movies {
        //MOVIE SEARCH AUTOCOMPLETE
        @GET("/search/movie")
        Call<SearchMovieResponse> search(@Query("api_key") String apiKey, @Query("query") String query);

        //TOP RATED MOVIES
        @GET("movie/top_rated")
        Call<MoviesResponse> topRated(@Query("api_key") String apiKey);

        //MOVIE DETAIL
        @GET("/3/movie/{id}")
        Call<MovieResponse> movieDetails(@Path("id") int movieID, @Query("api_key") String apiKey);

        //MOVIE IMAGES
        @GET("/movie/{id}/images")
        Call<ImagesResponse> movieImages(@Query("api_key") String apiKey, @Path("id") int movieID);

    }

    public interface Configurations {
        //CONFIGURATIONS
        @GET("/configuration")
        Call<ConfigurationResponse> configurations(@Query("api_key") String apiKey);
    }

    private static <T> T builder(Class<T> endpoint) {
        return new Retrofit.Builder()
                .baseUrl(Config.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(endpoint);
    }

    public static Movies movies() {
        return builder(Movies.class);
    }

    public static Configurations configurations() {
        return builder(Configurations.class);
    }

}
