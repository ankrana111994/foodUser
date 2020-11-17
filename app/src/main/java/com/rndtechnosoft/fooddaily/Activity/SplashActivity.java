package com.rndtechnosoft.fooddaily.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.Method;
import com.rndtechnosoft.fooddaily.Util.SharedPref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        getConfig();

        if (Method.haveNetworkConnection(SplashActivity.this)) {
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {

                    SharedPreferences preferences = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
                  //  SharedPref.clearAllPreferences(SplashActivity.this);
                    if(SharedPref.getMobileNumber(SplashActivity.this)!=null & SharedPref.getMobileNumber(SplashActivity.this).equals("")) {
                        Intent i = new Intent(SplashActivity.this, InfoActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        } else {
            Toast.makeText(SplashActivity.this,getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getResources().getString(R.string.internet_connection_message));
            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.exit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }



    private void getConfig() {
        final RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.config, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("config");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String currency = jsonObject1.getString("currency");
//                        String paypal = jsonObject1.getString("paypal");
                        String dashed = jsonObject1.getString("dashed");
                        String app_status = jsonObject1.getString("app_status");

                        SharedPref.setPreference(SharedPref.CURRENCY,currency,SplashActivity.this);
//                        SharedPref.setPreference(SharedPref.PAYPAL,paypal,SplashActivity.this);
                        SharedPref.setPreference(SharedPref.DASHED,dashed,SplashActivity.this);
                        SharedPref.setPreference(SharedPref.APP_STATUS,app_status,SplashActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
            }
        });
        requestQueue.add(stringRequest);
    }
}
