package edu.fsu.cs.bandmate.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.SelectedConversation;
import edu.fsu.cs.bandmate.adapters.MessageListAdapter;


public class ChatFragment extends Fragment{

    SelectedConversation selected;
    EditText message;
    ImageButton send;

    List<Object> messages;
    Bitmap avatar;
    ParseUser match;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent i = requireActivity().getIntent();
        selected = (SelectedConversation) i.getSerializableExtra("selected");

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        message = view.findViewById(R.id.etMessage);
        send = view.findViewById(R.id.btSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = message.getText().toString();

            }
        });
        return view;
    }

    public void SendMessage(){

    }

}