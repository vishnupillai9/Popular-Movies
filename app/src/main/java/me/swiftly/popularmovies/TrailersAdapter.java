package me.swiftly.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vishnu on 22/04/16.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private Context mContext;
    List<TMDbTrailer> trailers;

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.trailer_image) ImageView trailerImage;
        @Bind(R.id.trailer_description) TextView trailerDescription;

        TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    TrailersAdapter(Context c, List<TMDbTrailer> trailers) {
        mContext = c;
        this.trailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_trailer, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        final TMDbTrailer trailer = trailers.get(position);

        Picasso.with(mContext).load(TMDbHelper.buildImageUrlForTrailer(trailer.key)).into(holder.trailerImage);
        holder.trailerDescription.setText(trailer.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TMDbHelper.playTrailer(mContext, trailer.key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}