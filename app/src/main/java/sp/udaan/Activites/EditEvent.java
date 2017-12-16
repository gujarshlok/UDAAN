package sp.udaan.Activites;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import sp.udaan.R;

public class EditEvent extends AppCompatActivity {

    EditText e1,e2,e3,e4;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        e1 = (EditText) findViewById(R.id.editTime);
        e2 = (EditText) findViewById(R.id.editVenue);
        e3 = (EditText) findViewById(R.id.editFee);
        e4 = (EditText) findViewById(R.id.editPrize);

        e1.setText(getIntent().getStringExtra("time"));
        e2.setText(getIntent().getStringExtra("venue"));
        e3.setText(getIntent().getStringExtra("registrations"));
        e4.setText(getIntent().getStringExtra("prices"));

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getIntent().getStringExtra("key");
                mDatabaseReference = mFirebaseDatabase.getReference().child("Events");
                name = getIntent().getStringExtra("name");

                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                            //Added one more foreach as Mega,Major and Tech is added


                            for (DataSnapshot snapshot : snapshot2.getChildren()) {
                                String dname = (String) snapshot.child("name").getValue();
                                if (dname.equals(name)) {
                                    Map<String,Object> taskMap = new HashMap<String,Object>();
                                    taskMap.put("time",e1.getText().toString());
                                    taskMap.put("venue",e2.getText().toString());
                                    taskMap.put("feeScheme",e3.getText().toString());
                                    taskMap.put("prizeScheme",e4.getText().toString());
                                    snapshot.getRef().updateChildren(taskMap);
                                    Toast.makeText(EditEvent.this,"Data Updated",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                finish();
            }
        });



    }

}

