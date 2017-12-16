package sp.udaan.HelperClasses;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;

import sp.udaan.R;

/**
 * Created by Tejas on 19-08-2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    ArrayList<Feedback> feedbacks;
    Context context;

    public ReviewAdapter(ArrayList<Feedback> feedbacks,Context context)
    {
        this.feedbacks=feedbacks;
        this.context=context;
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reviewitem, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {

        //Perform operation here
        //Eg.
        Feedback f = feedbacks.get(position);
        holder.emailtext.setText(f.getEmail());
        holder.feedbacktext.setText(f.getFeedback());

        int rating=Integer.parseInt(f.getRating());

        if (rating>=0 && rating<=4)
        {
            holder.rating_smilerating.setSelectedSmile(rating);
        } else {
            holder.rating_smilerating.setSelectedSmile(4);
        }

    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView emailtext,ratingtext,feedbacktext;
        SmileRating rating_smilerating;
        CardView reviewcontainer;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            emailtext=(TextView)itemView.findViewById(R.id.review_email);
            feedbacktext=(TextView)itemView.findViewById(R.id.review_review);
            reviewcontainer=(CardView)itemView.findViewById(R.id.review_cardcontainer);
            rating_smilerating=(SmileRating)itemView.findViewById(R.id.review_rating_smilebar);
        }
    }
}
