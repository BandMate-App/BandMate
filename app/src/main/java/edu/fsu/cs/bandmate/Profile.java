package edu.fsu.cs.bandmate;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Profile")
public class Profile extends ParseObject {
    public static final String KEY_PROFILEPICTURE="ProfilePicture";
    public static final String KEY_USER="user";
    public static final String KEY_NAME="Name";
    public static final String KEY_GENRE="Genre";

    public ParseFile getImage(){
        return getParseFile(KEY_PROFILEPICTURE);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public String getName(){
        return (String) getParseUser(KEY_USER).get(KEY_NAME);
    }

    public String getGenre(){return getString(KEY_GENRE);}


}
