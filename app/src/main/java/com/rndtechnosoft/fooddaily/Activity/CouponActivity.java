package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Adapter.CouponCodeAdapter;
import com.rndtechnosoft.fooddaily.Model.CouponList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.Method;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CouponActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView coupon_list;
    CouponCodeAdapter codeAdapter;
    ArrayList<CouponList> couponLists;
    private EditText coupon_code;
    private Button apply_button;
    String amount;
    String sub_total;
    int wamt;
    private String error;
    private String pay_amount;
    private String title;
    private String message;
    private String api_amount;
    private String discount;
    AVLoadingIndicatorView progress_coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        toolbar = (Toolbar) findViewById(R.id.toolbar_coupon);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.title_coupon)+"</b>"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        sub_total = extras.getString("amount");
        wamt = extras.getInt("wamt");
        Log.e("subtotal->>>",sub_total);

//        String[] separated = sub_total.split(SharedPref.getCurrency(CouponActivity.this));
//        amount= separated[1].split("\\.")[0];

//        String[] separated = sub_total.split(SharedPref.getCurrency(CouponActivity.this));
        String[] splited = sub_total.split(Method.escapeSpecialRegexChars(SharedPref.getCurrency(CouponActivity.this)));
        try {
            amount= splited[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.e("amt->>>",amount);

        coupon_code = findViewById(R.id.coupon_code);
        apply_button = findViewById(R.id.apply_button);
        coupon_list = findViewById(R.id.coupon_list);
        progress_coupon = findViewById(R.id.progresbar_coupon);
        coupon_list.setLayoutManager(new LinearLayoutManager(this));

        coupon_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                apply_button.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(CouponActivity.this);
                getDiscount(coupon_code.getText().toString(),amount);
            }
        });

        getCouponList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void getDiscount(final String coupon_code, final String amount) {

        final RequestQueue requestQueue = Volley.newRequestQueue(CouponActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.get_discount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("GET_DISCOUNT");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        error = object.getString("error");
                        title = object.getString("title");
                        message = object.getString("message");
                        api_amount = object.getString("amount");
                        pay_amount = object.getString("pay_amount");
                        discount = object.getString("discount");
                        //showCustomDialog(title,message);
                    }
                    startActivity(new Intent(CouponActivity.this,CartActivity.class)
                            .putExtra("title",title).putExtra("message",message)
                            .putExtra("amount",amount).putExtra("pay_amount",pay_amount)
                            .putExtra("discount",discount).putExtra("coupon_code",coupon_code)
                            .putExtra("wamt",wamt));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                progress_coupon.setVisibility(View.GONE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userid", SharedPref.getUserId(CouponActivity.this));
                params.put("coupon_code", coupon_code);
                params.put("min_value", amount);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void getCouponList() {

        progress_coupon.setVisibility(View.VISIBLE);

        final RequestQueue requestQueue = Volley.newRequestQueue(CouponActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.coupon_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                couponLists = new ArrayList<>();
                progress_coupon.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("COUPON_LIST");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String title = object.getString("title");
                        String tandc = object.getString("tandc");
                        String min_order = object.getString("min_order");
                        String exp_date = object.getString("exp_date");
                        String coupon_code = object.getString("coupon_code");
                        String image = object.getString("image");

                        couponLists.add(new CouponList(id, title, tandc, min_order, exp_date, coupon_code, image));
                    }

                    codeAdapter = new CouponCodeAdapter(CouponActivity.this, couponLists,amount);
                    if (couponLists == null || couponLists.size() == 0) {
                        coupon_list.setVisibility(View.GONE);
                    } else {
                        coupon_list.setVisibility(View.VISIBLE);
                        coupon_list.setAdapter(codeAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_coupon.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                progress_coupon.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
