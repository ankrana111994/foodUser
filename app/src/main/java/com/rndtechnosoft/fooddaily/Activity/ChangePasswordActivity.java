package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText tvpassword, tvconpassword;
    Button btnSubmit;
    String password, conpassword,phone;
    AVLoadingIndicatorView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.toolbar_changepwd);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.change_pwd)+"</b>"));
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        phone = getIntent().getStringExtra(Constants.MOBILE);

        tvpassword = findViewById(R.id.tvpassword);
        tvconpassword = findViewById(R.id.tvconpassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(ChangePasswordActivity.this).equalsIgnoreCase("1")) {
            gd.setColor(getResources().getColor(R.color.colorGrey));
            gd.setCornerRadius(25);
//        gd.setStroke(2, Color.RED);
          //  gd.setStroke(4, getResources().getColor(R.color.colorGrey1), 12, 16);
        }else{
            gd.setColor(getResources().getColor(R.color.colorGrey));
            gd.setCornerRadius(25);
          //  gd.setStroke(4, getResources().getColor(R.color.colorGrey1));
        }
        tvpassword.setBackground(gd);
        tvconpassword.setBackground(gd);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password= tvpassword.getText().toString();
                conpassword = tvconpassword.getText().toString();
                if (password.equals(conpassword)){
                    //api call
                    updatePassword();
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "Both passwords doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePassword() {
        progressBar.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(ChangePasswordActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.forgot_password+"&mobile_no="+phone+"&password="+password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("CHANGE_PASSWORD");
                    String error = jsonObject1.getString("error");
                    if (error.equals("false")){
                        Toast.makeText(ChangePasswordActivity.this, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                        finish();
                    }else{
                        Toast.makeText(ChangePasswordActivity.this, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                progressBar.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
