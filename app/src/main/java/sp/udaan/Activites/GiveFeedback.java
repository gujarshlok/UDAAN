package sp.udaan.Activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.SmileRating;

import sp.udaan.HelperClasses.Feedback;
import sp.udaan.R;

public class GiveFeedback extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mPushDatabaseReference;
    private SharedPreferences sp;

    private Button mButton;
    SmileRating rating_smilebar;
    int smilebarrating=3;
    EditText mRating,mFeedback;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.85),(int)(height*0.5));

        // mRating = (EditText) findViewById(R.id.rating) ;
        mFeedback = (EditText) findViewById(R.id.feedback);

        rating_smilebar = (SmileRating) findViewById(R.id.rating_smilebar);
        rating_smilebar.setSelectedSmile(SmileRating.GOOD);

        rating_smilebar.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                switch (smiley)
                {
                    case SmileRating.TERRIBLE:
                        smilebarrating=0;
                        break;
                    case SmileRating.BAD:
                        smilebarrating=1;
                        break;
                    case SmileRating.OKAY:
                        smilebarrating=2;
                        break;
                    case SmileRating.GOOD:
                        smilebarrating=3;
                        break;
                    case SmileRating.GREAT:
                        smilebarrating=4;
                        break;
                }
            }
        });

        mButton = (Button) findViewById(R.id.rateConfirm);
        mButton.setClickable(false);

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Feedback").child(getIntent().getStringExtra("name"));
        mPushDatabaseReference = mFirebaseDatabase.getReference().child("Feedback").child(getIntent().getStringExtra("name"));

        email = sp.getString("email",null);

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String x = (String) snapshot.child("email").getValue();
                    if(email.equals(x)){
                        Toast.makeText(GiveFeedback.this,"Already!!",Toast.LENGTH_SHORT).show();
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
                String rating,feedback,email;
                email = sp.getString("email",null);
                //rating = mRating.getText().toString();
                rating=String.valueOf(smilebarrating);
                feedback = mFeedback.getText().toString();
                if(mFeedback.getText().toString().equals("")){
                    Toast.makeText(GiveFeedback.this,"Say something, don't hesitate :)",Toast.LENGTH_SHORT).show();
                    return;
                }
                mPushDatabaseReference.push().setValue(new Feedback(feedback,rating,email));
                Toast.makeText(GiveFeedback.this,"Thank you for your thoughts :)",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

