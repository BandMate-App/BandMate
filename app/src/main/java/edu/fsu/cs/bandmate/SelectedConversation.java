package edu.fsu.cs.bandmate;
import android.graphics.Bitmap;

import com.parse.ParseUser;

import java.io.Serializable;
import java.util.List;

public class SelectedConversation implements Serializable {
    public List<Object> messages;
    public ParseUser match;
    public Bitmap picture;
    public SelectedConversation(List<Object> messages,ParseUser match,Bitmap picture){
        this.messages = messages;
        this.match = match;
        this.picture = picture;
    }

}