package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intuz.addresspicker.LocationPickerActivity;
import com.rndtechnosoft.fooddaily.Model.Address;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rndtechnosoft.fooddaily.Util.SharedPref.FIRST_LAUNCH;

public class CreateAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Address> addressArrayList;
    private Address address;
    private Button btnAdd,btnMap;
    private TextView txtApartment, txtHouse, txtothers;
    private EditText edtname,edtmbl,edtemail,edtAddress,edtBuilding,edtlandmark,edtcity,edtpincode,edtPlot,edtFloor,edtBlock,edtFlat;
    private String type;
    private String id,user_id,flat_no,building_name,landmark,city,pincode,name,email,mbl,address_type,area_name;
    String params,url,area_id;
    Spinner spinner;
    private String fullname;
    int selected_area;
    private String phone;
    LinearLayout llApartmentView,llIndividualView;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    AVLoadingIndicatorView progress;
    double currentLatitude,currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);

        Bundle extras=getIntent().getExtras();
        if(extras == null) {
            type= null;
        } else {
            type= extras.getString("type");
            if (type.equals("edit")) {
                id = extras.getString("id");
                user_id = extras.getString("user_id");
                flat_no = extras.getString("flat_no");
                building_name = extras.getString("building_name");
                landmark = extras.getString("landmark");
                city = extras.getString("city");
                pincode = extras.getString("pincode");
                name = extras.getString("name");
                email = extras.getString("email");
                mbl = extras.getString("mbl");
                area_name = extras.getString("area_name");
                address_type = extras.getString("address_type");
            }
        }

        toolbar=findViewById(R.id.toolbar_create);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        if (type.equals("edit")) {
            toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.editadd)+"</b>"));
        }else {
            toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.newadd)+"</b>"));
        }
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        addressArrayList=new ArrayList<>();

        Constants.area_name.clear();
        Constants.area_name = new ArrayList<>();
        for (int i=0; i<Constants.areaLists.size(); i++){
            Constants.area_name.add(Constants.areaLists.get(i).getName());
            if (area_name!=null && area_name.equals(Constants.areaLists.get(i).getName())){
                selected_area = i;
            }
        }

        spinner = findViewById(R.id.spinner_area);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.area_name);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(selected_area);
        edtname = findViewById(R.id.edtname);
        edtmbl = findViewById(R.id.edtmbl);
        edtemail = findViewById(R.id.edtemail);
        edtAddress = findViewById(R.id.edtAddress);
        edtBuilding = findViewById(R.id.edtBuilding);
        edtlandmark = findViewById(R.id.edtlandmark);
        edtPlot = findViewById(R.id.edtPlot);
        edtFloor = findViewById(R.id.edtFloor);
        edtBlock = findViewById(R.id.edtBlock);
        edtFlat = findViewById(R.id.edtFlat);

        llApartmentView = findViewById(R.id.llApartmentView);
        llIndividualView = findViewById(R.id.llIndividualView);



        edtcity = findViewById(R.id.edtcity);
        edtpincode = findViewById(R.id.edtpincode);
        btnAdd=findViewById(R.id.btnAdd);
        txtApartment=findViewById(R.id.txtApartment);
        txtHouse=findViewById(R.id.txtHouse);
        txtothers=findViewById(R.id.txtothers);
        progress=findViewById(R.id.progress);
        btnMap=findViewById(R.id.btnMap);
        progress.hide();




        if (type!=null && type.equals("edit")) {
            edtname.setText(name);
            edtmbl.setText(mbl);
            edtemail.setText(email);
            edtAddress.setText(flat_no);
            edtBuilding.setText(building_name);
            edtlandmark.setText(landmark);
            edtcity.setText(city);
            edtpincode.setText(pincode);
            if (address_type.equals("home")){
                txtApartment.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
                txtHouse.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtothers.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtApartment.setTextColor(getResources().getColor(R.color.colorWhite));
                txtHouse.setTextColor(getResources().getColor(R.color.colorBlack));
                txtothers.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type="home";
            }else if (address_type.equals("office")){
                txtHouse.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
                txtApartment.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtothers.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtHouse.setTextColor(getResources().getColor(R.color.colorWhite));
                txtApartment.setTextColor(getResources().getColor(R.color.colorBlack));
                txtothers.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type="office";
            }else{
                txtothers.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
                txtApartment.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtHouse.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtothers.setTextColor(getResources().getColor(R.color.colorWhite));
                txtApartment.setTextColor(getResources().getColor(R.color.colorBlack));
                txtHouse.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type="others";
            }
            btnAdd.setText("Update Address");
        }
      if (!type.equals("edit")){
          llApartmentView.setVisibility(View.GONE);
          llIndividualView.setVisibility(View.VISIBLE);
          txtHouse.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
          txtHouse.setTextColor(getResources().getColor(R.color.colorWhite));
          address_type="home";
      }

        txtApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llApartmentView.setVisibility(View.VISIBLE);
                llIndividualView.setVisibility(View.GONE);
                txtApartment.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
                txtHouse.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtothers.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtApartment.setTextColor(getResources().getColor(R.color.colorWhite));
                txtHouse.setTextColor(getResources().getColor(R.color.colorBlack));
                txtothers.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type="home";
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAddressActivity.this, LocationPickerActivity.class);
                startActivityForResult(intent, 102);
            }
        });
        txtHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llApartmentView.setVisibility(View.GONE);
                llIndividualView.setVisibility(View.VISIBLE);
                txtHouse.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
                txtApartment.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtothers.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtHouse.setTextColor(getResources().getColor(R.color.colorWhite));
                txtApartment.setTextColor(getResources().getColor(R.color.colorBlack));
                txtothers.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type="office";
            }
        });

        txtothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtothers.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
                txtApartment.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtHouse.setBackground(getResources().getDrawable(R.drawable.rounded_gray_border_btn));
                txtothers.setTextColor(getResources().getColor(R.color.colorWhite));
                txtApartment.setTextColor(getResources().getColor(R.color.colorBlack));
                txtHouse.setTextColor(getResources().getColor(R.color.colorBlack));
                address_type="others";
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtname.getText().toString();
                String mbl = edtmbl.getText().toString();
                String email = edtemail.getText().toString();
                String landmark = edtlandmark.getText().toString();
                String city = edtcity.getText().toString();
                String pincode = edtpincode.getText().toString();

                String address = edtAddress.getText().toString();
                String apartmentName=edtBuilding.getText().toString();
                String  blockNo=edtBlock.getText().toString();
                String floor=edtFloor.getText().toString();
                String  house_number;
                if (address_type.equalsIgnoreCase("home")){
                    house_number=edtPlot.getText().toString(); }
                else {
                    house_number=edtFlat.getText().toString(); }

