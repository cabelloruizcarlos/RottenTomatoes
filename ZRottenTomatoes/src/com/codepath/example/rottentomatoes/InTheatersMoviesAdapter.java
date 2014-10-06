package com.codepath.example.rottentomatoes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InTheatersMoviesAdapter extends ArrayAdapter<InTheatersMovie> {

	Context context;

	public InTheatersMoviesAdapter(Context context,
			ArrayList<InTheatersMovie> aMovies) {
		super(context, 0, aMovies);
		this.context = context;
	}

	public class ViewHolder {
		ImageView image;
		TextView title;
		TextView cast;
		LinearLayout card;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Card List
		ViewHolder holder;
		// Get the data item for this position
		InTheatersMovie movie = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(
					R.layout.adapter_item_movie, null);

			// Card List
			holder = new ViewHolder();
			holder.card = (LinearLayout) convertView.findViewById(R.id.card);
			holder.image = (ImageView) convertView
					.findViewById(R.id.ivPosterImage);
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.cast = (TextView) convertView.findViewById(R.id.tvCast);

			convertView.setTag(holder);
		} else
			// Card List
			holder = (ViewHolder) convertView.getTag();

		Picasso.with(getContext()).load(movie.getLargePosterUrl())
				.into(holder.image);
		holder.title.setText(movie.getTitle());
		holder.cast.setText(movie.getCastList());

		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.card_animation);
		holder.card.startAnimation(animation);
		return convertView;
	}
}
