package com.attender;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Shai on 31/05/2015.
 */
public class AppData extends Application
{
    private ArrayList<Event>    _userEventList;
    private AttenderBL          _attenderBL;
    private String              _userToken;
    private String              _loginType;
    private String              _userName;

    public AppData()
    {
        set_userEventList(null);
        set_attenderBL(null);
        set_userToken(null);
        set_loginType("");
    }

    public void resetData(String loginType,String token, String userName)
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
                set_userName(null);
                break;
            default:
                set_attenderBL(new AttenderBL());
                set_userName(userName);
                set_userEventList(_attenderBL.getUserEvents(_userToken));
                saveData("token", _userToken);
                saveData("userName", _userName);
                break;
        }

        /* saving token and loginType */
        saveData("loginType", _loginType);
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

    public void getSavedData() {
        String loginType = getData("loginType");
        if (loginType != null)
            resetData(loginType, getData("token"),getData("userName"));
        else
            resetData("guest", null, null);
    }

    private String getData(String fileName) {
        FileInputStream inputStream;
        String retData = null;
        try {
            inputStream = openFileInput(fileName);
            while (inputStream.available() > 0) {
                retData += (char) inputStream.read();
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (retData != null)
            return retData.substring(4, retData.length());
        else
            return null;
    }


    public ArrayList<Event> get_userEventList() {
        return _userEventList;
    }

    public void set_userEventList(ArrayList<Event> _userEventList) { this._userEventList = _userEventList; }

    public AttenderBL get_attenderBL() {
        return _attenderBL;
    }

    private void set_attenderBL(AttenderBL _attenderBL) {
        this._attenderBL = _attenderBL;
    }

    public String get_userToken() {
        return _userToken;
    }

    private void set_userToken(String _userToken) {
        this._userToken = _userToken;
    }

    public String get_loginType()
    {
        return _loginType;
    }

    private void set_loginType(String _loginType)
    {
        this._loginType = _loginType;
    }

    private void set_userName(String userName) {
        this._userName = userName;
    }

    public String get_userName(){return this._userName;}
}
