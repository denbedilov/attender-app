package com.attender;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

/**
 * Created by Shai on 17/05/2015.
 */
public class MainPageActivity  extends Activity
{
    private AppData appData;
    private AttenderBL bl;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        bl = new AttenderBL();

        printDialog(getIntent().getStringExtra("user_type"));

        TextView userName = (TextView)findViewById(R.id.User_Name_textView);

        //=============================  Global AppData Set  ======================================

        // Calling Application class (see application tag in AndroidManifest.xml)
        appData = (AppData) getApplicationContext();



        //========================  Guest Check  ==================================================
        if(appData.get_loginType().compareTo("guest") == 0)
        {
            LinearLayout layout = (LinearLayout) findViewById(R.id.facebooklogutLayout);
            layout.setVisibility(View.INVISIBLE);
            layout = (LinearLayout) findViewById(R.id.Event_Calendar_Layout);
            layout.setAlpha(.5f);
            layout = (LinearLayout) findViewById(R.id.Chat_Layout);
            layout.setAlpha(.5f);

        }

        //====================== Hello "name" ======================================
        switch(appData.get_loginType())
        {
            case "facebook":
                userName.setText(Profile.getCurrentProfile().getName());
                break;
            case "guest":
                userName.setText("guest");
                break;
            case "server":
                userName.setText(bl.getUserDetails(appData.get_userToken()));
                break;
            default:
                userName.setText(getIntent().getStringExtra("name"));
                break;
        }
    }

    public void log_out_to_home(View v)
    {
        Intent intent = new Intent(this,loginPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
        appData.resetData("guest", null);
        AccessToken.setCurrentAccessToken(null);
    }

    public void userLogoutPressed(View v)
    {
        Intent intent = new Intent(this,loginPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
        appData.resetData("guest", null);
    }
    public void chat_pressed(View v)
    {
        if(appData.get_loginType().compareTo("guest")==0)
            printDialog("log-in for use");
        else {
            Intent intent = new Intent(this, ChatPageActivity.class);
            startActivity(intent);
        }
    }
    public void event_calendar_pressed(View v)
    {
        if(appData.get_loginType().compareTo("guest")==0)
            printDialog("log-in for use");
        else {
            Intent intent = new Intent(this, CalendarPageActivity.class);
            startActivity(intent);
        }
    }

    public void search_event_pressed(View v)
    {
        Intent intent=new Intent(this,searchEventActivity.class);
        startActivity(intent);
    }

    private void printDialog(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}