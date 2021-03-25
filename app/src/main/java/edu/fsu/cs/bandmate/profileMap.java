package edu.fsu.cs.bandmate;

/*
 Helper class, maps user profile data to an int to make storing easier.
 */
public class profileMap {
    private int guitar = 0;
    private int piano = 1;
    private int ukulele = 2;
    private int drums = 3;
    private int vocals = 4
            ;
    private String[] instruments = {"guitar","piano","ukulele","drums","vocals"};


    public int MapInstrument(String instrument){
        for(int i = 0; i <instruments.length;i++) {
            if (instruments[i].toLowerCase().trim().equals(instrument.toLowerCase().trim()))
                return i;
        }
        // return -1 if not in list of instruments
        return -1;
    }

    public String getInstrumentType(int i){
        if(i>=instruments.length)
            return "invalid choice";
        return instruments[i];
    }


    private int male = 0;
    private int female =1;
    private int other = 2;
    private String[] genders = {"male","female","other"};

    public int mapGender(String gender){
        int i = 0;
        for(;i<genders.length;i++) {
            if(instruments[i].toLowerCase().trim().equals(gender.toLowerCase().trim()))
                return i;
        }
        return -1;
    }

    public String getGender(int i){
        if(i>=genders.length)
            return "invalid choice";
        return genders[i];
    }

}
