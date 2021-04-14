package edu.fsu.cs.bandmate.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import edu.fsu.cs.bandmate.Conversation;
import edu.fsu.cs.bandmate.Message;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.SelectedConversation;
import edu.fsu.cs.bandmate.adapters.ChatAdapter;
import edu.fsu.cs.bandmate.adapters.MessageListAdapter;


public class ChatFragment extends Fragment{
    ParseUser currentUser;
    Conversation conversationOther;
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
    Handler myHandler = new android.os.Handler();
    Runnable RefreshChatRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                refreshMessages();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            myHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(3));
        }
    };
    @Override
    public void onResume() {

        super.onResume();
        myHandler.postDelayed(RefreshChatRunnable,TimeUnit.SECONDS.toMillis(3));
    }

    @Override
    public void onPause(){
        myHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

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
        currentUser = ParseUser.getCurrentUser();

        try {
            setConversationOther();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

                conversationOther.add(Conversation.KEY_MESSAGEOBJECT,message);
                conversationOther.saveInBackground();
                etmessage.setText(null);
                adapter.notifyDataSetChanged();


            }
        });

        return view;
    }

    public void refreshMessages() throws ParseException {
        ArrayList<Message>  current = (ArrayList<Message>) selected.conversation.fetch().get(Conversation.KEY_MESSAGEOBJECT);
        assert current != null;
        if(currentConversation.size() != current.size()){
            adapter.notifyDataSetChanged();
            if(firstload){
                mRecyclerView.scrollToPosition(0);
                firstload = false;
            }
        }
    }

    public void setConversationOther() throws ParseException {
        ParseQuery<Conversation> query = ParseQuery.getQuery(Conversation.class);
        ParseUser other = selected.match.fetchIfNeeded();
       // ParseUser self = (ParseUser) selected.match.fetchIfNeeded().get("self");
       // self.fetch();

        query.whereEqualTo(Conversation.KEY_SELF,other);
        assert other != null;
        query = query.whereEqualTo(Conversation.KEY_SELF,other);
        List<Conversation> temp =  query.find();

      /*  for(Conversation c : temp){
            other = (ParseUser) c.fetchIfNeeded().get("other");
            assert other != null;
            if(other.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                conversationOther = c;
            }
        }*/
        conversationOther = temp.get(0);
       // Toast.makeText(getContext(),"conversation set",Toast.LENGTH_SHORT).show();
    }

}