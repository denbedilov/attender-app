package com.attender;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class CalendarPageActivity extends Activity
{
    private AttenderBL bl;
    private GridView calendarGrid;
    private ArrayList<Event> userEvents;
    private Calendar _calendar;
    private int releventDaysGap;
    private ArrayList<Event> eventList;
    private DialogAdapter dialog;
    
    //==============================================================================================
    //                               onCreate
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //==============================  Reset Data  ==============================================

        bl = new AttenderBL();
        super.onCreate(savedInstanceState);
        userEvents = new ArrayList<Event>();
        setContentView(R.layout.activity_calendar_page);
        dialog = new DialogAdapter();
        //=========================  Global AppData Set  ===========================================

        // Calling Application class (see application tag in AndroidManifest.xml)
        final AppData appData = (AppData) getApplicationContext();

        //==============================  Text Fonts ===============================================
        TextView Calendar_TV = (TextView) findViewById(R.id.Calendar_TXT);
        TextView Event_TV = (TextView) findViewById(R.id.Event_List_TXT);
        Typeface tf = Typeface.createFromAsset(getAssets(), "ostrich-regular.ttf");
        Calendar_TV.setTypeface(tf);
        Event_TV.setTypeface(tf);


        //=============================  Event List  ===============================================
        eventList = appData.get_userEventList();    //get the updated user event list

        final ListView listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getApplicationContext(), Event_Page_Activity.class);
                int eventNum = position;
                Event testE = userEvents.get(eventNum);
                myIntent.putExtra("CurrentEvent", userEvents.get(eventNum));
                startActivity(myIntent);
            }
        });


        //===========================  Set Next Button  ============================================
        ImageView next = (ImageView) findViewById(R.id.Calendar_Next_Image);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setNextMonth();
            }
        });

        //===========================  Set Prev Button  ============================================
        ImageView prev = (ImageView) findViewById(R.id.Calendar_Prev_Image);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPrevMonth();
            }
        });




        //========================  On Calendar Click Listener  ====================================
        AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String prompt = (String)parent.getItemAtPosition(position) + getYear() + " - " + (getMonth() + 1) + " - " + (position - releventDaysGap);
//                Toast.makeText(getApplicationContext(), prompt, Toast.LENGTH_SHORT).show();


                //=========================  Show Event List =======================================

                if(position - releventDaysGap > 0 && position - releventDaysGap <= getMonthLength())
                {
                    userEvents.clear();

                    if (eventList != null) {
                        for (Event ev : eventList) {
                            if (ev.isDateEquals(getYear(), getMonth() + 1, position - releventDaysGap))
                                userEvents.add(ev);
                        }
                    }

                    if (userEvents.size() == 0) {
                        listView.setAdapter(null);
                        printToastDialog("No events");
                    } else {
                        EventAdapter adapter = new EventAdapter(getBaseContext(), userEvents);
                        listView.setAdapter(adapter);
                    }
                }
                else
                    printToastDialog("Choose only this month dates!");
            }
        };


        //============================  Calendar Grid Creation =====================================
        GridView calendarGrid = (GridView) findViewById(R.id.calendar_grid);
        calendarGrid.setOnItemClickListener(myOnItemClickListener);

        _calendar = Calendar.getInstance();         //Set Current Date
        refreshCalendarGrid();
    }



    //==============================================================================================
    //                               Calendar Functions
    //==============================================================================================

    private void refreshCalendarGrid()
    {
        //================== Set Calendar Header ==========================
        TextView monthName = (TextView) findViewById(R.id.Date_Name_TXT);
        monthName.setText(getMounthNameByNum(getMonth() ));
        TextView yearName = (TextView) findViewById(R.id.Date_Year_TXT);
        yearName.setText(getYear() + "");

        //================= Update Clendar Values =================================================
        GridView calGrid = (GridView) findViewById(R.id.calendar_grid);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, calendarDateGenerator() )
        {
            //============================  Color Cell Background  =================================
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                int backgroundColor = Color.WHITE;  //set default cell background color to White
                //int fontColor = Color.BLACK;      //set font color to Black

                if(position <= releventDaysGap || position > (releventDaysGap + getMonthLength()) )
                {
                    backgroundColor = 0x00FFFFFF;   //set cell background color to transperent
                }
                else if(isDateInUserEvents(position))
                {
                    backgroundColor = 0xFF9D57A0;   //set cell background color to Magenta
                }

                view.setBackgroundColor(backgroundColor);

                return view;
            }
        };



        calGrid.setAdapter(aa);

    }

    //============================  Calendar Date Generator ========================================
    private ArrayList<String> calendarDateGenerator()
    {
        ArrayList<String> items = new ArrayList<String>();
        int maxCalendatItems = 0;
        int numOfItems = 0;

        //==========  Adding last month final dates  ==========
        int lastMonthLength = getLastMonthLength();
        int dayOfWeek = _calendar.get(Calendar.DAY_OF_WEEK);

        for(int i = lastMonthLength - dayOfWeek + 2; i <= lastMonthLength ; i++)
        {
            items.add("" + i);
            numOfItems++;
        }

        releventDaysGap = numOfItems - 1;

        //=============  Adding this month dates  =============

        for(int i = 1 ; i <= getMonthLength() ; i++)
        {
            items.add("" + i);
            numOfItems++;
        }

        //==========  Adding next month first dates  ==========
        if(numOfItems > 35)         maxCalendatItems = 42;  //6 rows - 7*6
        else if(numOfItems <= 35)   maxCalendatItems = 35;  //5 rows - 7*5

        for(int i = 1 ; i <= maxCalendatItems - numOfItems ; i++)
        {
            items.add("" + i);
            //numOfItems++;
        }

        return items;
    }

    //==============================  Get Month By Num  ============================================
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


    //================================  Month Length Check  ========================================
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


    //==========================  Date In User Event Check  ========================================
    private boolean isDateInUserEvents(int day)
    {
        if(eventList != null)               //check if the event list is not null
            for(Event ev : eventList)       //for each event in the list, check if it equals to 'day'
            {
                if(ev.isDateEquals(getYear(), getMonth() + 1, day - releventDaysGap))
                    return true;
            }

        return false;
    }


    //==============================================================================================
    //                          Calendar Setters and Getters
    //==============================================================================================
    private void setCalendar(int year, int mounth, int day) {   _calendar = new GregorianCalendar(year, mounth, day);   }

    private int getYear()   {   return _calendar.get(Calendar.YEAR);  }
    private int getMonth()  {   return _calendar.get(Calendar.MONTH);  }
    private int getDay()    {   return _calendar.get(Calendar.DAY_OF_MONTH);  }


    //=====================================  Button Functions ======================================

    //==========  Next ==========
    private void setNextMonth()
    {
        if(getMonth() == 11)
            _calendar = new GregorianCalendar(getYear() + 1, 0, 1);
        else
            _calendar = new GregorianCalendar(getYear(), getMonth() + 1, 1);

        refreshCalendarGrid();
    }

    //==========  prev  ==========
    private void setPrevMonth()
    {
        if(getMonth() == 1)
            _calendar = new GregorianCalendar(getYear() - 1, 11, 1);
        else
            _calendar = new GregorianCalendar(getYear(), getMonth() - 1, 1);

        refreshCalendarGrid();
    }



    //==============================  Toast Dialog Print  ==========================================

    private void printToastDialog(String message)
    {
       dialog.printDialog(message, getApplicationContext());
    }

}
