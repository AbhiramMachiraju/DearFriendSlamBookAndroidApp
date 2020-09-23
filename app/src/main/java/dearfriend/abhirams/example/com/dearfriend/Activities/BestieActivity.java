package dearfriend.abhirams.example.com.dearfriend.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dearfriend.abhirams.example.com.dearfriend.Model.SlamBookDO;
import dearfriend.abhirams.example.com.dearfriend.Model.UserDO;
import dearfriend.abhirams.example.com.dearfriend.R;
import dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector;
import dearfriend.abhirams.example.com.dearfriend.Util.CustomCrashHandler;
import dearfriend.abhirams.example.com.dearfriend.Util.VolleyErrorHandle;
import dearfriend.abhirams.example.com.dearfriend.web.ApplicationController;
import dearfriend.abhirams.example.com.dearfriend.web.WebHelper;

public class BestieActivity extends AppCompatActivity {


    EditText petName,color,addicition,crush,laugh,annoy,zoadic,hobbies,crazy,movie,buy,memory,sports,song;
    Button uploadedImgBtn,submit,cancel;

    //Image.....
    private int  CAMERA = 2;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    Bitmap bitmap;
    private static final String IMAGE_DIRECTORY = "/DearFriend/DMS/SlamBookImg";
    String imageString=null;
    ImageView uploadedImg;
    String user_AutoId;

    //Internet
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestie);
        //Crash........
        Thread.setDefaultUncaughtExceptionHandler(new CustomCrashHandler(this));

        //Session............
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_AutoId =preferences.getString("userAutoIdForDashBoard", "defaultValue");


        intializeIds();

        uploadedImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BestieActivity.this,DashBoardActivity.class);
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean nullCheck = false;

                if (petName.getText().toString().trim().equals("") || petName.getText().length() == 0) {
                    petName.setError("UserName cannot be empty");
                    nullCheck = true;
                }

                if(null==uploadedImg.getDrawable())
                {
                    nullCheck=true;
                    MDToast.makeText(BestieActivity.this,"Upload image",MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                }

                if (nullCheck == false) {
                    UserDO userLoggedIn = new UserDO();
                    userLoggedIn.setId(Integer.parseInt(user_AutoId));

                    SlamBookDO slamBookDO = new SlamBookDO();

                    slamBookDO.setPetName(petName.getText().toString());
                    slamBookDO.setColor(color.getText().toString());
                    slamBookDO.setVideoGame(addicition.getText().toString());
                    slamBookDO.setCursh(crush.getText().toString());
                    slamBookDO.setFlaugh(laugh.getText().toString());
                    slamBookDO.setFanoys(annoy.getText().toString());
                    slamBookDO.setZoadic(zoadic.getText().toString());
                    slamBookDO.setHobby(hobbies.getText().toString());
                    slamBookDO.setCrazy(crazy.getText().toString());
                    slamBookDO.setMovie(movie.getText().toString());
                    slamBookDO.setBuy(buy.getText().toString());
                    slamBookDO.setMemory(memory.getText().toString());
                    slamBookDO.setSports(sports.getText().toString());
                    slamBookDO.setSong(song.getText().toString());
                    slamBookDO.setUserDO(userLoggedIn);
                    try
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                        byte[] imageBytes = baos.toByteArray();
                        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    }
                    catch (Exception e) { e.printStackTrace(); }

                    Gson gson = new Gson();
                    String jsonSlamDO = gson.toJson(slamBookDO);
                    saveData(jsonSlamDO);


                }

            }
        });


    }



    public void intializeIds()
    {


        petName= findViewById(R.id.petNameId);
        color= findViewById(R.id.favoriColorId);
        addicition= findViewById(R.id.addicitionId);
        crush= findViewById(R.id.CurrentcrushId);
        laugh= findViewById(R.id.laughId);
        annoy= findViewById(R.id.annoyId);
        zoadic= findViewById(R.id.zoadicId);
        hobbies= findViewById(R.id.hobbiesId);
        crazy= findViewById(R.id.crazyId);
        movie= findViewById(R.id.MovieId);
        buy= findViewById(R.id.buyId);
        memory= findViewById(R.id.memoryId);
        sports= findViewById(R.id.sportId);
        song= findViewById(R.id.songId);
        uploadedImgBtn=findViewById(R.id.uploadImg);
        uploadedImg=findViewById(R.id.takenImg);
        submit=findViewById(R.id.slamsubmit);
        cancel=findViewById(R.id.slamcancel);

    }

    private void showPictureDialog(){
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary()
    {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getAbsolutePath();
        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (data != null) {
                /*Uri contentURI = data.getData();*/
                try
                {
                    Uri imageUri = data.getData();
                    InputStream inputStream;

                    inputStream = getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    uploadedImg.setVisibility(View.VISIBLE);
                    uploadedImg.setImageBitmap(bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                    MDToast.makeText(getApplicationContext(), "Failed!",MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            }
        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            uploadedImg.setVisibility(View.VISIBLE);
            uploadedImg.setImageBitmap(bitmap);
            saveImage(bitmap);
            MDToast.makeText(BestieActivity.this, "Image Saved!",MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
        }
    }



    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void saveData(String  jsonSlamDO)
    {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            progress = ProgressDialog.show(this, "", getString(R.string.plsWait));
            StringRequest req = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL + "saveSlamData/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.v("Response:%n %s", response);
                    progress.dismiss();
                    if((response.substring(1,response.length()-1).contains("Successful")) || response.contains("Successful"))
                    {
                        try
                        {
                            JSONObject jsonObj =  new JSONObject(response);
                            jsonObj.length();
                            String key = jsonObj.keys().next();
                            String value = jsonObj.get(key).toString();
                            String[] values = value.split(java.util.regex.Pattern.quote("~"));

                            if(key.equals("Successful"))
                            {
                                    Intent i = new Intent(BestieActivity.this,DashBoardActivity.class);
                                    startActivity(i);
                                MDToast.makeText(BestieActivity.this, getString(R.string.datasave)+ "\n" + "of friendName:" + values[0], MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                            }
                            else
                            {
                                MDToast.makeText(BestieActivity.this, getString(R.string.datafail), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                            }
                        }
                        catch (Exception e)
                        {
                            MDToast.makeText(BestieActivity.this, getString(R.string.datafail), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                        }
                    }
                    else  if((response.substring(1,response.length()-1).contains("ImageUploadFailure")) || response.contains("ImageUploadFailure"))
                    {
                        MDToast.makeText(BestieActivity.this, getString(R.string.datafail), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                    else
                    {
                        MDToast.makeText(BestieActivity.this, getString(R.string.datafail), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("Error: " + volleyError);
                    progress.dismiss();
                    MDToast.makeText(BestieActivity.this, VolleyErrorHandle.VolleyErrorHandel(volleyError),
                            MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            })
            {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("jsonSlamDO", jsonSlamDO);//put your parameters here
                    if(null != imageString)
                        parameters.put("imageString", imageString);
                    return parameters;
                }
            };

            ApplicationController.getInstance().addToRequestQueue(req);

        }
        else {
            MDToast.makeText(BestieActivity.this,getString(R.string.noInternetConn) + "\n" + getString(R.string.plsConnectInternet),
                    MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();

            thread.start();
        }
    }
    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(Toast.LENGTH_SHORT); // As I am using LENGTH_SHORT in Toast
                BestieActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
