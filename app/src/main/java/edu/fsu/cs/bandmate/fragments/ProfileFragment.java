package edu.fsu.cs.bandmate.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import edu.fsu.cs.bandmate.Profile;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    ProfileFragmentInterface profileFragmentInterface;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ParseUser user;
    TextView profileUserName;
    TextView profileFirstName;
    TextView profileBirthday;
    TextView profileGenre;
    TextView profilePrimaryInstrument;
    TextView profileSecondaryInstrument;
    TextView profileGender;
    ImageView profileImage;
    Button buttonEdit;

    private String bundleUserName;

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

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Gets bundle if its not null and retrieves
     * the profileUsername.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null){
            bundleUserName = bundle.getString("profileUsername");
        }
    }

    /**
     * Inflates fragment_profile on creation of view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    /**
     * When view is created, set component views for ProfileFragment
     * enable and show edit button if the logged in user profile is being shown,
     * or disable and hide if loaded user from bundle.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonEdit=view.findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragmentInterface.openEditProfileFragment();
            }
        });

        createUserProfileIfNeeded();
        profileUserName = view.findViewById(R.id.profileUsernameValue);
        profileFirstName = view.findViewById(R.id.profileFirstNameValue);
        profileBirthday = view.findViewById(R.id.profileBirthdayValue);
        profileGenre = view.findViewById(R.id.profileGenreValue);
        profilePrimaryInstrument = view.findViewById(R.id.profilePrimaryInstrumentValue);
        profileSecondaryInstrument = view.findViewById(R.id.profileSecondaryInstrymentValue);
        profileGender = view.findViewById(R.id.profileGenderValue);
        profileImage = view.findViewById(R.id.profileImageValue);

        try {
            if (bundleUserName != null){
                queryProfile(bundleUserName);
                buttonEdit.setEnabled(false);
                buttonEdit.setVisibility(View.GONE);
            }
            else{
                queryProfile(user.getUsername());
                buttonEdit.setEnabled(true);
                buttonEdit.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    /**
     * Ensures context is as expected when fragments is attached
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragmentInterface) {
            profileFragmentInterface = (ProfileFragmentInterface) context;
        }else{
            throw new RuntimeException("Must implement ProfileFragmentInterface");
        }
    }

    /**
     * creates user profile is its needed
     * as determined by whether the user object
     * contains myProfile object.
     */
    public void createUserProfileIfNeeded(){
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
                        user.saveInBackground();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Welcome to Band Mate!");
                        builder.setMessage("It looks like you haven't set up your profile yet, would you like to do that now?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                profileFragmentInterface.openEditProfileFragment();
                            }
                        });
                        builder.setNegativeButton("Maybe Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }
            });
        }
    }//End of createUserProfileIfNeeded

    /**
     * Queries backend service for user, then loads profile.
     * Once the ParseUser and Profile are populated, set the
     * appropriate values in the profile view.
     *
     * @param userName
     * @throws ParseException
     */
    private void queryProfile(String userName) throws ParseException {
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereEqualTo("username", userName);
        List<ParseUser> users = userParseQuery.find();
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
        query.whereEqualTo(Profile.KEY_USER, users.get(0));
        List<Profile> userProfile = query.find();

        StringBuilder sb = new StringBuilder();
        String username = user.getUsername();
        String name = userProfile.get(0).getName();
        String birthday = userProfile.get(0).getBirthday();
        String genre = userProfile.get(0).getGenre();
        String primaryInstrument = userProfile.get(0).getPrimaryInstrument();
        ArrayList<String> secondaryInstrumentsList = userProfile.get(0).getSecondaryInstruments();
        for (String element: secondaryInstrumentsList) {
            sb.append(element).append('\n');
        }
        String secondaryInstruments = sb.toString();
        String gender = userProfile.get(0).getGender();

        Glide.with(getContext())
                .load(userProfile.get(0).getImage().getUrl())
                .into(profileImage);

        profileUserName.setText(userName);
        profileFirstName.setText(name);
        profileBirthday.setText(birthday);
        profileGenre.setText(genre);
        profilePrimaryInstrument.setText(primaryInstrument);
        profileSecondaryInstrument.setText(secondaryInstruments);
        profileGender.setText(gender);

    }

    /**
     * Interface to open edit profile fragment
     */
    public interface ProfileFragmentInterface{
        public void openEditProfileFragment();
    }


}//end Profile Fragment class

