package edu.fsu.cs.bandmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.fsu.cs.bandmate.fragments.FeedFragment;
import edu.fsu.cs.bandmate.fragments.MessagesFragment;
import edu.fsu.cs.bandmate.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.mainFragmentListener,
        LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener {
    FeedFragment feedFragment;
    MessagesFragment messagesFragment;
    ProfileFragment profileFragment;
    BottomNavigationView bottomNavigationView;
    Boolean loggedIn = false;

    public void onMain(){
        MainFragment fragment = new MainFragment();
        String tag = MainFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commitNow();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onMain();

        /*
        TODO Find a way to implement the ui so the bottom nav bar does not show until the user has logged int. Maybe start a second activity after login
         */

        /*
        if(loggedIn) {
            if (feedFragment == null)
                feedFragment = new FeedFragment();
            if (profileFragment == null)
                profileFragment = new ProfileFragment();
            if (messagesFragment == null)
                messagesFragment = new MessagesFragment();


            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = new Fragment();
                    switch (item.getItemId()) {
                        case R.id.itFeed:
                            Toast.makeText(MainActivity.this, "Navigated to FeedFragment", Toast.LENGTH_SHORT).show();
                            fragment = feedFragment;
                            break;
                        case R.id.itMessages:
                            Toast.makeText(MainActivity.this, "Navigated to MessagesFragment", Toast.LENGTH_SHORT).show();
                            fragment = messagesFragment;
                            break;
                        case R.id.itProfile:
                            Toast.makeText(MainActivity.this, "Navigated to ProfileFragment", Toast.LENGTH_SHORT).show();
                            fragment = profileFragment;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    return true;
                }
            });
        }

         */
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

        /*
         TODO Switch to the matching interface of the app with bottom nav
         */
        Intent intent = new Intent(this, BottomNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRegisterComplete() {
        Intent intent = new Intent(this, BottomNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCancel() {
        onMain();

    }
}