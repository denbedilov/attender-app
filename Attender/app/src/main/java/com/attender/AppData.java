package com.attender;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by Shai on 31/05/2015.
 */
public class AppData extends Application
{
    private ArrayList<Event> _userEventList;
    private AttenderBL       _attenderBL;
    private String           _userToken;
    private GoogleApiClient  _googleApiClient;
    private String           _loginType;

    public AppData()
    {
        set_userEventList(null);
        set_attenderBL(null);
        set_userToken(null);
        set_googleApiClient(null);
        set_loginType("");
    }


    public void resetData(String loginType, String facebookTok, GoogleApiClient  googleApiClient)
    {
        if(facebookTok != null || googleApiClient != null)
        {
            switch(loginType)
            {
                case "google":
                    if(googleApiClient != null)     setGoogleLogin(googleApiClient);
                    else                            setGuest();
                    break;

                case "facebook":
                    if(facebookTok != null)     setFacebookLogin(facebookTok);
                    else                        setGuest();
                    break;
            }
        }
        else
        {
            setGuest();
        }
    }

    //If tok is not null, return true, else return false
    private void setGuest()
    {
        set_loginType("guest");
        set_userEventList(null);
        set_attenderBL(null);
        set_userToken(null);
        set_googleApiClient(null);
    }

    private void setGoogleLogin(GoogleApiClient googleApiClient)
    {
        set_loginType("google");
        set_attenderBL(new AttenderBL());
        set_userToken(null);
        set_googleApiClient(googleApiClient);
        set_userEventList(_attenderBL.getUserEvents(_userToken));   //get array list of user marked events
    }

    private void setFacebookLogin(String userToken)
    {
        set_loginType("facebook");
        set_attenderBL(new AttenderBL());
        set_userToken(userToken);
        set_googleApiClient(null);
        set_userEventList(_attenderBL.getUserEvents(_userToken));   //get array list of user marked events
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

    public GoogleApiClient get_googleApiClient() {
        return _googleApiClient;
    }

    public void set_googleApiClient(GoogleApiClient _googleApiClient) {
        this._googleApiClient = _googleApiClient;
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
