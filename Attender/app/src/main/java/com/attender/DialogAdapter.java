package com.attender;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class DialogAdapter extends ActionBarActivity {

    boolean timerFinished = true;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_adapter);
    }
    public void printDialog(String message, Context c) {
        if (timerFinished) {

            toast = Toast.makeText(c, message, Toast.LENGTH_SHORT);
            toast.show();
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timerFinished = false;
                }

                public void onFinish() {
                    timerFinished = true;
                }
            }.start();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dialog_adapter, menu);
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
