package com.attender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.pm.PackageManager.GET_SIGNATURES;

public class loginPageActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;

    /* facebook login callback */
    CallbackManager callbackManager;
    AttenderBL bl;

    /* saved data */
    AppData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_page);
        bl = new AttenderBL();

        /* ================== getting saved in file user data ================== */
        getSavedData();

        //=============================================== Text Style =======================
        TextView Main_TV = (TextView) findViewById(R.id.attender_main_txt);
        TextView Sub_TV = (TextView) findViewById(R.id.attender_sub_txt);
        TextView Login_TV = (TextView) findViewById(R.id.login_txt);
        TextView Or_TV = (TextView) findViewById(R.id.or_txt);
        Typeface tf = Typeface.createFromAsset(getAssets(),"ostrich-regular.ttf");
        Main_TV.setTypeface(tf);
        Sub_TV.setTypeface(tf);
        Login_TV.setTypeface(tf);
        Or_TV.setTypeface(tf);

        //================================== Gmail Login ===================================
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope("profile"))
                .build();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
//        manager = AccountManager.get(this);
//        accounts = manager.getAccountsByType("com.google");
        //================================== internet connection ===============================

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //======================================= Facebook =====================================

        callbackManager = CallbackManager.Factory.create();

        // =================================== facebook login ==================================
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                // App code
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                appData.resetData("facebook", AccessToken.getCurrentAccessToken().getToken());
            }

            @Override
            public void onCancel()
            {
                // App code
            }

            @Override
            public void onError(FacebookException exception)
            {
                // App code
            }
        });

        //  ======== Print out the key hash "KeyHash" at log ========
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.attender",
                    GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
        //  ======== login check ========
        onResume();
    }

    private void getSavedData() {
        String loginType = getData("loginType");
        appData = (AppData) getApplicationContext();
        if(loginType != null)
            if(loginType.compareTo("server") == 0)
                appData.resetData("server", getData("token"));
            else
                appData.resetData(loginType, "");
        else
            appData.resetData("guest", null);
    }

    private String getData(String fileName) {
        FileInputStream inputStream;
        String retData = null;
        try {
            inputStream = openFileInput(fileName);
            while(inputStream.available() > 0){
                retData += (char) inputStream.read();
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(retData != null)
            return retData.substring(4, retData.length());
        else
            return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {
       mGoogleApiClient.connect();
    }
    //===========================================user register==========================================
    public void registerPressed(View v)
    {
        Intent intent=new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }
    //=======================================user login=================================================
    public void userLoginPressed(View v)
    {
        LinearLayout emailLayout= (LinearLayout)findViewById(R.id.email_layout);
        emailLayout.setVisibility(LinearLayout.VISIBLE);
        LinearLayout passwordLayout= (LinearLayout)findViewById(R.id.password_layout);
        passwordLayout.setVisibility(LinearLayout.VISIBLE);
        LinearLayout loginLayout= (LinearLayout)findViewById(R.id.login_layout);
        loginLayout.setVisibility(LinearLayout.VISIBLE);

    }
    public void loginPressed(View v)
    {
        EditText email=(EditText)findViewById(R.id.email_txt);
        EditText password=(EditText)findViewById(R.id.password_txt);
        String userToken;
        String response;
        String status;
        String userDetails;

        if(email.getText().toString().compareTo("")==0 || password.getText().toString().compareTo("")==0) {
            printDialog("please enter all fields");
            this.onStart();
        }
        else{
            response=bl.userLogin(email.getText().toString(), password.getText().toString().hashCode());
            status=response.substring(0,3);
            if(status.compareTo("200")==0)
            {
                userToken=response.substring(3,response.length());
                appData.resetData("server",userToken);
                printDialog(userToken);
                userDetails=bl.getUserDetails(userToken);
                Intent intent=new Intent(this,MainPageActivity.class);
                intent.putExtra("name",userDetails);
                startActivity(intent);
            }
            else{
                switch(status){
                    case "403":
                        printDialog("invalid mail");
                        this.onStart();
                        break;
                    case "500":
                        printDialog("wrong password");
                        this.onStart();
                        break;
                    case "501":
                        printDialog("user does not exist");
                        this.onStart();
                        break;

                }
            }





        }

    }
    //======================================================================================================
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //TODO: add google token to replace null
        if(mGoogleApiClient.isConnected()) {
            appData.resetData("google", bl.googleLogin());
            Intent intent = new Intent(this, MainPageActivity.class);
            intent.putExtra("name", "your google nickname");
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.reconnect();
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

        // check login
        if(appData.get_loginType().compareTo("guest") != 0 && appData.get_loginType().compareTo("") != 0)
        {
            String userDetails = "";
            // send id and token to own server
            switch(appData.get_loginType())
            {
                case "facebook":
                    bl.loginToServer(AccessToken.getCurrentAccessToken().getUserId(),
                            AccessToken.getCurrentAccessToken().getToken());
                    break;
                //TODO: send to own server google user id and google token
                case "google":
                    break;
                case "server":
                   userDetails=bl.getUserDetails(appData.get_userToken());

                    break;
                default:
                    break;
            }
            Intent intent = new Intent(this, MainPageActivity.class);
            intent.putExtra("user_type", appData.get_loginType());
            intent.putExtra("name", userDetails);
            startActivity(intent);
        }
    }

    public void confirmPressed(View v)
    {
        appData.resetData("guest", null);
        Intent intent=new Intent(this,MainPageActivity.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login_page, menu);
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
    private void printDialog(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
