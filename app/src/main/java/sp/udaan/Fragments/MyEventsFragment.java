package sp.udaan.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sp.udaan.Activites.AddEvent;
import sp.udaan.Activites.EditEvent;
import sp.udaan.Activites.MyRegistrations;
import sp.udaan.HelperClasses.Event;
import sp.udaan.HelperClasses.MyEventAdapter;
import sp.udaan.HelperClasses.RecyclerItemClickListener;
import sp.udaan.Activites.MainActivity;
import sp.udaan.R;

public class MyEventsFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Event> mEvents ;
    SharedPreferences userInfo;
    String email;
    String key;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    private ValueEventListener mValueEventListener;


    public MyEventsFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events,container,false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("Events");

        userInfo = this.getActivity().getSharedPreferences("userInfo", Context.MODE_APPEND);
        //email = userInfo.getString("email","xyz@gmail.com");

        email = MainActivity.Email;

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclermyEvents);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setNestedScrollingEnabled(false);
        mEvents = new ArrayList<Event>();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        String x = userInfo.getString("type","Guest");
                        if(x.equals("Event Organiser") || x.equals("Supervisor") ) {
                            Event event = mEvents.get(position);
                            Intent i = new Intent(getContext(), MyRegistrations.class);
                            i.putExtra("name", event.getName());
                        /*i.putExtra("image",event.getPosterUrl());
                        i.putExtra("description", event.getDescription());
                        i.putExtra("venue", event.getVenue());
                        i.putExtra("time", event.getTime());
                        i.putExtra("registration", event.getFeeScheme());
                        i.putExtra("prizes", event.getPrizeScheme());
                        i.putExtra("contact1name", event.getPocName1());
                        i.putExtra("contact1no", event.getPocNumber1());
                        i.putExtra("contact2name", event.getPocName2());
                        i.putExtra("contact2no", event.getPocNumber2());*/

                            startActivity(i);
                        }

                        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            ImageView poster = (ImageView)view.findViewById(R.id.thumbnail);
                            poster.setTransitionName("poster");
                            Pair pair = new Pair<>(poster, ViewCompat.getTransitionName(poster));

                            ActivityOptionsCompat optionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),pair);
                            ActivityCompat.startActivity(getActivity(),i,optionsCompat.toBundle());


                        }
                        else{}
                        //getContext().startActivity(i);
                        */
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        if(userInfo.getString("type","Guest").equals("Event Organiser")){

                            Event event = mEvents.get(position);

                            Intent i = new Intent(getContext(), EditEvent.class);
                            i.putExtra("name",event.getName());
                            i.putExtra("venue", event.getVenue());
                            i.putExtra("time", event.getTime());
                            i.putExtra("registration", event.getFeeScheme());
                            i.putExtra("prizes", event.getPrizeScheme());
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getContext(),"Only organisers can edit events",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
        );

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.eventFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),AddEvent.class);
                startActivity(i);
            }
        });

        MyEventsFragment.FetchMyEventList fml = new MyEventsFragment.FetchMyEventList();
        fml.execute();


        return view;
    }


    public void updateUI(){
        final MyEventAdapter evl = new MyEventAdapter(mEvents,getContext());
        //final MyEventAdapter evl=new MyEventAdapter(mEvents,getContext());
        recyclerView.setAdapter(evl);
        Log.v("Recycler","Hello");
        recyclerView.scrollToPosition(0);
    }



    public class FetchMyEventList extends AsyncTask<Void,Void,ArrayList<Event>> {

        @Override
        protected void onPreExecute() {
            //bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mEvents.clear();
                    for(DataSnapshot snapshot2 : dataSnapshot.getChildren())
                    {   //Added one more foreach as Mega,Major and Tech is added

                        if(userInfo.getString("type","Guest").equals("Supervisor")){

                            for (DataSnapshot snapshot : snapshot2.getChildren()) {
                                String dname = (String) snapshot.child("name").getValue();
                                if (dname == null) {
                                    break;
                                }
                                String org = (String) snapshot.child("eventOrgMail").getValue();
                                String description = (String) snapshot.child("description").getValue();
                                String posterUrl = (String) snapshot.child("posterUrl").getValue();
                                String dates = (String) snapshot.child("dates").getValue();
                                String time = (String) snapshot.child("time").getValue();
                                String venue = (String) snapshot.child("venue").getValue();
                                String orgMail = (String) snapshot.child("eventOrgMail").getValue();
                                String pocName1 = (String) snapshot.child("pocName1").getValue();
                                String pocName2 = (String) snapshot.child("pocName2").getValue();
                                String pocNumber1 = (String) snapshot.child("pocNumber1").getValue();
                                String pocNumber2 = (String) snapshot.child("pocNumber2").getValue();
                                String prizeScheme = (String) snapshot.child("prizeScheme").getValue();
                                String fees = (String) snapshot.child("fees").getValue(); //Calculated per person

                                key = (String) snapshot.getKey();

                                mEvents.add(new Event(dname, description, posterUrl, dates, time, venue, orgMail, pocName1,
                                        pocName2, pocNumber1, pocNumber2, prizeScheme, fees));

                            }
                        }

                        else {
                            for (DataSnapshot snapshot : snapshot2.getChildren()) {
                                String dname = (String) snapshot.child("name").getValue();
                                if (dname == null) {
                                    break;
                                }
                                String org = (String) snapshot.child("eventOrgMail").getValue();
                                if (email.equals(org)) {
                                    String description = (String) snapshot.child("description").getValue();
                                    String posterUrl = (String) snapshot.child("posterUrl").getValue();
                                    String dates = (String) snapshot.child("dates").getValue();
                                    String time = (String) snapshot.child("time").getValue();
                                    String venue = (String) snapshot.child("venue").getValue();
                                    String orgMail = (String) snapshot.child("eventOrgMail").getValue();
                                    String pocName1 = (String) snapshot.child("pocName1").getValue();
                                    String pocName2 = (String) snapshot.child("pocName2").getValue();
                                    String pocNumber1 = (String) snapshot.child("pocNumber1").getValue();
                                    String pocNumber2 = (String) snapshot.child("pocNumber2").getValue();
                                    String prizeScheme = (String) snapshot.child("prizeScheme").getValue();
                                    String fees = (String) snapshot.child("fees").getValue(); //Calculated per person

                                    key = (String) snapshot.getKey();

                                    mEvents.add(new Event(dname, description, posterUrl, dates, time, venue, orgMail, pocName1,
                                            pocName2, pocNumber1, pocNumber2, prizeScheme, fees));
                                }
                            }
                        }
                    }
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mItemDatabaseReference.addValueEventListener(mValueEventListener);
            return null;
        }
    }



}