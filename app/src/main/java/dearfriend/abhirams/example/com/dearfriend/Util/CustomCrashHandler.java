package dearfriend.abhirams.example.com.dearfriend.Util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dearfriend.abhirams.example.com.dearfriend.Activities.LoginActivity;
import dearfriend.abhirams.example.com.dearfriend.web.ApplicationController;
import dearfriend.abhirams.example.com.dearfriend.web.WebHelper;

public class CustomCrashHandler implements Thread.UncaughtExceptionHandler {

    Activity activity;
    String loggedInUserName;
    Context applicationContext;
    private static final String LOG_DIRECTORY = "/DearFriend/DMS/Log/";

    public CustomCrashHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {

        try
        {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra("APP_CRASHED", true);
            Log.d("ERROR -getMessage","---------" + ex.getMessage());
            Log.d("ERROR -getCause","--------" + ex.getCause());
            Log.d("ERROR -getStackTrace","--------" + Arrays.toString(ex.getStackTrace()));

            applicationContext = LoginActivity.getContextOfApplication();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            loggedInUserName = preferences.getString("loggedInUserName", "defaultValue");
            //String dirPath1 = Environment.getExternalStorageDirectory().toString();

            //LogCat[toMainActivity..]
            StringBuilder log = new StringBuilder();
            log.append("ERROR -getMessage ---------" + System.lineSeparator()+ex.getMessage()+System.lineSeparator());
            log.append("ERROR -getCause ---------" +System.lineSeparator()+ ex.getCause()+System.lineSeparator());
            log.append("ERROR -getStackTrace ---------" +System.lineSeparator()+ Arrays.toString(ex.getStackTrace()));
            //Convert log to string
            final String logString = new String(log.toString());
            String fileName = new SimpleDateFormat("ddMMyyyyHHmmss" ).format( new Date() );
            String intentData=loggedInUserName+" "+ StaticContents.user_AutoId+"E$~XZ"+logString+"E$~XZ"+fileName;
            intent.putExtra("INTENT_LOGCAT", intentData);

            //Allow RunTime Permissions && To save LogData(Before Crashing).........
            CheckPermission.checkAndRequestPermissions(activity);
            writeLogCat(loggedInUserName+" "+StaticContents.user_AutoId,ex,applicationContext,intent);

            activity.startActivity(intent);
            activity.finish();
            System.exit(0);
        }
        catch (Exception e) {  e.printStackTrace(); }
    }

    //LogCat For Client_Side
    protected void writeLogCat(String loggedInUserName,Throwable ex,Context applicationContext,Intent intent)
    {
        try
        {
            StringBuilder log = new StringBuilder();
            log.append("ERROR -getMessage ---------"+ System.lineSeparator()+ex.getMessage()+System.lineSeparator());
            log.append("ERROR -getCause ---------" +System.lineSeparator()+ ex.getCause()+System.lineSeparator());
            log.append("ERROR -getStackTrace ---------" +System.lineSeparator()+ Arrays.toString(ex.getStackTrace()));


            //Convert log to string
            final String logString = new String(log.toString());

//Create txt file in SD Card......

            //File dir = new File(dirPath, "/Android/data/" + applicationContext.getPackageName()+ "/AdishFabritech/DMS/Log/"+loggedInUserName);

            File dir = new File(Environment.getExternalStorageDirectory() + LOG_DIRECTORY+""+loggedInUserName);
            if(!dir.exists())
                dir.mkdirs();

            String fileName = new SimpleDateFormat("ddMMyyyyHHmmss" ).format( new Date() );

            File file = new File( dir,  fileName + ".txt" );
            //To write logcat in text file
            FileOutputStream fout = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fout);

            //Writing the string to file
            osw.write(logString);
            osw.flush();
            osw.close();
        }
        catch(FileNotFoundException e) { e.printStackTrace(); }
        catch(IOException e) { e.printStackTrace(); }
    }


    //LogCat For Server_Side
    public static void saveLogToServer(String loggedInUserName,String logString,String fileName,Context context)
    {
        ConnectionDetector cd;
        Boolean isInternetPresent = false;
        cd = new dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector(context);
        isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent)
        {
            StringRequest req = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL + "saveLogToServer/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.v("Response:%n %s", response);

                    if(CheckConditionUtil.stringCheckNotNull(response))
                    {
                        if((response.substring(1,response.length()-1).equalsIgnoreCase("Successful")) || response.equals("Successful"))
                        {
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("Error: "+volleyError);
                    String volleyError1 = volleyError.toString();
                }
            })
            {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("loggedInUserName",loggedInUserName);
                    parameters.put("logString",logString);
                    parameters.put("fileName",fileName);
                    return parameters;
                }
            };

            ApplicationController.getInstance().addToRequestQueue(req);
        }
        else
        {
            try
            {

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
