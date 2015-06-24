package com.attender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.pm.PackageManager.GET_SIGNATURES;

public class loginPageActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        ResultCallback<People.LoadPeopleResult> {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private static ProgressDialog progress;
    private boolean cancelGoogle = true;
    /* facebook login callback */
    CallbackManager callbackManager;
    AttenderBL bl;

    /* saved data */
    AppData appData;

    //Class Vars
    TextView Main_TV, Sub_TV, Login_TV, Or_TV;
    Typeface tf, tf1;
    LoginButton loginButton;
    DialogAdapter dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_page);
        bl = new AttenderBL();
        dialog = new DialogAdapter();
        /* ================== getting saved in file user data ================== */
        appData = (AppData) getApplicationContext();
        appData.getSavedData();

        //=============================================== Text Style =======================
        Main_TV = (TextView) findViewById(R.id.attender_main_txt);
        Sub_TV = (TextView) findViewById(R.id.attender_sub_txt);
        Login_TV = (TextView) findViewById(R.id.login_txt);
        Or_TV = (TextView) findViewById(R.id.or_txt);

        tf = Typeface.createFromAsset(getAssets(), "Caviar_Dreams_Bold.ttf");
        tf1 = Typeface.createFromAsset(getAssets(), "cooprblk.ttf");

        Main_TV.setTypeface(tf1);
        Sub_TV.setTypeface(tf);
        //================================== Gmail Login ===================================
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope("profile"))
                .build();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        SignInButton bu = (SignInButton) findViewById(R.id.sign_in_button);
        bu.setColorScheme(SignInButton.COLOR_LIGHT);

        //================================== internet connection ===============================

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //======================================= Facebook =====================================

        callbackManager = CallbackManager.Factory.create();

        // =================================== facebook login ==================================
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends", "email");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                String token = bl.loginToServer(AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getToken());
                appData.resetData("facebook", token.substring(3), Profile.getCurrentProfile().getName());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

//        printKeyHash();
    }


    private void printKeyHash() {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    public void registerPressed(View v) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    boolean pressed = false;

    //=======================================user login=================================================
    public void userLoginPressed(View v) {
        LinearLayout emailLayout = (LinearLayout) findViewById(R.id.email_layout);
        LinearLayout passwordLayout = (LinearLayout) findViewById(R.id.password_layout);
        LinearLayout loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        if (!pressed) {
            emailLayout.setVisibility(LinearLayout.VISIBLE);

            passwordLayout.setVisibility(LinearLayout.VISIBLE);

            loginLayout.setVisibility(LinearLayout.VISIBLE);

            pressed = true;
        } else {
            emailLayout.setVisibility(LinearLayout.INVISIBLE);

            passwordLayout.setVisibility(LinearLayout.INVISIBLE);

            loginLayout.setVisibility(LinearLayout.INVISIBLE);
            pressed = false;
        }


    }

    public void loginPressed(View v) {
        EditText email = (EditText) findViewById(R.id.email_txt);
        EditText password = (EditText) findViewById(R.id.password_txt);
        String userToken;
        String response;
        String status;

        if (email.getText().toString().compareTo("") == 0 || password.getText().toString().compareTo("") == 0) {
            printDialog("please enter all fields");
            this.onStart();
        } else {
            response = bl.userLogin(email.getText().toString(), password.getText().toString().hashCode());
            status = response.substring(0, 3);
            if (status.compareTo("200") == 0) {
                userToken = response.substring(3, response.length());
                appData.resetData("server", userToken, bl.getUserDetails(userToken));
                Intent intent = new Intent(this, MainPageActivity.class);
                startActivity(intent);
            } else {
                switch (status) {
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
                    default:
                        printDialog("Check Your Internet Connection");
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
                printDialog("Check Your Internet Connection");
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
        final Intent intent = new Intent(this, MainPageActivity.class);

        if (mGoogleApiClient.isConnected()) {

            progress = new ProgressDialog(this);
            progress.setMessage("Logging in, please wait...");
            progress.setCancelable(false);
            progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting())
                        cancelGoogle = false;
                }
            });
            progress.show();


            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                new Thread(new Runnable() {

                    int status;
                    String tok;

                    @Override
                    public void run() {
                        String firstname = capitalize(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getName().getGivenName());
                        String lastname = capitalize(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getName().getFamilyName());
                        tok = bl.googleLogin(
                                firstname,
                                lastname,
                                Plus.AccountApi.getAccountName(mGoogleApiClient)
                        );
                        status = getStatus(tok);
                        if (status == 200) {
                            appData.resetData("google", tok.substring(3), firstname + " " + lastname);
                            intent.putExtra("name", capitalize(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getName().getGivenName()) + " " +
                                    capitalize(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getName().getFamilyName()));
                        } else {
                            appData.resetData("guest", null, null);
                            printDialog("google login failed");
                        }
                        if (cancelGoogle)
                            startActivity(intent);
                        else
                            mGoogleApiClient.disconnect();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                }).start();
            }
        }
    }

    protected static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private int getStatus(String tok) {
        return Integer.parseInt(tok.substring(0, 3));
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
    protected void onResume() {
        super.onResume();
        appData.getSavedData();
        if(!appData.get_loginType().equals("guest"))
        {
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
            } finally {
                personBuffer.close();
            }
        }
    }

    public void confirmPressed(View v) {
        appData.resetData("guest", null, null);
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
    }


    private void printDialog(String message) {
      dialog.printDialog(message, getApplicationContext());
    }
}
