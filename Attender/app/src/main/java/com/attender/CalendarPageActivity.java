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
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

//import static com.example.rita.attender.R.id.listView;

public class CalendarPageActivity extends Activity
{
    private AttenderBL bl;
    private GridView calendarGrid;
    private ArrayList<Event> userEvents;
    Calendar _calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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


        //=============================  Event List  ======================================

        final ListView listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // private int position;

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getApplicationContext(), Event_Page_Activity.class);
                int eventNum = position;
                Event testE = userEvents.get(eventNum);
                myIntent.putExtra("CurrentEvent", userEvents.get(eventNum));
                startActivity(myIntent);
            }
        });


        //======================  Set Next Button  ==========================
        ImageView next = (ImageView) findViewById(R.id.Calendar_Next_Image);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setNextMonth();
            }
        });

        //======================  Set Prev Button  ==========================
        ImageView prev = (ImageView) findViewById(R.id.Calendar_Prev_Image);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPrevMonth();
            }
        });



        //============================  Calendar Grid =============================================

        _calendar = Calendar.getInstance();                     //Set Current Date
        refreshCalendarGrid();









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




    //==============================================================================================
    //                               Calendar Functions
    //==============================================================================================

    private void refreshCalendarGrid()
    {
        //====== Set Calendar Header =========
        TextView monthName = (TextView) findViewById(R.id.Date_Name_TXT);
        monthName.setText(getMounthNameByNum(getMonth() ));
        TextView yearName = (TextView) findViewById(R.id.Date_Year_TXT);
        yearName.setText(getYear() + "");

        //=============== Update Clendar Values =======================
        GridView calGrid = (GridView) findViewById(R.id.calendar_grid);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, calendarDateGenerator() );
        calGrid.setAdapter(aa);
    }


    private ArrayList<String> calendarDateGenerator()
    {
        ArrayList<String> items = new ArrayList<String>();

        int numOfItems = 0;

        //==============  Adding last month final dates  ======================
        int lastMonthLength = getLastMonthLength();
        int dayOfWeek = _calendar.get(Calendar.DAY_OF_WEEK);

        for(int i = lastMonthLength - dayOfWeek; i < lastMonthLength ; i++)
        {
            items.add("" + i);
            numOfItems++;
        }

        //==============  Adding this month dates  ======================

        for(int i = 1 ; i <= getMonthLength() ; i++)
        {
            items.add("" + i);
            numOfItems++;
        }

        //==============  Adding next month first dates  =================
        if(numOfItems > 35)
            for(int i = 1 ; numOfItems <= 41 ; i++)
            {
                items.add("" + i);
                numOfItems++;
            }

        return items;
    }

    private String getMounthNameByNum(int mounthNum)
    {
        mounthNum += 1; // Jan = 0, Dec = 11
        switch(mounthNum)
        {
            case 1:   return "January";
            case 2:   return "February";
            case 3:   return "March";
            case 4:   return "April";
            case 5:   return "May";
            case 6:   return "June";
            case 7:   return "July";
            case 8:   return "August";
            case 9:   return "September";
            case 10:  return "October";
            case 11:  return "November";
            case 12:  return "December";
            default:  return "";
        }
    }

    private int getMonthLength()
    {
        Calendar tempCal = new GregorianCalendar(getYear(), getMonth(), 1);
        return tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private int getLastMonthLength()
    {
        int month = getMonth();
        int year  = getYear();

        if(month == 1)  year -= 1;

        Calendar tempCal = new GregorianCalendar(year, month -1, 1);
        return tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    //============= Calendar Setters and Getters  ======================

    private void setCalendar(int year, int mounth, int day) {   _calendar = new GregorianCalendar(year, mounth, day);   }

    private int getYear()   {   return _calendar.get(Calendar.YEAR);  }
    private int getMonth()  {   return _calendar.get(Calendar.MONTH);  }
    private int getDay()    {   return _calendar.get(Calendar.DAY_OF_MONTH);  }


    //=================  Button Functions ===============================

    private void setNextMonth()
    {
        if(getMonth() == 11)
            _calendar = new GregorianCalendar(getYear() + 1, 0, 1);
        else
            _calendar = new GregorianCalendar(getYear(), getMonth() + 1, 1);

        refreshCalendarGrid();
    }
    private void setPrevMonth()
    {
        if(getMonth() == 1)
            _calendar = new GregorianCalendar(getYear() - 1, 11, 1);
        else
            _calendar = new GregorianCalendar(getYear(), getMonth() - 1, 1);

        refreshCalendarGrid();
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
