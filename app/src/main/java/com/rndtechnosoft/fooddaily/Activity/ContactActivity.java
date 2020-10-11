package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.rndtechnosoft.fooddaily.Util.Method;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactActivity extends AppCompatActivity {

    private EditText editText_name, editText_email, editText_phoneNumber, editText_subject, editText_description;
    private Button button_submit;
    private String name, email, phoneNumber, subject, description;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbar_contact);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.contact_us)+"</b>"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editText_name = (EditText) findViewById(R.id.textView_name_contactUS_fragment);
        editText_email = (EditText) findViewById(R.id.textView_email_contactUS_fragment);
        editText_phoneNumber = (EditText) findViewById(R.id.textView_phoneNo_contactUS_fragment);
        editText_subject = (EditText) findViewById(R.id.textView_subject_contactUS_fragment);
        editText_description = (EditText) findViewById(R.id.textView_description_contactUS_fragment);
        button_submit = (Button) findViewById(R.id.button_contactus_fragment);


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Method.haveNetworkConnection(ContactActivity.this)) {

                    name = SharedPref.getUserName(ContactActivity.this);
                    email = SharedPref.getUserEmail(ContactActivity.this);
                    phoneNumber = SharedPref.getMobileNumber(ContactActivity.this);
                    subject = editText_subject.getText().toString();
                    description = editText_description.getText().toString();

                    form();

                } else {
                    Toast.makeText(ContactActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void contactUs() {

        String url = Constants.contact_us + "&name=" + name + "&email=" + email + "&phone=" + phoneNumber + "&subject=" + subject + "&message=" + description;

        final RequestQueue requestQueue = Volley.newRequestQueue(ContactActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("CONTACT");

                    for (int i = 0; i <= jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String msg = object.getString("msg");
                        String success = object.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(ContactActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ContactActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void form() {

        editText_name.setError(null);
        editText_email.setError(null);
        editText_phoneNumber.setError(null);
        editText_subject.setError(null);
        editText_description.setError(null);

       /* if (name.isEmpty() || name.equals("")) {
            editText_name.requestFocus();
            editText_name.setError(getResources().getString(R.string.name_ContactUs));
        } else if (!isValidMail(email) || email.isEmpty()) {
            editText_email.requestFocus();
            editText_email.setError(getResources().getString(R.string.email_ContactUs));
        } else if (phoneNumber.isEmpty()) {
            editText_phoneNumber.requestFocus();
            editText_phoneNumber.setError(getResources().getString(R.string.phoneNumber_contactUs));
        } else*/ if (subject.isEmpty() || subject.equals("")) {
            editText_subject.requestFocus();
            editText_subject.setError(getResources().getString(R.string.subject_ContactUs));
        } else if (description.isEmpty() || description.equals("")) {
            editText_description.requestFocus();
            editText_description.setError(getResources().getString(R.string.description_ContactUs));
        } else {
//            editText_name.setText("");
//            editText_email.setText("");
//            editText_phoneNumber.setText("");
            editText_subject.setText("");
            editText_description.setText("");
            contactUs();
        }
    }
}
