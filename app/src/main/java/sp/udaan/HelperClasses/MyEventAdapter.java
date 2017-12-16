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
 * Created by Tejas on 16-08-2017.
 */

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.CustomViewHolder>{

    ArrayList<Event> eventdetailslist=new ArrayList<Event>();
    Context context;

    public MyEventAdapter(ArrayList<Event> eventdetailslist,Context context)
    {
        this.eventdetailslist=eventdetailslist;
        this.context=context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_thumbnail, parent, false);
        return new MyEventAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.eventtitle.setText(eventdetailslist.get(position).getName());
        Glide.with(context).load(eventdetailslist.get(position).getPosterUrl()).placeholder(R.drawable.download).override(400,400).into(holder.poster);
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
