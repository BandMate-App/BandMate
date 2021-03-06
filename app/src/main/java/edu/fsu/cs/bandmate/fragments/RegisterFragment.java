package edu.fsu.cs.bandmate.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.fsu.cs.bandmate.Conversation;
import edu.fsu.cs.bandmate.ConversationList;
import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.jetbrains.annotations.NotNull;


public class RegisterFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    public static final String KEY_PHONE="Phone",KEY_NAME="Name",KEY_USER="User";

    private static final String TAG =RegisterFragment.class.getCanonicalName() ;

    final private int MIN_AGE = 13; // Minimum age according to COPPA

    private RegisterFragmentListener listener;

    private EditText fName,lName,eMail,password,phoneNumber,datePrompt,etUsername;
    private EditText secondaryGenrePrompt, secondaryInstrumentPrompt, birthday;

    private RadioGroup genderSelector;

    private ListView secondaryInstruments;

    private Spinner primaryInstrument, primaryGenre;

    private DatePickerDialog datePicker;




    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);



        /*
         Edit Texts
         */
        fName = rootView.findViewById(R.id.registerFirstName);
        lName = rootView.findViewById(R.id.registerLastName);
        eMail = rootView.findViewById(R.id.registerEmail);
        password = rootView.findViewById(R.id.registerPassword);
        phoneNumber = rootView.findViewById(R.id.registerPhoneNumber);
        datePrompt = rootView.findViewById(R.id.datePickerPrompt);
        etUsername = rootView.findViewById(R.id.registerUsername);
        secondaryGenrePrompt = rootView.findViewById(R.id.registerSecondaryGenres);
        secondaryInstrumentPrompt = rootView.findViewById(R.id.registerSecondaryInstruments);
        birthday = datePrompt;



        /*
         Spinner/RadioGroups
         */
        genderSelector = rootView.findViewById(R.id.genderSelector);
        primaryInstrument = rootView.findViewById(R.id.registerPrimaryInstrument);
        primaryGenre = rootView.findViewById(R.id.registerPrimaryGenre);
        //secondaryInstruments = rootView.findViewById(R.id.registerInstrumentPreference);

        /*
         Buttons
         */
        Button signUp = rootView.findViewById(R.id.registerSignUpButton);
        Button cancel = rootView.findViewById(R.id.registerCancelButton);

        /*
         Get string-arrays from resource and update views

         */
        String [] instruments;
        String [] genres;
        instruments = getResources().getStringArray(R.array.instruments);
        genres = getResources().getStringArray(R.array.genres);

        /*
        Set on view Listeners
         */
        signUp.setOnClickListener(this);
        cancel.setOnClickListener(this);
        datePrompt.setOnFocusChangeListener(this);
        secondaryGenrePrompt.setOnFocusChangeListener(this);
        secondaryInstrumentPrompt.setOnFocusChangeListener(this);
        secondaryGenrePrompt.setOnClickListener(this);
        secondaryInstrumentPrompt.setOnClickListener(this);

        /*
         Disable keyboard input on secondary choice selection
         */
        secondaryInstrumentPrompt.setShowSoftInputOnFocus(false);
        secondaryGenrePrompt.setShowSoftInputOnFocus(false);
        datePrompt.setShowSoftInputOnFocus(false);


        /*
         Genre Spinner adapter, sets the first element to be not selectable and it's text to gray
         */
        ArrayAdapter<String> genreAdapter =new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,genres){
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
        ArrayAdapter<String> instrumentAdapter =new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,instruments){

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

        primaryGenre.setAdapter(genreAdapter);
        primaryInstrument.setAdapter(instrumentAdapter);

        // If the selected item is on the secondary list it removes it from secondary
        primaryGenre.setOnItemSelectedListener(
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
        primaryInstrument.setOnItemSelectedListener(
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


        return rootView;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragmentListener) {
            listener = (RegisterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.registerSignUpButton){
            onSignup();
        }
        else if(id== R.id.registerCancelButton){
            onCancel();
        }
        else if(id == R.id.registerSecondaryInstruments)
            onSelectSecondaryInstrument();

        else if(id == R.id.registerSecondaryGenres)
            onSelectSecondaryGenre();
    }

    /*
    Checks all fields are not empty and have valid input types
     */
    private Boolean formComplete(){
        int age = 0;
        boolean valid=true;
        if(fName.getText().toString().trim().isEmpty()) {
            fName.setError("Can not be empty");
            valid = false;
        }
        if (lName.getText().toString().trim().isEmpty()) {
            lName.setError("Can not be empty");
            valid = false;
        }
        if (password.getText().toString().trim().isEmpty()){
            valid= false;
            password.setError("Can not be empty");

        }
        if (etUsername.getText().toString().trim().isEmpty()){
            valid= false;
            etUsername.setError("Can not be empty");

        }
        if (eMail.getText().toString().trim().isEmpty()){
            valid= false;
            eMail.setError("Can not be empty");

        }
        if (phoneNumber.getText().toString().trim().isEmpty()) {
            valid = false;
            phoneNumber.setError("Can not be empty");
            //TODO check for valid phone number format
        }
        if (!android.util.Patterns.PHONE.matcher(phoneNumber.getText().toString().trim()).matches() || phoneNumber.getText().toString().length() != 10){
            valid = false;
            phoneNumber.setError("Please enter a valid phone number");
        }

        if (genderSelector.getCheckedRadioButtonId() == -1) {
            valid = false;
        }
        if (primaryInstrument.getSelectedItemPosition() == -1 || primaryInstrument.getSelectedItemPosition() == 0) {
            valid = false;
        }

        if (primaryGenre.getSelectedItemPosition() == -1 || primaryGenre.getSelectedItemPosition() == 0){
            valid = false;
            Toast.makeText(getContext(),"Primary genre is required", Toast.LENGTH_SHORT).show();
        }
        if (birthday.getText().toString().isEmpty()){
            valid = false;
            birthday.setError("Birthday can not be empty");
        }
        else{
            Calendar today = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            String [] m_d_y = birthday.getText().toString().split("/");
            int month = Integer.parseInt(m_d_y[0])+ 1;
            int day = Integer.parseInt(m_d_y[1]);
            int year = Integer.parseInt(m_d_y[2]);
            dob.set(year,month,day);
            age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if(today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
                age--;
            if (age < MIN_AGE){
                birthday.setError("Minimum age allowed is 13");
                valid = false;
            }

        }

        return valid;
    }


    /*
     Used for birthday, secondary instrument, and secondary genres
     Could change this to buttons if we think it looks better
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus && v.getId() == datePrompt.getId()){
            onEnterDate();
        }
        if (hasFocus && v.getId() == secondaryGenrePrompt.getId())
            onSelectSecondaryGenre();

        if(hasFocus && v.getId() == secondaryInstrumentPrompt.getId())
            onSelectSecondaryInstrument();
    }


    /*
     Check that the user has not already chosen this instrument as a secondary Instrument.
     Remove from secondary Instruments if they have
     */
    private void onSelectPrimaryInstrument() {
        String selected = primaryInstrument.getSelectedItem().toString();
        String secondary = secondaryInstrumentPrompt.getHint().toString();
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
            secondaryInstrumentPrompt.setHint(getResources().getString(R.string.secondaryInstrumentPrompt));
        else
            secondaryInstrumentPrompt.setHint(stringBuilder.toString());
    }

    /*
     Check that the user has not already chosen this instrument as a secondary Genre.
     Remove from secondary genres if they have
     */
    private void onSelectPrimaryGenre() {

        String selected = primaryGenre.getSelectedItem().toString();
        String secondary = secondaryGenrePrompt.getHint().toString();
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
            secondaryGenrePrompt.setHint(getResources().getString(R.string.secondaryGenrePrompt)); //TODO replace with R.string.message
        else
            secondaryGenrePrompt.setHint(stringBuilder.toString());
    }

    public interface RegisterFragmentListener{
        void onRegisterComplete() throws ParseException;
        void onCancel();
    }

    /*
     On click listener of datePrompt, creates a datepicker prompt and saves the value to the picked date
     */
    private void onEnterDate(){
    /*
     Set Datepicker Dialog
     */
    final Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // TODO fix this warning
            String date = ""+ (month+1) + "/" + day + "/" + year;
            datePrompt.setText(date);
        }
    },year,month,day);

    datePicker.show();

    }

    /*
     Checks for existing user in the database, adds new user to the database, uses interface to switch
     fragments
     */
    private void onSignup(){
        String registerName=fName.getText().toString()+" "+lName.getText().toString();
        String registerEmail=eMail.getText().toString();
        String registerPassword=password.getText().toString();
        String registerUsername=etUsername.getText().toString();
        String registerPhone=phoneNumber.getText().toString();




        if (formComplete()) {
            //This little ninny closes the keyboard
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            ArrayList<ParseUser> liked_users = new ArrayList<>();
            ParseUser user = new ParseUser();
            user.setUsername(registerUsername);
            user.setEmail(registerEmail);
            user.setPassword(registerPassword);
            user.put(KEY_NAME,registerName);
            user.put("liked_users",liked_users);
            user.put(KEY_PHONE, registerPhone);
            user.put(Profile.KEY_PRIMARYINSTRUMENT,primaryInstrument.getSelectedItem().toString().trim());
            user.put(Profile.KEY_PRIMARYGENRE,primaryGenre.getSelectedItem().toString().trim());
            user.put(Profile.KEY_BIRTHDAY,datePrompt.getText().toString().trim());

            Button gender = getActivity().findViewById(genderSelector.getCheckedRadioButtonId());
            user.put(Profile.KEY_GENDER,gender.getText().toString().trim());

            if(!secondaryInstrumentPrompt.getHint().toString().trim().equals(getResources().getString(R.string.secondaryInstrumentPrompt))) {
                String[] instruments = secondaryInstrumentPrompt.getHint().toString().split(" ");
                List<String> i = Arrays.asList(instruments);
                List<String> newList = new ArrayList<String>(i);
                user.put(Profile.KEY_SECONDARYINSTRUMENTS,newList);
            }
            else{
                ArrayList<String> temp = new ArrayList<>();
                user.put(Profile.KEY_SECONDARYINSTRUMENTS,temp);
            }

            if(!secondaryGenrePrompt.getHint().toString().trim().equals(getResources().getString(R.string.secondaryGenrePrompt))) {
                user.put(Profile.KEY_SECONDARYGENRE, Arrays.asList(secondaryGenrePrompt.getHint().toString().split(" ")));
            }
            else{
                ArrayList<String> temp = new ArrayList<>();
                user.put(Profile.KEY_SECONDARYGENRE,temp);
            }


            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        switch(e.getCode()){
                            case ParseException.EMAIL_TAKEN:
                                eMail.setError("Email already in use");
                                break;
                            case ParseException.USERNAME_TAKEN:
                                etUsername.setError("Username taken");
                            case ParseException.INVALID_EMAIL_ADDRESS:
                                eMail.setError("Invalid Email Address");
                                break;
                            case ParseException.PASSWORD_MISSING:
                                password.setError("Invalid Password");
                                break;
                            default:
                                Log.e(TAG,"error with signup",e);
                                Toast.makeText(getContext(), "Error with signup", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();

                        ParseUser.logInInBackground(registerUsername, registerPassword, new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(e!=null){
                                    Toast.makeText(getContext(), "Error Logging in", Toast.LENGTH_SHORT).show();
                                }else {
                                    try {
                                        /*
                                         After the user objects created, create their associated conversation objects
                                         */
                                        ParseObject messagesList = ParseObject.create("ConversationList");
                                        messagesList.put(ConversationList.KEY_USER, ParseUser.getCurrentUser());
                                        messagesList.save();
                                        ArrayList<Conversation> temp = new ArrayList<>();
                                        messagesList.put(ConversationList.KEY_CONVERSATION, temp);
                                        messagesList.saveEventually();
                                        listener.onRegisterComplete();
                                    } catch (ParseException parseException) {
                                        parseException.printStackTrace();
                                    }
                                }
                            }
                        });

                    }
                }
            });

        }
    }

    /*
     Creates the alert dialog. Only allows 3 choices that are not the primary genre
     */
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
                String primary = primaryGenre.getSelectedItem().toString();
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
                secondaryGenrePrompt.setHint(selectedGenres.toString());
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*
  Creates the alert dialog. Only allows 3 choices that are not the primary instrument
  */
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
                String primary = primaryInstrument.getSelectedItem().toString();

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
                secondaryInstrumentPrompt.setHint(selectedInstruments.toString());
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

/*
 Goes back to MainFragment
 */
    private void onCancel(){
        listener.onCancel();
    }

    public static void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }



}