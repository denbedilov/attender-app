package com.attender;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.util.ArrayList;


public class Event_Page_Activity extends Activity {
    Event currEvent;
    AttenderBL bl;
    ArrayList<Event> userEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final AppData appData = (AppData) getApplicationContext();
        super.onCreate(savedInstanceState);
        TextView tv;
        boolean checkedFlag=false;
        bl = new AttenderBL();
        userEvents=new ArrayList<Event>();
        setContentView(R.layout.activity_event__page_);
        Intent myIntent=getIntent();
        currEvent=  (Event)myIntent.getSerializableExtra("CurrentEvent");
        //appData.resetData(AccessToken.getCurrentAccessToken().getToken());
          //==========  ATTEND   =====================
        CheckBox attend = (CheckBox) findViewById(R.id.attend_check);
        userEvents=appData.get_userEventList();
        if(userEvents!=null)
        {
            for(Event ev: userEvents)
                if(ev.equals(currEvent)) {
                    attend.setChecked(true);
                    checkedFlag = true;
                }
           if(!checkedFlag)
                attend.setChecked(false);
        }
        if(AccessToken.getCurrentAccessToken() == null) {
            Button attendeesBTN=(Button)findViewById(R.id.attendees_cmd);
            Button chatBTN=(Button)findViewById(R.id.chat_cmd);
            chatBTN.setAlpha(.5f);
            chatBTN.setEnabled(false);
            attendeesBTN.setAlpha(.5f);
            attendeesBTN.setEnabled(false);

            attend.setClickable(false);
            attend.setAlpha(.5f);

        }
        attend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                    printToastDialog(bl.Attend(AccessToken.getCurrentAccessToken().getToken(), currEvent.getId(), isChecked));

            }
        });


        //==========  DATE   ==================
        tv =(TextView)findViewById(R.id.date_lbl);  //TODO - ADD DATE
        tv.setText(currEvent.getDate());

        //==========  NAME   ==================
        tv =(TextView)findViewById(R.id.evName_lbl);
        tv.setText(currEvent.getName());

        //==========  TIME   ==================
        tv =(TextView)findViewById(R.id.time_lbl);
        tv.setText(currEvent.getTime());

        //==========  CITY   ==================
        tv =(TextView)findViewById(R.id.cityName_lbl);
        tv.setText(currEvent.getCity());

        //==========  ADDRESS   ==================
        tv =(TextView)findViewById(R.id.address_lbl);
        tv.setText(currEvent.getAddress());

        //==========  DESCRIPTION   ==================
        tv =(TextView)findViewById(R.id.description_lbl);    ///TO ADD
       // tv.setText(currEvent.getDescription());
        tv.setText(R.string.description);
        tv.setClickable(true);



        //==========  EVENT URL   ==================
        tv =(TextView)findViewById(R.id.url_lbl);
        tv.setClickable(true);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='"+currEvent.getEventUrl()+"'> Event Info </a>";
        tv.setText(Html.fromHtml(text));

        //==========  HOST   ==================
        tv =(TextView)findViewById(R.id.host_lbl);
        tv.setText(currEvent.getHost());

        //==========  PRICE   ==================
        tv =(TextView)findViewById(R.id.price_lbl);  //chang to price
        tv.setText(currEvent.getPrice());
        if(!(currEvent.getPrice().contains("free"))) {
            tv.setClickable(true);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            text = "<a href='"+currEvent.getPrice()+"'> Price Info </a>";
            tv.setText(Html.fromHtml(text));

        }
        //==========  ATTENDEES   ==================
        tv =(TextView)findViewById(R.id.attending_lbl);
        tv.setText(currEvent.getAttendees());

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_event__page_, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    public void onInfoClick(View v)
    {
        TextView tv =(TextView)findViewById(R.id.description_lbl);    ///TO ADD
         tv.setText(currEvent.getDescription());
        tv.setClickable(false);
    }
    public void attendeesPressed(View v)
    {

            Intent intent = new Intent(this, AttendeesPage.class);
            String id = currEvent.getId();
            intent.putExtra("eventId", currEvent.getId());
            startActivity(intent);

    }

    private void printToastDialog(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
