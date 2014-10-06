package com.codepath.example.rottentomatoes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void boxOfficeClick(View v) {
		Intent i = new Intent(this, BoxOfficeActivity.class);
		startActivity(i);
	}

	public void inTheatersClick(View v) {
		Intent i = new Intent(this, InTheatersActivity.class);
		startActivity(i);
	}
}
