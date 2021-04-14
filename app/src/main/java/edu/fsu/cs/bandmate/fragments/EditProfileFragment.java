package edu.fsu.cs.bandmate.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseFileUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;


public class EditProfileFragment extends Fragment {
    public static final int PICK_PROFILE_PIC_REQUEST_CODE=0;
    public static final int PICK_PROFILE_MP3_REQUEST_CODE=1;
    private static final String TAG =EditProfileFragment.class.getCanonicalName();
    private static final String PHOTO_FILE_NAME ="photo.jpg";

    private ParseFile parseFileProfilePic;
    private ParseFile parseFileProfileMp3;
    private ParseUser currentParseUser;
    private Profile currentProfile;

    ImageView imageViewProfilePic;
    ImageButton imageButtonProfilePic;
    ImageButton imageButtonUploadMP3;
    EditText editTextName;
    Spinner spinnerPrimaryGenre;
    Spinner spinnerPrimaryInstrument;
    EditText editTextSecondaryGenre;
    EditText editTextSecondaryInstrument;
    EditText editTextBio;
    Button buttonSave;
    Button buttonExit;

    boolean hasGenreSpinnerChanged;
    boolean hasInstrumentSpinnerChanged;

    EditProfileFragmentInterface editProfileFragmentInterface;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentParseUser=ParseUser.getCurrentUser();
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
        query.whereEqualTo(Profile.KEY_USER,ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> objects, ParseException e) {
                if(e!=null){
                    Log.e(TAG,"Error querying Profile: ",e);
                }else{
                    currentProfile=objects.get(0);
                    try {
                        parseFileProfileMp3 = currentProfile.getProfileMP3();
                    }catch(NullPointerException | IllegalStateException ignored){
                        parseFileProfileMp3=null;
                    }


                    parseFileProfilePic=currentProfile.getImage();

                    //Populating with view with existing user information
                    Glide.with(getContext())
                            .load(currentProfile.getImage().getUrl())
                            .into(imageViewProfilePic);

                    editTextName.setText(currentProfile.getName());
                    editTextBio.setText(currentProfile.getBio());
                }
            }
        });

        imageViewProfilePic=view.findViewById(R.id.imageViewProfilePic);
        imageButtonProfilePic=view.findViewById(R.id.imageButtonProfilePic);
        imageButtonUploadMP3=view.findViewById(R.id.imageButtonUploadMP3);
        editTextName=view.findViewById(R.id.editTextName);
        spinnerPrimaryGenre=view.findViewById(R.id.spinnerPrimaryGenre);
        editTextSecondaryGenre=view.findViewById(R.id.editTextSecondaryGenre);
        spinnerPrimaryInstrument=view.findViewById(R.id.spinnerPrimaryInstrument);
        editTextSecondaryInstrument=view.findViewById(R.id.editTextSecondaryInstrument);
        editTextBio=view.findViewById(R.id.editTextBio);
        buttonSave=view.findViewById(R.id.buttonSave);
        buttonExit=view.findViewById(R.id.buttonExit);



        hasGenreSpinnerChanged=false;
        hasInstrumentSpinnerChanged=false;

        /*
         Disable keyboard input on secondary choice selection
        */

        //Todo: set onClickListeners for save and exit. possibly involves adding a new interface

        editTextSecondaryInstrument.setShowSoftInputOnFocus(false);
        editTextSecondaryInstrument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectSecondaryInstrument();
            }
        });
        editTextSecondaryInstrument.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId()==R.id.editTextSecondaryInstrument&&hasFocus)
                    onSelectSecondaryInstrument();
            }
        });
        editTextSecondaryGenre.setShowSoftInputOnFocus(false);
        editTextSecondaryGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectSecondaryGenre();
            }
        });
        editTextSecondaryGenre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId()==R.id.editTextSecondaryGenre&&hasFocus)
                    onSelectSecondaryGenre();
            }
        });

        spinnerSetup();
        imageButtonProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select a Profile Picture");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{pickIntent});
                startActivityForResult(chooserIntent, PICK_PROFILE_PIC_REQUEST_CODE);
            }
        });

        imageButtonUploadMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("audio/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setTypeAndNormalize("audio/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select an audio file");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{pickIntent});
                startActivityForResult(chooserIntent, PICK_PROFILE_MP3_REQUEST_CODE);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditProfileFragmentInterface) {
            editProfileFragmentInterface = (EditProfileFragmentInterface) context;
        }else{
            throw new RuntimeException("Must implement EditProfileFragmentInterface");
        }
    }

    public interface EditProfileFragmentInterface{
        public void ExitEditProfileFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data!=null)
        switch(requestCode){
            case PICK_PROFILE_PIC_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    Glide.with(getContext())
                            .load(data.getData())
                            .into(imageViewProfilePic);
                    parseFileProfilePic = getParseFileFromData(data);
                }else{
                    Log.e(TAG,"onActivityResult PickProfilePic case, result code="+resultCode);
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_PROFILE_MP3_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    parseFileProfileMp3 = getParseFileFromData(data);
                    Toast.makeText(getContext(), "Song Added", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e(TAG,"onActivityResult PickProfileMP3 case, result code="+resultCode);
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.e(TAG,"onActivityResult default case, Request code="+requestCode);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private ParseFile getParseFileFromData(Intent data){
        Uri uriSelectedImage = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = getActivity().getContentResolver().openInputStream(uriSelectedImage);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            ParseFile parseFile = new ParseFile(PHOTO_FILE_NAME,buffer);
            inputStream.close();
            return parseFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveProfileChanges(){
        boolean valid=true;
        if(editTextName.getText().toString().trim().isEmpty()){
            valid=false;
            editTextName.setError("Can not be empty");
        }
        if (spinnerPrimaryInstrument.getSelectedItemPosition() == -1 || spinnerPrimaryInstrument.getSelectedItemPosition() == 0) {
            valid = false;
        }
        if (spinnerPrimaryGenre.getSelectedItemPosition() == -1 || spinnerPrimaryGenre.getSelectedItemPosition() == 0){
            valid = false;
            Toast.makeText(getContext(),"Primary genre is required", Toast.LENGTH_SHORT).show();
        }

        if(valid) {
            //currentProfile.put(Profile.KEY_,);

            if(parseFileProfileMp3!=null) {
                currentProfile.putProfileMP3(parseFileProfileMp3);
            }
            currentProfile.putBio(editTextBio.getText().toString().trim());
            currentProfile.put(Profile.KEY_PRIMARYINSTRUMENT,spinnerPrimaryInstrument.getSelectedItem().toString().trim());
            currentProfile.put(Profile.KEY_PRIMARYGENRE,spinnerPrimaryGenre.getSelectedItem().toString().trim());
            currentProfile.put(Profile.KEY_SECONDARYINSTRUMENTS,Arrays.asList(editTextSecondaryInstrument.getHint().toString().split(" ")));
            currentProfile.put(Profile.KEY_SECONDARYGENRE,Arrays.asList(editTextSecondaryGenre.getHint().toString().split(" ")));
            currentProfile.putProfilePicture(parseFileProfilePic);
            currentProfile.putName(editTextName.getText().toString().trim());


            currentProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error Saving Profile: ", e);
                    } else {
                        Toast.makeText(getContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Profile Saved Successfully");
                    }
                }
            });
        }
    }//END OF SAVEPROFILECHANGES

