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


/**
 * Created by Shai on 17/05/2015.
 */
public class MainPageActivity  extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        TextView userName = (TextView)findViewById(R.id.User_Name_textView);

        if(AccessToken.getCurrentAccessToken() == null)
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

        // print server response to login
        //printAlertDialog(getIntent().getStringExtra("serverResponse"));

        if(AccessToken.getCurrentAccessToken() != null)
            userName.setText(Profile.getCurrentProfile().getName());
        else
            userName.setText("guest");
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

    private void printAlertDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LOGIN DIALOG");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        builder.show();
    }

}