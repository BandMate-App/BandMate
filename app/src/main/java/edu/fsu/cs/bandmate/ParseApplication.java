package edu.fsu.cs.bandmate;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application {
    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        //Register Profile Subclass
        ParseObject.registerSubclass(ConversationList.class);
        ParseObject.registerSubclass(Profile.class);
        ParseObject.registerSubclass(Conversation.class);
        ParseObject.registerSubclass(Message.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fWhSBuadXNK6VFHBkBietdcclx3s6YxAM8fXzMgC")
                .clientKey("WERJdDH0W5fs2voJLYZtoh76CvKHB5ZmtbPGBfgY")
                .server("https://parseapi.back4app.com")
                .build()
        );
        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
        parseInstallation.put("GCMSenderId","779436643107");
        parseInstallation.saveInBackground();
    }
}   