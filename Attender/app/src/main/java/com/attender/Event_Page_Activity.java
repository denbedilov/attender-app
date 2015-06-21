package com.attender;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.util.ArrayList;


public class Event_Page_Activity extends Activity {
    Event currEvent;
    private AttenderBL bl;
    private ArrayList<Event> userEvents;
    private ArrayList<Attendee> attendees;
    private  AppData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        appData = (AppData) getApplicationContext();
        super.onCreate(savedInstanceState);
        TextView tv;
        boolean checkedFlag=false;
        bl = new AttenderBL();
        userEvents=new ArrayList<Event>();
        setContentView(R.layout.event_page);
        Intent myIntent=getIntent();
        currEvent=  (Event)myIntent.getSerializableExtra("CurrentEvent");
        if(appData.get_loginType().compareTo("facebook")==0)
            attendees=bl.getAttendees(currEvent.getId(),appData.get_userToken(),AccessToken.getCurrentAccessToken().getToken());
        else
            attendees=bl.getAttendees(currEvent.getId(),appData.get_userToken(),null);

        //appData.resetData(AccessToken.getCurrentAccessToken().getToken());



          //==========  ATTEND   =====================

        Switch attendSwitch = (Switch) findViewById(R.id.attend_switch);
        userEvents = appData.get_userEventList();

        if(userEvents!=null)
        {
            for(Event ev: userEvents)
                if(ev.equalscheck(currEvent)) {
                    attendSwitch.setChecked(true);
                    checkedFlag = true;
                }
            if(!checkedFlag)
                attendSwitch.setChecked(false);
        }
        if(appData.get_loginType().compareTo("guest")==0)
        {
            Button attendeesBTN=(Button)findViewById(R.id.attendees_cmd);
            Button chatBTN=(Button)findViewById(R.id.chat_cmd);
            chatBTN.setAlpha(.5f);
            chatBTN.setEnabled(false);
            attendeesBTN.setAlpha(.5f);
            attendSwitch.setAlpha(.5f);
            attendSwitch.setEnabled(false);

        }
        attendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                 bl.Attend(appData.get_userToken(), currEvent.getId(), isChecked);
                appData.set_userEventList(bl.getUserEvents(appData.get_userToken()));
                if(isChecked)
                    printToastDialog("Attending");
                else
                    printToastDialog("Unattending");


            }
        });




   /*
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
        if(appData.get_loginType().compareTo("guest")==0) {
            Button attendeesBTN=(Button)findViewById(R.id.attendees_cmd);
            Button chatBTN=(Button)findViewById(R.id.chat_cmd);
            chatBTN.setAlpha(.5f);
            chatBTN.setEnabled(false);
            attendeesBTN.setAlpha(.5f);
            attend.setAlpha(.5f);
            attend.setEnabled(false);

        }
        attend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                printToastDialog(bl.Attend(appData.get_userToken(), currEvent.getId(), isChecked));
                appData.set_userEventList(bl.getUserEvents(appData.get_userToken()));

            }
        });
*/

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
        tv.setText(currEvent.getDescription());
        //tv.setText(R.string.description);
        //tv.setClickable(true);

        //tv.setMovementMethod(new ScrollingMovementMethod());



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
    public void chatPressed(View v)
    {
        if(appData.get_loginType().compareTo("guest")==0)
            printToastDialog("Please log in");
        else{
        Switch attendSwitch = (Switch) findViewById(R.id.attend_switch);
        if (attendSwitch.isChecked() == true) {
            Intent myIntent = new Intent(getApplicationContext(), ChatActivity.class);

            myIntent.putExtra("EventID", currEvent.getId());
            startActivity(myIntent);
        } else
            printToastDialog("You need to attend to open chat");
    }
    }

    public void onInfoClick(View v)
    {
        TextView tv =(TextView)findViewById(R.id.description_lbl);    ///TO ADD
        TextView desc=(TextView)findViewById(R.id.desc_txt);
        tv.setText(currEvent.getDescription());
        tv.setClickable(false);
        //desc.setText(R.string.description_close);
        //desc.setClickable(true);

    }
    public void descCloseClick(View v)
    {
        TextView tv =(TextView)findViewById(R.id.description_lbl);    ///TO ADD
        TextView desc=(TextView)findViewById(R.id.desc_txt);
        tv.setText(R.string.description);
        tv.setClickable(true);
        desc.setText("Description");
        desc.setClickable(false);

    }
    public void attendeesPressed(View v)
    {
            if(appData.get_loginType().compareTo("guest")==0)
                printToastDialog("For Logged In User Only");
        else {
                Intent intent = new Intent(this, AttendeesPage.class);
                String id = currEvent.getId();
                intent.putExtra("eventId", currEvent.getId());
                startActivity(intent);
            }

    }

    private void printToastDialog(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
