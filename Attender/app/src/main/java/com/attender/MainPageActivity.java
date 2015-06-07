package com.attender;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class MainPageActivity  extends Activity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        TextView userName = (TextView)findViewById(R.id.User_Name_textView);

        //=============================  Global AppData Set  ======================================

        // Calling Application class (see application tag in AndroidManifest.xml)
        AppData appData = (AppData) getApplicationContext();



        //========================  Guest Check  ==================================================
        if(AccessToken.getCurrentAccessToken() == null && appData.get_googleApiClient() == null)
        {
            LinearLayout layout = (LinearLayout) findViewById(R.id.facebooklogutLayout);
            layout.setVisibility(View.INVISIBLE);
            layout = (LinearLayout) findViewById(R.id.Event_Calendar_Layout);
            layout.setAlpha(.5f);
            layout.setEnabled(false);
            layout = (LinearLayout) findViewById(R.id.Chat_Layout);
            layout.setAlpha(.5f);
            layout.setEnabled(false);
        }


        if(AccessToken.getCurrentAccessToken() != null)
        {
            // facebook login
            userName.setText(Profile.getCurrentProfile().getName());
            //Set name and email in global/application context
            //appData.resetData("facebook", AccessToken.getCurrentAccessToken().getToken(), null);
        }
        else if(appData.get_googleApiClient() != null)
        {
            if(appData.get_googleApiClient().isConnected())
            {
            // google login
            // String name = Plus.PeopleApi.getCurrentPerson(appData.get_googleApiClient()).getDisplay();
            userName.setText(getIntent().getStringExtra("google name"));
            // appData.resetData("google", null, appData.get_googleApiClient());
            }
            else
                userName.setText("google login failed");
        }
        else
        {
            // guest login
            userName.setText("guest");
            //Set name and email in global/application context
            //appData.resetData("guest", null, null);
        }


    }
    public void log_out_to_home(View v)
    {
        Intent intent = new Intent(this,loginPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity

        AccessToken.setCurrentAccessToken(null);
    }


    public void chat_pressed(View v)
    {
        Intent intent = new Intent(this,ChatPageActivity.class);
        startActivity(intent);
    }
    public void event_calendar_pressed(View v)
    {
        Intent intent = new Intent(this,CalendarPageActivity.class);
        startActivity(intent);
    }

    public void search_event_pressed(View v)
    {
        Intent intent=new Intent(this,searchEventActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        AppData appData = (AppData) getApplicationContext();
        appData.get_googleApiClient().disconnect();
    }

}