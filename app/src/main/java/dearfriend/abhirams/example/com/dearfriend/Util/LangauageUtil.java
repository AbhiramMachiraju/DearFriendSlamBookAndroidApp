package dearfriend.abhirams.example.com.dearfriend.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Locale;

public class LangauageUtil extends Application {


    /*context="getBaseContext()"*/
    public static void changeLangauage(String code,String message, Context context)
    {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) { config.setLocale(locale); }
        else { config.locale = locale; }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        MDToast.makeText(context,message, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
    }

}
