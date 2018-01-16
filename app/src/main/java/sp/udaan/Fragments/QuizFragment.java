package sp.udaan.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import sp.udaan.Activites.LeaderboardActivity;
import sp.udaan.HelperClasses.QuizResponse;
import sp.udaan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    TextView questiontext,questiontext2,questiontext3;
    TextView previousscoretext;

    RadioGroup radioGroup;
    RadioButton selectedButton;
    RadioButton option1,option2,option3,option4;
    RadioButton option21,option22,option23,option24;
    RadioButton option31,option32,option33,option34;
    Button submitbutton;
    ImageButton leaderboardbutton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    private DatabaseReference mResponseReference;

    String previous_score="Not Attempted";
    String correct_answer1,correct_answer2,correct_answer3;
    String quizID;
    int score=0;
    SharedPreferences userInfo;
    boolean already_checker;


    public QuizFragment() {
        // Required empty public constructor
    }


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        userInfo = getActivity().getSharedPreferences("userInfo", Context.MODE_APPEND);
        final String user_email=userInfo.getString("email","defaultemail");
        final String user_name=userInfo.getString("name","defaultname");

        already_checker=false;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("Quiz");
        mResponseReference = mFirebaseDatabase.getReference().child("Quiz").child("Responses");

        mItemDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String question=String.valueOf(dataSnapshot.child("question1").getValue());
                String question2 = String.valueOf(dataSnapshot.child("question2").getValue());
                String question3 = String.valueOf(dataSnapshot.child("question3").getValue());

                String s_option1=String.valueOf(dataSnapshot.child("option11").getValue());
                String s_option2=String.valueOf(dataSnapshot.child("option12").getValue());
                String s_option3=String.valueOf(dataSnapshot.child("option13").getValue());
                String s_option4=String.valueOf(dataSnapshot.child("option14").getValue());

                String s_option21=String.valueOf(dataSnapshot.child("option21").getValue());
                String s_option22=String.valueOf(dataSnapshot.child("option22").getValue());
                String s_option23=String.valueOf(dataSnapshot.child("option23").getValue());
                String s_option24=String.valueOf(dataSnapshot.child("option24").getValue());

                String s_option31=String.valueOf(dataSnapshot.child("option31").getValue());
                String s_option32=String.valueOf(dataSnapshot.child("option32").getValue());
                String s_option33=String.valueOf(dataSnapshot.child("option33").getValue());
                String s_option34=String.valueOf(dataSnapshot.child("option34").getValue());

                correct_answer1=String.valueOf(dataSnapshot.child("answer1").getValue());
                correct_answer2=String.valueOf(dataSnapshot.child("answer2").getValue());
                correct_answer3=String.valueOf(dataSnapshot.child("answer3").getValue());

                quizID=String.valueOf(dataSnapshot.child("quizid").getValue());

                questiontext.setText(question);
                questiontext2.setText(question2);
                questiontext3.setText(question3);

                option1.setText(s_option1);
                option2.setText(s_option2);
                option3.setText(s_option3);
                option4.setText(s_option4);

                option21.setText(s_option21);
                option22.setText(s_option22);
                option23.setText(s_option23);
                option24.setText(s_option24);

                option31.setText(s_option31);
                option32.setText(s_option32);
                option33.setText(s_option33);
                option34.setText(s_option34);

                DataSnapshot d=dataSnapshot.child("Responses").child(quizID);

                for (DataSnapshot ds:d.getChildren())
                {
                    if (user_email.equals(String.valueOf(ds.child("email").getValue())))
                    {
                        already_checker=true;
                        previous_score=String.valueOf(ds.child("score").getValue());
                    }
                }
                String scoreString="Your Score: "+previous_score;
                previousscoretext.setText(scoreString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        questiontext=(TextView)view.findViewById(R.id.quizfragment_questiontext);
        questiontext2=(TextView)view.findViewById(R.id.quizfragment_questiontext2);
        questiontext3=(TextView)view.findViewById(R.id.quizfragment_questiontext3);

        leaderboardbutton=(ImageButton)view.findViewById(R.id.leaderboardbutton);
        previousscoretext=(TextView)view.findViewById(R.id.quizfragment_previousscore);

        radioGroup=(RadioGroup)view.findViewById(R.id.quizfragment_optiongroup);
        submitbutton=(Button)view.findViewById(R.id.quizfragment_submitbutton);

        option1=(RadioButton)view.findViewById(R.id.quizfragment_radiooption1);
        option2=(RadioButton)view.findViewById(R.id.quizfragment_radiooption2);
        option3=(RadioButton)view.findViewById(R.id.quizfragment_radiooption3);
        option4=(RadioButton)view.findViewById(R.id.quizfragment_radiooption4);

        option21=(RadioButton)view.findViewById(R.id.quizfragment_radiooption21);
        option22=(RadioButton)view.findViewById(R.id.quizfragment_radiooption22);
        option23=(RadioButton)view.findViewById(R.id.quizfragment_radiooption23);
        option24=(RadioButton)view.findViewById(R.id.quizfragment_radiooption24);

        option31=(RadioButton)view.findViewById(R.id.quizfragment_radiooption31);
        option32=(RadioButton)view.findViewById(R.id.quizfragment_radiooption32);
        option33=(RadioButton)view.findViewById(R.id.quizfragment_radiooption33);
        option34=(RadioButton)view.findViewById(R.id.quizfragment_radiooption34);

        leaderboardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LeaderboardFragment leaderboardfragment = new LeaderboardFragment();
                fragmentTransaction.replace(R.id.fragment_container, leaderboardfragment);
                fragmentTransaction.commit();*/
                Intent i =new Intent(getActivity(), LeaderboardActivity.class);
                i.putExtra("QuizID",quizID);
                startActivity(i);

            }
        });
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean option_checked=false;
                boolean option2_checked=false;
                boolean option3_checked=false;
                boolean total_checker=false;

                if (option1.isChecked()||option2.isChecked()||option3.isChecked()||option4.isChecked())
                {
                    option_checked=true;
                }

                if (option21.isChecked()||option22.isChecked()||option23.isChecked()||option24.isChecked())
                {
                    option2_checked=true;
                }

                if (option31.isChecked()||option32.isChecked()||option33.isChecked()||option34.isChecked())
                {
                    option3_checked=true;
                }

                if (option_checked&&(option2_checked&&option3_checked))
                {
                    total_checker=true;
                }

                if (already_checker)
                {
                    Toast.makeText(getActivity(),"Already Answered!",Toast.LENGTH_SHORT).show();
                }else {

                    if (total_checker) {
                        try {
                            switch (Integer.parseInt(correct_answer1)) {
                                case 1:
                                    if (option1.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 2:
                                    if (option2.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 3:
                                    if (option3.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 4:
                                    if (option4.isChecked()) {
                                        score++;
                                    }
                                    break;

                                default:
                            }

                            switch (Integer.parseInt(correct_answer2)) {
                                case 1:
                                    if (option21.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 2:
                                    if (option22.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 3:
                                    if (option23.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 4:
                                    if (option24.isChecked()) {
                                        score++;
                                    }
                                    break;

                                default:
                            }
                            switch (Integer.parseInt(correct_answer3)) {
                                case 1:
                                    if (option31.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 2:
                                    if (option32.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 3:
                                    if (option33.isChecked()) {
                                        score++;
                                    }
                                    break;

                                case 4:
                                    if (option34.isChecked()) {
                                        score++;
                                    }
                                    break;

                                default:
                            }

                            Toast.makeText(getActivity(),"You scored: "+score+"/3",Toast.LENGTH_SHORT).show();
                            mResponseReference.child(quizID).push().setValue(new QuizResponse(user_name, user_email, String.valueOf(score), ServerValue.TIMESTAMP));

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error Occured! Check Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(),"Please select an option for each Question",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

}
