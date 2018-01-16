package sp.udaan.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sp.udaan.HelperClasses.LeaderboardAdapter;
import sp.udaan.HelperClasses.LeaderboardUser;
import sp.udaan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardFragment extends Fragment {


    public LeaderboardFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        /*recyclerView=(RecyclerView)view.findViewById(R.id.leaderboard_recycler);

        ArrayList<LeaderboardUser> leaderboardUsers=new ArrayList<>();
        leaderboardUsers.add(new LeaderboardUser("1","Tejas","t","3"));
        leaderboardUsers.add(new LeaderboardUser("2","Shlok","s","2"));
        leaderboardUsers.add(new LeaderboardUser("3","Gujar","sg","1"));


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new LeaderboardAdapter(leaderboardUsers,getContext()));*/

        return view;
    }

}
