package dearfriend.abhirams.example.com.dearfriend.Util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Spinner;

public class CheckConditionUtil
{

    public static Boolean editTextCheckNull(EditText editText)
    {
        if(null != editText.getText() && editText.getText().toString().length() > 0 && !editText.getText().toString().trim().equals(""))
            return true;
        else
            return false;
    }

    public static Boolean spinnerCheckNotNull(Spinner spinner)
    {
        if (null != spinner.getSelectedItem() && spinner.getSelectedItem().toString().length() > 0 && !spinner.getSelectedItem().toString().trim().equals(""))
            return true;
        else
            return false;
    }


    public static Boolean normalSpinnerCheckNotNull(Spinner spinner,String noneValue)
    {
        if (null != spinner.getSelectedItem() && spinner.getSelectedItem().toString().length() > 0 && !spinner.getSelectedItem().toString().trim().equals(noneValue))
            return true;
        else
            return false;
    }


    /*public static Boolean spinnerCheckNotNull(SearchableSpinner spinner)
    {
        if (null != spinner.getSelectedItem() && spinner.getSelectedItem().toString().length() > 0 && !spinner.getSelectedItem().toString().trim().equals(""))
            return true;
        else
            return false;
    }*/


    public static Boolean stringCheckNotNull(String string)
    {
        if (null != string && string.length() > 0 && !string.trim().equals(""))
            return true;
        else
            return false;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
