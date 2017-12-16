package sp.udaan.HelperClasses;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sp.udaan.R;

/**
 * Created by USER on 30-08-2017.
 */

public class MainSponsorAdapter extends RecyclerView.Adapter<MainSponsorAdapter.CustomViewHolder> {

    ArrayList<MainSponsor> eventdetailslist=new ArrayList<MainSponsor>();
    Context context;

    public MainSponsorAdapter(ArrayList<MainSponsor> eventdetailslist,Context context)
    {
        this.eventdetailslist=eventdetailslist;
        this.context=context;
    }

    @Override
    public MainSponsorAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_thumbnail, parent, false);
        return new MainSponsorAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainSponsorAdapter.CustomViewHolder holder, int position) {

        holder.eventtitle.setText(eventdetailslist.get(position).getType());
        Glide.with(context).load(eventdetailslist.get(position).getPicUrl()).placeholder(R.drawable.download).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return eventdetailslist.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView eventtitle;
        ImageView poster;
        public CustomViewHolder(View itemView) {
            super(itemView);

            cardView=(CardView)itemView.findViewById(R.id.myaccount_cardview);
            eventtitle=(TextView)itemView.findViewById(R.id.event_title);
            poster=(ImageView)itemView.findViewById(R.id.thumbnail);
        }
    }
}
