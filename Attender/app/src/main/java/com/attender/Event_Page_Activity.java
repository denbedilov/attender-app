package com.attender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.facebook.AccessToken;

import java.util.ArrayList;


public class Event_Page_Activity extends Activity {
    private Switch attendSwitch;
    private Event currEvent;
    private AttenderBL bl;
    private  AppData appData;
    private DialogAdapter dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ArrayList<Event> userEvents;
        ArrayList<Attendee> attendees;
        Button attendeesBTN=(Button)findViewById(R.id.attendees_cmd);
        Button chatBTN=(Button)findViewById(R.id.chat_cmd);
        appData = (AppData) getApplicationContext();
        super.onCreate(savedInstanceState);
        TextView tv;
        boolean checkedFlag=false;
        dialog = new DialogAdapter();
        bl = new AttenderBL();
        setContentView(R.layout.event_page);
        Intent myIntent=getIntent();
        currEvent=  (Event)myIntent.getSerializableExtra("CurrentEvent");
        if(appData.get_loginType().compareTo("facebook")==0) {
            attendees=bl.getAttendees(currEvent.getId(),appData.get_userToken(), AccessToken.getCurrentAccessToken().getToken());
        }
        else {
            attendees=bl.getAttendees(currEvent.getId(),appData.get_userToken(),null);
        }

        //==========  ATTEND   =====================

        attendSwitch = (Switch) findViewById(R.id.attend_switch);
        userEvents = appData.get_userEventList();

        if(userEvents!=null)
        {
            for(Event ev: userEvents)
                if(ev.equalscheck(currEvent)) {
                    attendSwitch.setChecked(true);
                    checkedFlag = true;
                }
            if(!checkedFlag) {
                attendSwitch.setChecked(false);
            }
        }
        if(appData.get_loginType().compareTo("guest")==0)
        {
            chatBTN.setAlpha(.5f);
            attendeesBTN.setAlpha(.5f);
            attendSwitch.setAlpha(.5f);

        }
        attendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(appData.get_loginType().compareTo("guest")==0)
                    printToastDialog("For Logged In User Only");
                else {
                    bl.Attend(appData.get_userToken(), currEvent.getId(), isChecked);
                    appData.set_userEventList(bl.getUserEvents(appData.get_userToken()));
                    if (isChecked)
                        printToastDialog("Attending");
                    else
                        printToastDialog("Unattending");
                }
            }
        });


        //==========  DATE   ==================
        tv =(TextView)findViewById(R.id.date_lbl);
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
        tv.setText(currEvent.getDescription());

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
        if(attendees!=null)
            tv.setText(String.valueOf(attendees.size()));
        else
            tv.setText("no attendees");

    }

    public void chatPressed(View v)
    {
        if(appData.get_loginType().compareTo("guest")==0)
            printToastDialog("For logged-in User Only");
        else{
        if (attendSwitch.isChecked()) {
            Intent myIntent = new Intent(getApplicationContext(), ChatActivity.class);
            myIntent.putExtra("EventID", currEvent.getId());
            startActivity(myIntent);
        } else
            printToastDialog("You need to attend to open chat");
    }
    }
    public void attendeesPressed(View v)
    {
            if(appData.get_loginType().compareTo("guest")==0)
                printToastDialog("For Logged In User Only");
        else {
                Intent intent = new Intent(this, AttendeesPage.class);
                intent.putExtra("eventId", currEvent.getId());
                startActivity(intent);
            }
    }

    private void printToastDialog(String message)
    {
        dialog.printDialog(message, getApplicationContext());
    }
}
