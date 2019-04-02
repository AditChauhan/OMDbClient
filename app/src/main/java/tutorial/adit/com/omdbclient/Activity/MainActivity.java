/*
 * Copyright (c) 2019. Created By Adit Chauhan
 */

package tutorial.adit.com.omdbclient.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorial.adit.com.omdbclient.R;
import tutorial.adit.com.omdbclient.adapter.ImageListAdapter;
import tutorial.adit.com.omdbclient.api.MovieApi;
import tutorial.adit.com.omdbclient.model.Search;
import tutorial.adit.com.omdbclient.model.SearchResults;

public class MainActivity extends AppCompatActivity {
	static final int universal_cache = 10;
	private static final String TAG = "stage";
	public String mquery ="";
	static int page = 1;
	int cache_size = 0;
	LinearLayout blankpage;
	RecyclerView imageRecyclerView;
	GridLayoutManager mLayoutManager;
	ImageListAdapter imageListAdapter;
	Context ctx;
	Boolean onscroll = false;
	boolean isSearch = false;
	boolean isLoading = false;
	static boolean isonline = false;
	MaterialSearchView searchView;
	public ProgressBar progressBar;
	FrameLayout frameLayout ;
	static String query_tracker = "" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx = this;
		 frameLayout = (FrameLayout)findViewById(R.id.toolbar_container) ;
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

		blankpage = (LinearLayout)findViewById(R.id.blankpage);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		searchView = (MaterialSearchView) findViewById(R.id.search_view);
		searchView.setVoiceSearch(false);
		imageRecyclerView = (RecyclerView) findViewById(R.id.images_recycler_view);
		mLayoutManager = new GridLayoutManager(ctx, 2);
		imageRecyclerView.setLayoutManager(mLayoutManager);
		imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
		imageListAdapter = new ImageListAdapter(ctx);
		imageRecyclerView.setAdapter(imageListAdapter);

		searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				mquery = query;

				if(query_tracker =="")
				{
					query_tracker = mquery;

				}
				else if(query_tracker != mquery)
				{
					imageListAdapter.movieList.clear();
				}
				progressBar.setVisibility(View.VISIBLE);
				downloadImages();
				searchView.closeSearch();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
			@Override
			public void onSearchViewShown() {
			}

			@Override
			public void onSearchViewClosed() {
			}
		});
		imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				onscroll = true ;
				if(!isLoading && ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() == imageListAdapter.getItemCount() - 3) {
					isLoading = true;
					page++;
					if(mquery != "") {
						Log.d("stage","mquery scroll");
						downloadImages();
					}

				}
			}
		});

	}

	public void downloadImages() {
		MovieApi.Client.getInstance().searchMovies(mquery, page).enqueue(new Callback<SearchResults>() {
			@Override
			public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
				progressBar.setVisibility(View.GONE);
				isonline = true;
				List<Search> mMovie = new ArrayList<>();
				mMovie =    response.body().getSearch();
				if(mMovie == null)
				{
					Snackbar snackbar = Snackbar
							.make(frameLayout, "List has no more movies matching", Snackbar.LENGTH_LONG);
					snackbar.show();
				}
				else {
					blankpage.setVisibility(View.GONE);
					imageListAdapter.addImages(mMovie);
					isLoading = false;
				}
			}
			@Override
			public void onFailure(Call<SearchResults> call, Throwable t) {
				Snackbar snackbar = Snackbar
						.make(frameLayout, "No Response, Check internet for connection", Snackbar.LENGTH_LONG);
				snackbar.show();
				t.printStackTrace();
				t.getMessage();
				Toast.makeText(getApplicationContext(),"kindly check the internet connection",Toast.LENGTH_LONG).show();
			}
		});
	}







	@Override
	public void onBackPressed() {
		if(searchView.isSearchOpen()) {

			searchView.closeSearch();
		}else
		{
			if(mquery =="")
			{
				super.onBackPressed();
			}
			else
			{
				Log.d("stage","what happedns");
				mquery = "";
				imageListAdapter.addImages(null);
				blankpage.setVisibility(View.VISIBLE);
			}
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		final MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView.setMenuItem(searchItem);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
			case R.id.span2:
				mLayoutManager.setSpanCount(2);
				return true;
			case R.id.span3:
				mLayoutManager.setSpanCount(3);
				return true;
			case R.id.span4:
				mLayoutManager.setSpanCount(4);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onStart() {

		//downloadImages();
		super.onStart();

	}

	@Override
	protected void onStop() {

		super.onStop();



	}

}
