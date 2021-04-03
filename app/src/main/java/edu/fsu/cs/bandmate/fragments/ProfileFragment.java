package edu.fsu.cs.bandmate.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import edu.fsu.cs.bandmate.Profile;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ParseUser user;
    TextView profileUserName;
    TextView profileFirstName;
    TextView profileBirthday;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //If the user doesn't have a profile, create a profile for them and upload it
        user = ParseUser.getCurrentUser();
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
                        user.saveEventually();
                    }
                }
            });
        }
        profileUserName = view.findViewById(R.id.profileUsernameValue);
        profileFirstName = view.findViewById(R.id.profileFirstNameValue);
        profileBirthday = view.findViewById(R.id.profileBirthdayValue);
        try {
            queryProfile();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void queryProfile() throws ParseException {
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
        query.whereEqualTo(Profile.KEY_USER, user);
        List<Profile> userProfile = query.find();

        if (userProfile.size() != 1)
            Toast.makeText(getActivity(), "error getting profile", Toast.LENGTH_SHORT).show();

        String username = userProfile.get(0).getName();
        String birthday = userProfile.get(0).getBirthday();
        //String firstName = userProfile.get(0).get
        profileFirstName.setText(username);
        profileBirthday.setText(birthday);
    }
}