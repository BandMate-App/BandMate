package edu.fsu.cs.bandmate.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Context;

import edu.fsu.cs.bandmate.R;


/*
This fragment has the buttons for logging in and registering an account
 */
public class MainFragment extends Fragment {

    private Button login,register;
    mainFragmentListener listener;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        login = rootView.findViewById(R.id.login);
        register = rootView.findViewById(R.id.register);

        /*
         Set on click listeners
         */

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRegister();
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
        if (context instanceof mainFragmentListener) {
            listener = (mainFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface mainFragmentListener{
        void onLogin();
        void onRegister();
    }


}