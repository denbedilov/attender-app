package com.attender;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegistrationActivity extends Activity {
AttenderBL bl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        bl=new AttenderBL();


    }

    public void confirmPressed(View v)
    {
        String userToken="";
        EditText firstName=(EditText)findViewById(R.id.name_text);
        EditText lastName= (EditText)findViewById(R.id.LastName_text);
        EditText email=(EditText)findViewById(R.id.email_txt);
        EditText password= (EditText)findViewById(R.id.password_txt);
        EditText confPass=(EditText)findViewById(R.id.confPass_txt);
        Button confirm_btn=(Button)findViewById(R.id.confirm_cmd);
        if(firstName.toString().equals("") || lastName.toString().equals("") || password.toString().equals("") ||email.toString().equals("")||confPass.toString().equals("") )
            printToastDialog("Please fill all Fields");

        if(!password.toString().equals(confPass.toString()))
            printToastDialog("passwords are not equal!");
        else
            userToken=bl.userRegistration(firstName.toString(),lastName.toString(),email.toString(),password.toString().hashCode());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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
    private void printToastDialog(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
