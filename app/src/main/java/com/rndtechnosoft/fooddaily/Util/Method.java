package com.rndtechnosoft.fooddaily.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Method {

    private Activity activity;

    public static Typeface OpenSans_Regular;
    public static Typeface OpenSans_Semibold;

    public static boolean onBackPress = false;

    public Method(Activity activity) {
        this.activity = activity;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                OpenSans_Regular = activity.getResources().getFont(R.font.roboto_regular);//Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans_Regular.ttf");
//                OpenSans_Semibold = activity.getResources().getFont(R.font.roboto_regular);//Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans_Semibold.ttf");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void forceRTLIfSupported(Window window, Activity activity) {
        if (activity.getResources().getString(R.string.isRTL).equals("true")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    public static String getAndroidID(Context context){
         String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
         return android_id;
    }

    public static class sendData extends AsyncTask<Void, Void, String> {
        Context context;
        String token,aid;
        public sendData(Context context, String token, String aid) {
            this.context=context;
            this.token=token;
            this.aid=aid;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {
            final String[] result = {""};

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.update_version_fcm,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("token_reg",response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                        }
                    }) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", SharedPref.getUserId(context));
                    params.put("token", token);
                    params.put("mobileid", aid);
                    params.put("versioncode", Constants.VERSION);
                    return params;
                }
            };
            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            //adding the string request to request queue
            requestQueue.add(stringRequest);
            return result[0];
        }

        @Override
        protected void onPostExecute(String result) {
            //dialog.dismiss();
            //resultAlert(Result);
        }
    }

    public static String getCartCount(Context context, final TextView tvCartCount) {

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.cart_count+"&user_id="+SharedPref.getUserId(context), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("CART_COUNT");
                    for (int i=0; i< jsonArray.length();i++){
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        Constants.CART_COUNT = jsonObj.getString("count");
                        if (Integer.parseInt(Constants.CART_COUNT)>0){
                            tvCartCount.setVisibility(View.VISIBLE);
                            tvCartCount.setText(Constants.CART_COUNT);
                        }else{
                            tvCartCount.setVisibility(View.GONE);
                        }
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

        return Constants.CART_COUNT;
    }

    public static String getTotalPrice(final Context context, final TextView tvTotalPrice, final RelativeLayout rel_total) {

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.total_price+"&user_id="+SharedPref.getUserId(context), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("TOTAL_PRICE");
                    for (int i=0; i< jsonArray.length();i++){
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        Constants.TOTAL_PRICE = jsonObj.getString("total_price");
                        if (Double.parseDouble(Constants.TOTAL_PRICE)>0){
                            tvTotalPrice.setVisibility(View.VISIBLE);
                            rel_total.setVisibility(View.VISIBLE);
                            tvTotalPrice.setText(context.getResources().getString(R.string.total_price)+" "+SharedPref.getCurrency(context)+Constants.TOTAL_PRICE);
                        }else{
                            tvTotalPrice.setVisibility(View.GONE);
                            rel_total.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    rel_total.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                rel_total.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);

        return Constants.CART_COUNT;
    }

    public static void sendRegistrationToServer(Context context, String token, String aid) {

        new sendData(context,token,aid).execute();
    }

    public static boolean haveNetworkConnection(Context context)
    {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo)
        {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
            {
                if (ni.isConnected())
                {
                    haveConnectedWifi = true;
                    Log.v("WIFI CONNECTION ", "AVAILABLE");
                } else
                {
                    Log.v("WIFI CONNECTION ", "NOT AVAILABLE");
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
            {
                if (ni.isConnected())
                {
                    haveConnectedMobile = true;
//                    Log.v("MOBILE INTERNET CONNECTION ", "AVAILABLE");
                } else
                {
//                    Log.v("MOBILE INTERNET CONNECTION ", "NOT AVAILABLE");
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    //network check
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    static Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");

    Pattern toSafePattern(String text)
    {
        return Pattern.compile(".*" + escapeSpecialRegexChars(text) + ".*");
    }

    public static String escapeSpecialRegexChars(String str) {

        return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");
    }

}
