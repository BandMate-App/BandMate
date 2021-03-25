import java.util.List;
/*
 Stores all necessary user profile information

 Uses profile map to map gender, instrument, and secondary instruments to integers
 */
public class userProfile {
    private String fName;
    private String lName;
    private String eMail;
    private String phoneNumber;
    private String password;
    private int gender;
    private int primaryInstrument;
    private List<Integer> secondaryInstruments;

    /*
     Default Constructor sets all variables to invalid values
     */
    userProfile(){
        fName = "";
        lName = "";
        eMail = "";
        phoneNumber = "";
        password = "";
        gender = -1;
        primaryInstrument = -1;
        secondaryInstruments.add(-1);
    }

    /*
     Parameterized Constructor
     */
    userProfile(String fName,String lName, String eMail, String phoneNumber,String password,
                int gender,int primaryInstrument,List<Integer> secondaryInstruments){
        this.fName = fName;
        this.lName = lName;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.gender = gender;
        this.primaryInstrument = primaryInstrument;
        this.secondaryInstruments = secondaryInstruments;
    }

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

    public void setSecondaryInstruments(List<Integer> secondaryInstruments) {
        this.secondaryInstruments = secondaryInstruments;
    }

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
