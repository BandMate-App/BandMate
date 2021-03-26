package edu.fsu.cs.bandmate.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.User;
import edu.fsu.cs.bandmate.profileMap;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/*
TODO:
      Update form complete to check for all input,
      make a validate input method that checks for proper email,birthday etc
 */

public class RegisterFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    public static final String KEY_PHONE="Phone",
            KEY_NAME="Name";

    private RegisterFragmentListener listener;

    private EditText fName,lName,eMail,password,phoneNumber,datePrompt,etUsername;;

    private RadioGroup genderSelector;

    private ListView secondaryInstruments;

    private Spinner primaryInstrument;

    private Button signUp,cancel;

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

        /*
         Spinner/RadioGroups
         */
        genderSelector = rootView.findViewById(R.id.genderSelector);
        primaryInstrument = rootView.findViewById(R.id.registerPrimaryInstrument);
        //secondaryInstruments = rootView.findViewById(R.id.registerInstrumentPreference);

        /*
         Buttons
         */
        signUp = rootView.findViewById(R.id.registerSignUpButton);
        cancel = rootView.findViewById(R.id.registerCancelButton);

        /*
         Get string-arrays from resource and update views
         TODO Set up list of instruments/genres programmatically,
          maybe need a dialog to prevent the screen from getting crowded
         */
        String [] instrument;
        String [] genres;
        instrument = getResources().getStringArray(R.array.instruments);
        genres = getResources().getStringArray(R.array.genres);

        /*
        Set on view Listeners
         */
        signUp.setOnClickListener(this);
        cancel.setOnClickListener(this);
        datePrompt.setOnFocusChangeListener(this);


        /*
         TODO
           In the adapter disable the first item in each spinner, set item 0 selected listener to do nothing,
           set first item text to grey
         */

        return rootView;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onAttach(Context context) {
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
    }

    private Boolean formComplete(){
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
        }

        if (genderSelector.getCheckedRadioButtonId() == -1) {
            valid = false;
        }
        if (primaryInstrument.getSelectedItemPosition() == -1) {
            valid = false;
        }
        // Secondary instrument is optional

        return valid;
    }

    private User createUser(){
        RadioButton genderButton = getActivity().findViewById(genderSelector.getCheckedRadioButtonId());
        profileMap profileMap = new profileMap();
        ArrayList<Integer> secondary = new ArrayList<>();
        Integer pInstrument = profileMap.MapInstrument(primaryInstrument.getSelectedItem().toString().trim());
        Integer selectedGender = profileMap.mapGender(genderButton.getText().toString().trim());
        secondary.add(-1); //TODO implement multiple choice picking for secondary instrument
        return new User(fName.getText().toString().trim(),lName.getText().toString().trim(),
                eMail.getText().toString().trim(),password.getText().toString().trim(),phoneNumber.getText().toString().trim(),selectedGender,
                -1,secondary);
    }

    /*
     Used for birthday prompt edit text, if it becomes the focus it opens a date picker dialog
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus && v.getId() == datePrompt.getId()){
            onEnterDate();
        }
    }

    public interface RegisterFragmentListener{
        void onRegisterComplete(User user);
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
            datePrompt.setText(day + "/" + (month +1) + "/" +year);
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

        //TODO: Set Up and Collect extra info (gender,instruments)



        if (formComplete()) {
            //This little ninny closes the keyboard
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            ParseUser user = new ParseUser();
            user.setUsername(registerUsername);
            user.setEmail(registerEmail);
            user.setPassword(registerPassword);
            user.put(KEY_NAME,registerName);
            user.put(KEY_PHONE, registerPhone);
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
                                //Todo: cover the other errors
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
                                    /*
                                      TODO, Do we need this object in addition to the Profile Object or should we get rid of User class?
                                     */
                                    User newUser = createUser();
                                    listener.onRegisterComplete(newUser);
                                }
                            }
                        });

                    }
                }
            });

        }
}

/*
 Goes back to MainFragment
 */
    private void onCancel(){
        listener.onCancel();
    }


}