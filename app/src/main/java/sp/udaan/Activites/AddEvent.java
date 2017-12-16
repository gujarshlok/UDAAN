package sp.udaan.Activites;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import sp.udaan.R;

public class AddEvent extends AppCompatActivity {

    String name,desc,date,eventCategory,time,venue,orgMail,pocName1,pocName2,pocNumber1,pocNumber2,prizeScheme,feeScheme;
    EditText edesc,edate,etime,evenue,eOrgMail,epocName1,epocName2,epocNumber1,epocNumber2,eprize,efees;
    EditText ename;
    ImageView poster;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    TextInputLayout nametextinputlayout,desctextinputlayout,datetextinputlayout;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    Spinner ecategoryspinner;

    String url = null;

    private static final int RC_PHOTO_PICKER =  2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ename = (EditText) findViewById(R.id.eventName);
        edesc = (EditText) findViewById(R.id.eventDescription);
        edate = (EditText) findViewById(R.id.eventDates);
        ecategoryspinner = (Spinner) findViewById(R.id.eventCategorySpinner);

        nametextinputlayout = (TextInputLayout)findViewById(R.id.addevent_EventName_TextInputLayout);
        desctextinputlayout = (TextInputLayout)findViewById(R.id.addevent_EventDescription_TextInputLayout);
        datetextinputlayout = (TextInputLayout)findViewById(R.id.addevent_EventDate_TextInputLayout);

        /*etime = (EditText) findViewById(R.id.eventTime);
        evenue = (EditText) findViewById(R.id.eventVenue);
        eOrgMail = (EditText) findViewById(R.id.eventOrgMail);
        epocName1 = (EditText) findViewById(R.id.eventPocName1);
        epocNumber1 = (EditText) findViewById(R.id.eventPocNumber1);
        epocName2 = (EditText) findViewById(R.id.eventPocName2);
        epocNumber2 = (EditText) findViewById(R.id.eventPocNumber2);

        eprize = (EditText) findViewById(R.id.eventPrizeScheme);
        efees = (EditText) findViewById(R.id.eventFeeScheme);*/

        poster = (ImageView) findViewById(R.id.eventPoster);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Events");

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("EventPosters");

        final String eventTypes[]={"Championships","Talks and Exhibitions","Featured","Fun"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,eventTypes);
        ecategoryspinner.setAdapter(adapter);

        ecategoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index=ecategoryspinner.getSelectedItemPosition();
                eventCategory=eventTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /*Create a form like layout to input all the fields of the events
        Except the eventOrgMail field, it should be automatically added from the sharedprefs
        Just focus on functionality, ui later
        Dont use the v4 vaala circle image view, use the imported one
     */


    public void addPoster(View v){
        // TODO: Fire an intent to show an image picker
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER){
            if(resultCode == RESULT_OK){
                Uri selectedImageUri = data.getData();
                StorageReference photoRef = mStorageReference.child(selectedImageUri.getLastPathSegment());

                photoRef.putFile(selectedImageUri)
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //noinspection VisibleForTests
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                url = downloadUrl.toString();
                                Glide.with(AddEvent.this).load(url).into(poster);
                            }
                        });
            }
        }
    }

    public void addEvent(View v){
        name = ename.getText().toString();
        desc = edesc.getText().toString();
        date = edate.getText().toString();
        //time = etime.getText().toString();
        /*venue = evenue.getText().toString();
        orgMail = eOrgMail.getText().toString();
        pocName1 = epocName1.getText().toString();
        pocNumber1 = epocNumber1.getText().toString();
        pocName2 = epocName2.getText().toString();
        pocNumber2 = epocNumber2.getText().toString();
        prizeScheme = eprize.getText().toString();
        feeScheme = efees.getText().toString();*/
        nametextinputlayout.setErrorEnabled(false);
        desctextinputlayout.setErrorEnabled(false);
        datetextinputlayout.setErrorEnabled(false);

        if(TextUtils.isEmpty(name))
        {
            nametextinputlayout.setError("Event name cannot be empty");
            nametextinputlayout.setFocusable(true);
            return;
        }else if (TextUtils.isEmpty(desc))
        {
            desctextinputlayout.setError("Event description cannot be empty");
            return;
        }else if (TextUtils.isEmpty(date))
        {
            datetextinputlayout.setError("Date must be specified");
            return;
        }

        /*Hardcoded Poster*/
        if(url == null) {
            url = "https://s-media-cache-ak0.pinimg.com/originals/8e/f5/9d/8ef59dc3c90a3abd56c87a5901709132.jpg";
        }
        //mDatabaseReference.push().setValue(new Event(name,desc,url,date,time,venue,orgMail,pocName1,pocName2,pocNumber1,pocNumber2,prizeScheme,feeScheme));
        //Toast.makeText(this,"Done!",Toast.LENGTH_SHORT);

        Intent intent=new Intent(AddEvent.this,AddEvent2.class);
        intent.putExtra("name",name);
        intent.putExtra("url",url);
        intent.putExtra("desc",desc);
        intent.putExtra("date",date);
        intent.putExtra("eventCategory",eventCategory);
        startActivity(intent);

        finish();
    }

}
