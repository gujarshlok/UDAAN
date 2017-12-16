package sp.udaan.Activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import sp.udaan.HelperClasses.Event;
import sp.udaan.HelperClasses.RecyclerItemClickListener;
import sp.udaan.HelperClasses.Registration;
import sp.udaan.HelperClasses.RegistrationAdapter;
import sp.udaan.R;

public class MyRegistrations extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RegistrationAdapter mRegAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    private ValueEventListener mValueEventListener;
    //Comment
    private ArrayList<Registration> mRegistration;
    private ProgressBar pg;
    private String toDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_registrations);

        pg = (ProgressBar) findViewById(R.id.myRegPg);
        mRecyclerView = (RecyclerView) findViewById(R.id.regRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("Registrations").child(getIntent().getStringExtra("name"));
        mRegistration = new ArrayList<Registration>();


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MyRegistrations.this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Nothing to do
                    }

                    @Override public void onLongItemClick(View view, final int position) {
                        // do whatever
                        new AlertDialog.Builder(MyRegistrations.this)
                                .setPositiveButton("Delete",new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog,int which){
                                        toDelete = mRegistration.get(position).getName();
                                        MyRegistrations.DeleteReg frl = new MyRegistrations.DeleteReg();
                                        frl.execute();


                                        Uri sms_uri = Uri.parse("smsto:" + mRegistration.get(position).getEmail());
                                        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                                        sms_intent.putExtra("sms_body","Dear " + toDelete + ",\n\tWe are forced to cancel your registration due to unforeseeable issues. Please contact the respected people for the event for further information.\n\n Regards\nTeam " + getIntent().getStringExtra("name") );
                                        startActivity(sms_intent);

                                    }
                                })
                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog,int which){
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                })
        );

        MyRegistrations.FetchRegList frl = new MyRegistrations.FetchRegList();
        frl.execute();

    }

    public class FetchRegList extends AsyncTask<Void,Void,ArrayList<Event>> {

        @Override
        protected void onPreExecute() {
            //bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mRegistration.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String namex = (String) snapshot.child("email").getValue();
                        if(namex == null) {
                            break;
                        }
                        String email = (String) snapshot.child("email").getValue();
                        String name = (String) snapshot.child("name").getValue();
                        String from = (String) snapshot.child("from").getValue();
                        mRegistration.add(new Registration(name,email,from));

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


    public class DeleteReg extends AsyncTask<Void,Void,ArrayList<Event>> {

        @Override
        protected void onPreExecute() {
            //bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String namex = (String) snapshot.child("name").getValue();
                        if(toDelete.equals(namex)){
                            snapshot.getRef().removeValue();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mItemDatabaseReference.addValueEventListener(mValueEventListener);
            return null;
        }
    }

    public void updateUI(){
        Collections.reverse(mRegistration);
        mRegAdapter = new RegistrationAdapter(mRegistration,MyRegistrations.this);
        mRecyclerView.setAdapter(mRegAdapter);
        mRecyclerView.scrollToPosition(0);
        pg.setVisibility(View.GONE);
    }
}

