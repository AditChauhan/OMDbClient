/*
 * Copyright (c) 2019. Created By Adit Chauhan
 */

package tutorial.adit.com.omdbclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import tutorial.adit.com.omdbclient.Activity.SingleImage;
import tutorial.adit.com.omdbclient.R;
import tutorial.adit.com.omdbclient.model.Search;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private static final String TAG = "staged";
    Context ctx;
    public  ArrayList<Search> movieList;
    private static RecyclerViewClickListener itemListener;

    public interface RecyclerViewClickListener
    {
        public void recyclerViewListClicked(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView moviename,yearReleased;
        public ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.image_thumbnail);
            moviename = (TextView) itemView.findViewById(R.id.moviename);
            yearReleased = (TextView) itemView.findViewById(R.id.yearreleased);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }

    }

    public ImageListAdapter(Context context) {
        Log.d(TAG,"ImageListAdapter");
        ctx = context;
        movieList = new ArrayList<>();
    }

    public ImageListAdapter()
    {
        movieList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(ctx).inflate(R.layout.image_list_item, parent, false);
        Log.d(TAG,"onCreateViewHolder");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ctx, SingleImage.class);
                i.putExtra("Image",movieList.get(position).getImdbID());
                ctx.startActivity(i);
                Log.d("Click","adressed");
            }
        });
        Search moviemodel = movieList.get(position);
        String imageUrl = "";
        imageUrl = moviemodel.getPoster();

        holder.moviename.setText(moviemodel.getTitle());
        holder.yearReleased.setText(moviemodel.getYear());
        holder.progressBar.setVisibility(View.VISIBLE);

      //  Log.d(TAG,"----****passing imageurl to glide" );
        Log.d("imageadapter",imageUrl+"");

        Glide.with(ctx).load(imageUrl)
                .thumbnail(0.5f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d("imageadapter","onloadfailed");
                        holder.progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.thumbnail.setVisibility(View.VISIBLE);
                        return false;
                    }

                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        Log.d("TAG","getItemCount :"+movieList.size());
        return movieList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addImages(List<Search> list){
            if(list != null){

                Log.d(TAG, "------**sent adapter" + list.size());
                movieList.addAll(list);
                Log.d(TAG, "----**now adapter size " + movieList.size());
                notifyDataSetChanged();
            }
            else
            {
                Log.d("stage", "" + movieList.size());
                movieList.clear();
                notifyDataSetChanged();

            }
    }



}
