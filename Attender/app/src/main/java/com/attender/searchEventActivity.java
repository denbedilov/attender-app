package com.attender;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class searchEventActivity extends Activity
{
    private AttenderBL bl;
    private Spinner typeSpinner;
    private Spinner dateSpinner;
    private Spinner citySpinner;
    private  ListView listView;
    private ArrayList<Event> events;
    private static ProgressDialog progress;
    private static Context context;
    int lisFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        bl = new AttenderBL();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        dateSpinner = (Spinner) findViewById(R.id.date_spinner);
        citySpinner = (Spinner) findViewById(R.id.city_spinner);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // private int position;

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent myIntent = new Intent(getApplicationContext(), Event_Page_Activity.class);
                int eventNum = position;
                myIntent.putExtra("CurrentEvent",events.get(eventNum));
                startActivity(myIntent);
            }
        });

        ArrayAdapter<CharSequence> type_adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> date_adapter = ArrayAdapter.createFromResource(this,
                R.array.dates_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> city_adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);

        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        dateSpinner.setAdapter(date_adapter);
        typeSpinner.setAdapter(type_adapter);
        citySpinner.setAdapter(city_adapter);

        AdapterView.OnItemSelectedListener lis;
        lis = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /* listener plaster */
                if(lisFlag >= 2)
                    searchPressed(getCurrentFocus());
                else
                    lisFlag++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                searchPressed(getCurrentFocus());
            }
        };

        typeSpinner.setOnItemSelectedListener(lis);
        citySpinner.setOnItemSelectedListener(lis);
        dateSpinner.setOnItemSelectedListener(lis);

    }

    public void searchPressed(View v)
    {
        progress = ProgressDialog.show(this, "Loading Events",
                "Please wait..", true);

        context = this;

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // getting data from spinners
                String theDate=dateSpinner.getSelectedItem().toString();
                String theType= typeSpinner.getSelectedItem().toString();
                theType=theType.replaceAll("\\s","%20");
                String theCity=citySpinner.getSelectedItem().toString();
                theCity=theCity.replaceAll("\\s","%20");
                switch(theDate)
                {
                    case "1 day ahead":      theDate="1d";   break;
                    case "1 week ahead":     theDate="1w";   break;
                    case "1 month ahead":    theDate="1m";   break;
                }
                // getting events from the server
                events = bl.getEvents(theType, theDate, theCity);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        if(events == null)
                        {
                            listView.setAdapter(null);
                            printAlertDialog("No events to show!");
                        }
                        else {
                            EventAdapter adapter = new EventAdapter(context, events);
                            listView.setAdapter(adapter);
                        }
                        progress.dismiss();
                    }
                });
            }
        }).start();
    }
    public void itemPressed(View v)
    {
        Intent intent=new Intent(this,Event_Page_Activity.class);
        startActivity(intent);
    }
    public void explorePressed(View v)
    {
        Intent intent=new Intent(this,CalendarPageActivity.class);
        startActivity(intent);
    }
    public void chatPressed(View v)
    {
        Intent intent=new Intent(this,ChatPageActivity.class);
        startActivity(intent);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_search_event, menu);
//        return true;
//    }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            // Handle action bar item clicks here. The action bar will
//            // automatically handle clicks on the Home/Up button, so long
//            // as you specify a parent activity in AndroidManifest.xml.
//            int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
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
