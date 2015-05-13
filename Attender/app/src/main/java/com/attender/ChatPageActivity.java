package com.attender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatPageActivity extends Activity
{
    AttenderBL bl;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bl = new AttenderBL();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        ListView listView = (ListView) findViewById(R.id.listView);

//        events = bl.getEvents("type", "fds", "fdsf");  ///!!!!!!!!!!!!!!!!!!!!!!!!!!
//
//        EventAdapter adapter = new EventAdapter(this, events);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            // private int position;
//
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                Intent myIntent = new Intent(getApplicationContext(), EventPageActivity.class);
//                Event testE = events.get(position);
//                myIntent.putExtra("CurrentEvent",events.get(position));
//                startActivity(myIntent);
//            }
//        });
    }
    public void eventsPressed(View v)
    {
        Intent intent=new Intent(this,SearchEventActivity.class);
        startActivity(intent);
    }
    public void explorePressed(View v)
    {
        Intent intent=new Intent(this,CalendarPageActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
