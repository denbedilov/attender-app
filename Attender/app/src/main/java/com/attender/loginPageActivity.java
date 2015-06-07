package com.attender;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.IntentSender.SendIntentException;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.auth.GoogleAuthUtil;

public class loginPageActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;

    AccountManager manager;
    android.accounts.Account[] accounts;

    /* facebook login callback */
    CallbackManager callbackManager;
    AttenderBL bl;
    AppData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_page);
        bl = new AttenderBL();
        appData = (AppData) getApplicationContext();


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
        manager = AccountManager.get(this);
        accounts = manager.getAccountsByType("com.google");
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

        //  ======== check facebook login ========
        if(AccessToken.getCurrentAccessToken() != null)
        {
            // send id and token to own server
            String serverResponse = bl.loginToServer(AccessToken.USER_ID_KEY, AccessToken.getCurrentAccessToken().toString());
            Intent intent = new Intent(this, MainPageActivity.class);
            intent.putExtra("serverResponse", serverResponse);
            startActivity(intent);
        }

        //  ======== Print out the key hash "KeyHash" at log ========
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.attender",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
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
    public void loginPressed(View v)
    {
        Intent intent=new Intent(this,UserLoginActivity.class);
        startActivity(intent);
    }
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
        appData.resetData("google", null);
        String name = "";
        if(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null)
        {
            name = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getDisplayName();
        }
        else
        {
            name = "google name failed";
        }
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
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

        // check facebook login
        if(AccessToken.getCurrentAccessToken() != null)
        {
            // send id and token to own server
            String serverResponse = bl.loginToServer(AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getToken());
            Intent intent = new Intent(this, MainPageActivity.class);
            intent.putExtra("serverResponse", serverResponse);
            startActivity(intent);
        }
    }

    public void confirmPressed(View v)
    {
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

}
