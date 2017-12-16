package sp.udaan.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import sp.udaan.R;

public class CommitteeFragment
        extends Fragment{


    ImageView cp,vcp,tech,hoes,creative,admin,sec,hop,council;

    public CommitteeFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_committee2,container,false);

        cp = (ImageView)view.findViewById(R.id.mcp);
        vcp = (ImageView)view.findViewById(R.id.mvcp);
        tech = (ImageView)view.findViewById(R.id.mtech);
        hoes = (ImageView)view.findViewById(R.id.mhoes);
        creative = (ImageView)view.findViewById(R.id.mcreative);
        admin = (ImageView)view.findViewById(R.id.madmin);
        sec = (ImageView)view.findViewById(R.id.msecurity);
        hop = (ImageView)view.findViewById(R.id.mhop);
        council = (ImageView) view.findViewById(R.id.council);

        Picasso.with(getContext()).load(R.drawable.mcp).into(cp);
        Picasso.with(getContext()).load(R.drawable.matrixvcps).into(vcp);
        Picasso.with(getContext()).load(R.drawable.mtech).into(tech);
        Picasso.with(getContext()).load(R.drawable.mhoes).into(hoes);
        Picasso.with(getContext()).load(R.drawable.mcreative).into(creative);
        Picasso.with(getContext()).load(R.drawable.madmin).into(admin);
        Picasso.with(getContext()).load(R.drawable.msecurity).into(sec);
        Picasso.with(getContext()).load(R.drawable.mhospitality).into(hop);
        Picasso.with(getContext()).load(R.drawable.council).into(council);




        return view;
    }
}