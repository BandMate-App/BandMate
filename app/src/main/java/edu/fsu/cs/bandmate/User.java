package edu.fsu.cs.bandmate;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
/*
 Stores all necessary user profile information

 Uses profile map to map gender, instrument, and secondary instruments to integers
 */
public class User{

/*
Default Constructor sets all variables to invalid values
*/
    public User(){
    }

    private String uName;
    private String fName;
    private String lName;
    private String eMail;
    private String phoneNumber;
    private String password;
    private int gender;
    private int primaryInstrument;
    private ArrayList<Integer> secondaryInstruments;

/*
 Parsing Constants
 */
    public static final String KEY_PROFILEPICTURE="ProfilePicture";
    public static final String KEY_USER="user";
    public static final String KEY_FIRSTNAME="FirstName";
    public static final String KEY_lASTNAME="LastName";
    public static final String KEY_PRIMARY_INSTRUMENT = "PrimaryInstrument";
    public static final String KEY_SECONDARY_INSTRUMENTS = "SecondaryInstruments";
    public static final String KEY_GENRE="Genre";
    public static final String KEY_MP3 = "Mp3";


/*
 Parameterized Constructor
 */
    public User(String fName, String lName, String eMail, String phoneNumber, String password,
                int gender, int primaryInstrument, ArrayList<Integer> secondaryInstruments){
        this.fName = fName;
        this.lName = lName;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.gender = gender;
        this.primaryInstrument = primaryInstrument;
        this.secondaryInstruments = secondaryInstruments;
    }


/*
 SET FUNCTIONS
 */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPrimaryInstrument(int primaryInstrument) {
        this.primaryInstrument = primaryInstrument;
    }

    public void setSecondaryInstruments(ArrayList<Integer> secondaryInstruments) {
        this.secondaryInstruments = secondaryInstruments;
    }

/*
 GET FUNCTIONS
 */
    public int getGender() {
        return gender;
    }

    public int getPrimaryInstrument() {
        return primaryInstrument;
    }

    public List<Integer> getSecondaryInstruments() {
        return secondaryInstruments;
    }

    public String geteMail() {
        return eMail;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
