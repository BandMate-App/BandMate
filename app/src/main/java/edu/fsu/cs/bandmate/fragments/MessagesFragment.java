package edu.fsu.cs.bandmate.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import edu.fsu.cs.bandmate.Conversation;
import edu.fsu.cs.bandmate.ConversationList;
import edu.fsu.cs.bandmate.Message;
import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.SelectedConversation;
import edu.fsu.cs.bandmate.User;
import edu.fsu.cs.bandmate.adapters.MessageListAdapter;

public class MessagesFragment extends Fragment {

    private ArrayList<Conversation> m_conversations;
    MessageListAdapter adapter;
    //ArrayList<Pair<Conversation,ArrayList<String>>> conversations;
    ConversationList list;
    Date lastUpdated;
    ArrayList<List<Object>> messages;
    ArrayList<ParseUser> matches;
    ArrayList<Bitmap> pictures;
    CardView conversationItem;
    MessagesHost listener;
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



    public MessagesFragment() {
        // Required empty public constructor
    }

    ParseUser user;
    private RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        //conversations = new ArrayList<Pair<Conversation, ArrayList<String>>>();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        m_conversations = new ArrayList<Conversation>();
        messages = new ArrayList<List<Object>>();
        matches = new ArrayList<>();
        pictures = new ArrayList<>();
        lastUpdated = new Date(1999,1,1);



        try {
            queryConversations();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        adapter = new MessageListAdapter(getActivity(), matches, m_conversations, pictures);
        adapter.setListener(listener);
        mRecyclerView = view.findViewById(R.id.rvConversationList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    public interface MessagesHost {
        public void onConversationClick(SelectedConversation selected);

        public boolean isConversationSelected(final String conversationId);
    }

    /*
     Get all conversations where the self feild matches the current user.
     */
    private void queryConversations() throws ParseException {
        ParseQuery<ConversationList> query = ParseQuery.getQuery(ConversationList.class);
        query.include(ConversationList.KEY_USER);
        query.whereEqualTo(ConversationList.KEY_USER, user);
        List<ConversationList> conversationLists = query.find();

        if (conversationLists == null || conversationLists.size() == 0) {
            ParseObject messagesList = ParseObject.create("ConversationList");
            messagesList.put(ConversationList.KEY_USER, ParseUser.getCurrentUser());
            messagesList.saveInBackground();
        }
        list = conversationLists.get(0);
        m_conversations = list.getConversations();
        if(m_conversations != null)
            initConversationItems();


    }

  /*  private void queryProfile() throws ParseException {
        for (ParseUser other : matches) {
            ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
            query.include(Profile.KEY_USER);
            query.whereEqualTo(Profile.KEY_USER, other);
            List<Profile> otherProfile = query.find();
            if (otherProfile.size() < 1)
                Toast.makeText(getActivity(), "error getting profile", Toast.LENGTH_SHORT).show();

            byte[] data = otherProfile.get(0).getImage().getData();
            Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);
            pictures.add(bmp);
        }
    }

   */

    private void loadMessages() {
        ParseQuery<Conversation> query = ParseQuery.getQuery(Conversation.class);
    }

    private void initConversationItems() throws ParseException {
        for (Conversation c : m_conversations) {
            //messages.add(c.fetchIfNeeded().getList(Conversation.KEY_MESSAGES));
            ParseUser other = (ParseUser) c.fetchIfNeeded().getParseObject(Conversation.KEY_OTHER);
            Profile otherProfile = new Profile();
            otherProfile = (Profile) other.fetchIfNeeded().getParseObject("myProfile");
            byte[] data = otherProfile.fetchIfNeeded().getParseFile(Profile.KEY_PROFILEPICTURE).getData();
            Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);
            pictures.add(bmp);
            matches.add(other);
        }
        //queryProfile();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof MessagesFragment.MessagesHost) {
            listener = (MessagesFragment.MessagesHost) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void refreshMessages() throws ParseException {
        //ConversationList query = (ConversationList)list.fetch().get(ConversationList.KEY_CONVERSATION);
        if (lastUpdated != list.getUpdatedAt())
        {
            adapter.notifyDataSetChanged();
            lastUpdated = list.getUpdatedAt();
        }
    }


}