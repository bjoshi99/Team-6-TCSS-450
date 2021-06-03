package edu.uw.team6tcss450;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.team6tcss450.databinding.ActivityMainBinding;
import edu.uw.team6tcss450.model.LocationViewModel;
import edu.uw.team6tcss450.model.NewMessageCountViewModel;
import edu.uw.team6tcss450.model.UserInfoViewModel;
import edu.uw.team6tcss450.services.PushReceiver;
import edu.uw.team6tcss450.ui.chat.ChatMessage;
import edu.uw.team6tcss450.ui.chat.ChatViewModel;
import edu.uw.team6tcss450.ui.contact.Contact;
import edu.uw.team6tcss450.ui.contact.ContactModel;
import edu.uw.team6tcss450.ui.home.HomeNotificationDetail;
import edu.uw.team6tcss450.ui.home.HomeRecyclerViewAdapter;
import edu.uw.team6tcss450.ui.home.HomeViewModel;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;
    private MainPushMessageReceiver mPushMessageReceiver;
    private NewMessageCountViewModel mNewMessageModel;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
    private LocationRequest mLocationRequest;
    //Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;
    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;
    //The ViewModel that will store the current location
    private LocationViewModel mLocationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt(), args.getUsername())
        ).get(UserInfoViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.chat_list_fragment, R.id.navigation_contact, R.id.navigation_weather)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.chat_list_fragment) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.reset();
            }
            if (destination.getId() == R.id.navigation_contact) {
                BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_contact);
                badge.setVisible(false);
            }
        });
        mNewMessageModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.chat_list_fragment);
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            //The user has already allowed the use of Locations. Get the current location.
            requestLocation();
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Log.d("LOCATION UPDATE!", location.toString());
                    if (mLocationModel == null) {
                        mLocationModel = new ViewModelProvider(MainActivity.this)
                                .get(LocationViewModel.class);
                    }
                    mLocationModel.setLocation(location);
                }
            };
        };
        createLocationRequest();
    }

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {
        private ChatViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);

        //for contact request
        private HomeViewModel mHomeModel =
                new ViewModelProvider(MainActivity.this)
                    .get(HomeViewModel.class);

        private ContactModel mContactModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ContactModel.class);

        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            if (intent.hasExtra("chatMessage")) {
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                System.out.println("Before if in onRecieve");
                if (nd.getId() != R.id.chatFragment) {
                    mNewMessageModel.increment();

                    //add to home for notification
                    mHomeModel.addNotification("New chat message from " + cm.getSender());
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);

            }

            if(intent.hasExtra("ContactRequest")){
                if(nd.getId() != R.id.navigation_contact){
                    //display a badge on home tab
                    //...
                    BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_contact);
                    badge.setVisible(true);
                }

                String[] responses = intent.getStringExtra("ContactRequest").split(":");
                String msg="", eml="";
//                int id = intent.getIntExtra("memberid", 0);

                if(responses.length > 1){
                    msg = responses[0];
                    eml = responses[1];
                }

                mHomeModel.addNotification(msg);

                //to get name, substring from 25 to length
                String name = (msg).substring(25);

                Contact tmp = new Contact.Builder(name, name, eml).build();
                tmp.req = true;
                mContactModel.addRequest(tmp);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
            System.out.println("in if onresume");
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
        //stopLocationUpdates();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null){
            unregisterReceiver(mPushMessageReceiver);
        }
        //stopLocationUpdates();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//            navController.navigateUp();
            navController.navigate(R.id.settingFragment2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION DENIED", "Nothing to see or do here.");

                    //Shut down the app. In production release, you would let the user
                    //know why the app is shutting down...maybe ask for permission again?
                    finishAndRemoveTask();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("LOCATION", location.toString());
                                if (mLocationModel == null) {
                                    mLocationModel = new ViewModelProvider(MainActivity.this)
                                            .get(LocationViewModel.class);
                                }
                                mLocationModel.setLocation(location);
                            }
                        }
                    });
        }
    }

    /**
     * Create and configure a Location Request used when retrieving location updates
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }
    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

}