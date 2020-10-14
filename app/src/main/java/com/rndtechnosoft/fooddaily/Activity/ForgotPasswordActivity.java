package com.rndtechnosoft.fooddaily.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordActivity extends AppCompatActivity {

    public Toolbar toolbar;
    EditText tvmobile,tvotp;
    Button btnValidate,btnSubmit;
    private FirebaseAuth mAuth;
    String mVerificationId;
    RelativeLayout mobile_layout, otp_layout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    AVLoadingIndicatorView progressBar;
    CountryCodePicker ccp;
    private String selected_country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar_forget);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.forgot_password)+"</b>"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        phoneNumberAuthCallbackListener();

        tvmobile = findViewById(R.id.tvmobile);
        tvotp = findViewById(R.id.tvotp);
        btnValidate = findViewById(R.id.btnValidate);
        btnSubmit = findViewById(R.id.btnSubmit);
        mobile_layout = findViewById(R.id.mobile_layout);
        otp_layout = findViewById(R.id.otp_layout);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        ccp = findViewById(R.id.ccp);

        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(ForgotPasswordActivity.this).equalsIgnoreCase("1")) {
            gd.setColor(getResources().getColor(R.color.colorGrey));
            gd.setCornerRadius(25);
       // gd.setStroke(2, Color.RED);
            gd.setStroke(4, getResources().getColor(R.color.colorGrey1), 12, 16);
        }else{
            gd.setColor(getResources().getColor(R.color.colorGrey));
            gd.setCornerRadius(25);
            gd.setStroke(4, getResources().getColor(R.color.colorGrey1));
        }
        tvmobile.setBackground(gd);
        ccp.setBackground(gd);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = checkPhone();
                if (flag) {
                    String mobile = tvmobile.getText().toString();
                    verify_mobile(mobile);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvotp.setError(null);
                if (tvotp.length() == 0) {

                    tvotp.setError(getString(R.string.please_enter_otp));

                    return;
                }
                if(progressBar.getVisibility()==View.GONE)
                    progressBar.setVisibility(View.VISIBLE);

                verifyPhoneNumberWithCode(mVerificationId, tvotp.getText().toString().trim());
            }
        });

        ccp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCountryPickerClick(v);
                selected_country_code=ccp.getSelectedCountryCode();
            }
        });
    }

    public Boolean checkPhone() {

        String mobile = tvmobile.getText().toString();
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

    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = ccp.getSelectedCountryCode();
                Log.d("Country Code", selected_country_code);
            }
        });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(progressBar.getVisibility() == View.VISIBLE)
                            progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");

                            tvotp.setText("");
                            Toast.makeText(ForgotPasswordActivity.this,"Verified successfully",Toast.LENGTH_LONG).show();

                            openChangePwdActivity();
                        } else {
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(ForgotPasswordActivity.this,"Verification failed!Please enter correct otp",Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }

    public void phoneNumberAuthCallbackListener()
    {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:" + credential);
                if(progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                tvotp.setText("");

                openChangePwdActivity();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("TAG", "onVerificationFailed", e);
                if(progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(ForgotPasswordActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(ForgotPasswordActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    return;

                }
                Toast.makeText(ForgotPasswordActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                if(progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                Log.d("TAG", "onCodeSent:" + verificationId);
                Toast.makeText(ForgotPasswordActivity.this,"Otp has been sent to your mobile number",Toast.LENGTH_LONG).show();
                // Save verification ID and resending token so we can use them later

                mVerificationId = verificationId;
                mobile_layout.setVisibility(View.GONE);
                otp_layout.setVisibility(View.VISIBLE);
            }
        };
    }

    private void openChangePwdActivity() {

        Intent intent = new Intent(this,ChangePasswordActivity.class);
        intent.putExtra(Constants.MOBILE,tvmobile.getText().toString());
        startActivity(intent);
        finish();
    }

    private void verify_mobile(final String mobile) {
        progressBar.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(ForgotPasswordActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.verify_mobile_no+"="+mobile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject object = jsonObject.getJSONObject("VERIFY_MOBILE_NUMBER");

                    if(object.getString("error").equals("true"))
                    {
                        String message = object.getString("message");
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPasswordActivity.this,message,Toast.LENGTH_LONG).show();

                        return;
                    }
                    else
                    {
                        //send code
                        if(progressBar.getVisibility()==View.GONE)
                            progressBar.setVisibility(View.VISIBLE);
//                        String noWithCode = "+91"+mobile;
                        selected_country_code = ccp.getSelectedCountryCodeWithPlus();
                        String noWithCode = selected_country_code+mobile;
                        startPhoneNumberVerification(noWithCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
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

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                ForgotPasswordActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
