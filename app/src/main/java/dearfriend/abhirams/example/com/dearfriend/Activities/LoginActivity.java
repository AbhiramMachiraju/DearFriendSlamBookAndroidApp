package dearfriend.abhirams.example.com.dearfriend.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dearfriend.abhirams.example.com.dearfriend.R;
import dearfriend.abhirams.example.com.dearfriend.Util.AlertMessage;
import dearfriend.abhirams.example.com.dearfriend.Util.CheckPermission;
import dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector;
import dearfriend.abhirams.example.com.dearfriend.Util.CustomCrashHandler;
import dearfriend.abhirams.example.com.dearfriend.Util.StaticContents;
import dearfriend.abhirams.example.com.dearfriend.Util.VolleyErrorHandle;
import dearfriend.abhirams.example.com.dearfriend.web.ApplicationController;
import dearfriend.abhirams.example.com.dearfriend.web.WebHelper;
import info.hoang8f.widget.FButton;

public class LoginActivity extends AppCompatActivity {

    //fields-----
    TextInputEditText username, password;
    Button register, SignIn,forgotPwd;
    CheckBox remMe;
    TextView remMeTxt, serverSts;
    Boolean isNullCheck = false,serverSts_bol=false;
    String UserName, Userpwd;
    ImageView msgBox;


    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean storeLogin;


    //Internet & append stuff-----
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    ProgressDialog progress;
    StringBuffer stringBuffer;

    public static Context contextOfApplication;
    Boolean appCharshed = false;
    String logCatIntent;
    String[] intentData;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intializeIds();

        //Allow RunTime Permissions
        CheckPermission.checkAndRequestPermissions(LoginActivity.this);

