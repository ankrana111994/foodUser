package com.rndtechnosoft.fooddaily.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.rndtechnosoft.fooddaily.Adapter.PaymentAdapter;
import com.rndtechnosoft.fooddaily.Model.Payment;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayActivity extends AppCompatActivity implements PaymentResultListener {

    public Toolbar toolbar;
    TextView tvContinue,tvCOD,tvPickup,tvPayOnline;
    String payment_type="",payment_id="",payment_key="";
    String wallet_amount,comment,sub_price,total_price,discount="",address_id,delivery;
    AVLoadingIndicatorView progress_pay;
    private long mLastClickTime=0;
    LinearLayout cod_lay, pickup_lay, online_pay;
    RadioGroup radioGroup;
    ArrayList<Payment> payments;
//    RecyclerView recycler_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        toolbar = (Toolbar) findViewById(R.id.toolbar_payment);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.payment)+"</b>"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        wallet_amount = getIntent().getStringExtra("wallet_amount");
        comment = getIntent().getStringExtra("comment");
        delivery = getIntent().getStringExtra("delivery");
        sub_price = getIntent().getStringExtra("sub_price");
        total_price = getIntent().getStringExtra("total_price");
        discount = getIntent().getStringExtra("discount");
        address_id = getIntent().getStringExtra("address_id");

        radioGroup = findViewById(R.id.radiogroup);
        tvContinue = findViewById(R.id.tvContinue);
        tvCOD = findViewById(R.id.tvCOD);
        tvPickup = findViewById(R.id.tvPickup);
        tvPayOnline = findViewById(R.id.tvPayOnline);
        progress_pay = findViewById(R.id.progress_pay);
        cod_lay = findViewById(R.id.cod_lay);
        pickup_lay = findViewById(R.id.pickup_lay);
        online_pay = findViewById(R.id.online_pay);
//        recycler_pay = findViewById(R.id.recycler_pay);
//        recycler_pay.setLayoutManager(new LinearLayoutManager(PayActivity.this));
        progress_pay.setVisibility(View.GONE);

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (payment_type.equals("")) {
                    Toast.makeText(PayActivity.this, "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (wallet_amount==null)
                    wallet_amount="";
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                tvContinue.setEnabled(false);
                mLastClickTime = SystemClock.elapsedRealtime();
                if (payment_type.equalsIgnoreCase("Cash On Delivery") || payment_type.equals("Pickup")){
                    tvContinue.setEnabled(true);
                    placeOrder();
                }else if (payment_type.equalsIgnoreCase("Razorpay")){
                    tvContinue.setEnabled(true);
                    startPayment(Double.parseDouble(total_price));
                }else if (payment_type.equalsIgnoreCase("Paypal")){
                    tvContinue.setEnabled(true);
                    Toast.makeText(PayActivity.this, "Paypal Integration", Toast.LENGTH_SHORT).show();
                }

            }
        });

        clickevents();

        paymethods();

    }

    private void paymethods() {
        final RequestQueue requestQueue = Volley.newRequestQueue(PayActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.payment_method, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                payments=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("PAYMENT_METHOD_LIST");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String title = jsonObject1.getString("title");
                        String method_key = jsonObject1.getString("method_key");
                        String image = jsonObject1.getString("image");
                        payments.add(new Payment(id,title,method_key,image));
                    }
                    addRadioButtons();
//                    recycler_pay.setAdapter(new PaymentAdapter(PayActivity.this,payments));
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

    public void addRadioButtons() {
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        //
        for (int i = 0; i < payments.size(); i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(payments.get(i).getTitle());
            rdbtn.setTextSize(24);
            rdbtn.setPadding(20,20,20,20);
            Typeface font = Typeface.createFromAsset(getResources().getAssets(), "calibri_regular.ttf");
            rdbtn.setTypeface(font);
            final int finalI = i;
            rdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    payment_type = String.valueOf(((RadioButton)v).getText());
                    payment_key = payments.get(finalI).getMethod_key();
                }
            });
            radioGroup.addView(rdbtn);
        }
    }

    private void placeOrder() {

        progress_pay.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(PayActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.place_order, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] separated = response.split("end");

                String res= separated[0];
                progress_pay.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("PLACE_ORDER");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String error = object.getString("error");
                        int order_id = Integer.parseInt(object.getString("order_id"));
                        String message = object.getString("message");
                        Intent intent = new Intent(PayActivity.this, BillActivity.class).putExtra("order_id",order_id);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_pay.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                progress_pay.setVisibility(View.GONE);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", SharedPref.getUserId(PayActivity.this));
                params.put("wallet_amount", wallet_amount);
                params.put("comment", comment);
                params.put("payment_type", payment_type);
                params.put("payment_id", payment_id);
                params.put("discount", discount);
                params.put("total_price", total_price);
                params.put("sub_price", sub_price);
                params.put("address_id", address_id);
                params.put("delivery_charges", delivery);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void clickevents() {
        tvCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type="COD";
                tvCOD.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
                tvPickup.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
                tvPayOnline.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
            }
        });
        tvPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type="Pickup";
                tvCOD.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
                tvPickup.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
                tvPayOnline.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
            }
        });
        tvPayOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type="Online";
                tvCOD.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
                tvPickup.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
                tvPayOnline.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
            }
        });
        cod_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type="COD";
                tvCOD.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
                tvPickup.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
                tvPayOnline.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
            }
        });
        pickup_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type="Pickup";
                tvCOD.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
                tvPickup.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
                tvPayOnline.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
            }
        });
        online_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type="Online";
                tvCOD.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
                tvPickup.setBackground(getResources().getDrawable(R.drawable.ic_circle_border));
                tvPayOnline.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
            }
        });
    }

    public void startPayment(double v) {
//        String total = "500";

        float price = (float) (v * 100);
        final Checkout co = new Checkout();
        co.setKeyID(payment_key);
        try {
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("description", "Total payable amount");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", price);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            options.put("prefill", preFill);
            co.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onPaymentSuccess(String s) {
        placeOrder();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(PayActivity.this,s,Toast.LENGTH_SHORT).show();
    }
}
