package edu.uw.team6tcss450;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.team6tcss450.databinding.ActivityMainBinding;
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
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null){
            unregisterReceiver(mPushMessageReceiver);
        }
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
}