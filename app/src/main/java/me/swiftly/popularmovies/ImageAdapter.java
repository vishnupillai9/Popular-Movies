package me.swiftly.popularmovies;

import android.content.Context;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vishnu on 28/02/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<TMDBMovie> movies;

    public ImageAdapter(Context c, List<TMDBMovie> m) {
        mContext = c;
        movies = m;
    }

    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TMDBMovie movie = (TMDBMovie) getItem(position);
        final MovieGridViewHolder viewHolder;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.grid_view_item, parent, false);

            viewHolder = new MovieGridViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MovieGridViewHolder) convertView.getTag();
        }

        String imageUrl = movie.posterPath;

        Picasso.with(mContext).load(imageUrl).into(viewHolder.posterImageView,
                PicassoPalette.with(imageUrl, viewHolder.posterImageView)
                        .use(PicassoPalette.Profile.VIBRANT)
                        .intoBackground(viewHolder.titleTextView, PicassoPalette.Swatch.RGB)
                        .intoTextColor(viewHolder.titleTextView, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                        .intoCallBack(new PicassoPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(Palette palette) {
                                viewHolder.titleTextView.setVisibility(View.VISIBLE);
                            }
                        })
        );

        viewHolder.titleTextView.setText(movie.title);

        return convertView;
    }

    static class MovieGridViewHolder {
        @Bind(R.id.main_poster_image_view) ImageView posterImageView;
        @Bind(R.id.main_title_text_view) TextView titleTextView;

        public MovieGridViewHolder(View view) {
            ButterKnife.bind(this, view);
            posterImageView.setAdjustViewBounds(true);
        }
    }
}
