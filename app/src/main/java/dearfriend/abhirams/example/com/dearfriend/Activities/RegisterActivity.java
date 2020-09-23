package dearfriend.abhirams.example.com.dearfriend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dearfriend.abhirams.example.com.dearfriend.Model.UserDO;
import dearfriend.abhirams.example.com.dearfriend.R;
import dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector;
import dearfriend.abhirams.example.com.dearfriend.Util.CustomCrashHandler;
import dearfriend.abhirams.example.com.dearfriend.web.ApplicationController;
import dearfriend.abhirams.example.com.dearfriend.web.WebHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText Name,Password,rePwd,area,email;
    private Button Submit,Cancel;

    private ProgressDialog progress;
    // Connection detector class
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intializeIds();
        Thread.setDefaultUncaughtExceptionHandler(new CustomCrashHandler(this));

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean nullCheck = false;

                if (Name.getText().toString().trim().equals("") || Name.getText().length() == 0) {
                    Name.setError("UserName cannot be empty");
                    nullCheck = true;
                }else if ((Name.getText().toString()).length() >= 15) {
                    Name.setError("Username too long");
                    nullCheck = true;
                }

                if (Password.getText().toString().trim().equals("") || Password.getText().length() == 0) {
                    Password.setError("Password cannot be empty");
                    nullCheck = true;
                }

                if (rePwd.getText().toString().trim().equals("") || rePwd.getText().length() == 0) {
                    rePwd.setError("Password cannot be empty");
                    nullCheck = true;
                }

                if (email.getText().toString().trim().equals("") || email.getText().length() == 0)
                {
                    email.setError("Email cannot be empty");
                    nullCheck = true;
                }
                else  if (!(email.getText().toString()).matches(emailPattern)) {
                    email.setError("Invalid email address");
                    nullCheck = true;
                }


                if (Password.getText().toString().equals(rePwd.getText().toString())) {
                    if (nullCheck == false) {


                        UserDO  userDO = new UserDO();
                        userDO.setUserId(Name.getText().toString().trim());
                        userDO.setPassword(Password.getText().toString());
                        userDO.setArea(area.getText().toString());
                        userDO.setEmail(email.getText().toString());
                        String todayDate = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());
                        userDO.setAuditDateTime(todayDate);

                        Gson gson = new Gson();
                       /* Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();*/
                        String jsonUserDO = gson.toJson(userDO);
                        createUser(jsonUserDO);
                    }
                }
                    else{   MDToast.makeText(getApplicationContext(), "Password Not Match",MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show(); }

            }
        });



        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }



    public void intializeIds()
    {
        Name= findViewById(R.id.regUserId);Name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        Password= findViewById(R.id.regPwdId);
        rePwd= findViewById(R.id.rePwdId);
        email= findViewById(R.id.regEmailId);
        area= findViewById(R.id.regAreaId);
        Submit= findViewById(R.id.regsubmit);
        Cancel= findViewById(R.id.cancel);

    }


    public void createUser( String jsonUserDO)
    {
        cd = new dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent)
        {
            progress = ProgressDialog.show(this, "", getString(R.string.plsWait));
            StringRequest req = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL+"createUser/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.v("Response:%n %s", response);
                    progress.dismiss();
                    if((response.substring(1,response.length()-1).equalsIgnoreCase("Successful")) || response.equals("Successful") )
                    {
                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(i);


                        MDToast.makeText(RegisterActivity.this, "User Creation Successfully:"+Name.getText().toString(),MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                    }
                    else if((response.substring(1,response.length()-1).equalsIgnoreCase("Duplicate")) || response.equals("Duplicate"))
                    {
                        MDToast.makeText(RegisterActivity.this, getString(R.string.duplicate),MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                    }
                    else
                    {
                        MDToast.makeText(RegisterActivity.this, getString(R.string.userCreationFailed),MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: "+error);
                    progress.dismiss();
                    MDToast.makeText(RegisterActivity.this, getString(R.string.error),MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            }){
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("jsonUserDO",jsonUserDO);//put your parameters here
                    return parameters;
                }
            };

            // add the request object to the queue to be executed
            ApplicationController.getInstance().addToRequestQueue(req);
        }
        else
        {
            MDToast.makeText(this, getString(R.string.noInternetConn) + "\n" + getString(R.string.plsConnectInternet), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
        }
    }


}