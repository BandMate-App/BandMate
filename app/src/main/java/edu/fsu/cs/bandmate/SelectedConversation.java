package edu.fsu.cs.bandmate;
import android.graphics.Bitmap;

import com.parse.ParseUser;

import java.io.Serializable;
import java.util.List;

public class SelectedConversation implements Serializable {
    public Conversation conversation;
    public ParseUser match;
    public Bitmap picture;
    public SelectedConversation(Conversation messages,ParseUser match,Bitmap picture){
        this.conversation = messages;
        this.match = match;
        this.picture = picture;
    }

}