package edu.fsu.cs.bandmate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Context;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private RegisterFragmentListener listener;
    private EditText fName,lName,eMail,password,phoneNumber;
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
                /*
                TODO Add user object to the database
                 */
                if (formComplete())
                    listener.onRegisterComplete();
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
        if(fName.getText().toString().trim().isEmpty())
            return false;
        if (fName.getText().toString().trim().isEmpty())
                return false;
        if (password.getText().toString().trim().isEmpty())
            return false;
        if (eMail.getText().toString().trim().isEmpty())
                return false;
        if (phoneNumber.getText().toString().trim().isEmpty())
            return false;
        if (!genderSelector.isPressed())
            return false;
        if (!primaryInstrument.isPressed())
            return false;
        // Secondary instrument is optional

        return true;
    }

    public interface RegisterFragmentListener{
        void onRegisterComplete();
        void onCancel();
    }


}