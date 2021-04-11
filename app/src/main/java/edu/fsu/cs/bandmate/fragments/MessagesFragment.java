package edu.fsu.cs.bandmate.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.fsu.cs.bandmate.Conversation;
import edu.fsu.cs.bandmate.ConversationList;
import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.User;
import edu.fsu.cs.bandmate.adapters.MessageListAdapter;

public class MessagesFragment extends Fragment {

    private ArrayList<Conversation>  m_conversations;
    //ArrayList<Pair<Conversation,ArrayList<String>>> conversations;
    ConversationList list;
    ArrayList<List<Object>> messages;
    ArrayList<Object> matches;
    ArrayList<Object> pictures;





    public MessagesFragment() {
        // Required empty public constructor
    }

    ParseUser user;
    private RecyclerView mRecyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_messages, container, false);

        //conversations = new ArrayList<Pair<Conversation, ArrayList<String>>>();


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        m_conversations = new ArrayList<Conversation>();
        messages = new ArrayList<List<Object>>();
        matches = new ArrayList<Object>();
        pictures = new ArrayList<Object>();
        
        try {
            queryConversations();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        MessageListAdapter adapter = new MessageListAdapter(getActivity(),matches,messages,pictures);

        mRecyclerView = view.findViewById(R.id.rvConversationList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());



    }

    public interface MessagesHost {
        public void onConversationClick();
        public boolean isConversationSelected(final String conversationId);
    }

    private void queryConversations() throws ParseException {
        ParseQuery<ConversationList> query = ParseQuery.getQuery(ConversationList.class);
        query.include(ConversationList.KEY_USER);
        query.whereEqualTo(ConversationList.KEY_USER, user);
        List<ConversationList> conversationLists = query.find();

        if (conversationLists == null || conversationLists.size()==0) {
            ParseObject messagesList = ParseObject.create("ConversationList");
            messagesList.put(ConversationList.KEY_USER, ParseUser.getCurrentUser());
            messagesList.saveInBackground();
        }
        list = conversationLists.get(0);
        m_conversations = list.getConversations();
        initConversationItems();


    }

    private void loadMessages(){
        ParseQuery<Conversation> query = ParseQuery.getQuery(Conversation.class);
    }

    private void initConversationItems() throws ParseException {
        for (Conversation c : m_conversations){
            messages.add(c.fetchIfNeeded().getList(Conversation.KEY_MESSAGES));
            ParseUser other = (ParseUser) c.fetchIfNeeded().getParseObject(Conversation.KEY_OTHER);
            Profile otherProfile = new Profile();
            otherProfile = (Profile) other.fetchIfNeeded().getParseObject("myProfile");
            matches.add(other);
            pictures.add(otherProfile.fetchIfNeeded().get(Profile.KEY_PROFILEPICTURE));
        }
    }
}