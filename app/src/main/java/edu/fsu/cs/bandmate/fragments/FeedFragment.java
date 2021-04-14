package edu.fsu.cs.bandmate.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.adapters.ProfileAdapter;


public class FeedFragment extends Fragment{
    private static final String TAG = FeedFragment.class.getCanonicalName();
    CardStackView rvFeed;
    ProfileAdapter profileAdapter;
    List<Profile> profileList;
    Context context;
    CardStackListener cardStackListener = new CardStackListener() {
        @Override
        public void onCardDragging(Direction direction, float ratio) {

        }

        @Override
        public void onCardSwiped(Direction direction) {
            if(direction==Direction.Left){
                Toast.makeText(context, "Left Swipe", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Right Swipe", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCardRewound() {

        }

        @Override
        public void onCardCanceled() {

        }

        @Override
        public void onCardAppeared(View view, int position) {
            Log.i(TAG,"Appeared: "+profileList.get(position).getName()+" position: "+position);

        }

        @Override
        public void onCardDisappeared(View view, int position) {
            Log.i(TAG,"Disappeared: "+profileList.get(position).getName()+" position: "+position);
        }
    };


    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: Change RecyclerView to CardView(I think?) and implement swipe to match
        context=getContext();
        rvFeed = view.findViewById(R.id.rvFeed);
        CardStackLayoutManager cardStackLayoutManager = new CardStackLayoutManager(context,cardStackListener);
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        cardStackLayoutManager.setCanScrollVertical(false);

        rvFeed.setLayoutManager(cardStackLayoutManager);

        profileList = new ArrayList<>();

        profileAdapter = new ProfileAdapter(profileList, context);

        rvFeed.setAdapter(profileAdapter);

        queryProfiles();
    }

    private void queryProfiles() {
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
        query.whereNotEqualTo(Profile.KEY_USER,ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> profiles, ParseException e) {
                if(e!=null){
                    Log.e("ParseQuery", "Error Querying Profiles", e);
                    return;
                }

                profileAdapter.addAll(profiles);
                profileAdapter.notifyDataSetChanged();
            }
        });
    }
    public void onOpenProfile(){
        ProfileFragment fragment = new ProfileFragment();
        String tag = ProfileFragment.class.getCanonicalName();
        getFragmentManager().beginTransaction().replace(R.id.fragmentFrame,fragment,tag).commit();
    }
    public interface feedListener{
        void profileView();
    }
}