package sp.udaan.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import sp.udaan.HelperClasses.QuizResponse;
import sp.udaan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    TextView questiontext;
    RadioGroup radioGroup;
    RadioButton selectedButton;
    RadioButton option1,option2,option3,option4;
    Button submitbutton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    private DatabaseReference mResponseReference;

    String correct_answer;
    String quizID;
    SharedPreferences userInfo;
    boolean already_checker;


    public QuizFragment() {
        // Required empty public constructor
    }


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

                String question=String.valueOf(dataSnapshot.child("question").getValue());

                String s_option1=String.valueOf(dataSnapshot.child("option1").getValue());
                String s_option2=String.valueOf(dataSnapshot.child("option2").getValue());
                String s_option3=String.valueOf(dataSnapshot.child("option3").getValue());
                String s_option4=String.valueOf(dataSnapshot.child("option4").getValue());

                correct_answer=String.valueOf(dataSnapshot.child("answer").getValue());
                quizID=String.valueOf(dataSnapshot.child("quizid").getValue());

                questiontext.setText(question);
                option1.setText(s_option1);
                option2.setText(s_option2);
                option3.setText(s_option3);
                option4.setText(s_option4);

                DataSnapshot d=dataSnapshot.child("Responses").child(quizID);

                for (DataSnapshot ds:d.getChildren())
                {
                    if (user_email.equals(String.valueOf(ds.child("email").getValue())))
                    {
                        already_checker=true;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        questiontext=(TextView)view.findViewById(R.id.quizfragment_questiontext);
        radioGroup=(RadioGroup)view.findViewById(R.id.quizfragment_optiongroup);
        submitbutton=(Button)view.findViewById(R.id.quizfragment_submitbutton);
        option1=(RadioButton)view.findViewById(R.id.quizfragment_radiooption1);
        option2=(RadioButton)view.findViewById(R.id.quizfragment_radiooption2);
        option3=(RadioButton)view.findViewById(R.id.quizfragment_radiooption3);
        option4=(RadioButton)view.findViewById(R.id.quizfragment_radiooption4);


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean option_checked=false;
                if (option1.isChecked()||option2.isChecked()||option3.isChecked()||option4.isChecked())
                {
                    option_checked=true;
                }

                if (already_checker)
                {
                    Toast.makeText(getActivity(),"Already Answered!",Toast.LENGTH_SHORT).show();
                }else {

                    if (option_checked) {
                        try {
                            boolean rightanswer = false;
                            switch (Integer.parseInt(correct_answer)) {
                                case 1:
                                    if (option1.isChecked()) {
                                        rightanswer = true;
                                    }
                                    break;

                                case 2:
                                    if (option2.isChecked()) {
                                        rightanswer = true;
                                    }
                                    break;

                                case 3:
                                    if (option3.isChecked()) {
                                        rightanswer = true;
                                    }
                                    break;

                                case 4:
                                    if (option4.isChecked()) {
                                        rightanswer = true;
                                    }
                                    break;

                                default:
                            }


                            if (rightanswer) {
                                Toast.makeText(getActivity(), "Correct Answer!", Toast.LENGTH_SHORT).show();
                                mResponseReference.child(quizID).push().setValue(new QuizResponse(user_name, user_email, "1", ServerValue.TIMESTAMP));

                            } else {
                                Toast.makeText(getActivity(), "Wrong Answer:(", Toast.LENGTH_SHORT).show();
                                mResponseReference.child(quizID).push().setValue(new QuizResponse(user_name, user_email, "0", ServerValue.TIMESTAMP));
                            }

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(),"Please select an option",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        return view;
    }

}
