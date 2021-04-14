package edu.fsu.cs.bandmate.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
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
import edu.fsu.cs.bandmate.ConversationList;
import edu.fsu.cs.bandmate.Message;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.SelectedConversation;
import edu.fsu.cs.bandmate.adapters.ChatAdapter;
import edu.fsu.cs.bandmate.adapters.MessageListAdapter;


public class ChatFragment extends Fragment{
    ConversationList self_list;
    ConversationList other_list;
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
                 refreshChat();
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
            currentConversation = (ArrayList<Message>) selected.conversation.fetchIfNeeded().get(Conversation.KEY_MESSAGEOBJECT);
            adapter = new ChatAdapter(getActivity(),selected.match.getUsername(),currentConversation,selected.picture);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        etmessage = view.findViewById(R.id.etMessage);
        send = view.findViewById(R.id.btSend);
            mRecyclerView = view.findViewById(R.id.rvChat);
        mRecyclerView.setAdapter(adapter);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {   // make a pointless save to each users conversation list to updated the objects updated at value, so the message fragment will be updated
                    other_list = (ConversationList)selected.conversation.fetchIfNeeded().getParseObject("c_list1");
                    self_list = (ConversationList) selected.conversation.fetchIfNeeded().getParseObject("c_list2");
                    other_list.fetchIfNeeded().put(ConversationList.KEY_CONVERSATION,other_list.fetchIfNeeded().get(ConversationList.KEY_CONVERSATION));
                    self_list.fetchIfNeeded().put(ConversationList.KEY_CONVERSATION,self_list.fetchIfNeeded().get(ConversationList.KEY_CONVERSATION));
                    other_list.saveInBackground();
                    self_list.saveInBackground();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                String data = etmessage.getText().toString();

                // Add the new message to the current users conversation object
                ParseObject message = ParseObject.create("Message");
                message.put(Message.KEY_USER,ParseUser.getCurrentUser().getUsername());
                message.put(Message.KEY_BODY,data);

                //add the new message to the matches conversation object
                /*
                ParseObject otherMessage = ParseObject.create("Message");
                otherMessage.put(Message.KEY_USER,ParseUser.getCurrentUser().getUsername());
                otherMessage.put(Message.KEY_BODY,data);


                try {
                    message.save();
                    otherMessage.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
 */
                try {
                    selected.conversation.fetchIfNeeded();
                    selected.conversation.add(Conversation.KEY_MESSAGEOBJECT,message.fetchIfNeeded());
                    conversationOther.add(Conversation.KEY_MESSAGEOBJECT,message.fetchIfNeeded());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                selected.conversation.saveInBackground();
                conversationOther.saveInBackground();
                etmessage.setText(null);

                //update the list passed to the adapter and notify the adapter of a new item
                currentConversation.add((Message)message);
                adapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);

            }
        });

        return view;
    }

    public void refreshChat() throws ParseException {
        ArrayList<Message>  current = (ArrayList<Message>) selected.conversation.fetch().get(Conversation.KEY_MESSAGEOBJECT);
        assert current != null;
        if(current.size() == 1 && currentConversation.size() == 0) {
            currentConversation.add(current.get(0));
            adapter.notifyDataSetChanged();
        }

        else if(currentConversation.size() != current.size()){
            int index = currentConversation.size();
            while(currentConversation.size()!= current.size())
            {
                currentConversation.add(current.get(index));
                index +=1;
            }
            adapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);

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


        conversationOther = temp.get(0);
    }

}