package edu.fsu.cs.bandmate.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.parse.ParseUser;
import com.yuyakaido.android.cardstackview.StackFrom;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.fsu.cs.bandmate.MainActivity;
import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.adapters.ProfileAdapter;


public class FeedFragment extends Fragment{
    private static final String TAG = FeedFragment.class.getCanonicalName();
    CardStackView rvFeed;
    ProfileAdapter profileAdapter;
    List<Profile> profileList;
    feedListener listener;
    Context context;
    Profile profileForCurrentUser;
    int intProfileOnTop;
    CardStackLayoutManager cardStackLayoutManager;
    CardStackListener cardStackListener = new CardStackListener() {
        @Override
        public void onCardDragging(Direction direction, float ratio) {

        }

        //Listen to see if the profile was swiped off screen
        //If the swipe was a left swipe no action is taken
        //if the swipe was a right swipe the profile is "liked" and the database updated
        //if the other profile had already liked the user, then the users are added to each others mutual matches
        //and the user that committed the "final like" gets an alert notifying them of the match
        @Override
        public void onCardSwiped(Direction direction) {
            if(direction==Direction.Left){
                Toast.makeText(context, "Left Swipe", Toast.LENGTH_SHORT).show();
            }else{
                int liked_index = cardStackLayoutManager.getTopPosition()-1;
                try {
                    Profile liked_profile = profileList.get(liked_index).fetchIfNeeded();
                    ParseUser liked_user = Objects.requireNonNull(liked_profile.fetchIfNeeded().getParseUser("user")).fetchIfNeeded();
                    listener.addLikedUser(liked_user.fetchIfNeeded());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "Right Swipe Liked", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"Liked: "+profileList.get(intProfileOnTop).getName()+" position: "+intProfileOnTop);
                profileForCurrentUser.likeAUser(profileList.get(intProfileOnTop));

                Profile likedProfile = profileList.get(intProfileOnTop);

                        likedProfile.getRelation(Profile.KEY_WHO_I_LIKE)
                        .getQuery()
                        .whereEqualTo(Profile.KEY_OBJECT_ID,profileForCurrentUser.getObjectId())
                        .findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e!=null){
                                    Log.e(TAG,"Error Querying for matches: ");
                                }else if(objects.size()!=0){
                                    likedProfile.getRelation(Profile.KEY_MUTUAL_MATCH).add(profileForCurrentUser);
                                    likedProfile.saveInBackground();
                                    profileForCurrentUser.getRelation(Profile.KEY_MUTUAL_MATCH).add(likedProfile);
                                    profileForCurrentUser.saveInBackground();
                                    AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                                    builder.setTitle("It's A Match!");
                                    builder.setMessage("Why don't you try sending your new match a message?");
                                    builder.create().show();
                                }
                            }
                        });
            }
            if(intProfileOnTop==profileList.size()-1){
                profileList.clear();
                profileAdapter.notifyItemRangeRemoved(0,intProfileOnTop+1);
                queryProfiles();
            }
        }

        @Override
        public void onCardRewound() {

        }

        @Override
        public void onCardCanceled() {

        }

        //Logging for debugging and tracking the currently visible profile
        @Override
        public void onCardAppeared(View view, int position) {
            Log.i(TAG,"Appeared: "+profileList.get(position).getName()+" position: "+position);
            intProfileOnTop=position;
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
        context=getContext();
        rvFeed = view.findViewById(R.id.rvFeed);
        //Initializing the layout manager for the feed
        //so that the profiles can only be swiped horizontally
        cardStackLayoutManager = new CardStackLayoutManager(context,cardStackListener);
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        cardStackLayoutManager.setCanScrollVertical(false);
        cardStackLayoutManager.setScaleInterval(0.95f);
        cardStackLayoutManager.setTranslationInterval(8.0f);
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setStackFrom(StackFrom.TopAndRight);


        rvFeed.setLayoutManager(cardStackLayoutManager);
        profileList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(profileList, context);
        rvFeed.setAdapter(profileAdapter);

        //Querying to get the current user's profile,
        //If for some reason the user does not have an existing profile (note: profiles should be
        // automatically created upon registration), a new profile is created.
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
        query.include(Profile.KEY_WHO_LIKES_ME);
        query.include(Profile.KEY_WHO_I_LIKE);
        query.whereEqualTo(Profile.KEY_USER,ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> objects, ParseException e) {
                if(e!=null){
                    Log.e(TAG,"Error Getting Profile", e);
                }else{
                    try {
                        profileForCurrentUser = objects.get(0);
                        queryProfiles();
                    }catch(IndexOutOfBoundsException exception){
                        createUserProfileIfNeeded();
                    }
                }
            }
        });

    }

    //The queryProfiles function looks for new profiles to populate the user's feed
    //cautions are taken so that the user does not encounter their own profile and that
    //the user doesn't encounter a profile that they have already liked.
    //However, a user can re-encounter a profile that they had previously skipped.
    private void queryProfiles() {
        ParseRelation<ParseObject> relationWhoILike = profileForCurrentUser.getRelation(Profile.KEY_WHO_I_LIKE);
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        relationWhoILike.getQuery().include(Profile.KEY_OBJECT_ID).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    Log.e(TAG,"Error:" ,e);
                }else{
                    List<String> exclusions = new ArrayList<>();
                    for(ParseObject object: objects){
                        exclusions.add(object.getObjectId());
                    }
                    query.include(Profile.KEY_USER);
                    query.include(Profile.KEY_WHO_LIKES_ME);
                    query.include(Profile.KEY_WHO_I_LIKE);
                    query.whereNotEqualTo(Profile.KEY_USER,ParseUser.getCurrentUser());
                    query.whereNotContainedIn(Profile.KEY_OBJECT_ID,exclusions);
                    query.findInBackground(new FindCallback<Profile>() {
                        @Override
                        public void done(List<Profile> profiles, ParseException e) {
                            if(e!=null){
                                Log.e("ParseQuery", "Error Querying Profiles", e);
                                return;
                            }

                            profileAdapter.addAll(profiles);
                            profileAdapter.notifyItemRangeInserted(0,profiles.size());
                        }
                    });
                }
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
        void addLikedUser(ParseUser liked_user) throws ParseException;

        void openEditProfileFragment();
    }

    //detaching an interface
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //Attaching an interface
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof FeedFragment.feedListener) {
            listener = (FeedFragment.feedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FeedListener");
        }
    }

    //This function is unlikely to ever be called but is placed here anyway for safety concerns.
    //Upon registration a profile is created for the newly registered user, the profile contains a
    //user's outward facing information such as name, biography, etc.
    //If the profile fails to be created and the user makes it to the feed fragment this function
    //is meant to act as a failsafe to prevent the app from crashing.
    //I would have liked to put this code serverside so the chance of profile creation failing is
    //lower but I don't have a strong enough understanding of javascript required for serverside code.
    public void createUserProfileIfNeeded(){
        ParseUser user = ParseUser.getCurrentUser();
        if(user.getParseObject("myProfile")==null){
            Profile prof = new Profile();
            prof.putUser(user);
            prof.putName(user.getString("Name"));
            prof.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        Toast.makeText(getActivity(), "Error creating profile", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Profile successfully created", Toast.LENGTH_SHORT).show();
                        user.put("myProfile",prof);
                        user.saveInBackground();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Welcome to Band Mate!");
                        builder.setMessage("It looks like you haven't set up your profile yet, would you like to do that now?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.openEditProfileFragment();
                            }
                        });
                        builder.setNegativeButton("Maybe Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        queryProfiles();
                    }
                }
            });
        }
    }//End of createUserProfileIfNeeded
}