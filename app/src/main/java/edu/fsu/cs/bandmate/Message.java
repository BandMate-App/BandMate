package edu.fsu.cs.bandmate;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.SaveCallback;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.Objects;

import java.util.ArrayList;
@ParseClassName("Message")
public class Message extends ParseObject {
    public Message(){}

    public static final String KEY_BODY = "body";
    public static final String KEY_USER = "user";

    public String getbody(){
        return getString(KEY_BODY).replace("\"", "");
    }

    public String getUser(){
        return getString(KEY_USER);
    }

    public void setUser(String user){
        put(KEY_USER,user);
    }

    public void setBody(String body){
        put(KEY_BODY,body);
    }


}
