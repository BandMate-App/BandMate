package edu.fsu.cs.bandmate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import android.content.Context;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    public static final String KEY_PHONE="Phone",
                                KEY_NAME="Name";

    private RegisterFragmentListener listener;
    private EditText fName,lName,eMail,password,phoneNumber,etUsername;
    private RadioGroup genderSelector;
    private ListView secondaryInstruments;
    private Spinner primaryInstrument;
    private Button signUp,cancel;

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

        fName = rootView.findViewById(R.id.registerFirstName);
        lName = rootView.findViewById(R.id.registerLastName);
        eMail = rootView.findViewById(R.id.registerEmail);
        password = rootView.findViewById(R.id.registerPassword);
        etUsername = rootView.findViewById(R.id.etUsername);
        phoneNumber = rootView.findViewById(R.id.registerPhoneNumber);

        genderSelector = rootView.findViewById(R.id.genderSelector);
        primaryInstrument = rootView.findViewById(R.id.registerPrimaryInstrument);
        secondaryInstruments = rootView.findViewById(R.id.registerInstrumentPreference);

        signUp = rootView.findViewById(R.id.registerSignUpButton);
        cancel = rootView.findViewById(R.id.registerCancelButton);

        /*
        Set on click Listeners
         */
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                            listener.onRegisterComplete();
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
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

        /*
            Add logic to validate login once database is implemented
         */
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
        if (phoneNumber.getText().toString().trim().isEmpty()){
            valid= false;
            phoneNumber.setError("Can not be empty");

        }
        /*
        if (!genderSelector.isPressed()){
            valid= false;
        }
        if (!primaryInstrument.isPressed()) {
            valid = false;
        }
         */
        // Secondary instrument is optional

        return valid;
    }

    public interface RegisterFragmentListener{
        void onRegisterComplete();
        void onCancel();
    }


}