package edu.fsu.cs.bandmate;
import android.graphics.Bitmap;

import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectedConversation implements Serializable {
    public Conversation conversation;
    public ParseUser match;
    public Bitmap picture;
    public ArrayList<Conversation> users_conversations;
    public ConversationList conversationList;
    public SelectedConversation(Conversation messages,ParseUser match,Bitmap picture,ArrayList<Conversation> users_conversations,ConversationList conversationList){
        this.conversation = messages;
        this.match = match;
        this.picture = picture;
        this.users_conversations = users_conversations;
        this.conversationList = conversationList;
    }

}