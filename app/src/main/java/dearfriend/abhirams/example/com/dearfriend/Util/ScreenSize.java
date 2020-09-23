package dearfriend.abhirams.example.com.dearfriend.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class ScreenSize {

    public static void getScreenSize(Context context)
    {

        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg="";
        switch (screenSize) {

            case  Configuration.SCREENLAYOUT_SIZE_XLARGE:
                toastMsg = "XLargeScreen";
                break;

            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "LargeScreen";
                break;

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "NormalScreen";
                break;

            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "SmallScreen";
                break;
            default:
                toastMsg = "DefaultScreen";
        }
    }

    public static  void getScreenSize_px(Context context)
    {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float Height = displayMetrics.heightPixels ;
        float Width = displayMetrics.widthPixels;
        Toast.makeText(context, "width:"+Width+",height:"+Height, Toast.LENGTH_LONG).show();
    }
}
