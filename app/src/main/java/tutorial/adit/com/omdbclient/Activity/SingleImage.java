/*
 * Copyright (c) 2019. Created By Adit Chauhan
 */

package tutorial.adit.com.omdbclient.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorial.adit.com.omdbclient.R;
import tutorial.adit.com.omdbclient.api.MovieApi;
import tutorial.adit.com.omdbclient.model.MovieDetails;


public class SingleImage extends AppCompatActivity {

    ImageView msingleView;

    public ProgressBar progressBar;
    int counter=0;
    String mUrl ;
    int size ;
    public  MovieDetails movieModel;

    TextView name, type, year, rated, rating, runtime, plot;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);

        mUrl = getIntent().getStringExtra("Image");
        Log.d("-------------------*******","--*****"+mUrl);

        name =(TextView)findViewById(R.id.moviename);
        type =(TextView)findViewById(R.id.genre);
        year =(TextView)findViewById(R.id.year);
        rated =(TextView)findViewById(R.id.ratedby);
        rating =(TextView)findViewById(R.id.rating);
        runtime =(TextView)findViewById(R.id.runtime);
        plot =(TextView)findViewById(R.id.plot);

        msingleView = (ImageView)findViewById(R.id.image_thumbnail);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        fetchMovieDetails();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    public void fetchMovieDetails()
    {
        MovieApi.Client.getInstance().getMovieDetails(mUrl).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                Log.d("-------------------*******","--"+response.raw().request().url());
                movieModel =response.body();
                doassign(movieModel);
               fetchImage(movieModel.getPoster());
                Log.d("here",movieModel.getPoster()+"");
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {


            }
        });


    }

    public void doassign(MovieDetails movie)
    {
        name.setText(movie.getTitle());
        type.setText(movie.getGenre());
        year.setText(movie.getYear());
        rated.setText(movie.getRated());
        rating.setText(movie.getImdbRating());
        runtime.setText(movie.getRuntime());
        plot.setText(movie.getPlot());


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void fetchImage(String imagePoster)
    {

        Glide.with(getApplicationContext())
                .load(imagePoster)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        msingleView.setBackgroundResource(R.drawable.noimage);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                         progressBar.setVisibility(View.GONE);
                        msingleView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(msingleView);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
