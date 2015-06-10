package com.attender;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;
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
            case "server":
                saveData("token", _userToken);
                set_attenderBL(new AttenderBL());
                set_userEventList(_attenderBL.getUserEvents(_userToken));
            default:
                set_attenderBL(new AttenderBL());
                set_userEventList(_attenderBL.getUserEvents(_userToken));
                break;
        }

        saveData("loginType", loginType);
    }

    /* saving into the internal storage 'data' in 'fileName'  */
    private void saveData(String fileName, String data) {
        try {
            FileOutputStream outputStream;
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "cant save data", Toast.LENGTH_SHORT).show();
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
