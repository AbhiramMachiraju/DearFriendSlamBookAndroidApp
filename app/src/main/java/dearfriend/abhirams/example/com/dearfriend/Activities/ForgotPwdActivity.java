package dearfriend.abhirams.example.com.dearfriend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.HashMap;
import java.util.Map;

import dearfriend.abhirams.example.com.dearfriend.R;
import dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector;
import dearfriend.abhirams.example.com.dearfriend.web.ApplicationController;
import dearfriend.abhirams.example.com.dearfriend.web.WebHelper;

public class ForgotPwdActivity extends AppCompatActivity {

    EditText userName,email;
    Button send;


    private ProgressDialog progress;
    // Connection detector class
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        userName=findViewById(R.id.regUserIdd);
        email=findViewById(R.id.emailPwdId);
        send=findViewById(R.id.forgotsendId);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean nullCheck = false;

                if (userName.getText().toString().trim().equals("") || userName.getText().length() == 0) {
                    userName.setError("UserName cannot be empty");
                    nullCheck = true;
                }else if ((userName.getText().toString()).length() >= 35) {
                    userName.setError("Username too long");
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

                if (nullCheck == false) {


                        sendData(userName.getText().toString().trim(),email.getText().toString());

                }

            }
        });




    }


    public void sendData( String name,String email)
    {
        cd = new dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent)
        {
            progress = ProgressDialog.show(this, "", getString(R.string.plsWait));
            StringRequest req = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL+"sendForgotPwdMail/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.v("Response:%n %s", response);
                    progress.dismiss();
                    if((response.substring(1,response.length()-1).equalsIgnoreCase("Successful")) || response.equals("Successful") )
                    {
                        Intent i = new Intent(ForgotPwdActivity.this,LoginActivity.class);
                        startActivity(i);


                        MDToast.makeText(ForgotPwdActivity.this, "Sent mail successfully,Please do check to reset password",MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                    }
                    else if((response.substring(1,response.length()-1).equalsIgnoreCase("NODATA")) || response.equals("NODATA"))
                    {
                        MDToast.makeText(ForgotPwdActivity.this, "Entered data is invalid,Please check again...",MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                    }
                    else
                    {
                        MDToast.makeText(ForgotPwdActivity.this, getString(R.string.error),MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: "+error);
                    progress.dismiss();
                    MDToast.makeText(ForgotPwdActivity.this, getString(R.string.error),MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            }){
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("name",name);//put your parameters here
                    parameters.put("email",email);//put your parameters here
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

