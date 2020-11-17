package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Adapter.AddressListAdapter;
import com.rndtechnosoft.fooddaily.Model.Address;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private ArrayList<Address> addressArrayList;
    RecyclerView recyclerView;
    private AddressListAdapter addressAdapter;
    AVLoadingIndicatorView progresbar_address;
    TextView nodata;
    SwipeRefreshLayout swipeRefreshLayout;
    RadioGroup radioGroup;
    LinearLayout llnodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar = (Toolbar) findViewById(R.id.toolbar_address);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.manage_address)+"</b>"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipeRefreshLayout=findViewById(R.id.pullToRefresh);
        llnodata=findViewById(R.id.nodata);
        radioGroup=findViewById(R.id.radiogroup);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorWhite));
        int col = getResources().getColor(R.color.colorAccent);
        swipeRefreshLayout.setColorSchemeColors(col,col,col);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });
        nodata=findViewById(R.id.tvnodata);
        recyclerView=findViewById(R.id.listaddress);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        progresbar_address=findViewById(R.id.progresbar_address);
        addressArrayList=new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        newAddress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.add:
                 startActivity(new Intent(this,CreateAddressActivity.class).putExtra("type","add"));
//                Intent intent = new Intent(AddressActivity.this, SearchDeliveryLocationActivity.class);
//
//                startActivity(intent);
//                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newAddress() {
        progresbar_address.setVisibility(View.VISIBLE);
        llnodata.setVisibility(View.GONE);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.view_address+"&user_id="+ SharedPref.getUserId(AddressActivity.this), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("DELIVERY_ADDRESS_LIST");
                    addressArrayList.clear();
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Address address= new Address();

                        String error=jsonObject1.getString("error");
                        if (error.equals("false")) {
                            String id = jsonObject1.getString("id");
                            String user_id = jsonObject1.getString("user_id");
                            String flat_no = jsonObject1.getString("flat_no");
                            String building_name = jsonObject1.getString("building_name");
                            String landmark = jsonObject1.getString("landmark");
                            String city = jsonObject1.getString("city");
                            String pincode = jsonObject1.getString("pincode");
                            String name = jsonObject1.getString("name");
                            String email = jsonObject1.getString("email");
                            String mobile = jsonObject1.getString("mobile");
                            String add_type = jsonObject1.getString("address_type");
                            String area_name = jsonObject1.getString("area_name");
                            String delivery_amount = jsonObject1.getString("delivery_amount");

                            address.setId(id);
                            address.setUser_id(user_id);
                            address.setFlat_no(flat_no);
                            address.setBuilding_name(building_name);
                            address.setLandmark(landmark);
                            address.setCity(city);
                            address.setName(name);

                            address.setPincode(pincode);
                            address.setEmail(email);
                            address.setMobile(mobile);
                            address.setAddress_type(add_type);
                            address.setArea_name(area_name);
                            address.setDelivery(delivery_amount);

                            addressArrayList.add(address);
                        }else{
                            //Toast.makeText(AddressActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }


                    }
                    if(addressArrayList.size()!=0) {
                        setAdapter();
                        recyclerView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                        llnodata.setVisibility(View.GONE);
//                        addRadioButtons();
                    }else{
                        nodata.setVisibility(View.VISIBLE);
                        llnodata.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progresbar_address.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progresbar_address.setVisibility(View.GONE);
                    nodata.setVisibility(View.GONE);
                    llnodata.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(VideoDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                requestQueue.stop();
                progresbar_address.setVisibility(View.GONE);
                nodata.setVisibility(View.GONE);
                llnodata.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        requestQueue.add(stringRequest);

    }

    private void setAdapter() {
        addressAdapter = new AddressListAdapter(AddressActivity.this, addressArrayList);
        recyclerView.setAdapter(addressAdapter);
        progresbar_address.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

}
