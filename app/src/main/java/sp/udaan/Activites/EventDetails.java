package sp.udaan.Activites;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
import java.util.Calendar;
import java.util.Objects;

import sp.udaan.R;

public class EventDetails
        extends AppCompatActivity
{
    private boolean isFavouriteEvent;
    private boolean isReminderSet;
    private String event_name;
    private MenuItem mi_reminder;
    private long mEventID;
    private boolean visitedCalendar, isFirstLaunch;
    ImageView mainImageView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CardView organizers_card,prizes_card,registration_card,venue_time_card;
    TextView hardcodedDate;
    private RecyclerView mRecyclerView;
    private sp.udaan.HelperClasses.ReviewAdapter mReviewAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    private ValueEventListener mValueEventListener;
    //Comment
    private ArrayList<sp.udaan.HelperClasses.Feedback> mFeedback;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        mRecyclerView = (RecyclerView) findViewById(R.id.tv_feedbackRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mItemDatabaseReference = mFirebaseDatabase.getReference().child("Feedback").child(getIntent().getStringExtra("name"));
        mFeedback = new ArrayList<sp.udaan.HelperClasses.Feedback>();

        EventDetails.FetchFeedbackList fel = new EventDetails.FetchFeedbackList();
        fel.execute();


        final AppCompatTextView textViews[] = {
                (AppCompatTextView) findViewById(R.id.tv_event_description),
                (AppCompatTextView) findViewById(R.id.tv_event_venue_time),
                (AppCompatTextView) findViewById(R.id.tv_event_registration),
                (AppCompatTextView) findViewById(R.id.tv_event_prizes),
                (AppCompatTextView) findViewById(R.id.tv_event_organizers)
        };

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsingToolbar_event);
        organizers_card =(CardView)findViewById(R.id.organizers_card);
        prizes_card = (CardView)findViewById(R.id.prizes_card);
        registration_card =(CardView)findViewById(R.id.registration_card);
        venue_time_card=(CardView)findViewById(R.id.venue_time_card);

        hardcodedDate=(TextView)findViewById(R.id.hardcodedDate);

        visitedCalendar = false;
        isFirstLaunch = true;

        //get intent from eventlist adapter
        if (getIntent().getStringExtra("name") != null && getSupportActionBar() != null) {
            event_name = getIntent().getStringExtra("name");
            this.setTitle(event_name);
        }
        else
            this.setTitle("Some event");

        hardcodedDate.setText(getIntent().getStringExtra("dates"));

        setDescription(getIntent().getStringExtra("description"));
        setVenueAndTime(getIntent().getStringExtra("venue"), getIntent().getStringExtra("time"));
        setRegistration(getIntent().getStringExtra("registration"));
        setPrizes(getIntent().getStringExtra("prizes"));
        setContacts(getIntent().getStringExtra("contact1name"), getIntent().getStringExtra("contact1no"), getIntent().getStringExtra("contact2name"), getIntent().getStringExtra("contact2no"));

        //isFavouriteEvent = getIntent().getIntExtra("favorite", 0) == 1;
        //isReminderSet = getIntent().getIntExtra("reminder", 0) == 1;

        mainImageView = (ImageView) findViewById(R.id.main_imageView);
        assert mainImageView != null;
        Glide.with(this).load(getIntent().getStringExtra("image")).centerCrop().fitCenter().into(mainImageView);


       /* Bitmap bitmap = ((BitmapDrawable)mainImageView.getDrawable()).getBitmap();

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener()
        {
            @Override
            public void onGenerated(Palette palette)
            {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if(swatch == null)
                    swatch = palette.getMutedSwatch();
                if(swatch != null)
                {
                    int color = swatch.getRgb();
                    if(Color.red(color)+Color.green(color)+Color.green(color) > 420)
                        color = Color.rgb((int)(Color.red(color)*0.8), (int)(Color.green(color)*0.8), (int)(Color.blue(color)*0.8));

                    fab.setBackgroundTintList(ColorStateList.valueOf(color));
                    fab.setRippleColor(swatch.getTitleTextColor());
                    collapsingToolbarLayout.setContentScrimColor(color);
                    collapsingToolbarLayout.setBackgroundColor(color);
                    collapsingToolbarLayout.setStatusBarScrimColor(color);

                    if(Build.VERSION.SDK_INT >= 21){
                        getWindow().setStatusBarColor(color);
                    }

                    for (AppCompatTextView textView : textViews) textView.setTextColor(color);
                }
            }
        });
        */


    }

    @Override
    protected void onStart() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Slide slide = new Slide(Gravity.BOTTOM);

            if(isFirstLaunch) {

                isFirstLaunch = false;
            }

            slide.addTarget(R.id.description_card);
            slide.addTarget(R.id.venue_time_card);
            slide.addTarget(R.id.registration_card);
            slide.addTarget(R.id.prizes_card);
            slide.addTarget(R.id.organizers_card);
            slide.setInterpolator(new LinearOutSlowInInterpolator());
            getWindow().setEnterTransition(slide);
            getWindow().setExitTransition(slide);
            getWindow().setReenterTransition(slide);

            setupEnterAnimation();
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mi_reminder = menu.findItem(R.id.action_set_reminder);
        if(isReminderSet)
            mi_reminder.setIcon(R.drawable.svg_alarm_on_white_48px);
        return super.onPrepareOptionsMenu(menu);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.transition);
        transition.setDuration(300);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }
            @Override
            public void onTransitionEnd(Transition transition) {

            }
            @Override
            public void onTransitionCancel(Transition transition) {
            }
            @Override
            public void onTransitionPause(Transition transition) {
            }
            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_reminder:
                setReminder();
                break;
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Check out the event '"+event_name+"' at Udann 18! For more details, download the official app here:\n"+getResources().getString(R.string.playstore_link));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share event via"));
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTitle(final String title) {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar_event);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

    }

    private void setDescription(String description) {
        AppCompatTextView descriptionTextView = (AppCompatTextView) findViewById(R.id.description_textView);
        assert descriptionTextView != null;
        descriptionTextView.setText(description);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setVenueAndTime(String venue, String time) {
        AppCompatTextView venueTimeTextView = (AppCompatTextView) findViewById(R.id.venue_time_textView);
        assert venueTimeTextView != null;
        System.out.println(venue+time);
        if(Objects.equals(venue, "None")&&Objects.equals(time, "None")){
            venue_time_card.setVisibility(View.GONE);
            return;
        }
        if(Objects.equals(venue, "None")){
            venueTimeTextView.setText(time);
            return;
        }
        if(Objects.equals(time, "None")) {
            venueTimeTextView.setText(venue);
            return;
        }
        venueTimeTextView.setText(venue + "\n" + time);
    }

    private void setRegistration(String registration) {
        AppCompatTextView registrationTextView = (AppCompatTextView) findViewById(R.id.registration_textView);
        assert registrationTextView != null;
        registrationTextView.setText(registration);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPrizes(String prizes) {
        AppCompatTextView prizesTextView = (AppCompatTextView) findViewById(R.id.prizes_textView);
        assert prizesTextView != null;
        if(Objects.equals(prizes, "None"))
            prizes_card.setVisibility(View.GONE);
        prizesTextView.setText(prizes);

    }

    private void setContacts(final String name1, final String number1, final String name2, final String number2) {
        TextView contactTextViewOne = (TextView) findViewById(R.id.contact_name_one);
        TextView contactTextViewTwo = (TextView) findViewById(R.id.contact_name_two);

        ImageButton callOne = (ImageButton) findViewById(R.id.call_contact_person_one);
        ImageButton saveOne = (ImageButton) findViewById(R.id.save_contact_person_one);
        ImageButton callTwo = (ImageButton) findViewById(R.id.call_contact_person_two);
        ImageButton saveTwo = (ImageButton) findViewById(R.id.save_contact_person_two);

        assert contactTextViewOne != null;
        contactTextViewOne.setText(name1 + "\n" + number1);
        assert contactTextViewTwo != null;
        contactTextViewTwo.setText(name2 + "\n" + number2);

        View.OnClickListener callOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                switch (v.getId()) {
                    case R.id.call_contact_person_one:
                        intent.setData(Uri.parse("tel:" + number1));
                        break;
                    case R.id.call_contact_person_two:
                        intent.setData(Uri.parse("tel:" + number2));
                        break;
                }
                startActivity(intent);
            }
        };

        View.OnClickListener saveOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                switch (v.getId()) {
                    case android.R.id.home:
                        EventDetails.super.onBackPressed();
                        break;
                    case R.id.save_contact_person_one:
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name1);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "" + number1);
                        break;
                    case R.id.save_contact_person_two:
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name2);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "" + number2);
                        break;
                }
                startActivity(intent);
            }
        };

        assert callOne != null;
        callOne.setOnClickListener(callOnClickListener);
        assert callTwo != null;
        callTwo.setOnClickListener(callOnClickListener);
        assert saveOne != null;
        saveOne.setOnClickListener(saveOnClickListener);
        assert saveTwo != null;
        saveTwo.setOnClickListener(saveOnClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setReminder() {
        if(Objects.equals(event_name, "Ethical Hacking")){
            Toast.makeText(this,"Date and time have not yet been finalized!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(ContextCompat.checkSelfPermission(EventDetails.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
        else
        {
            if (isReminderSet)
                Toast.makeText(this, "Reminder already set", Toast.LENGTH_SHORT).show();
            else {
                final Calendar beginTime = Calendar.getInstance();
                final Calendar endTime = Calendar.getInstance();

                beginTime.set(2018, 2, 16, 9, 0);
                endTime.set(2018, 2, 16, 22, 0);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Remind on?");
                CharSequence day[];
                if(Objects.equals(event_name, "Daniel Fernandes")||Objects.equals(event_name,"Techshiksha"))
                    day=new CharSequence[]{"Day 1"};
                else if(Objects.equals(event_name,"Sky Observation"))
                    day=new CharSequence[]{"Day 2"};
                else
                    day=new CharSequence[]{"Day 1","Day 2","Day 3"};
                builder.setItems(day, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            goToCalendar(beginTime,endTime);
                        }
                        else
                        {
                            beginTime.set(2018, 2, 18, 9, 0);
                            endTime.set(2018, 2, 18, 22, 0);
                            goToCalendar(beginTime, endTime);
                        }
                    }
                });
                builder.show();
            }
        }
    }

    private void goToCalendar(Calendar beginTime, Calendar endTime) {
        mEventID = getLastEventId(getContentResolver())+1;

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events._ID, mEventID)
                .putExtra(CalendarContract.Events.TITLE, event_name)
                .putExtra(CalendarContract.Events.DESCRIPTION, "Event at Udaan 18")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, getIntent().getStringExtra("venue")+", S.P.I.T.")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        visitedCalendar = true;
        startActivity(intent);
    }

    private long getLastEventId(ContentResolver cr) {
        if(ContextCompat.checkSelfPermission(EventDetails.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = cr.query(CalendarContract.Events.CONTENT_URI, new String[]{"MAX(_id) as max_id"}, null, null, "_id");
            assert cursor != null;
            cursor.moveToFirst();
            long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));
            cursor.close();
            return max_val;
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] != 0)
            Toast.makeText(EventDetails.this, "Reminder cannot be added without permission to read calendar", Toast.LENGTH_SHORT).show();
        else
            setReminder();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (visitedCalendar)
        {
            if (getLastEventId(getContentResolver()) == mEventID)
            {
                isReminderSet = true;
                mi_reminder.setIcon(R.drawable.svg_alarm_on_white_48px);
                Toast.makeText(EventDetails.this, "Successfully added reminder", Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(EventDetails.this, "Reminder not added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        mainImageView.bringToFront();
        registration_card.setVisibility(View.GONE);
        prizes_card.setVisibility(View.GONE);
        organizers_card.setVisibility(View.GONE);
        super.onBackPressed();
    }

    public void register(View v){
        //Toast.makeText(this,"Not yet :(",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,RegConfirm.class);
        i.putExtra("name",getIntent().getStringExtra("name"));
        startActivity(i);
    }

    public void feedback(View v){
        //Toast.makeText(this,"Not yet :(",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,GiveFeedback.class);
        i.putExtra("name",getIntent().getStringExtra("name"));
        startActivity(i);
    }


    public class FetchFeedbackList extends AsyncTask<Void,Void,ArrayList<sp.udaan.HelperClasses.Event>> {

        @Override
        protected void onPreExecute() {
            //bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<sp.udaan.HelperClasses.Event> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mFeedback.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String name = (String) snapshot.child("email").getValue();
                        if(name == null) {
                            break;
                        }
                        String email = (String) snapshot.child("email").getValue();
                        String rating = (String) snapshot.child("rating").getValue();
                        String feedback = (String) snapshot.child("feedback").getValue();
                        mFeedback.add(new sp.udaan.HelperClasses.Feedback(feedback,rating,email));

                    }
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mItemDatabaseReference.addValueEventListener(mValueEventListener);
            return null;
        }
    }

    public void updateUI(){
        mReviewAdapter = new sp.udaan.HelperClasses.ReviewAdapter(mFeedback,EventDetails.this);
        mRecyclerView.setAdapter(mReviewAdapter);
        mRecyclerView.scrollToPosition(0);
    }
}