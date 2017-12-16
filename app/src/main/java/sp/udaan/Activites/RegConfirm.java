package sp.udaan.Activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

import sp.udaan.HelperClasses.Registration;
import sp.udaan.R;

public class RegConfirm extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mPushDatabaseReference;
    private SharedPreferences sp, myReg;
    private SharedPreferences.Editor spa;

    private Button mButton;
    TextView eventname;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_confirm);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.5));

        mButton = (Button) findViewById(R.id.regButton);
        eventname = (TextView)findViewById(R.id.registerforevent_eventname);
        eventname.setText(getIntent().getStringExtra("name"));
        mButton.setClickable(false);

        sp = getSharedPreferences("userInfo", Context.MODE_APPEND);

        myReg = getSharedPreferences("myReg",Context.MODE_APPEND);
        spa = myReg.edit();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Registrations").child(getIntent().getStringExtra("name"));
        mPushDatabaseReference = mFirebaseDatabase.getReference().child("Registrations").child(getIntent().getStringExtra("name"));

        email = sp.getString("name",null);

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String x = (String) snapshot.child("name").getValue();
                    if(email.equals(x)){
                        Toast.makeText(RegConfirm.this,"Already registered!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                mButton.setClickable(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,email,from;
                name = sp.getString("name",null);
                email = sp.getString("phone","123456");
                from = sp.getString("from",null);

                mPushDatabaseReference.push().setValue(new Registration(name,email,from));
                Toast.makeText(RegConfirm.this,"Registered!!",Toast.LENGTH_SHORT).show();

                Set<String> str = new HashSet<String>();
                str.addAll(myReg.getStringSet("myReg",str));

                str.add(getIntent().getStringExtra("name"));
                spa.putStringSet("myReg",str);
                spa.commit();

                finish();
            }
        });
    }
}

