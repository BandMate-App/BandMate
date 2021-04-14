package edu.fsu.cs.bandmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.fsu.cs.bandmate.adapters.MessageListAdapter;
import edu.fsu.cs.bandmate.fragments.ChatFragment;
import edu.fsu.cs.bandmate.fragments.EditProfileFragment;
import edu.fsu.cs.bandmate.fragments.FeedFragment;
import edu.fsu.cs.bandmate.fragments.LoginFragment;
import edu.fsu.cs.bandmate.fragments.MainFragment;
import edu.fsu.cs.bandmate.fragments.MessagesFragment;
import edu.fsu.cs.bandmate.fragments.ProfileFragment;
import edu.fsu.cs.bandmate.fragments.RegisterFragment;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity implements
        MainFragment.mainFragmentListener,
        LoginFragment.LoginFragmentListener,
        RegisterFragment.RegisterFragmentListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.ProfileFragmentInterface,
        EditProfileFragment.EditProfileFragmentInterface,
          MessagesFragment.MessagesHost, FeedFragment.feedListener
{
    private Boolean m_loggedIn = false;
    private BottomNavigationView bottomNavigationView;
    private AlertDialog dialog;
    private Menu m_menu = null;
    public static final String TAG=MainActivity.class.getCanonicalName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        if(!m_loggedIn)
            bottomNavigationView.setVisibility(View.GONE);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        onMain();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Log.i(TAG,"User already logged in, proceding to home screen");
        if(ParseUser.getCurrentUser()!=null){
            ParseQuery<ConversationList> query = ParseQuery.getQuery(ConversationList.class);
            query.include(ConversationList.KEY_USER);
            try {
                query.whereEqualTo(ConversationList.KEY_USER, ParseUser.getCurrentUser().fetchIfNeeded());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ConversationList conversationList = null;
            try {
                conversationList = query.getFirst();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (conversationList == null) {
                ParseObject messagesList = ParseObject.create("ConversationList");
                messagesList.put(ConversationList.KEY_USER, ParseUser.getCurrentUser());
                messagesList.saveInBackground();
            }
            onValidLogin();
        }

    }

    /*
     Menu Functions
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.logout){
            ParseUser.logOutInBackground();
            bottomNavigationView.setVisibility(View.GONE);
            onMain();
        }
        else if(id == R.id.quit){
            //TODO confrim dialog
            confirmQuit();
            dialog.show();
        }
        return true;
    }

    public void confirmQuit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Quit");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing if cancel is clicked
            }
        });
        dialog = builder.create();
    }

    // Set the logout item to be visible only if the current user != null
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem logout = menu.findItem(R.id.logout);
        logout.setVisible(ParseUser.getCurrentUser()!= null);
        return true;
    }

    /*
    End Menu Functions
     */


    /*
     Fragment interface function implementations
     */

    @Override
    public void onLogin() {
        LoginFragment fragment = new LoginFragment();
        String tag = LoginFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment).commitNow();

    }

    @Override
    public void onRegister() {
        RegisterFragment fragment = new RegisterFragment();
        String tag = RegisterFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment).commitNow();

    }

    @Override
    public void onValidLogin() {
        m_loggedIn = true;
        onFeed();
        getSupportFragmentManager().executePendingTransactions();
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRegisterComplete() {

        m_loggedIn = true;
        onFeed();
        getSupportFragmentManager().executePendingTransactions();
        /*
         Update the appropriate views to be visible to the user
         */
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCancel() {
        onMain();
    }
    /*
      End fragment interface function implementations
     */


    /*
     Fragment Navigation
     */

    public void onFeed(){
        FeedFragment fragment = new FeedFragment();
        String tag = FeedFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commit();
    }

    public void onMain(){
        MainFragment fragment = new MainFragment();
        String tag = MainFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commitNow();

    }

    public void onOpenMessages(){
        MessagesFragment fragment = new MessagesFragment();
        String tag = MessagesFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commit();
    }

    public void onOpenProfile(){
        ProfileFragment fragment = new ProfileFragment();
        //String tag = ProfileFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment).commit();
    }

    /*
     End Fragment Navigation
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.itFeed) {
                onFeed();
                return true;
        }
        if(id == R.id.itMessages) {
                onOpenMessages();
                return true;
        }
        if(id == R.id.itProfile) {
            onOpenProfile();
            return true;
        }
    return false;
    }


    @Override
    public void onConversationClick(SelectedConversation selected) {
        ChatFragment fragment = new ChatFragment();
        String tag = ChatFragment.class.getCanonicalName();
        getIntent().putExtra("selected",selected);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment).commitNow();
    }

    @Override
    public boolean isConversationSelected(String conversationId) {
        return false;
    }
    @Override
    public void openEditProfileFragment() {
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        //String tag = EditProfileFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,editProfileFragment).commit();
    }

    @Override
    public void ExitEditProfileFragment() {
        bottomNavigationView.setSelectedItemId(R.id.itProfile);
    }

    @Override
    public void profileView() {

    }

    @Override
    public void addLikedUser(ParseUser liked_user) throws ParseException {
        boolean match = false;
        ParseUser user = ParseUser.getCurrentUser();
        user.fetchIfNeeded().add("liked_users",user.fetchIfNeeded());
        user.saveEventually();
        ArrayList<ParseUser> liked_users_other =(ArrayList<ParseUser>) liked_user.fetchIfNeeded().get("liked_users");

        for (ParseUser u: liked_users_other){
            if(u.fetchIfNeeded().getObjectId().equals(user.fetchIfNeeded().getObjectId())){
                match = true;
                break;
            }
        }
        if(match){
            createConversation(liked_user);
        }

    }

    private void createConversation(ParseUser matched_user) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser().fetchIfNeeded();
        matched_user.fetchIfNeeded();

        /*
        Query Each Conversation List
         */
        ParseQuery<ConversationList> query_self = ParseQuery.getQuery(ConversationList.class);
        query_self.whereEqualTo(ConversationList.KEY_USER,user);
        query_self.setLimit(1);
        ConversationList cvListSelf = query_self.getFirst().fetchIfNeeded();

        ParseQuery<ConversationList> query_other = ParseQuery.getQuery(ConversationList.class);
        query_other.whereEqualTo(ConversationList.KEY_USER,user);
        query_other.setLimit(1);
        ConversationList cvListOther = query_other.getFirst().fetchIfNeeded();


        /*
        Initialize each Conversation Object
         */
        ParseObject cvSelf = ParseObject.create("Conversation");
        cvSelf.put(Conversation.KEY_SELF,user.fetchIfNeeded());
        cvSelf.put(Conversation.KEY_OTHER,matched_user.fetchIfNeeded());
        ArrayList<Message> messagesSelf = new ArrayList<Message>();
        cvSelf.put(Conversation.KEY_MESSAGEOBJECT,messagesSelf);
        cvSelf.put("c_list1",cvListSelf.fetchIfNeeded());
        cvSelf.put("c_list2",cvListOther.fetchIfNeeded());


        ParseObject cvOther = ParseObject.create("Conversation");
        cvOther.put(Conversation.KEY_SELF,matched_user.fetchIfNeeded());
        cvOther.put(Conversation.KEY_OTHER,user.fetchIfNeeded());
        ArrayList<Message> messagesOther = new ArrayList<Message>();
        cvOther.put(Conversation.KEY_MESSAGEOBJECT,messagesOther);
        cvOther.put("c_list1",cvListOther.fetchIfNeeded());
        cvOther.put("c_list2",cvListSelf.fetchIfNeeded());


        /*
         Put each conversation Object in it's corresponding conversation list, now each user should be able to message each other
         */

        //TODO create a notification here
        cvListSelf.add(ConversationList.KEY_CONVERSATION,cvSelf);
        cvListOther.add(ConversationList.KEY_CONVERSATION,cvOther);

    }
}
