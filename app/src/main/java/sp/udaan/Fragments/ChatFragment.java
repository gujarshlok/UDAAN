package sp.udaan.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.BuildConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sp.udaan.HelperClasses.FriendlyMessage;
import sp.udaan.HelperClasses.MessageAdapter;
import sp.udaan.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final String MESSAGE_LENGTH_KEY = "message_length";
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private String mUsername;
    private String mEmail;
    String mailReceived;

    //Firebase Instance Variables
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosReference;
    private FirebaseRemoteConfig mRemoteConfig;

    //FirebaseRecyclerAdapter<FriendlyMessage,chat_rec> adapter;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUsername = ANONYMOUS;

        //Initializing Firebase Object
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseReference = database.getReference().child("messages");
        mChatPhotosReference = mFirebaseStorage.getReference().child("chat_photos");
        mRemoteConfig = FirebaseRemoteConfig.getInstance();


        // Initialize references to views
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mMessageListView = (ListView)view.findViewById(R.id.messageListView);
        //mMessageRecyclerView= (RecyclerView) view.findViewById(R.id.messageRecyclerView);
        mPhotoPickerButton = (ImageButton) view.findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText)view.findViewById(R.id.messageEditText);
        mSendButton = (Button)view.findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        final List<FriendlyMessage> friendlyMessages = new ArrayList<>();

        mMessageAdapter = new MessageAdapter(getActivity(), R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
        if(mAuth.getCurrentUser().getEmail().equals("gujarshlok@gmail.com")) {

            mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
//                    FriendlyMessage selectedFromList = (FriendlyMessage) mMessageListView.getItemAtPosition(position);
//                    Log.e("TAG", String.valueOf(selectedFromList));
                    TextView textView = (TextView) view.findViewById(R.id.nameTextView);
                    TextView emailView=(TextView)view.findViewById(R.id.messageTextView);
                    String text= textView.getText().toString();
                    String email=emailView.getText().toString();
                   // Log.d("TAG",s);

                    showpopup(text,email);
                }
            });
        }
        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FriendlyMessage message = new
                        FriendlyMessage(mMessageEditText.getText().toString(), mUsername, mEmail,null,mEmail);
                mFirebaseReference.push().setValue(message);

                // Clear input box
                mMessageEditText.setText("");
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user signed-in
                    OnSignedIn(user.getDisplayName(),user.getEmail());
                   // Toast.makeText(Main, "You are Signed-in.Welcome to Friendly Chat App.",Toast.LENGTH_SHORT).show();
                } else {
                    //user signed-out
                    OnSignedOut();

                }
            }
        };

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        mRemoteConfig.setConfigSettings(configSettings);

        //Creating Default Config map
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put(MESSAGE_LENGTH_KEY, DEFAULT_MSG_LENGTH_LIMIT);
        mRemoteConfig.setDefaults(defaultConfigMap);

        fetchConfig();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getActivity(), "Signed-In", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Sign-In Cancelled", Toast.LENGTH_SHORT).show();
                //finish();
            } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
                Uri imageUri = data.getData();

                //get the reference to stored file at database
                StorageReference photoReference = mChatPhotosReference.child(imageUri.getLastPathSegment());

                //upload file to firebase
                photoReference.putFile(imageUri).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        FriendlyMessage message = new FriendlyMessage(null, mUsername, mEmail,downloadUrl.toString(),null);
                        mFirebaseReference.push().setValue(message);
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener !=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mMessageAdapter.clear();

    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    protected void OnSignedIn(String userName,String userEmail) {
        mUsername = userName;
        mEmail = userEmail;
        attachDatabaseReadListener();
    }

    protected void OnSignedOut() {
        mUsername = "Anonymous";
        mMessageAdapter.clear();
        detachDatabaseReadListener();
    }



    protected void attachDatabaseReadListener() {

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                      if(dataSnapshot.child("rec_email").getValue().toString().equals(mAuth.getCurrentUser().getEmail().toString())) {
                          FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                          mMessageAdapter.add(friendlyMessage);
                      }
                      if(dataSnapshot.child("rec_email").getValue().toString().equals("gujarshlok@gmail.com"))
                      {
                          FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                          mMessageAdapter.add(friendlyMessage);
                      }
                      else if(mAuth.getCurrentUser().getEmail().equals("aditya.bhave41@gmail.com"))
                      {
                          FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                          mMessageAdapter.add(friendlyMessage);
                      }
//                      else
//                      {
//                          FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
//                          mMessageAdapter.add(friendlyMessage);
//                      }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };

            mFirebaseReference.addChildEventListener(mChildEventListener);
        }

    }

    protected void detachDatabaseReadListener() {

        if (mChildEventListener !=null) {
            mFirebaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }

    }
    public void fetchConfig() {
        long cacheExpiration = 3600;
        if (mRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mRemoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRemoteConfig.activateFetched();
                applyRetrievedLength();;


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Log.w(TAG, "Error fetching Config", e);
                applyRetrievedLength();
            }
        });
    }

    private void applyRetrievedLength() {
        Long message_length = mRemoteConfig.getLong(MESSAGE_LENGTH_KEY);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(message_length.intValue())});
        //Log.d(TAG, MESSAGE_LENGTH_KEY + " = " + message_length);
    }

    private void showpopup(final String email,final String text){

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup, null);
        final TextInputEditText committeemessagetext = dialogView.findViewById(R.id.committeemessage);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage("Message");

        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final String committeemessage = committeemessagetext.getText().toString();
                if(committeemessage.isEmpty()){
                    Toast.makeText(getActivity(),"Please Enter a message :)",Toast.LENGTH_LONG).show();
                }
                else{
                    /*mFirebaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Log.e("gg","hello");
                            for (DataSnapshot ds:dataSnapshot.getChildren())
                            {
                                if(ds.child("name").getValue().toString().equals(email)
                                        &&ds.child("text").getValue().toString().equals(text))
                                {
                                    mailReceived=ds.child("email").getValue().toString();
                                    Log.d("TAG",mailReceived);
                                    FriendlyMessage message1 = new FriendlyMessage(committeemessage, mUsername, mEmail,null,mailReceived);
                                    mFirebaseReference.push().setValue(message1);
                                    //Toast.makeText(getActivity(),"Message Sent!",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                               // Log.d("HH", String.valueOf(ds));
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/
                    mFirebaseReference.push().setValue(new FriendlyMessage(committeemessage,mUsername,mEmail,null,email));
                    committeemessagetext.setText("");

                }
            }
        });

        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Cancel
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


}
