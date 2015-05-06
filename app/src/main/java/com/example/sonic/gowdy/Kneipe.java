package com.example.sonic.gowdy;

/**
 * Created by sonic on 06.05.15.
 */
public class Kneipe {

    private String mName;
    private String mAdresse;
    private String mTyp;
    private String mBewertung;


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getAdresse() {
        return mAdresse;
    }

    public void setAdresse(String adresse) {
        this.mAdresse = adresse;
    }

    public String getTyp() {
        return mTyp;
    }

    public void setTyp(String typ) {
        this.mTyp = typ;
    }

    public String getBewertung() {
        return mBewertung;
    }

    public void setBewertung(String bewertung) {
        this.mBewertung = bewertung;
    }
}
