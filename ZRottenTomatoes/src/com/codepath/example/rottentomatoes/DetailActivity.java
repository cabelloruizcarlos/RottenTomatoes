package com.codepath.example.rottentomatoes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends Activity {
	private ImageView ivPosterImage;
	private TextView tvTitle;
	private TextView tvSynopsis;
	private TextView tvCast;
	private TextView tvAudienceScore;
	private TextView tvCriticsScore;
	private TextView tvCriticsConsensus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		// Fetch views
		ivPosterImage = (ImageView) findViewById(R.id.ivPosterImage);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvSynopsis = (TextView) findViewById(R.id.tvSynopsis);
		tvCast = (TextView) findViewById(R.id.tvCast);
		tvCriticsConsensus = (TextView) findViewById(R.id.tvCriticsConsensus);
		tvAudienceScore =  (TextView) findViewById(R.id.tvAudienceScore);
		tvCriticsScore = (TextView) findViewById(R.id.tvCriticsScore);
		// Load movie data
		Bundle intent = getIntent().getExtras();
		int loadAPI = intent.getInt("api", 0);
		
		switch (loadAPI){
		
		case 1:
			BoxOfficeMovie movie = (BoxOfficeMovie) getIntent().getSerializableExtra(BoxOfficeActivity.MOVIE_DETAIL_KEY);
			loadMovie(movie);
			break;
		case 2:
			InTheatersMovie movie1 = (InTheatersMovie) getIntent().getSerializableExtra(InTheatersActivity.MOVIE_DETAIL_KEY);
			loadMovie(movie1);
			break;
		default:
			break;
		}
		
		
		
	}
	
	// Populate the data for the movie
	@SuppressLint("NewApi")
	public void loadMovie(BoxOfficeMovie movie) {
		if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
		    getActionBar().setTitle(movie.getTitle());
		}
		// Populate data
		tvTitle.setText(movie.getTitle());
		tvCriticsScore.setText(Html.fromHtml("<b>Critics Score:</b> " + movie.getCriticsScore() + "%"));
		tvAudienceScore.setText(Html.fromHtml("<b>Audience Score:</b> " + movie.getAudienceScore() + "%"));
		tvCast.setText(movie.getCastList());
		tvSynopsis.setText(Html.fromHtml("<b>Synopsis:</b> " + movie.getSynopsis()));
		tvCriticsConsensus.setText(Html.fromHtml("<b>Consensus:</b> " + movie.getCriticsConsensus()));
		// R.drawable.large_movie_poster from 
        // http://content8.flixster.com/movie/11/15/86/11158674_pro.jpg -->
		Picasso.with(this).load(movie.getLargePosterUrl()).
		    placeholder(R.drawable.large_movie_poster).
		    into(ivPosterImage);
	}
	public void loadMovie(InTheatersMovie movie) {
		if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
		    getActionBar().setTitle(movie.getTitle());
		}
		// Populate data
		tvTitle.setText(movie.getTitle());
		tvCriticsScore.setText(Html.fromHtml("<b>Critics Score:</b> " + movie.getCriticsScore() + "%"));
		tvAudienceScore.setText(Html.fromHtml("<b>Audience Score:</b> " + movie.getAudienceScore() + "%"));
		tvCast.setText(movie.getCastList());
		tvSynopsis.setText(Html.fromHtml("<b>Synopsis:</b> " + movie.getSynopsis()));
		tvCriticsConsensus.setText(Html.fromHtml("<b>Consensus:</b> " + movie.getCriticsConsensus()));
		// R.drawable.large_movie_poster from 
        // http://content8.flixster.com/movie/11/15/86/11158674_pro.jpg -->
		Picasso.with(this).load(movie.getLargePosterUrl()).
		    placeholder(R.drawable.large_movie_poster).
		    into(ivPosterImage);
	}
}
