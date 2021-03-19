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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.adapters.ProfileAdapter;


public class FeedFragment extends Fragment {
    RecyclerView rvFeed;
    ProfileAdapter profileAdapter;
    List<Profile> profileList;
    Context context;


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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvFeed.setLayoutManager(linearLayoutManager);

        profileList = new ArrayList<>();

        profileAdapter = new ProfileAdapter(profileList, context);

        rvFeed.setAdapter(profileAdapter);

        queryProfiles();
    }

    private void queryProfiles() {
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
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
}