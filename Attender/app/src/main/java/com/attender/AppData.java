package com.attender;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Shai on 31/05/2015.
 */
public class AppData extends Application
{
    private boolean          _isGuest;
    private ArrayList<Event> _userEventList;
    private AttenderBL       _attenderBL;
    private String           _userToken;


    public AppData()
    {
        set_isGuest(true);
        set_userEventList(null);
        set_attenderBL(null);
        set_userToken(null);
    }


    public void resetData(String userToken)
    {
        if(userToken == null)
        {
            set_isGuest(true);
            set_userEventList(null);
            set_attenderBL(null);
            set_userToken(null);
        }
        else
        {
            set_isGuest(false);
            set_attenderBL(new AttenderBL());
            set_userToken(userToken);
            set_userEventList(_attenderBL.getUserEvents(_userToken));   //get arry list of user marked events
        }
    }

    public boolean is_isGuest() {
        return _isGuest;
    }

    public void set_isGuest(boolean _isGuest) {
        this._isGuest = _isGuest;
    }

    public ArrayList<Event> get_userEventList() {
        return _userEventList;
    }

    public void set_userEventList(ArrayList<Event> _userEventList) {
        this._userEventList = _userEventList;
    }

    public AttenderBL get_attenderBL() {
        return _attenderBL;
    }

    public void set_attenderBL(AttenderBL _attenderBL) {
        this._attenderBL = _attenderBL;
    }

    public String get_userToken() {
        return _userToken;
    }

    public void set_userToken(String _userToken) {
        this._userToken = _userToken;
    }
}
