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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.fsu.cs.bandmate.Conversation;
import edu.fsu.cs.bandmate.Message;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.SelectedConversation;
import edu.fsu.cs.bandmate.adapters.ChatAdapter;
import edu.fsu.cs.bandmate.adapters.MessageListAdapter;


public class ChatFragment extends Fragment{

    SelectedConversation selected;
    EditText etmessage;
    ImageButton send;
    ChatAdapter adapter;
    List<Object> messages;
    Bitmap avatar;
    ParseUser match;
    RecyclerView mRecyclerView;
    ArrayList<Message> currentConversation;
    boolean firstload;

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
        firstload = true;

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        etmessage = view.findViewById(R.id.etMessage);
        send = view.findViewById(R.id.btSend);
        try {
            adapter = new ChatAdapter(getActivity(),selected.match.getUsername(),selected.conversation,selected.picture);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            currentConversation = (ArrayList<Message>) selected.conversation.fetchIfNeeded().get(Conversation.KEY_MESSAGEOBJECT);
            mRecyclerView = view.findViewById(R.id.rvChat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etmessage.getText().toString();
                ParseObject message = ParseObject.create("Message");
                message.put(Message.KEY_USER,ParseUser.getCurrentUser().getUsername());
                message.put(Message.KEY_BODY,data);
                try {
                    message.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                currentConversation.add((Message)message);
                //selected.conversation.put(Conversation.KEY_MESSAGEOBJECT,currentConversation);
                selected.conversation.add(Conversation.KEY_MESSAGEOBJECT,message);
                selected.conversation.saveInBackground();
                etmessage.setText(null);


            }
        });
        return view;
    }

    public void refreshMessages() throws ParseException {
        if(currentConversation != (ArrayList<Message>)selected.conversation.fetch().get(Conversation.KEY_MESSAGEOBJECT));{
            adapter.notifyDataSetChanged();
            if(firstload){
                mRecyclerView.scrollToPosition(0);
                firstload = false;
            }
        }
    }

}