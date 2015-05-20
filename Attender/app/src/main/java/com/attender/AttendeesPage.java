package com.attender;

import android.app.Activity;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;

import java.util.ArrayList;


public class AttendeesPage extends Activity {

    AttenderBL bl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_page);

        ListView listView = (ListView) findViewById(R.id.listView);
        bl = new AttenderBL();
        ArrayList<Attendee> attendees;
        attendees = bl.getAttendees(getIntent().getStringExtra("eventId"), AccessToken.getCurrentAccessToken().getToken());
        if(attendees != null)
        {
            AttendeesAdapter attendeesAdapter = new AttendeesAdapter(this, attendees);
            listView.setAdapter(attendeesAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attendees_page, menu);
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
}