        //App Crash..
        contextOfApplication = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new CustomCrashHandler(this));
        if (null != getIntent().getExtras()) {
            appCharshed = getIntent().getExtras().getBoolean("APP_CRASHED");

            logCatIntent = getIntent().getStringExtra("INTENT_LOGCAT");
            if (null != logCatIntent) {
                intentData = logCatIntent.split(java.util.regex.Pattern.quote("E$~XZ"));
            }
            if (appCharshed) {
                CustomCrashHandler.saveLogToServer(intentData[0], intentData[1], intentData[2], getApplicationContext());
                MDToast.makeText(this, "App Crashed, due to unknown error", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

            }
        }

        //Animation
        Animation animatee = AnimationUtils.loadAnimation(this, R.anim.animation);
        msgBox.startAnimation(animatee);

        msgBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgBox.clearAnimation();
                showDialog(builder);
            }
        });


       checkServerStatus();
        blinkTextView();


        //LoginPrefs--------
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        storeLogin = loginPreferences.getBoolean("storeLogin", false);
        if (storeLogin == true) {
            username.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            remMe.setChecked(true);
        }


        remMeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remMe.isChecked())
                    remMe.setChecked(false);
                else
                    remMe.setChecked(true);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emptyCheck();
                UserName = username.getText().toString();
                Userpwd = password.getText().toString();


                if (remMe.isChecked()) {
                    loginPrefsEditor.putBoolean("storeLogin", true);
                    loginPrefsEditor.putString("username", UserName);
                    loginPrefsEditor.putString("password", Userpwd);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                stringBuffer = new StringBuffer();
                if (isNullCheck == false) {
                    stringBuffer.append(username.getText().toString());
                    stringBuffer.append("$" + password.getText().toString());
                    validateCredentials();
                }
            }
        });

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,ForgotPwdActivity.class);
                startActivity(i);
            }
        });


    }


    public void intializeIds() {
        username = findViewById(R.id.userId);
        username.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        password = findViewById(R.id.pwdId);
        register = findViewById(R.id.createId);
        SignIn = findViewById(R.id.signId);
        remMe = findViewById(R.id.rememberMeId);
        remMeTxt = findViewById(R.id.textView26);
        msgBox = findViewById(R.id.adminMsg);
        serverSts = findViewById(R.id.serverStsId);
        forgotPwd = findViewById(R.id.forgotPwdId);
    }

    public void emptyCheck() {
        if ((username.getText().length() == 0 || username.getText().toString().trim().equals("")) && (password.getText().length() == 0 || password.getText().toString().trim().equals(""))) {
            username.setError("username Cannot be empty");
            password.setError("password Cannot be empty");
            isNullCheck = true;
        } else if (username.getText().length() == 0 || username.getText().toString().trim().equals("")) {
            username.setError("username Cannot be empty");
            isNullCheck = true;
        } else if (password.getText().length() == 0 || password.getText().toString().trim().equals("")) {
            password.setError("password Cannot be empty");
            isNullCheck = true;
        } else {
            isNullCheck = false;
        }

    }


    public void validateCredentials() {
        cd = new dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            progress = ProgressDialog.show(this, "", getString(R.string.plsWait));
            String loginDetails = stringBuffer.toString();
            StringRequest req = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL + "validateCredentials/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.v("Response:%n %s", response);
                    progress.dismiss();

                    try {
                        if (null != response && !response.equals("UnSuccessful") && !response.substring(1, response.length() - 1).equalsIgnoreCase("UnSuccessful")) {
                            if ((!response.substring(1, response.length() - 1).equalsIgnoreCase("UnSuccessful")) || response.contains("Successful")) {
                                JSONObject jsonObj = new JSONObject(response);
                                jsonObj.length();
                                String key = jsonObj.keys().next();
                                String value = jsonObj.get(key).toString();
                                String val[] = value.split(java.util.regex.Pattern.quote("$"));

                                if (key.equals("Successful")) {
                                    //SessionManagement................
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("userAutoIdForDashBoard", val[0]);
                                    editor.putString("loggedInUserName", val[1]);
                                    StaticContents.UserName = val[1];
                                    StaticContents.user_AutoId = val[0];
                                    editor.apply();

                                    Intent i = new Intent(LoginActivity.this, DashBoardActivity.class);
                                    startActivity(i);
                                    MDToast.makeText(getApplicationContext(), "Welcome" + " " + username.getText().toString(), MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                } else
                                    MDToast.makeText(LoginActivity.this, "Invalid", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                            } else
                                MDToast.makeText(LoginActivity.this, "Invalid", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                        } else
                            MDToast.makeText(LoginActivity.this, "Invalid", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        MDToast.makeText(LoginActivity.this, getString(R.string.error), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("Error: " + volleyError);
                    progress.dismiss();
                    MDToast.makeText(LoginActivity.this, VolleyErrorHandle.VolleyErrorHandel(volleyError), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                }
            }) {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("loginDetails", loginDetails);//put your parameters here
                    return parameters;
                }
            };

            ApplicationController.getInstance().addToRequestQueue(req);

        } else {
            MDToast.makeText(LoginActivity.this, getString(R.string.noInternetConn) + "\n" + getString(R.string.plsConnectInternet), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
            thread.start();

        }
    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                Thread.sleep(Toast.LENGTH_SHORT); // As I am using LENGTH_SHORT in Toast
                LoginActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }


    public void showDialog(AlertDialog.Builder builder) {

        builder = new AlertDialog.Builder(this);
        builder.setMessage("1.Server timings: 10am to 10pm.\n 2.Due to limited resource we are maintaining certain timesheet.\nWe promise you in future to get complete access with 24/7 usage.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Sorry for inconvenience");
        alert.show();
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }


    public void checkServerStatus() {
        cd = new dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            // progress = ProgressDialog.show(this, "", getString(R.string.plsWait));
            StringRequest req = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL + "serverSts/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.v("Response:%n %s", response);
                    //  progress.dismiss();
                    if (null!=response) {

                        serverSts_bol=true;
                        serverSts.setText(R.string.online);
                        serverSts.setTextColor(Color.parseColor("#16a085"));


                    } else {
                        serverSts_bol=false;
                        serverSts.setText(R.string.offline);
                       // serverSts.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.red));
                        serverSts.setTextColor(Color.parseColor("#e74c3c"));

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("Error: " + volleyError);
                    // progress.dismiss();
                    MDToast.makeText(LoginActivity.this, VolleyErrorHandle.VolleyErrorHandel(volleyError), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                }
            }) {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("action", "create");//put your parameters here
                    return parameters;
                }
            };

            ApplicationController.getInstance().addToRequestQueue(req);

        } else {
            MDToast.makeText(LoginActivity.this, getString(R.string.noInternetConn) + "\n" + getString(R.string.plsConnectInternet), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
            thread.start();

        }
    }


    private void blinkTextView() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(serverSts.getVisibility() == View.VISIBLE){
                            serverSts.setVisibility(View.INVISIBLE);
                        } else{serverSts.setVisibility(View.VISIBLE); }
                        blinkTextView();
                    }
                });
            }
        }).start();
    }


}





