package com.attender;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.Calendar;

//import static com.example.rita.attender.R.id.listView;

public class CalendarPageActivity extends Activity
{
    private AttenderBL bl;
    private GridView calendarGrid;
    private ArrayList<Event> userEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userEvents = new ArrayList<Event>();
        bl = new AttenderBL();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        //=========================  Global AppData Set  ======================================

        // Calling Application class (see application tag in AndroidManifest.xml)
        final AppData appData = (AppData) getApplicationContext();


        //===========================  Text Fonts =======================================
        TextView Calendar_TV = (TextView) findViewById(R.id.Calendar_TXT);
        TextView Event_TV = (TextView) findViewById(R.id.Event_List_TXT);
        Typeface tf = Typeface.createFromAsset(getAssets(), "ostrich-regular.ttf");
        Calendar_TV.setTypeface(tf);
        Event_TV.setTypeface(tf);


        //=============================  load date  ======================================

        final ListView listView = (ListView) findViewById(R.id.listView);
        calendarGrid = (GridView) findViewById(R.id.calendar_grid);


        listView.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            // private int position;

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent myIntent = new Intent(getApplicationContext(), Event_Page_Activity.class);
                int eventNum = position;
                Event testE = userEvents.get(eventNum);
                myIntent.putExtra("CurrentEvent", userEvents.get(eventNum));
                startActivity(myIntent);
            }
        });

  /*      myCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {

                userEvents.clear();
                //appData.resetData(AccessToken.getCurrentAccessToken().getToken());
                if(appData.get_userEventList() != null)
                    for(Event ev : appData.get_userEventList())
                    {
                        if(ev.isDateEquals(year, month+1,dayOfMonth))
                            userEvents.add(ev);
                    }
                else
                    printToastDialog("no events to show");


                if(userEvents.size()==0)
                {
                    listView.setAdapter(null);
                    printToastDialog("No events to show!");
                }
                else {
                    EventAdapter adapter = new EventAdapter(getBaseContext(),userEvents);
                    listView.setAdapter(adapter);
                }
            }
        });
*/
    }




    //==============  Alert Dialog ===============
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
    private void printToastDialog(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
