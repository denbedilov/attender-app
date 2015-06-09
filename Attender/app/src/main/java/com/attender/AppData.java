package com.attender;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Shai on 31/05/2015.
 */
public class AppData extends Application
{
    private ArrayList<Event> _userEventList;
    private AttenderBL       _attenderBL;
    private String           _userToken;
    private String           _loginType;

    public AppData()
    {
        set_userEventList(null);
        set_attenderBL(null);
        set_userToken(null);
        set_loginType("");
    }


    public void resetData(String loginType,String token)
    {
        FileOutputStream outputStream;

        if(token == null)
            loginType = "guest";

        set_loginType(loginType);
        set_userToken(token);

        switch (loginType)
        {
            case "guest":
                set_attenderBL(null);
                set_userEventList(null);
                break;
            default:
                set_attenderBL(new AttenderBL());
                set_userEventList(_attenderBL.getUserEvents(_userToken));
                break;
        }

        try {
            outputStream = openFileOutput("loginType", Context.MODE_PRIVATE);
            outputStream.write(loginType.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //If tok is not null, return true, else return false

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

    public String get_loginType()
    {
        return _loginType;
    }

    public void set_loginType(String _loginType)
    {
        this._loginType = _loginType;
    }
}
