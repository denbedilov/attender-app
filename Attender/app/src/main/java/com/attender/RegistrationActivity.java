package com.attender;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegistrationActivity extends Activity {
AttenderBL bl;
    AppData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        bl=new AttenderBL();
        appData = (AppData) getApplicationContext();
        TextView register_lbl=(TextView)findViewById(R.id.register_lbl);
        Typeface tf = Typeface.createFromAsset(getAssets(),"LeagueGothic-CondensedRegular.otf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(),"Oswald-Regular.ttf");
//        register_lbl.setTypeface(tf);
        TextView name_lbl=(TextView)findViewById(R.id.name_lbl);
        TextView lastName_lbl=(TextView)findViewById(R.id.lName_lbl);
        TextView email_lbl=(TextView)findViewById(R.id.email_lbl);
        TextView password_lbl=(TextView)findViewById(R.id.password_lbl);
        TextView confPass_lbl=(TextView)findViewById(R.id.confPass_lbl);
        name_lbl.setTypeface(tf2);
        lastName_lbl.setTypeface(tf2);
        email_lbl.setTypeface(tf2);
        password_lbl.setTypeface(tf2);
        confPass_lbl.setTypeface(tf2);
    }

    public void confirmPressed(View v)
    {
        String userToken;
        String response;
        String status;
        EditText firstName=(EditText)findViewById(R.id.name_text);
        EditText lastName= (EditText)findViewById(R.id.LastName_text);
        EditText email=(EditText)findViewById(R.id.email_txt);
        EditText password= (EditText)findViewById(R.id.password_txt);
        EditText confPass=(EditText)findViewById(R.id.confPass_txt);

        if(firstName.getText().toString().compareTo("") == 0 ||
                lastName.getText().toString().compareTo("") ==0 ||
                password.getText().toString().compareTo("") ==0 ||
                email.getText().toString().compareTo("") == 0 ||
                confPass.getText().toString().compareTo("") == 0 ) {
            printToastDialog("All Fields Must Be Filled");
            this.onStart();
        }
        else if(password.getText().toString().compareTo(confPass.getText().toString()) != 0)
            printToastDialog("passwords not match");
        else {
            response = bl.userRegistration(firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString().hashCode());
            status=response.substring(0, 3);
            if(status.compareTo("200")==0)
            {
                userToken = response.substring(3,response.length());
                appData.resetData("server", userToken);
                Intent intent = new Intent(this, MainPageActivity.class);
                startActivity(intent);
            }
            else {
                switch (status){
                    case "403":
                        printToastDialog("invalid mail");
                        break;
                    case "502":
                        printToastDialog("user already exists");
                        break;
                    default:
                        printToastDialog("connection error");
                }

            }
        }
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
