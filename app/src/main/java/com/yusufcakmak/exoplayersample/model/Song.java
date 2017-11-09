package com.yusufcakmak.exoplayersample.model;

import java.io.Serializable;

/**
 * Created by ishan on 09-11-2017.
 */

public class Song implements Serializable{
    private String _ID;
    private String ARTIST;
    private String TITLE;
    private String DATA;
    private String DISPLAY_NAME;
    private String DURATIO;

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getDURATIO() {
        return DURATIO;
    }

    public void setDURATIO(String DURATIO) {
        this.DURATIO = DURATIO;
    }
}
