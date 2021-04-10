package edu.fsu.cs.bandmate;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Objects;
@ParseClassName("Conversation")
public class Conversation extends ParseObject {
    public Conversation(){}
    public static final String KEY_MESSAGES = "messages";
    public static final String KEY_CONVERSATION_ID = "conversationId";
    public static final String KEY_SELF="self";
    public static final String KEY_OTHER="other";

    public ArrayList<String> getMessages(){
        return (ArrayList<String>) this.get(KEY_MESSAGES);
    }
}
