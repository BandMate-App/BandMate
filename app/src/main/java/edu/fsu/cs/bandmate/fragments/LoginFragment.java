package edu.fsu.cs.bandmate.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import edu.fsu.cs.bandmate.R;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final String TAG=LoginFragment.class.getCanonicalName();
    LoginFragmentListener listener;
    EditText username,password;
    Button login,cancel;
    
    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        username = rootView.findViewById(R.id.onLoginUsername);
        password = rootView.findViewById(R.id.onLoginPassword);

        login = rootView.findViewById(R.id.onLoginLogin);
        cancel = rootView.findViewById(R.id.onLoginCancel);

        /*
         Set on click listeners
         */
        login.setOnClickListener(this);
        cancel.setOnClickListener(this);

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
        if (context instanceof LoginFragmentListener) {
            listener = (LoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.onLoginCancel){
            listener.onCancel();
        }
        if(id == R.id.onLoginLogin){
            onLogin();
        }


    }

    public void onLogin(){
        String uname= username.getText().toString();
        String pass= password.getText().toString();

        ParseUser.logInInBackground(uname, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e!=null){
                    if(e.getCode()==ParseException.OBJECT_NOT_FOUND){
                        username.setError("Invalid Username or Password");
                    }else
                        Toast.makeText(getActivity(), "Error Logging In", Toast.LENGTH_SHORT).show();
                }else{
                    listener.onValidLogin();
                }
            }
        });
    }

    public interface LoginFragmentListener{
        void onValidLogin();
        void onCancel();
    }


}