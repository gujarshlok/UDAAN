package sp.udaan.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import sp.udaan.HelperClasses.Event;
//import spit.matrix2017.HelperClasses.MatrixContentProvider;
import sp.udaan.HelperClasses.Registration;
import sp.udaan.HelperClasses.RegistrationAdapter;
import sp.udaan.R;

public class FavoritesFragment extends Fragment {

    RecyclerView mRecyclerView;
    private RegistrationAdapter mRegAdapter;
    ProgressBar pg;

    Set<String> name;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference1;
    private DatabaseReference mItemDatabaseReference2;
    private DatabaseReference mItemDatabaseReference3;
    private DatabaseReference mItemDatabaseReference4;
    private ValueEventListener mValueEventListener;

    SharedPreferences myReg;
    //Comment
    private ArrayList<Registration> mRegistration;

    public FavoritesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);

        pg = (ProgressBar) view.findViewById(R.id.mypg);
        myReg = getContext().getSharedPreferences("myReg", Context.MODE_PRIVATE);
        name = new HashSet<String>();
        name.addAll(myReg.getStringSet("myReg", name));

        //for(int i = 0; i < name.size(); i++){
        //  Toast.makeText(getContext(),name.iterator(),)
        //}

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference1 = mFirebaseDatabase.getReference().child("Events").child("Literary Arts");
        mItemDatabaseReference2 = mFirebaseDatabase.getReference().child("Events").child("Performing Arts");
        mItemDatabaseReference3 = mFirebaseDatabase.getReference().child("Events").child("Fun Events");
        mItemDatabaseReference4 = mFirebaseDatabase.getReference().child("Events").child("Featured");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragmentRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        mRegistration = new ArrayList<Registration>();


        FavoritesFragment.FetchMyRegList fmg = new FavoritesFragment.FetchMyRegList();
        fmg.execute();


        return view;
    }

    public void updateUI() {
        mRegAdapter = new RegistrationAdapter(mRegistration, getContext());
        mRecyclerView.setAdapter(mRegAdapter);
        mRecyclerView.scrollToPosition(0);
        if (mRegAdapter.getItemCount() == 0)
            getActivity().findViewById(R.id.no_regs_view).setVisibility(View.VISIBLE);
        else getActivity().findViewById(R.id.no_regs_view).setVisibility(View.GONE);
        pg.setVisibility(View.GONE);
    }


    public class FetchMyRegList extends AsyncTask<Void, Void, ArrayList<Event>> {

        @Override
        protected void onPreExecute() {
            //bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   //Added one more foreach as Mega,Major and Tech is added
                        String dname = (String) snapshot.child("name").getValue();
                        if (dname == null) {
                            break;
                        }
                        if (name.contains(dname)) {
                            String dates = (String) snapshot.child("dates").getValue();
                            String time = (String) snapshot.child("time").getValue();
                            String venue = (String) snapshot.child("venue").getValue();

                            mRegistration.add(new Registration(dname, dates, time + "," + venue));
                        }
                    }
                }
                //updateUI();


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mItemDatabaseReference1.addValueEventListener(mValueEventListener);


            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   //Added one more foreach as Mega,Major and Tech is added

                        String dname = (String) snapshot.child("name").getValue();
                        if (dname == null) {
                            break;
                        }
                        if (name.contains(dname)) {
                            String dates = (String) snapshot.child("dates").getValue();
                            String time = (String) snapshot.child("time").getValue();
                            String venue = (String) snapshot.child("venue").getValue();

                            mRegistration.add(new Registration(dname, dates, time + "," + venue));
                        }
                    }
                }
                //updateUI();


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mItemDatabaseReference2.addValueEventListener(mValueEventListener);


            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   //Added one more foreach as Mega,Major and Tech is added
                        String dname = (String) snapshot.child("name").getValue();
                        if (dname == null) {
                            break;
                        }
                        if (name.contains(dname)) {
                            String dates = (String) snapshot.child("dates").getValue();
                            String time = (String) snapshot.child("time").getValue();
                            String venue = (String) snapshot.child("venue").getValue();

                            mRegistration.add(new Registration(dname, dates, time + "," + venue));
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mItemDatabaseReference3.addValueEventListener(mValueEventListener);


            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {   //Added one more foreach as Mega,Major and Tech is added
                        String dname = (String) snapshot.child("name").getValue();
                        if (dname == null) {
                            break;
                        }
                        if (name.contains(dname)) {
                            String dates = (String) snapshot.child("dates").getValue();
                            String time = (String) snapshot.child("time").getValue();
                            String venue = (String) snapshot.child("venue").getValue();

                            mRegistration.add(new Registration(dname, dates, time + "," + venue));
                        }
                    }

                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mItemDatabaseReference4.addValueEventListener(mValueEventListener);

            return null;
        }
    }

}