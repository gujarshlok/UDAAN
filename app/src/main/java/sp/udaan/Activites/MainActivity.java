package sp.udaan.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import sp.udaan.Fragments.AboutAppFragment;
import sp.udaan.Fragments.ChatFragment;
import sp.udaan.Fragments.CommitteeFragment;
import sp.udaan.Fragments.ContactUsFragment;
import sp.udaan.Fragments.DevelopersFragment;
import sp.udaan.Fragments.FavoritesFragment;
import sp.udaan.Fragments.MainFragment;
import sp.udaan.Fragments.MyEventsFragment;
import sp.udaan.Fragments.QuizFragment;
import sp.udaan.Fragments.SponsorsFragment;
import sp.udaan.HelperClasses.CustomPagerAdapter;
import sp.udaan.HelperClasses.CustomViewPager;
import sp.udaan.R;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarLayout appBarLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    CollapsingToolbarLayout collapsingToolbarLayout;

    FragmentManager fm;
    String backStageName;

    CustomPagerAdapter mCustomPagerAdapter;
    CustomViewPager mViewPager;

    private static final long DRAWER_DELAY = 250;
    private static int NUM_PAGES = 3;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser user;

    SharedPreferences.Editor sp, spa, shouldMapeditor;
    SharedPreferences userInfo;
    SharedPreferences shouldMap;
    SharedPreferences firstTime;

    static String type;
    private int i = 1;

    public static String Email;

    private ArrayList admin, eventOrg;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;
    private DatabaseReference mPushDatabaseReference;

    public static int valid = 0;
    TextView navDrawerUsername, navDrawerUseremailid;
    private static final String TAG = "MainActivity";

    String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21)
            setContentView(R.layout.activity_main_v21);
        else
            setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        //Getting Event Category here
        try
        {
            currentCategory=getIntent().getStringExtra("EventCategory");
        }catch (Exception e)
        {
            currentCategory="Literary Arts";
        }

        admin = new ArrayList();
        admin.add("asdfas");// and so on

        eventOrg = new ArrayList();
        eventOrg.add("sdfasd");// and so on


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mPushDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        userInfo = getSharedPreferences("userInfo", Context.MODE_APPEND);
        sp = userInfo.edit();
        firstTime = getSharedPreferences("firstTime", Context.MODE_APPEND);

        mFirebaseAuth = FirebaseAuth.getInstance();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        View headerview = navigationView.getHeaderView(0);
        navDrawerUsername = (TextView) headerview.findViewById(R.id.NavigationDrawer_Username);
        navDrawerUseremailid = (TextView) headerview.findViewById(R.id.NavigationDrawer_UserEmail);

        shouldMap=getSharedPreferences("Mapsharedprefs",Context.MODE_APPEND);
        shouldMapeditor=shouldMap.edit();
        //shouldMapeditor.putString("LoginDone","0");
        //shouldMapeditor.commit();

        /// /SharedPreferences.Editor editor=sp
        //user=mFirebaseAuth.getCurrentUser();
        //SharedPreferences sharedPreferences=getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        //navDrawerUsername.setText(sharedPreferences.getString("name","raju"));
        //navDrawerUseremailid.setText(sharedPreferences.getString("email","rajes"));
        //navDrawerUsername.setText((String)user.getDisplayName());
        //navDrawerUseremailid.setText((String)user.getEmail());

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // Code to save userdata
                    Log.v("Userdetails", user.getDisplayName() + " " + user.getEmail());
                    sp.putString("name", user.getDisplayName());
                    sp.putString("email", user.getEmail());
                    sp.putString("profile", user.getPhotoUrl().toString());
                    sp.putString("UID", user.getUid());
                    navDrawerUsername.setText((String) user.getDisplayName());
                    navDrawerUseremailid.setText((String) user.getEmail());

                    sp.commit();


                    if (shouldMap.getString("LoginDone","0").equals("1") && valid==0 ){
                        Intent mapsActivity = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(mapsActivity);
                        valid=1;
                    }

                    Email = user.getEmail();


                } else {
                    // User is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(AuthUI.GOOGLE_PROVIDER)
                                    .setTheme(R.style.LoginTheme).setLogo(R.mipmap.ic_launcher)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        //ViewPager
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager_main);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        mViewPager.setAdapter(mCustomPagerAdapter);
        indicator.setViewPager(mViewPager);

        final Handler h = new Handler(Looper.getMainLooper());
        final Runnable r = new Runnable() {
            public void run() {
                mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % NUM_PAGES, true);
                h.postDelayed(this, 3000);
            }
        };
        h.postDelayed(r, 3000);


        //instantiation
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        //navigationView =(NavigationView)findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar_main);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
        appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
        layoutParams.setBehavior(appBarLayoutBehaviour);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            MainFragment mainFragment = MainFragment.newInstance();
            transaction.replace(R.id.fragment_container, mainFragment).commit();
        }

        setupDrawerLayout();

        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                //String x = "Signed In as" + user.getDisplayName().toString();
                //Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
                user = mFirebaseAuth.getCurrentUser();
                Toast.makeText(MainActivity.this, "Registering!", Toast.LENGTH_SHORT);
                Intent i = new Intent(MainActivity.this, SignInVideo.class);
                i.putExtra("name", user.getDisplayName());
                i.putExtra("email", user.getEmail());
                i.putExtra("profile", user.getPhotoUrl().toString());
                i.putExtra("uid", user.getUid());
                i.putExtra("type", type);
                startActivity(i);

            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void setupDrawerLayout() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        drawerLayout.closeDrawers();
                        if (!item.isChecked()) {
                            final FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                            switch (item.getItemId()) {
                                case R.id.homepage_menuItem:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            boolean isFragmentInStack = fm.popBackStackImmediate(backStageName, 0);
                                            if (!isFragmentInStack) {
                                                MainFragment fragment = MainFragment.newInstance();
                                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                                backStageName = fragment.getClass().getName();
                                                fragmentTransaction.addToBackStack(backStageName).commit();
                                            }
                                            appBarLayout.setExpanded(true, true);
                                            collapsingToolbarLayout.setTitle("Matrix 17");
                                        }
                                    }, DRAWER_DELAY);
                                    break;
                                case R.id.myRegistrations_menuItem:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().popBackStackImmediate();
                                            fragmentTransaction.replace(R.id.fragment_container, new FavoritesFragment());
                                            appBarLayout.setExpanded(false, true);
                                            fragmentTransaction.addToBackStack(null).commit();
                                            collapsingToolbarLayout.setTitle("My Registrations");
                                        }
                                    }, DRAWER_DELAY);
                                    break;

                                case R.id.view_events:
                                    Intent j = new Intent(MainActivity.this,MapsActivity.class);
                                    startActivity(j);
                                    break;

                                case R.id.quiz_menuItem:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().popBackStackImmediate();
                                            fragmentTransaction.replace(R.id.fragment_container, new QuizFragment());
                                            appBarLayout.setExpanded(false, true);
                                            fragmentTransaction.addToBackStack(null).commit();
                                            collapsingToolbarLayout.setTitle("Weekly Quiz");
                                        }
                                    }, DRAWER_DELAY);

                                    break;

                                case R.id.chat:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().popBackStackImmediate();
                                            fragmentTransaction.replace(R.id.fragment_container, new ChatFragment());
                                            appBarLayout.setExpanded(false, true);
                                            fragmentTransaction.addToBackStack(null).commit();
                                            collapsingToolbarLayout.setTitle("Queries");
                                        }
                                    }, DRAWER_DELAY);

                                    break;

                                case R.id.sponsors_menuItem:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().popBackStackImmediate();
                                            fragmentTransaction.replace(R.id.fragment_container, new SponsorsFragment());
                                            appBarLayout.setExpanded(false, true);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            collapsingToolbarLayout.setTitle("Sponsors");
                                        }
                                    }, DRAWER_DELAY);
                                    break;


                                case R.id.commitee_menuItem:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().popBackStackImmediate();
                                            fragmentTransaction.replace(R.id.fragment_container, new CommitteeFragment());
                                            appBarLayout.setExpanded(false, true);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            collapsingToolbarLayout.setTitle("Committee");
                                        }
                                    }, DRAWER_DELAY);
                                    break;

                                case R.id.developers_menuItem:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().popBackStackImmediate();
                                            fragmentTransaction.replace(R.id.fragment_container, new DevelopersFragment());
                                            appBarLayout.setExpanded(false, true);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            collapsingToolbarLayout.setTitle("Developers");
                                        }
                                    }, DRAWER_DELAY);
                                    break;

                                case R.id.myEvents_menuItem:
                                    userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                    String x = userInfo.getString("type", "Guest");
                                    if (x.equals("Event Organiser") || x.equals("Supervisor")) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getSupportFragmentManager().popBackStackImmediate();
                                                fragmentTransaction.replace(R.id.fragment_container, new MyEventsFragment());
                                                appBarLayout.setExpanded(false, true);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                                collapsingToolbarLayout.setTitle("My Events");

                                            }
                                        }, DRAWER_DELAY);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Guests cant organize an event :(", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case R.id.contact_us_menuItem:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().popBackStackImmediate();
                                            fragmentTransaction.replace(R.id.fragment_container, new ContactUsFragment());
                                            appBarLayout.setExpanded(false, true);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            collapsingToolbarLayout.setTitle("Contact us");
                                        }
                                    }, DRAWER_DELAY);
                                    break;

                                case R.id.about_menuItem:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().popBackStackImmediate();
                                            fragmentTransaction.replace(R.id.fragment_container, new AboutAppFragment());
                                            appBarLayout.setExpanded(false, true);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            collapsingToolbarLayout.setTitle(getResources().getString(R.string.aboutapp));
                                        }
                                    }, DRAWER_DELAY);
                                    break;
                                case R.id.sign_out:
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            boolean isFragmentInStack = fm.popBackStackImmediate(backStageName, 0);
                                            if (!isFragmentInStack) {
                                                MainFragment fragment = MainFragment.newInstance();
                                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                                backStageName = fragment.getClass().getName();
                                                fragmentTransaction.addToBackStack(backStageName).commit();
                                            }
                                            appBarLayout.setExpanded(true, true);
                                            collapsingToolbarLayout.setTitle("Matrix 17");
                                        }
                                    }, DRAWER_DELAY);
                                    AuthUI.getInstance()
                                            .signOut(MainActivity.this)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    i = 1;
                                                    navigationView.getMenu().getItem(0).setChecked(true);
                                                }
                                            });

                            }
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Uri uri = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            //case R.id.follow_us:
            //  return true;
            //case R.id.menu_visit_website:
            //  uri = Uri.parse(getResources().getString(R.string.matrix_website));
            //break;
            case R.id.menu_follow_facebook:
                uri = Uri.parse(getResources().getString(R.string.matrix_fb_link));
                break;
            // case R.id.menu_follow_twitter:
            //   uri = Uri.parse(getResources().getString(R.string.matrix_twit_link));
            // break;
            case R.id.menu_follow_instagram:
                uri = Uri.parse(getResources().getString(R.string.matrix_insta_link));
                break;
            case R.id.menu_follow_snapchat:
                uri = Uri.parse(getResources().getString(R.string.matrix_snap_link));
                break;
            //case R.id.menu_sign_out:

        }

        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else {
            navigationView.getMenu().getItem(0).setChecked(true);
            collapsingToolbarLayout.setTitle("Matrix 17");
            appBarLayout.setExpanded(true, true);

            super.onBackPressed();
        }
    }


}
