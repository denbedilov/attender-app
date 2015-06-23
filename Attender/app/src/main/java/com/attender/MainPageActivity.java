package com.attender;

import android.app.Activity;
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

/**
 * Created by Shai on 17/05/2015.
 */

public class MainPageActivity extends Activity {
    private AppData appData;
    private TextView userName;
    private AttenderBL bl;
    private Toast toast;
    private DialogAdapter dialog;

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

        //=============================  Global AppData Set  ======================================

        // Calling Application class (see application tag in AndroidManifest.xml)
        appData = (AppData) getApplicationContext();


        Button logoutUser = (Button) findViewById(R.id.user_logout_cmd);


        //====================== Main Page login handle ======================================
        loadUserName(userName);
    }

    private void loadUserName(TextView userName) {
        if (appData.get_loginType().equals("guest"))
            userName.setText("Guest");
        else
            userName.setText(appData.get_userName());
    }

    public void logOutMethod(View v) {
        switch (appData.get_loginType()) {
            case "google":
                userLogoutPressed(v);
                break;
            case "facebook":
                userLogoutPressed(v);
                AccessToken.setCurrentAccessToken(null);
                break;
            case "server":
                userLogoutPressed(v);
                break;
        }
    }

    public void userLogoutPressed(View v) {
        Intent intent = new Intent(this, loginPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
        appData.resetData("guest", null, null);
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
        else
        {
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