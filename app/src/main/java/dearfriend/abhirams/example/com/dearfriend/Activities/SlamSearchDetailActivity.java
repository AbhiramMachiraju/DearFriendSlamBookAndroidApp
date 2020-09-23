package dearfriend.abhirams.example.com.dearfriend.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import dearfriend.abhirams.example.com.dearfriend.Model.SlamBookDO;
import dearfriend.abhirams.example.com.dearfriend.R;

public class SlamSearchDetailActivity extends AppCompatActivity {

    TextView createdFrndId;
    ImageView takenImg;
    EditText petName,color,addicition,crush,laugh,annoy,zoadic,hobbies,crazy,movie,buy,memory,sports,song;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slam_search_detail);
        intializeId();
        List<SlamBookDO> slamList = (List<SlamBookDO>) getIntent().getSerializableExtra("friendList");
        String imageString=getIntent().getStringExtra("imageString");
        SlamBookDO slamDOVals=slamList.get(0);
        bitmap = StringToBitMap(imageString);
        takenImg.setVisibility(View.VISIBLE);
        takenImg.setImageBitmap(bitmap);


        createdFrndId.setText(slamDOVals.getUserDO().getUserId()+"'s Amigo");
        petName.setText(slamDOVals.getPetName());
        color.setText(slamDOVals.getColor());
        addicition.setText(slamDOVals.getVideoGame());
        crush.setText(slamDOVals.getCursh());
        laugh.setText(slamDOVals.getFlaugh());
        annoy.setText(slamDOVals.getFanoys());
        zoadic.setText(slamDOVals.getZoadic());
        hobbies.setText(slamDOVals.getHobby());
        crazy.setText(slamDOVals.getCrazy());
        movie.setText(slamDOVals.getMovie());
        buy.setText(slamDOVals.getBuy());
        memory.setText(slamDOVals.getMemory());
        sports.setText(slamDOVals.getSports());
        song.setText(slamDOVals.getSong());

    }


    public void intializeId()
    {
        createdFrndId=findViewById(R.id.createdFriendId);
        takenImg=findViewById(R.id.takenSlamImg);
        petName=findViewById(R.id.petSlamNameId);
        addicition=findViewById(R.id.addicitionSlamId);
        color= findViewById(R.id.favoriColorSlamId);
        crush= findViewById(R.id.CurrentcrushSlamId);
        laugh= findViewById(R.id.laughSlamId);
        annoy= findViewById(R.id.annoySlamId);
        zoadic= findViewById(R.id.zoadicSlamId);
        hobbies= findViewById(R.id.hobbiesSlamId);
        crazy= findViewById(R.id.crazySlamId);
        movie= findViewById(R.id.MovieSlamId);
        buy= findViewById(R.id.buySlamId);
        memory= findViewById(R.id.memorySlamId);
        sports= findViewById(R.id.sportSlamId);
        song= findViewById(R.id.songSlamId);

    }

    public static Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
