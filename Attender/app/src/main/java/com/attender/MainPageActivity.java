package com.attender;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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



        if(AccessToken.getCurrentAccessToken() != null)
            userName.setText(Profile.getCurrentProfile().getName());
        else
            userName.setText("guest");
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


}