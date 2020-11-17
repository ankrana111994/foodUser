package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Adapter.CommonAdapter;
import com.rndtechnosoft.fooddaily.Model.ItemNotification;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    public Toolbar toolbar;
    RecyclerView recycler_notification;
    AVLoadingIndicatorView progress_notification;
    CommonAdapter adapter;
    ArrayList<ItemNotification> mListItem;
    TextView tvNodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = (Toolbar) findViewById(R.id.toolbar_notification);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.notification)+"</b>"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progress_notification = findViewById(R.id.progress_notification);
        tvNodata = findViewById(R.id.tvNodata);
        recycler_notification = findViewById(R.id.recycler_notification);
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(NotificationActivity.this, DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(NotificationActivity.this, R.drawable.dashed_line));
        recycler_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotificationList();
    }

    private void getNotificationList() {
        progress_notification.setVisibility(View.VISIBLE);
        tvNodata.setVisibility(View.GONE);
        final RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.notification_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mListItem=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("NOTIFICATION_LIST");

                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String title = jsonObject1.getString("notification_title");
                        String msg = jsonObject1.getString("notification_msg");
                        String image = jsonObject1.getString("notification_image");
                        mListItem.add(new ItemNotification(id,title,msg,image));
                    }

                    adapter = new CommonAdapter(NotificationActivity.this, mListItem);
                    recycler_notification.setAdapter(adapter);
                    progress_notification.setVisibility(View.GONE);
                    tvNodata.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_notification.setVisibility(View.GONE);
                    tvNodata.setVisibility(View.VISIBLE);
                    recycler_notification.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();
                progress_notification.setVisibility(View.GONE);
                tvNodata.setVisibility(View.VISIBLE);
                recycler_notification.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userid", SharedPref.getUserId(NotificationActivity.this));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