//                newAddress(name, mbl, email, flat, building, landmark, city, pincode);
                if (!checkFullname()) {
                    Toast.makeText(CreateAddressActivity.this, "Please Enter full name", Toast.LENGTH_SHORT).show();

                    edtname.requestFocus();
                }
                else if (!checkPhone()){
                    Toast.makeText(CreateAddressActivity.this, "Please check phone number", Toast.LENGTH_SHORT).show();

                    edtmbl.requestFocus();
                }
                else if (!email.matches(emailPattern)) {
                    Toast.makeText(CreateAddressActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                    edtemail.requestFocus();
                }

                ///Apartment

                else if (!checkBuildingName(apartmentName)){
                    Toast.makeText(CreateAddressActivity.this, "Please check Apartment/Building name", Toast.LENGTH_SHORT).show();

                    edtBuilding.requestFocus();
                }
                else if (!checkBlockNumber(blockNo)){
                    Toast.makeText(CreateAddressActivity.this, "Please check Block number", Toast.LENGTH_SHORT).show();

                    edtBlock.requestFocus();
                }
                ///Apartment
                else if (!checkHouseNumber(house_number)){
                    Toast.makeText(CreateAddressActivity.this, "Please check house number", Toast.LENGTH_SHORT).show();
                    if (address_type.equalsIgnoreCase("home")){
                        Toast.makeText(CreateAddressActivity.this, "Please check house number", Toast.LENGTH_SHORT).show();
                        edtPlot.requestFocus();

                    }
                    else {
                        Toast.makeText(CreateAddressActivity.this, "Please check Flat number", Toast.LENGTH_SHORT).show();
                        edtFlat.requestFocus();

                    }
                }


//house
                else if (!checkFloor(floor)){
                    Toast.makeText(CreateAddressActivity.this, "Please check Floor number", Toast.LENGTH_SHORT).show();

                    edtFloor.requestFocus();
                }
//house



                else if (!checkCity()){
                    Toast.makeText(CreateAddressActivity.this, "Please enter your city", Toast.LENGTH_SHORT).show();

                    edtcity.requestFocus();
                }
                else if (!checkPincode()){
                    Toast.makeText(CreateAddressActivity.this, "Please check pin", Toast.LENGTH_SHORT).show();

                    edtpincode.requestFocus();
                }



                else {
                    newAddress(name, mbl, email, house_number, apartmentName,blockNo,floor,String.valueOf(currentLatitude),String.valueOf(currentLongitude),address,landmark, city, pincode);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area_id = Constants.areaLists.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (SharedPref.getAppLaunchStatus(CreateAddressActivity.this).equalsIgnoreCase("true")){
            Intent intent = new Intent(CreateAddressActivity.this, LocationPickerActivity.class);
            startActivityForResult(intent, 102);
            SharedPref.setPreference(FIRST_LAUNCH,"false",CreateAddressActivity.this);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102) {
            try {
                if (data != null && data.getStringExtra("address") != null) {
                    String address = data.getStringExtra("address");
                     currentLatitude = data.getDoubleExtra("lat", 0.0);
                     currentLongitude = data.getDoubleExtra("long", 0.0);
                    String city = data.getStringExtra("city");
                    String state = data.getStringExtra("state");
                    String postalcode = data.getStringExtra("postalcode");
                    String area = data.getStringExtra("area");


                    edtcity.setText(city);
                    edtpincode.setText(postalcode);
                    edtAddress.setText(address);
                    edtBuilding.setText(area);
                    edtAddress.setText(address);
                    Log.e("addrsss=====>",address);
                    // txtAddress.setText("Address: "+address);
                    //  txtLatLong.setText("Lat:"+currentLatitude+"  Long:"+currentLongitude);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public Boolean checkPhone() {

        phone = edtmbl.getText().toString();
        if (phone.length() == 0) {
            edtmbl.setError(getString(R.string.error_field_empty));
            return false;
        }
        if (phone.length()!=10){
            edtmbl.requestFocus();
            Toast.makeText(this, "Phone number not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        edtmbl.setError(null);

        return true;
    }

    public Boolean checkPincode() {

        pincode = edtpincode.getText().toString();
        if (pincode.length() == 0) {
            edtpincode.setError(getString(R.string.error_field_empty));
            return false;
        }
        if (pincode.length()!=6){
            edtpincode.requestFocus();
            Toast.makeText(this, "Pincode not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        edtpincode.setError(null);

        return true;
    }
    public Boolean checkCity() {

        city = edtcity.getText().toString();
        if (city.length() == 0) {
            edtcity.setError(getString(R.string.error_field_empty));
            return false;
        }

        edtcity.setError(null);

        return true;
    }
    public Boolean checkBuildingName(String apartmentName) {
        apartmentName = edtBuilding.getText().toString();
        if (!address_type.equalsIgnoreCase("home")) {

            if (apartmentName.length() == 0) {
                edtBuilding.setError(getString(R.string.error_field_empty));
                return false;
            }
            else {
                edtBuilding.setError(null);
                return  true;
            }
        }
        edtBuilding.setError(null);
        return  true;
    }
    public Boolean checkBlockNumber(String BlockNumber) {
        BlockNumber = edtBlock.getText().toString();
        if (!address_type.equalsIgnoreCase("home")) {

            if (BlockNumber.length() == 0) {
            edtBlock.setError(getString(R.string.error_field_empty));
            return false;
        }else {
                edtBlock.setError(null);
                return  true;
            }
        }

        edtBlock.setError(null);
        return  true;
    }
    public Boolean checkFloor(String floor) {
        floor = edtFloor.getText().toString();
        if (address_type.equalsIgnoreCase("home")) {

            if (floor.length() == 0) {
                edtFloor.setError(getString(R.string.error_field_empty));
                return false;
            }
            else {
                edtFloor.setError(null);
                return  true;
            }
        }
        edtFloor.setError(null);
        return  true;
    }

    public Boolean checkHouseNumber(String houseNumber) {
       // houseNumber = edtname.getText().toString();
        if (houseNumber.length() == 0) {
            if (address_type.equalsIgnoreCase("home")){
                edtPlot.setError(getString(R.string.error_field_empty));

                }
            else {
                edtFlat.setError(getString(R.string.error_field_empty));
            }

            return false;
        }

        if (address_type.equalsIgnoreCase("home")){
            edtPlot.setError(null);

        }
        else {
            edtFlat.setError(null);
        }
        return  true;
    }

    public Boolean checkFullname() {
        fullname = edtname.getText().toString();
        if (fullname.length() == 0) {
            edtname.setError(getString(R.string.error_field_empty));
            return false;
        }
        if (fullname.length() < 2) {
            edtname.setError(getString(R.string.enter_your_name));
            return false;
        }
        edtname.setError(null);
        return  true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void newAddress(String name, String mbl, String email, String flat, String building,String blockNo,String floor,String lati,String longi,String addressComplete, String landmark, String city, String pincode) {
        progress.show();
        if (type!=null && type.equals("edit")) {
            params = "&id=" + id + "&user_id=" + SharedPref.getUserId(CreateAddressActivity.this) + "&area=" + area_id + "&flat_no=" + flat + "&building_name=" + building + "&floor=" + floor+ "&blockNo=" + blockNo+ "&landmark=" + landmark + "&city=" + city + "&pincode=" + pincode + "&latitude=" + lati +  "&longitude=" + longi +  "&addressComplete=" + addressComplete +"&name=" + name + "&email=" + email + "&mobile=" + mbl + "&address_type=" + address_type;
            url= Constants.edit_address;
        }
        else {
            params = "&user_id=" + SharedPref.getUserId(CreateAddressActivity.this) + "&area=" + area_id + "&flat_no=" + flat + "&building_name=" + building + "&floor=" + floor+ "&blockNo=" + blockNo+ "&landmark=" + landmark + "&city=" + city + "&pincode=" + pincode + "&lat=" + lati +  "&long=" + longi +  "&addressComplete=" + addressComplete + "&name=" + name + "&email=" + email + "&mobile=" + mbl + "&address_type=" + address_type;
            url=Constants.new_address;
        }
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url +params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("DELIVERY_ADDRESS");

                    address= new Address();

                    String error = jsonObject1.getString("error");
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
                        String type = jsonObject1.getString("address_type");

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
                        address.setAddress_type(type);

                        progress.hide();
                        addressArrayList.add(address);
                        onBackPressed();
                    }else{
                        progress.hide();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.hide();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(VideoDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                requestQueue.stop();
                progress.hide();
            }
        });
        requestQueue.add(stringRequest);

    }

}