//All functions below were written by Ryan or are slight modifications of functions written by Ryan
    public void spinnerSetup(){
        String [] stringArrayInstruments;
        String [] stringArrayGenres;
        stringArrayInstruments = getResources().getStringArray(R.array.instruments);
        stringArrayGenres = getResources().getStringArray(R.array.genres);

        /*
         Genre Spinner adapter, sets the first element to be not selectable and it's text to gray
        */

        ArrayAdapter<String> genreAdapter =new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,stringArrayGenres){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent){
                View view = super.getDropDownView(position,convertView,parent);
                TextView textView = (TextView)view;
                if (position == 0)
                {
                    textView.setTextColor(Color.GRAY);
                }
                else
                    textView.setTextColor(Color.BLACK);
                return view;
            }

        };

                 /*
         Instrument Spinner adapter, sets the first element to be not selectable and it's text to gray
         */
        ArrayAdapter<String> instrumentAdapter =new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,stringArrayInstruments){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                    return false;
                else
                    return true;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent){
                View view = super.getDropDownView(position,convertView,parent);
                TextView textView = (TextView)view;
                if (position == 0)
                {
                    textView.setTextColor(Color.GRAY);
                }
                else
                    textView.setTextColor(Color.BLACK);
                return view;
            }

        };

        spinnerPrimaryGenre.setAdapter(genreAdapter);
        spinnerPrimaryInstrument.setAdapter(instrumentAdapter);

        // If the selected item is on the secondary list it removes it from secondary
        spinnerPrimaryGenre.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (view != null)
                            onSelectPrimaryGenre();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getContext(),"Nothing selected",Toast.LENGTH_SHORT).show();
                    }
                });

        // If the selected item is on the secondary list it removes it from secondary
        spinnerPrimaryInstrument.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (view != null)
                            onSelectPrimaryInstrument();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getContext(),"Nothing selected",Toast.LENGTH_SHORT).show();
                    }
                });


    }//end of spinnerSetup

    private void onSelectPrimaryInstrument() {
        hasInstrumentSpinnerChanged=true;
        String selected = spinnerPrimaryInstrument.getSelectedItem().toString();
        String secondary = editTextSecondaryInstrument.getHint().toString();
        String[] secondaryItems = secondary.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String secondaryItem : secondaryItems) {
            if (!selected.equals(secondaryItem)) {
                stringBuilder.append(secondaryItem);
                stringBuilder.append(" ");
            } else {
                Toast.makeText(getContext(), "Cannot be both primary and secondary \nRemoved " + selected + " from secondary genres", Toast.LENGTH_SHORT).show();

            }
        }
        if(stringBuilder.toString().equals(""))
            editTextSecondaryInstrument.setHint("Enter any other instruments you like to play"); //TODO replace with R.string.message
        else
            editTextSecondaryInstrument.setHint(stringBuilder.toString());
    }//End of onSelectPrimaryInstrument

    private void onSelectPrimaryGenre() {
        hasGenreSpinnerChanged=true;
        String selected = spinnerPrimaryGenre.getSelectedItem().toString();
        String secondary = editTextSecondaryGenre.getHint().toString();
        String[] secondaryItems = secondary.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String secondaryItem : secondaryItems) {
            if (!selected.equals(secondaryItem)) {
                stringBuilder.append(secondaryItem);
                stringBuilder.append(" ");
            } else {
                Toast.makeText(getContext(), "Cannot be both primary and secondary \nRemoved " + selected + " from secondary genres", Toast.LENGTH_SHORT).show();
            }

        }
        if(stringBuilder.toString().equals(""))
            editTextSecondaryGenre.setHint("Enter any other genres you like to play"); //TODO replace with R.string.message
        else
            editTextSecondaryGenre.setHint(stringBuilder.toString());
    }//End of onSelectPrimaryGenre

    private void onSelectSecondaryGenre(){
        /*
         Initialize Variables to be shown in the dialog
         */
        String[] genres = Arrays.copyOfRange(getResources().getStringArray(R.array.genres),1,getResources().getStringArray(R.array.genres).length);
        boolean[] checkedItems = new boolean[genres.length];
        final List<String> genreList = Arrays.asList(genres);
        final int[] count = {0};      // Keep track of how many choices the user selects

        /*
         Initialize AlertDialog builder
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select up to 3 secondary genres");
        builder.setMultiChoiceItems(genres, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String primary = spinnerPrimaryGenre.getSelectedItem().toString();
                if(isChecked) {
                    // Check that the user has not exceeded the maximum number of allowed secondary choices
                    if (count[0] < 3) {

                        // Check that the user is not trying to choose their primary instrument
                        if(primary.equals(genres[which])){
                            Toast.makeText(getContext(),"Cannot select primary instrument",Toast.LENGTH_SHORT).show();
                            ((AlertDialog)dialog).getListView().setItemChecked(which,false);
                            checkedItems[which] = false;
                        }
                        // Set the item as checked
                        else {
                            ((AlertDialog) dialog).getListView().setItemChecked(which, true);
                            checkedItems[which] = isChecked;
                            String currentItem = genres[which];
                            count[0] += 1;
                        }
                    }
                    // The user has exceeded their maximum allowed choices
                    else {
                        Toast.makeText(getContext(), "Choose up to 3 secondary generes", Toast.LENGTH_SHORT).show();
                        ((AlertDialog)dialog).getListView().setItemChecked(which,false);
                        checkedItems[which] = false;
                    }
                }
                // The user has unchecked an option
                else {
                    count[0]--;
                    checkedItems[which] = false;
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder selectedGenres = new StringBuilder();
                for(int i= 0; i <checkedItems.length;i++)
                    if(checkedItems[i]) {
                        selectedGenres.append(genres[i]);
                        selectedGenres.append(" ");
                    }
                editTextSecondaryGenre.setHint(selectedGenres.toString());
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onSelectSecondaryInstrument(){
                /*
         Initialize Variables to be shown in the dialog
         */
        String[] instruments = Arrays.copyOfRange(getResources().getStringArray(R.array.instruments),1,getResources().getStringArray(R.array.instruments).length);
        boolean[] checkedItems = new boolean[instruments.length];
        final int[] count = {0};      // Keep track of how many choices the user selects

        /*
         Initialize AlertDialog builder
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select up to 3 secondary Instruments");
        builder.setMultiChoiceItems(instruments, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String primary = spinnerPrimaryInstrument.getSelectedItem().toString();

                if(isChecked) {
                    // Check that the user has not exceeded the maximum number of allowed secondary choices
                    if (count[0] < 3) {

                        // Check that the user is not trying to choose their primary instrument
                        if(primary.equals(instruments[which])){
                            Toast.makeText(getContext(),"Cannot select primary instrument",Toast.LENGTH_SHORT).show();
                            ((AlertDialog)dialog).getListView().setItemChecked(which,false);
                            checkedItems[which] = false;
                        }
                        // Set the item as checked
                        else {
                            ((AlertDialog) dialog).getListView().setItemChecked(which, true);
                            checkedItems[which] = isChecked;
                            String currentItem = instruments[which];
                            count[0] += 1;
                        }
                    }
                    // The user has exceeded their maximum allowed choices
                    else {
                        Toast.makeText(getContext(), "Choose up to 3 secondary generes", Toast.LENGTH_SHORT).show();
                        ((AlertDialog)dialog).getListView().setItemChecked(which,false);
                        checkedItems[which] = false;
                    }
                }
                // The user has unchecked an option
                else {
                    count[0]--;
                    checkedItems[which] = false;
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder selectedInstruments = new StringBuilder();
                // Get each checked item
                for(int i= 0; i <checkedItems.length;i++)
                    if(checkedItems[i]){
                        selectedInstruments.append(instruments[i]);
                        selectedInstruments.append(" ");
                    }
                // Set the hint to the selected choices
                // Checks that the value is not empty
                if(selectedInstruments.toString().equals(""))
                    selectedInstruments.append(getResources().getString(R.string.secondaryInstrumentPrompt));
                editTextSecondaryInstrument.setHint(selectedInstruments.toString());
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}//end of class