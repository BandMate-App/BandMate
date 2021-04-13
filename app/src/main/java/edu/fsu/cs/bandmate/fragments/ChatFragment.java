package edu.fsu.cs.bandmate.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import edu.fsu.cs.bandmate.adapters.ChatAdapter;
import edu.fsu.cs.bandmate.adapters.MessageListAdapter;


public class ChatFragment extends Fragment{

    SelectedConversation selected;
    EditText message;
    ImageButton send;
    ChatAdapter adapter;
    List<Object> messages;
    Bitmap avatar;
    ParseUser match;
    RecyclerView mRecyclerView;

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
        adapter = new ChatAdapter(getActivity(),selected.match.getUsername(),selected.messages,selected.picture);
        mRecyclerView = view.findViewById(R.id.rvChat);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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

    public void setupChat(){
    }

}