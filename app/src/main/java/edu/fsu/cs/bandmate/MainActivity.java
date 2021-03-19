package edu.fsu.cs.bandmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.fsu.cs.bandmate.fragments.FeedFragment;
import edu.fsu.cs.bandmate.fragments.MessagesFragment;
import edu.fsu.cs.bandmate.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    FeedFragment feedFragment;
    MessagesFragment messagesFragment;
    ProfileFragment profileFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(feedFragment==null)
            feedFragment=new FeedFragment();
        if(profileFragment==null)
            profileFragment=new ProfileFragment();
        if(messagesFragment==null)
            messagesFragment=new MessagesFragment();


        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new Fragment();
                switch(item.getItemId()){
                    case R.id.itFeed:
                        Toast.makeText(MainActivity.this, "Navigated to FeedFragment", Toast.LENGTH_SHORT).show();
                        fragment=feedFragment;
                        break;
                    case R.id.itMessages:
                        Toast.makeText(MainActivity.this, "Navigated to MessagesFragment", Toast.LENGTH_SHORT).show();
                        fragment=messagesFragment;
                        break;
                    case R.id.itProfile:
                        Toast.makeText(MainActivity.this, "Navigated to ProfileFragment", Toast.LENGTH_SHORT).show();
                        fragment=profileFragment;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,fragment).commit();
                return true;
            }
        });


    }
}