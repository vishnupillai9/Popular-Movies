package me.swiftly.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vishnu on 23/04/16.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private Context mContext;
    List<TMDbReview> reviews;

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.review_author) TextView author;
        @Bind(R.id.review_content) TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ReviewsAdapter(Context c, List<TMDbReview> reviews) {
        mContext = c;
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_review, parent, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final TMDbReview review = reviews.get(position);

        holder.author.setText(review.author);
        holder.content.setText(review.content);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
