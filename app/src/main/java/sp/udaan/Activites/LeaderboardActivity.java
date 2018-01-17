package sp.udaan.Activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

import sp.udaan.HelperClasses.LeaderboardAdapter;
import sp.udaan.HelperClasses.LeaderboardUser;
import sp.udaan.R;

public class LeaderboardActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference quizReference;
    RecyclerView recyclerView;
    String QuizID2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView=(RecyclerView)findViewById(R.id.leaderboard_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //final String QuizID=getIntent().getStringExtra("QuizID");

        //Toast.makeText(this,QuizID,Toast.LENGTH_SHORT).show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        quizReference = firebaseDatabase.getReference().child("Quiz");

        final ArrayList<LeaderboardUser> leaderboardUsers=new ArrayList<>();
        final Context context=this;

        LinearLayoutManager layoutManager = new LinearLayoutManager(LeaderboardActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences userInfo;
        userInfo = context.getSharedPreferences("userInfo", Context.MODE_APPEND);
        final String user_name=userInfo.getString("name","defaultname");

        quizReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QuizID2=String.valueOf(dataSnapshot.child("quizid").getValue());

                DataSnapshot d=dataSnapshot.child("Responses").child(QuizID2);
                int position=1;
                for (DataSnapshot ds:d.getChildren())
                {
                    String name=String.valueOf(ds.child("name").getValue());
                    String score = String.valueOf(ds.child("score").getValue());
                    leaderboardUsers.add(new LeaderboardUser(String.valueOf(position),name,"t",score));
                    position++;
                    leaderboardUsers.sort(new Comparator<LeaderboardUser>() {
                        @Override
                        public int compare(LeaderboardUser leaderboardUser, LeaderboardUser t1) {
                            return -(Integer.parseInt(leaderboardUser.score)-Integer.parseInt(t1.score));
                        }
                    });
                    recyclerView.setAdapter(new LeaderboardAdapter(leaderboardUsers,context,user_name));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
    }
}
