package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import static com.rndtechnosoft.fooddaily.Util.SharedPref.FIRST_LAUNCH;

public class LoginActivity extends AppCompatActivity {

    EditText tvmobile,tvpassword;
    TextView tvForgotPwd;
    LinearLayout btnSignup;
    Button btnLogin,btnMenu;
    private String mobile="";
    private String password="";
    AVLoadingIndicatorView progress_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


    }

    private void initView() {
        tvmobile = findViewById(R.id.tvusername);
        tvpassword = findViewById(R.id.tvpassword);
        tvForgotPwd = findViewById(R.id.tvForgotPwd);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        btnMenu = findViewById(R.id.btnMenu);
        progress_login = findViewById(R.id.progress_login);
        progress_login.setVisibility(View.GONE);
        TextView signup_txt = (TextView) findViewById(R.id.signup_txt);
        signup_txt.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

//        SpannableString SpanString = new SpannableString(
//                getResources().getString(R.string.dont_have_account));
//
//        ClickableSpan signup = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                Intent mIntent = new Intent(LoginActivity.this, SignUpActivity.class);
//                mIntent.putExtra("signup", true);
//                startActivity(mIntent);
//            }
//        };

//        SpanString.setSpan(signup, 23, 30, 0);
//        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 23, 30, 0);
//        SpanString.setSpan(new UnderlineSpan(), 23, 30, 0);

//        btnSignup.setMovementMethod(LinkMovementMethod.getInstance());
//        btnSignup.setText(SpanString, TextView.BufferType.SPANNABLE);
//        btnSignup.setSelected(true);

        clickevents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(LoginActivity.this).equalsIgnoreCase("1")) {
            gd.setColor(getResources().getColor(R.color.colorGrey));
            gd.setCornerRadius(25);
//        gd.setStroke(2, Color.RED);
         //   gd.setStroke(4, getResources().getColor(R.color.colorGrey1), 12, 16);
        }else{
            gd.setColor(getResources().getColor(R.color.colorGrey));
            gd.setCornerRadius(25);
          //  gd.setStroke(4, getResources().getColor(R.color.colorGrey1));
        }
//        tvmobile.setBackground(gd);
//        tvpassword.setBackground(gd);
    }

    private void clickevents() {
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_login.setVisibility(View.VISIBLE);
                boolean flag = checkPhone();
                if (flag){
                    mobile = tvmobile.getText().toString();
                    password = tvpassword.getText().toString();
                    login();
                }
            }
        });

        tvForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
    }

    public Boolean checkPhone() {

        mobile = tvmobile.getText().toString();
        if (mobile.length() == 0) {
            tvmobile.setError(getString(R.string.error_field_empty));
            return false;
        }

        if (mobile.length()!=10) {

            tvmobile.setError(getString(R.string.wrong_format));

            return false;
        }
        tvmobile.setError(null);

        return true;
    }

    private void login() {
        final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.login+"&mobile="+mobile+"&password="+password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject object = jsonObject.getJSONObject("LOGGED_IN_USER");
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String mobile = object.getString("mobile");
                    String email = object.getString("email");
                    String gender = object.getString("gender");
                    String image = object.getString("image");
                    String wallet = object.getString("wallet");
                    String searchRadius = object.getString("search_radius");

                  //  String hasAddress = "false";

                   String hasAddress = object.getString("hasAddress");

                    SharedPref.setPreference(SharedPref.USER_NAME,name,LoginActivity.this);
                    SharedPref.setPreference(SharedPref.USER_MOBILE,mobile,LoginActivity.this);
                    SharedPref.setPreference(SharedPref.USER_ID,id,LoginActivity.this);
                    SharedPref.setPreference(SharedPref.USER_GENDER,gender,LoginActivity.this);
                    SharedPref.setPreference(SharedPref.USER_EMAIL,email,LoginActivity.this);
                    SharedPref.setPreference(SharedPref.USER_IMAGE,image,LoginActivity.this);
                    SharedPref.setPreference(SharedPref.WALLET,wallet,LoginActivity.this);
                    SharedPref.setPreference(SharedPref.SEARCH_RADIUS,searchRadius,LoginActivity.this);

                    SharedPref.setPreference(FIRST_LAUNCH,hasAddress,LoginActivity.this);

                    progress_login.setVisibility(View.GONE);

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_login.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Invalid mobile or password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                progress_login.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Invalid mobile or password", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
