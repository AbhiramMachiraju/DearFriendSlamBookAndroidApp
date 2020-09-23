package dearfriend.abhirams.example.com.dearfriend.web;


import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {

    public static String response;
    public static String performPostCall(Context context, String requestURL,final String postDataParams){

        //sending to server
        StringRequest request = new StringRequest(Request.Method.POST, WebHelper.ROOT_URL+requestURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                System.out.println("RESPONE -- "+s);
                response = s;
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("ERROR -- "+volleyError.getMessage());
                response = "error";
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("postDataParams",postDataParams);//put your parameters here
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);

        return response;
    }
}
