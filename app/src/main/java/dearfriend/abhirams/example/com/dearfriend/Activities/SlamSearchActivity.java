package dearfriend.abhirams.example.com.dearfriend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dearfriend.abhirams.example.com.dearfriend.Model.CommonSearchDO;
import dearfriend.abhirams.example.com.dearfriend.Model.SlamBookDO;
import dearfriend.abhirams.example.com.dearfriend.R;
import dearfriend.abhirams.example.com.dearfriend.Util.AutoCompleteCommonAdapter;
import dearfriend.abhirams.example.com.dearfriend.Util.ConnectionDetector;
import dearfriend.abhirams.example.com.dearfriend.web.ApplicationController;
import dearfriend.abhirams.example.com.dearfriend.web.WebHelper;

public class SlamSearchActivity extends AppCompatActivity {


    AutoCompleteTextView frndIdAuto;
    String textValue,text;
    Button getAllFrnd;
    LinearLayout frndIdLayout ;
    ListView listViewId;
    List<SlamBookDO> slamBookDOList = new ArrayList<SlamBookDO>();
    Boolean closeListView=false;
    LinearLayout headerLytBtn;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    ProgressDialog progress;
    private List<CommonSearchDO> commonSearchDOs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slam_search);
        intializeIds();

        //ListView.............
        listViewId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                CommonSearchDO commonSearchDO = (CommonSearchDO)parent.getAdapter().getItem(position);
                textValue=null;
                text = commonSearchDO.getHiddenValue();
                searchFriend();
            }});

        frndIdAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id)
            {
                CommonSearchDO commonSearchDO = (CommonSearchDO)parent.getAdapter().getItem(pos);

                text=null;
                textValue = commonSearchDO.getHiddenValue();
                searchFriend();
            }
        });

        getAllFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllFriends();
                if(closeListView==true)
                {
                    listViewId.setVisibility(View.INVISIBLE);
                    closeListView=false;
                }
                else
                {
                    listViewId.setVisibility(View.VISIBLE);
                    closeListView=true;
                }

            }
        });


    }

    public void intializeIds()
    {
        frndIdAuto = findViewById(R.id.friendIdAuto);
        getAllFrnd = findViewById(R.id.getAllFrnds);;
        frndIdLayout=findViewById(R.id.frndIdLayout);
        listViewId=findViewById(R.id.listViewId);
    }

    public void searchFriend() {

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            progress = ProgressDialog.show(this, "", getString(R.string.plsWait));
            StringRequest req = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL + "searchfriend/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    VolleyLog.v("Response:%n %s", response);
                    progress.dismiss();
                    if (response != null) {
                        try {
                            JSONObject jsonObj = null;

                            jsonObj = new JSONObject(response);
                            jsonObj.length();
                            String key = jsonObj.keys().next();
                            String value = jsonObj.get(key).toString();

                            String key_Split[] = jsonObj.keys().next().split(java.util.regex.Pattern.quote("$"));
                            String imageString=key_Split[1];

                            if (key_Split[0].equals("Successful"))
                            {
                                ObjectMapper objectMapper = new ObjectMapper();
                                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                TypeReference<List<SlamBookDO>> mapType = new TypeReference<List<SlamBookDO>>() {
                                };

                                List<SlamBookDO> friendList = null;

                                friendList = objectMapper.readValue(value, mapType);
                                if (friendList.size() > 0) {

                                    Intent i = new Intent(SlamSearchActivity.this, SlamSearchDetailActivity.class);
                                    i.putExtra("friendList", (Serializable) friendList);
                                    i.putExtra("imageString", imageString);
                                    startActivity(i);
                                    MDToast.makeText(getApplicationContext(), "Search Completed", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                } else {
                                    MDToast.makeText(getApplicationContext(), "No records found", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                                }
                            }

                        }catch(Exception e) {e.printStackTrace();}
                    }
                    else
                    {
                        MDToast.makeText(getApplicationContext(), "No records found", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("Error: " + volleyError);
                    progress.dismiss();
                    MDToast.makeText(SlamSearchActivity.this,"Error Try Again!!", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                }
            })
            {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<String, String>();
                    if(textValue!=null)
                    {
                        parameters.put("textValue", textValue);
                    }
                    else if(text!=null) {
                        parameters.put("listViewTxt", text);
                    }
                    return parameters;
                }
            };

            ApplicationController.getInstance().addToRequestQueue(req);
        } else {
            MDToast.makeText(this, getString(R.string.noInternetConn) + "\n" + getString(R.string.plsConnectInternet), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            thread.start();
        }
    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                Thread.sleep(Toast.LENGTH_SHORT); // As I am using LENGTH_SHORT in Toast
                SlamSearchActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void getAllFriends()
    {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            progress = ProgressDialog.show(this, "", getString(R.string.plsWait));
            StringRequest req = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL + "commonDOList/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    VolleyLog.v("Response:%n %s", response);
                    progress.dismiss();
                    if( !(response.substring(1,response.length()-1).equalsIgnoreCase("UnSuccessful")) && !response.equals("UnSuccessful") )
                    {
                        if((response.substring(1,response.length()-1).equalsIgnoreCase("NoResult")) || response.equals("NoResult") )
                        {
                            MDToast.makeText(getApplicationContext(), "No Data Received", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
                        }
                        else
                        {
                            try
                            {
                                ObjectMapper objectMapper = new ObjectMapper();
                                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                TypeReference<List<SlamBookDO>> mapType = new TypeReference<List<SlamBookDO>>() {};
                                slamBookDOList = objectMapper.readValue(response, mapType);

                                if (slamBookDOList.size() > 0)
                                {
                                    frndIdLayout.setVisibility(View.VISIBLE);
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    commonSearchDOs = new ArrayList<>();
                                    for(SlamBookDO customerDO : slamBookDOList)
                                    {
                                        arrayList.add(customerDO.getPetName());
                                        commonSearchDOs.add(new CommonSearchDO(customerDO.getPetName(),"",customerDO.getId().toString()));
                                    }
                                    AutoCompleteCommonAdapter adapter = new AutoCompleteCommonAdapter(SlamSearchActivity.this,commonSearchDOs);
                                    listViewId.setAdapter(adapter);
                                    frndIdAuto.setAdapter(adapter);
                                }
                                else
                                {
                                    frndIdLayout.setVisibility(View.INVISIBLE);
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                MDToast.makeText(getApplicationContext(), getString(R.string.error), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                            }

                        }
                    }
                    else
                    {
                        MDToast.makeText(SlamSearchActivity.this, getString(R.string.error), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("Error: " + volleyError);
                    progress.dismiss();
                    MDToast.makeText(SlamSearchActivity.this, "Error Try Again!!", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                }
            })
            {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("ObjectName", "SlamBookDO");//put your parameters here
                    return parameters;
                }
            };

            ApplicationController.getInstance().addToRequestQueue(req);
        } else {
            MDToast.makeText(this, getString(R.string.noInternetConn) + "\n" + getString(R.string.plsConnectInternet), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            thread.start();
        }
    }


}
