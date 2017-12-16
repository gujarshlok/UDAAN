/*
 * *
 *  * This file is part of Matrix2017
 *  * Created for the annual technical festival of Sardar Patel Institute of Technology
 *  *
 *  * The original contributors of the software include:
 *  * - Adnan Ansari (psyclone20)
 *  * - Tejas Bhitle (TejasBhitle)
 *  * - Mithil Gotarne (mithilgotarne)
 *  * - Rohit Nahata (rohitnahata)
 *  * - Akshay Shah (akshah1997)
 *  *
 *  * Matrix2017 is free software: you can redistribute it and/or modify
 *  * it under the terms of the MIT License as published by the Massachusetts Institute of Technology
*/

package sp.udaan.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import sp.udaan.Activites.EventDetails;
import sp.udaan.R;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Event> eventNames;
    private Event event;
    private ProgressBar progressBar;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventTitle;
        ImageView thumbnail;
        View background;
        CardView cardView;

        MyViewHolder(View view) {
            super(view);
            eventTitle = (TextView) view.findViewById(R.id.event_title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            background = view.findViewById(R.id.textView_background);
            cardView = (CardView) view.findViewById(R.id.myaccount_cardview);
            //progressBar  = (ProgressBar) view.findViewById(R.id.progressBar);
        }

        @Override
        public void onClick(View v) {
            int position  =   getAdapterPosition();
            event = eventNames.get(position);

            Intent i = new Intent(v.getContext(), EventDetails.class);

            i.putExtra("image",event.getPosterUrl());
            i.putExtra("name", event.getName());
            i.putExtra("description", event.getDescription());
            i.putExtra("venue", event.getVenue());
            i.putExtra("time", event.getTime());
            i.putExtra("registration", event.getFeeScheme());
            i.putExtra("prizes", event.getPrizeScheme());
            i.putExtra("contact1name", event.getPocName1());
            i.putExtra("contact1no", event.getPocNumber1());
            i.putExtra("contact2name", event.getPocName2());
            i.putExtra("contact2no", event.getPocNumber2());

            v.getContext().startActivity(i);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
               // ImageView poster = (ImageView)v.findViewById(R.id.thumbnail);
                //poster.setTransitionName("poster");
                //Pair pair = new Pair<>(poster, ViewCompat.getTransitionName(poster));

                //ActivityOptionsCompat optionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(v.getActivity(),pair);
                //ActivityCompat.startActivity(getActivity(),i,optionsCompat.toBundle());


            }
            else{//Unknown//
            }
        }
    }




    public EventListAdapter(Context mContext, List<Event> eventNames) {
        this.mContext = mContext;
        this.eventNames = eventNames;
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Event eventName = eventNames.get(position);
        holder.eventTitle.setText(eventName.getName());

        holder.thumbnail.setImageResource(R.mipmap.ic_launcher);
        if(eventName.getPosterUrl()!= "") {
            //Picasso.with(mContext).load(eventName.getPosterUrl()).placeholder(R.drawable.about_firebase).resize(400,400).centerCrop().into(holder.thumbnail);

            Glide.with(mContext).load(eventName.getPosterUrl())
            .placeholder(android.R.drawable.progress_horizontal)
            .listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    //progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    //progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).override(200,200).crossFade().centerCrop().into(holder.thumbnail);
        }
        //holder.thumbnail.setTag(eventName);
        holder.eventTitle.setText(eventName.getName());



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Card clicked","hello");
                Toast.makeText(v.getContext(),"MyEvent Clicked",Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return eventNames.size();
    }



}
