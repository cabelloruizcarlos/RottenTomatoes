package com.codepath.example.rottentomatoes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.codepath.example.rottentomatoes.util.AlphaForegroundColorSpan;
import com.codepath.example.rottentomatoes.view.KenBurnsView;
import com.loopj.android.http.JsonHttpResponseHandler;

public class BoxOfficeActivity extends Activity {

	private ListView lvMovies;
	private BoxOfficeMoviesAdapter adapterMovies;
	private RottenTomatoesClient client;
	public static final String MOVIE_DETAIL_KEY = "movie";
	public static final String API_DETAIL_KEY = "api";

	// Fancy action bar

	private int mActionBarTitleColor;
	private int mActionBarHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;

	private KenBurnsView mHeaderPicture;
	private ImageView mHeaderLogo;
	private View mHeader;
	private View mPlaceHolderView;
	private AccelerateDecelerateInterpolator mSmoothInterpolator;

	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();

	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableString;
	private SpannableString mSpannableString2;

	private TypedValue mTypedValue = new TypedValue();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		lvMovies = (ListView) findViewById(R.id.lvMovies);
		// Fancy action bar

		mSmoothInterpolator = new AccelerateDecelerateInterpolator();
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.header_height);
		mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
		mHeader = findViewById(R.id.header);
		mHeaderPicture = (KenBurnsView) findViewById(R.id.header_picture);
		mHeaderPicture.setResourceIds(R.drawable.logo);

		mActionBarTitleColor = getResources().getColor(R.color.white);
		mSpannableString = new SpannableString(getString(R.string.box_office_title));
		mSpannableString2 = new SpannableString("http://www.rottentomatoes.com");
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(
				mActionBarTitleColor);
		mPlaceHolderView = getLayoutInflater().inflate(
				R.layout.view_header_placeholder, lvMovies, false);
		lvMovies.addHeaderView(mPlaceHolderView);

		ArrayList<BoxOfficeMovie> aMovies = new ArrayList<BoxOfficeMovie>();

		adapterMovies = new BoxOfficeMoviesAdapter(this, aMovies);

		lvMovies.setAdapter(adapterMovies);
		// Fetch the data remotely
		fetchBoxOfficeMovies();
		setupMovieSelectedListener();

		// Fancy actino bar
		lvMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int scrollY = getScrollY();
				// Sticky actionbar
				mHeader.setTranslationY(Math.max(-scrollY,
						mMinHeaderTranslation));
				// Header_logo --> actionbar icon
				float ratio = clamp(mHeader.getTranslationY()
						/ mMinHeaderTranslation, 0.0f, 1.0f);
				interpolate(mHeaderLogo, getActionBarIconView(),
						mSmoothInterpolator.getInterpolation(ratio));
				setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
			}
		});
	}

	private void fetchBoxOfficeMovies() {
		client = new RottenTomatoesClient();
		client.getBoxOfficeMovies(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int code, JSONObject body) {
				JSONArray items = null;
				try {
					// Get the movies json array
					items = body.getJSONArray("movies");
					// Parse json array into array of model objects
					ArrayList<BoxOfficeMovie> movies = BoxOfficeMovie
							.fromJson(items);
					// Load model objects into the adapter which displays them
					adapterMovies.addAll(movies);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setupMovieSelectedListener() {
		lvMovies.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View item,
					int position, long rowId) {
				Intent i = new Intent(BoxOfficeActivity.this,
						DetailActivity.class);

				i.putExtra(MOVIE_DETAIL_KEY, adapterMovies.getItem((int)rowId));
				i.putExtra(API_DETAIL_KEY, 1);
				startActivity(i);
			}
		});
	}

	/** Everything below this is for the fancy action bar. **/
	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableString2.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		getActionBar().setTitle(mSpannableString);
		getActionBar().setSubtitle(mSpannableString2);

	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	private void interpolate(View view1, View view2, float interpolation) {
		getOnScreenRect(mRect1, view1);
		getOnScreenRect(mRect2, view2);

		float scaleX = 1.0F + interpolation
				* (mRect2.width() / mRect1.width() - 1.0F);
		float scaleY = 1.0F + interpolation
				* (mRect2.height() / mRect1.height() - 1.0F);
		float translationX = 0.5F * (interpolation * (mRect2.left
				+ mRect2.right - mRect1.left - mRect1.right));
		float translationY = 0.5F * (interpolation * (mRect2.top
				+ mRect2.bottom - mRect1.top - mRect1.bottom));

		view1.setTranslationX(translationX);
		view1.setTranslationY(translationY - mHeader.getTranslationY());
		view1.setScaleX(scaleX);
		view1.setScaleY(scaleY);
	}

	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
		return rect;
	}

	public int getScrollY() {
		View c = lvMovies.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = lvMovies.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mPlaceHolderView.getHeight();
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	private void setupActionBar() {

		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_transparent);
	}

	private ImageView getActionBarIconView() {

		return (ImageView) findViewById(android.R.id.home);
	}

	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}

		getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
				true);
		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());
		return mActionBarHeight;
	}
}
