package edu.fsu.cs.bandmate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Context;
import android.widget.EditText;

public class LoginFragment extends Fragment implements View.OnClickListener {

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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                TODO Set logic to query the database and check for an exisiting username/password pair
                 */

                /*
                 TODO go to the main application screen after successful login
                 */
                listener.onValidLogin();
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
        if (context instanceof LoginFragmentListener) {
            listener = (LoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View v) {

        /*
         TODO validate login input and query database
         */
    }

    public interface LoginFragmentListener{
        void onValidLogin();
        void onCancel();
    }


}