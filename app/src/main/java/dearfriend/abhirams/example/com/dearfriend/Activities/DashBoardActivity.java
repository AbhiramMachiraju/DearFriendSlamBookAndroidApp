package dearfriend.abhirams.example.com.dearfriend.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import dearfriend.abhirams.example.com.dearfriend.Model.DataModel;
import dearfriend.abhirams.example.com.dearfriend.R;
import dearfriend.abhirams.example.com.dearfriend.Util.AutoFitGridLayoutManager;
import dearfriend.abhirams.example.com.dearfriend.Util.CheckPermission;
import dearfriend.abhirams.example.com.dearfriend.Util.CustomCrashHandler;
import dearfriend.abhirams.example.com.dearfriend.Util.RecyclerViewAdapter;




public class DashBoardActivity extends AppCompatActivity  implements  RecyclerViewAdapter.ItemListener {

    RecyclerView recyclerView;
    Intent intentNew = null;
    ArrayList<DataModel> dataModelArrayList = new ArrayList<>();
    ImageButton volBtn;
    static MediaPlayer mp=null;
    Boolean appCharshed=false;

    //LoggedInUserAutoId
    String userAutoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);



        //Crash........
        Thread.setDefaultUncaughtExceptionHandler(new CustomCrashHandler(this));
        if(null!=getIntent().getExtras())
        {
            appCharshed = getIntent().getExtras().getBoolean("APP_CRASHED");
            if(appCharshed)
                MDToast.makeText(DashBoardActivity.this,"App Crashed, due to unknown error", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
        }

        //Maintaining a Session.............
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userAutoId = preferences.getString("userAutoIdForDashBoard","defaultValue");
         if(null==mp)
         {
             mp=MediaPlayer.create(getApplicationContext(),R.raw.slippingaway);
             mp.start();
         }

        volBtn = findViewById(R.id.volumeBtn);
        volBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mp.isPlaying())
                {
                    volBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_volume_up_black_24dp));
                    mp.start();
                }
                else
                {

                    volBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_volume_off_black_24dp));
                    mp.pause();
                }

            }
        });

        dashBoardFeature();
    }


    public void dashBoardFeature()
    {
        dataModelArrayList.add(new DataModel("MY BESTIE", R.drawable.iconfrnd,"1"));
        dataModelArrayList.add(new DataModel("SLAM BOOK", R.drawable.iconslam,"2"));
        dataModelArrayList.add(new DataModel("ABOUT", R.drawable.iconabout,"3"));


        recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, dataModelArrayList, this);
        recyclerView.setAdapter(adapter);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        recyclerView.setLayoutManager(layoutManager);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onItemClick(DataModel item) {
        switch(item.text) {
            case "MY BESTIE":
                intentNew = new Intent(DashBoardActivity.this,BestieActivity.class);
                startActivity(intentNew);
                break;
            case "SLAM BOOK":
                intentNew = new Intent(DashBoardActivity.this,SlamSearchActivity.class);
                startActivity(intentNew);
                break;

            case "ABOUT":
                intentNew = new Intent(DashBoardActivity.this,AboutMEActivity.class);
                startActivity(intentNew);
                break;

            default:
                MDToast.makeText(DashBoardActivity.this, "Not Configured Yet", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                break;

        }


    }


    Thread thread = new Thread(){
        @Override
        public void run()
        {
            try
            {
                Thread.sleep(Toast.LENGTH_SHORT); // As I am using LENGTH_SHORT in Toast
                DashBoardActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void showDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Logout")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(DashBoardActivity.this,LoginActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            if(null!=mp && mp.isPlaying())
                            {
                                mp.stop();
                                mp.reset();
                                mp.release();
                                mp = null;
                            }
                        }catch (Exception e){e.printStackTrace();}
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("LogOut");
        alert.show();
    }

    @Override
    public void onDestroy() {

        try {
            if(null!=mp && mp.isPlaying())
            {
                mp.stop();
                mp.reset();
                mp.release();
                mp = null;
            }
            }catch (Exception e){e.printStackTrace();}
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

        showDialog();
    }
}
