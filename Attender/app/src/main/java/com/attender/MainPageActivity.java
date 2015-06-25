package com.attender;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

/**
 * Created by Shai on 17/05/2015.
 */

public class MainPageActivity extends Activity{
    private AppData appData;
    private TextView userName;
    private AttenderBL bl;
    private Toast toast;
    private DialogAdapter dialog;
    private Button logoutUser;
    private View chat,calender;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        loadPage();
    }

    private void loadPage() {
        setContentView(R.layout.activity_main_page);
        dialog = new DialogAdapter();
        bl = new AttenderBL();

        userName = (TextView) findViewById(R.id.User_Name_textView);
        chat = findViewById(R.id.Chat_Layout);
        calender = findViewById(R.id.Event_Calendar_Layout);
        logoutUser = (Button) findViewById(R.id.user_logout_cmd);

        //=============================  Global AppData Set  ======================================

        // Calling Application class (see application tag in AndroidManifest.xml)
        appData = (AppData) getApplicationContext();
        appData.getSavedData();




        //====================== Main Page login handle ======================================
        loadUserName(userName);
    }

    private void loadUserName(TextView userName) {
        if (appData.get_loginType().equals("guest"))
        {
            userName.setText("Guest");
            chat.setAlpha(.5f);
            calender.setAlpha(.5f);
            logoutUser.setText("Log In");
        }
        else{
            userName.setText(appData.get_userName());
        }
    }

    public void logOutMethod(View v) {
        if (appData.get_loginType().compareTo("facebook")==0) {
            AccessToken.setCurrentAccessToken(null);
        }
        userLogoutPressed(v);
    }

    public void userLogoutPressed(View v) {
        Intent intent = new Intent(this, loginPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (appData.get_loginType().compareTo("google")==0) {
            intent.putExtra("GOOGLE_LOGOUT_STATE", "logout");
    }
        appData.resetData("guest", null, null);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }

    public void chat_pressed(View v) {
        if (appData.get_loginType().compareTo("guest") == 0)
            dialog.printDialog("Login to use", getApplicationContext());
        else {
            Intent intent = new Intent(this, ChatPageActivity.class);
            startActivity(intent);
        }
    }

    public void event_calendar_pressed(View v) {
        if (appData.get_loginType().compareTo("guest") == 0)
            dialog.printDialog("Login to use", getApplicationContext());
        else {
            Intent intent = new Intent(this, CalendarPageActivity.class);
            startActivity(intent);
        }
    }

    public void search_event_pressed(View v) {
        Intent intent = new Intent(this, searchEventActivity.class);
        startActivity(intent);
    }

    /* back button canceling */
    @Override
    public void onBackPressed() {
        if (appData.get_loginType().compareTo("guest") == 0)
            finish();
        else {
            dialog.printDialog("press logout button to exit", getApplicationContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    @Override
    protected void onPause() {

        super.onPause();
        //add reading from app Data
    }
}