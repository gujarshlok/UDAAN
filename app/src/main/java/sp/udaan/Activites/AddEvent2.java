package sp.udaan.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sp.udaan.HelperClasses.Event;
import sp.udaan.R;

public class AddEvent2 extends AppCompatActivity {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    String timings,venue,mail,name1,num1,name2,num2,prize,fee;
    String name,url,desc,date,eventCategory;
    EditText etimings,evenue,email,ename1,enum1,ename2,enum2,eprize,efee;
    TextInputLayout timelayout,venuelayout,emaillayout,name1layout,contact1layout,name2layout,contact2layout,prizelayout,feelayout;
    SharedPreferences userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event2);

        etimings=(EditText)findViewById(R.id.event2_timings);
        evenue=(EditText)findViewById(R.id.event2_venue);
        //email=(EditText)findViewById(R.id.event2_mail);
        ename1=(EditText)findViewById(R.id.event2_name1);
        enum1=(EditText)findViewById(R.id.event2_num1);
        ename2=(EditText)findViewById(R.id.event2_name2);
        enum2=(EditText)findViewById(R.id.event2_num2);
        eprize=(EditText)findViewById(R.id.event2_prize);
        efee=(EditText)findViewById(R.id.event2_fee);

        timelayout=(TextInputLayout)findViewById(R.id.addevent2_timing_TextInputLayout);
        venuelayout=(TextInputLayout)findViewById(R.id.addevent2_venue_TextInputLayout);
        //emaillayout=(TextInputLayout)findViewById(R.id.addevent2_organizeremail_TextInputLayout);
        name1layout=(TextInputLayout)findViewById(R.id.addevent2_organizer1name_TextInputLayout);
        name2layout=(TextInputLayout)findViewById(R.id.addevent2_organizer2name_TextInputLayout);
        contact1layout=(TextInputLayout)findViewById(R.id.addevent2_organizer1contact_TextInputLayout);
        contact2layout=(TextInputLayout)findViewById(R.id.addevent2_organizer2contact_TextInputLayout);
        prizelayout=(TextInputLayout)findViewById(R.id.addevent2_prizes_TextInputLayout);
        feelayout=(TextInputLayout)findViewById(R.id.addevent2_fee_TextInputLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Events");

        /*  Get a caegory from user out of Mega, Major and Fun,
        accordingly branch the database reference one branch further and then push*/

        userInfo = getSharedPreferences("userInfo", Context.MODE_APPEND);
    }

    public void addEvent2(View v)
    {
        timings=etimings.getText().toString();
        venue=evenue.getText().toString();
        //mail=email.getText().toString();
        name1=ename1.getText().toString();
        num1=enum1.getText().toString();
        name2=ename2.getText().toString();
        num2=enum2.getText().toString();
        prize=eprize.getText().toString();
        fee=efee.getText().toString();

        timelayout.setErrorEnabled(false);
        venuelayout.setErrorEnabled(false);
        //emaillayout.setErrorEnabled(false);
        name1layout.setErrorEnabled(false);
        contact1layout.setErrorEnabled(false);
        name2layout.setErrorEnabled(false);
        contact2layout.setErrorEnabled(false);
        prizelayout.setErrorEnabled(false);
        feelayout.setErrorEnabled(false);


        if (TextUtils.isEmpty(timings))
        {
            timelayout.setError(getString(R.string.notemptyfield));
            timelayout.setFocusable(true);
            return;
        } else if(TextUtils.isEmpty(venue)){
            venuelayout.setError(getString(R.string.notemptyfield));
            return;
        }
        //else if (TextUtils.isEmpty(mail)){
        //emaillayout.setError(getString(R.string.notemptyfield));
        //return;
        //}
        else if (TextUtils.isEmpty(name1)){
            name1layout.setError(getString(R.string.notemptyfield));
            return;
        } else if (TextUtils.isEmpty(num1)){
            contact1layout.setError(getString(R.string.notemptyfield));
            return;
        } else if (TextUtils.isEmpty(name2)){
            name2layout.setError(getString(R.string.notemptyfield));
            return;
        } else if (TextUtils.isEmpty(num2)){
            contact2layout.setError(getString(R.string.notemptyfield));
            return;
        } else if(TextUtils.isEmpty(prize)){
            prizelayout.setError(getString(R.string.notemptyfield));
            return;
        } else if (TextUtils.isEmpty(fee)){
            feelayout.setError(getString(R.string.notemptyfield));
            return;
        }

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        url=intent.getStringExtra("url");
        desc=intent.getStringExtra("desc");
        date=intent.getStringExtra("date");
        eventCategory=intent.getStringExtra("eventCategory");
        mail = userInfo.getString("email","xyz@gmail.com");

        mDatabaseReference.child(eventCategory).push().setValue(new Event(name,desc,url,date,timings,venue,mail,name1,name2,num1,num2,prize,fee));
        finish();
    }
}