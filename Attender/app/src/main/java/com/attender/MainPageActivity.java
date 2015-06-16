package com.attender;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class MainPageActivity  extends Activity
{
    private AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        AttenderBL bl = new AttenderBL();

        printDialog(getIntent().getStringExtra("user_type"));

        TextView userName = (TextView)findViewById(R.id.User_Name_textView);

        //=============================  Global AppData Set  ======================================

        // Calling Application class (see application tag in AndroidManifest.xml)
        appData = (AppData) getApplicationContext();




        //====================== Main Page login handle ======================================
        switch(appData.get_loginType())
        {
            case "facebook":
                userName.setText(Profile.getCurrentProfile().getName());
                Button logoutUser= (Button)findViewById(R.id.user_logout_cmd);
                logoutUser.setVisibility(logoutUser.INVISIBLE);
                break;
            case "guest":
                userName.setText("guest");
                LinearLayout layout = (LinearLayout) findViewById(R.id.facebooklogutLayout);
                layout.setVisibility(View.INVISIBLE);
                layout = (LinearLayout) findViewById(R.id.Event_Calendar_Layout);
                layout.setAlpha(.5f);
                layout = (LinearLayout) findViewById(R.id.Chat_Layout);
                layout.setAlpha(.5f);
                Button userLogout = (Button) findViewById(R.id.user_logout_cmd);
                userLogout.setVisibility(View.INVISIBLE);
                break;
            case "server":
                userName.setText(bl.getUserDetails(appData.get_userToken()));
                layout = (LinearLayout) findViewById(R.id.facebooklogutLayout);
                layout.setVisibility(View.INVISIBLE);
                break;
            case "google":
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

    /* back button canceling */
    @Override
    public void onBackPressed()
    {
        if(appData.get_loginType().compareTo("guest")==0)
            finish();
        else
            printDialog("press logout button to exit");
    }

    private void printDialog(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}