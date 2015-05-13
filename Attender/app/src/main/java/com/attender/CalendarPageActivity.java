package com.attender;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

//import static com.example.rita.attender.R.id.listView;

public class CalendarPageActivity extends Activity
{
    AttenderBL bl;
    ArrayList<Event> events;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        bl = new AttenderBL();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        ListView listView = (ListView) findViewById(R.id.listView);


     //   events = bl.getEvents("type", "fds", "aaa");  ///!!!!!!!!!!!!!!!!!!!!!!!!!!

      //  if(events == null)
     //   {
     //       printAlertDialog("No events to show!");
     ////   }
      //  else
      //  {
      //      EventAdapter adapter = new EventAdapter(this, events);
       //     listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                // private int position;

                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent myIntent = new Intent(getApplicationContext(), Event_Page_Activity.class);
                    int eventNum = position;
                    Event testE = events.get(eventNum);
                    myIntent.putExtra("CurrentEvent",events.get(eventNum));
                    startActivity(myIntent);
                }
            });
        }
    //}
    public void eventsPressed(View v)
    {
        Intent intent=new Intent(this,searchEventActivity.class);
        startActivity(intent);
    }
    public void chatPressed(View v)
    {
        Intent intent=new Intent(this,ChatPageActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_page, menu);
        return true;
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
