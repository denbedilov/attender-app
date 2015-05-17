package com.attender;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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