package dearfriend.abhirams.example.com.dearfriend.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertMessage {
	
	 /**
     * Function to display simple Alert Dialog
      * @param context - application context
      * @param title - alert dialog title
      * @param message - alert message
      * @param status - success/failure (used to set icon)                 */
    @SuppressWarnings("deprecation")
	public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
         
    
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
  
  
  

}
