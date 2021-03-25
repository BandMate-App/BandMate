package edu.fsu.cs.bandmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.fsu.cs.bandmate.fragments.FeedFragment;
import edu.fsu.cs.bandmate.fragments.LoginFragment;
import edu.fsu.cs.bandmate.fragments.MainFragment;
import edu.fsu.cs.bandmate.fragments.MessagesFragment;
import edu.fsu.cs.bandmate.fragments.ProfileFragment;
import edu.fsu.cs.bandmate.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.mainFragmentListener,
        LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener,BottomNavigationView.OnNavigationItemSelectedListener {
    private Boolean m_loggedIn = false;
    private BottomNavigationView bottomNavigationView;


    public void onMain(){
        MainFragment fragment = new MainFragment();
        String tag = MainFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commitNow();

    }

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


    }

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
    public void onRegisterComplete(User user) {
        m_loggedIn = true;
        onFeed();
        getSupportFragmentManager().executePendingTransactions();
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCancel() {
        onMain();
    }

    public void onFeed(){
        FeedFragment fragment = new FeedFragment();
        String tag = FeedFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commit();
    }

    public void onOpenMessages(){
        MessagesFragment fragment = new MessagesFragment();
        String tag = MessagesFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commit();
    }

    public void onOpenProfile(){
        ProfileFragment fragment = new ProfileFragment();
        String tag = ProfileFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.itFeed:
                onFeed();
                break;
            case R.id.itMessages:
                onOpenMessages();
                break;
            case R.id.itProfile:
                onOpenProfile();
                break;
        }
        return false;
    }
}