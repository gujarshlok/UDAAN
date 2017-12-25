package sp.udaan.Activites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sp.udaan.HelperClasses.User;
import sp.udaan.R;


public class LoginPage extends AppCompatActivity {


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mPushDatabaseReference;

    int i = 1;
    String name,email,profile,uid,type,phoneNo;

    SharedPreferences.Editor sp,spa,shouldMapEditor;
    SharedPreferences userInfo;
    SharedPreferences firstTime;
    SharedPreferences shouldMap;

    TextView uname,uemail,utype;
    TextInputEditText uclass,ucontact;
    ImageView uprofile;
    AppCompatSpinner spinner;
    Button b;
    String x,y,z;

    private ArrayList admin, eventOrg;


    String fixedFrom;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        admin = new ArrayList();
        //admin.add("aditya.bhave41@gmail.com");
        admin.add("gujarshlok@gmail.com");
        admin.add("sushmenkc179@gmail.com");
        admin.add("riya.bakhtiani@gmail.com");
        admin.add("ashnehete@gmail.com");
        admin.add("aditya.bhave41@gmail.com");
        admin.add("jainamsoni33@gmail.com");
        admin.add("pardeshi.vishwa25@gmail.com");

        // and so on



        eventOrg = new ArrayList();
        //eventOrg.add("aditya.bhave41@gmail.com");
        eventOrg.add("gujarshlok@gmail.com");
        eventOrg.add("madhugore1@gmail.com");
        eventOrg.add("tejas4888@gmail.com");
        eventOrg.add("lineseguidor@gmail.com");
        eventOrg.add("spitlanmafia.2017@gmail.com");
        eventOrg.add("matrixvsm17@gmail.com");
        eventOrg.add("iplauction2k17@gmail.com");
        eventOrg.add("paperpresentation.matrix17@gmail.com");
        eventOrg.add("chaotichouse2k17@gmail.com");
        eventOrg.add("lasertag1718@gmail.com");
        eventOrg.add("spitmatrixlangaming@gmail.com");
        eventOrg.add("codatron.matrix17@gmail.com");
        eventOrg.add("mindthatword@gmail.com");
        eventOrg.add("matrix2k17.zk@gmail.com");
        eventOrg.add("humanfoosball2017@gmail.com");
        eventOrg.add("robowars.matrix@gmail.com");
        eventOrg.add("robosoccermatrix2k17@gmail.com");
        eventOrg.add("spit.iete@gmail.com");
        eventOrg.add("zorbing.matrix17@gmail.com");
        eventOrg.add("spit.csi@gmail.com");
        eventOrg.add("projectexhibition.matrix2k17@gmail.com");

        // and so on
        //prjct exhibition, tech quz

        userInfo = getSharedPreferences("userInfo", Context.MODE_APPEND);
        sp = userInfo.edit();

        shouldMap = getSharedPreferences("Mapsharedprefs",Context.MODE_APPEND);
        shouldMapEditor = shouldMap.edit();

        firstTime = getSharedPreferences("firstTime", Context.MODE_APPEND);
        spa = firstTime.edit();

        Intent i = getIntent();
        name = i.getStringExtra("name");
        email = i.getStringExtra("email");
        profile = i.getStringExtra("profile");
        uid = i.getStringExtra("uid");
        //type = i.getStringExtra("type");

        if(admin.contains(email)){
            type = "Supervisor";
        }
        else if(eventOrg.contains(email)) {
            type = "Event Organiser";
        }
        else{
            type = "Guest";
        }
        sp.putString("type",type);
        sp.commit();


        b = (Button) findViewById(R.id.regButton);
        b.setClickable(false);
        /*Populate the XML fields here

        And add the spinners for getting year and branch
        And hide them until we dont check if we already have them

         */

        uname = (TextView) findViewById(R.id.uName);
        uemail =  (TextView) findViewById(R.id.uEmail);
        utype = (TextView) findViewById(R.id.uType);
        uprofile = (ImageView) findViewById(R.id.uProfile);
        uclass = (TextInputEditText) findViewById(R.id.uClass);
        spinner = (AppCompatSpinner) findViewById(R.id.spinner);
        ucontact = (TextInputEditText) findViewById(R.id.uContact);
        uname.setText(name);
        uemail.setText(email);
        utype.setText(type);
        Glide.with(this).load(profile).into(uprofile);

        Toast.makeText(this,userInfo.getString("name","huh?"),Toast.LENGTH_SHORT);



        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("COMPS");
        categories.add("IT");
        categories.add("EXTC");
        categories.add("ETRX");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                x = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fixedFrom = uclass.getText().toString() + "" + "COMPS";
            }
        });



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mPushDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String x = (String) snapshot.child("email").getValue();
                    if(email.equals(x)){
                        Toast.makeText(LoginPage.this,"Welcome back!",Toast.LENGTH_SHORT).show();
                        String y = (String) snapshot.child("from").getValue();
                        sp.putString("from",y);
                        sp.commit();
                        spa.putBoolean("firstSignIn",false);
                        shouldMapEditor.putString("LoginDone","1");
                        shouldMapEditor.commit();
                        finish();
                    }
                }
                b.setClickable(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(LoginPage.this,"Please Register Yourself",Toast.LENGTH_SHORT).show();
    }

    public void regUser(View v){
        // Get the class + branch here!!

        if(uclass.getText().toString().equals("") || ucontact.getText().toString().equals("")){
            Toast.makeText(LoginPage.this,"Dont leave any field blank :)",Toast.LENGTH_SHORT).show();
            return;
        }
        if(ucontact.getText().toString().length()!=10)
        {
            Toast.makeText(LoginPage.this,"Enter a valid Contact Number :)",Toast.LENGTH_SHORT).show();
            return;
        }

        phoneNo = ucontact.getText().toString();
        z = uclass.getText().toString();
        fixedFrom = z + " " + x;
        mPushDatabaseReference.push().setValue(new User(name,email,profile,uid,type,fixedFrom,phoneNo));
        Toast.makeText(this,"You are registered !!",Toast.LENGTH_SHORT).show();
        spa.putBoolean("firstSignIn",false);
        spa.commit();
        sp.putString("from", fixedFrom);
        sp.putString("phone",phoneNo);
        sp.commit();
        shouldMapEditor.putString("LoginDone","1");
        shouldMapEditor.commit();
        finish();
    }



}


