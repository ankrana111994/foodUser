package com.rndtechnosoft.fooddaily.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rndtechnosoft.fooddaily.Adapter.ViewPagerAdapter;
import com.rndtechnosoft.fooddaily.Fragment.HomeFragment;
import com.rndtechnosoft.fooddaily.Fragment.LoginFragment;
import com.rndtechnosoft.fooddaily.Fragment.OrderFragment;
import com.rndtechnosoft.fooddaily.Fragment.ProfileFragment;
import com.rndtechnosoft.fooddaily.Model.AboutUsList;
import com.rndtechnosoft.fooddaily.Model.AreaList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.Method;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.rndtechnosoft.fooddaily.Util.Constants.MY_PREFS_NAME;
import static com.rndtechnosoft.fooddaily.Util.Constants.refresh_flag;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    private DrawerLayout drawer;
    public ViewPager viewPager;
    public ViewPagerAdapter adapter;
    public static BottomNavigationView bottomNavigationMenu;
    MenuItem prevMenuItem;
    private NavigationView navigationView;
    TextView tvToolbarTitle;
    String version;
    private TextView textView_appDevlopBy,tvCartCount;
    private ImageView imgNotification,imgCart;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    public final static int ALL_PERMISSIONS_RESULT = 102;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean canGetLocation = true;
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  checkPer();
//        Places.initialize(getApplicationContext(), Constants.APIKEY);
        initView();

        if (getIntent().hasExtra(Constants.type)) {
            if (getIntent().getStringExtra(Constants.type).equals(Constants.order))
                type = Constants.order;
            if (getIntent().getStringExtra(Constants.type).equals(Constants.normal))
                type = Constants.normal;
        }
    }

    public void checkPer() {
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
       // permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        // check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
                //Log.d(TAG, "Permission requests");
                canGetLocation = false;
            }
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                try {
                    //Log.d(TAG, "onRequestPermissionsResult");
                    for (String perms : permissionsToRequest) {
                        if (!hasPermission(perms)) {
                            permissionsRejected.add(perms);
                        }
                    }

                    if (permissionsRejected.size() > 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                                showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(permissionsRejected.toArray(
                                                            new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
                break;
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }
                if (!canUseExternalStorage) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cannot_use_save_permission), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("cancel", null)
                .create()
                .show();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        textView_appDevlopBy = (TextView) findViewById(R.id.textView_app_developed_by);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        bottomNavigationMenu = (BottomNavigationView) findViewById(R.id.navigation);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        imgNotification = (ImageView) findViewById(R.id.imgNotification);
        imgCart = (ImageView) findViewById(R.id.imgCart);
        tvCartCount = (TextView) findViewById(R.id.tvCartCount);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        setSupportActionBar(toolbar);
        tvToolbarTitle.setText(getResources().getString(R.string.home));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_dehaze_24));
        bottomNavigationMenu .setItemIconTintList(null);
        bottomNavigationMenu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setNavigationItemSelectedListener(this);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(0);
        findViewById(R.id.toolbar_logo).setVisibility(View.VISIBLE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationMenu.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationMenu.getMenu().getItem(position).setChecked(true);


                prevMenuItem = bottomNavigationMenu.getMenu().getItem(position);

                switch (position) {
                    case 0:
                        Constants.refresh_flag=false;
                        tvToolbarTitle.setText(getResources().getString(R.string.home));
                        findViewById(R.id.toolbar_logo).setVisibility(View.VISIBLE);
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.home));
                        break;
                    case 1:
                        Constants.refresh_flag=false;
                        tvToolbarTitle.setText(getResources().getString(R.string.myorders));
                        tvToolbarTitle.setVisibility(View.VISIBLE);
                        findViewById(R.id.toolbar_logo).setVisibility(View.GONE);
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.myorders));
                        break;
                    case 2:
                        Constants.refresh_flag=false;
                        tvToolbarTitle.setText(getResources().getString(R.string.profile));
                        tvToolbarTitle.setVisibility(View.VISIBLE);
                        findViewById(R.id.toolbar_logo).setVisibility(View.GONE);
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.profile));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NotificationActivity.class));
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });

        if (type.equals("order")) {
            viewPager.setCurrentItem(1);
            getIntent().removeExtra(Constants.type);
        }
        else if(type.equals("Normal")) {
            viewPager.setCurrentItem(0);
            getIntent().removeExtra(Constants.type);
        }
        else {
            viewPager.setCurrentItem(0);
            getIntent().removeExtra(Constants.type);
        }

        loadAppDetail();
    }

    private void getConfig() {
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.config, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("config");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String currency = jsonObject1.getString("currency");
//                        String paypal = jsonObject1.getString("paypal");
                        String dashed = jsonObject1.getString("dashed");
                        String app_status = jsonObject1.getString("app_status");

                        SharedPref.setPreference(SharedPref.CURRENCY,currency,MainActivity.this);
//                        SharedPref.setPreference(SharedPref.PAYPAL,paypal,MainActivity.this);
                        SharedPref.setPreference(SharedPref.DASHED,dashed,MainActivity.this);
                        SharedPref.setPreference(SharedPref.APP_STATUS,app_status,MainActivity.this);
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

    private void loadAppDetail() {
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.about_us, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", new String(response));
                String res = new String(response);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("APP_INFO");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String app_name = object.getString("app_name");
                        String app_logo = object.getString("app_logo");
                        String app_version = object.getString("app_version");
                        String app_author = object.getString("app_author");
                        String app_contact = object.getString("app_contact");
                        String app_email = object.getString("app_email");
                        final String app_website = object.getString("app_website");
                        String app_description = object.getString("app_description");
                        String app_developed_by = object.getString("app_developed_by");
                        String app_privacy_policy = object.getString("app_privacy_policy");
                        Constants.aboutUs = new AboutUsList(app_name, app_logo, app_version, app_author, app_contact, app_email, app_website, app_description, app_developed_by, app_privacy_policy);
                        textView_appDevlopBy.setText(Constants.aboutUs.getApp_developed_by());
                        textView_appDevlopBy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse(Constants.aboutUs.getApp_website());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });
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

    public void cartcount() {
        Method.getCartCount(MainActivity.this,tvCartCount);
        if (Integer.parseInt(Constants.CART_COUNT)>0){
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText(Constants.CART_COUNT);
        }else{
            tvCartCount.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Method.getCartCount(MainActivity.this,tvCartCount);
        getConfig();
        getAreaList();
        if (refresh_flag) {
            refresh_flag=false;
            setupViewPager(viewPager);
            viewPager.setCurrentItem(0);
        }
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            Constants.VERSION=version;
            Log.e("versioncode",version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!Constants.CART_COUNT.equals("") && Integer.parseInt(Constants.CART_COUNT)>0){
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText(Constants.CART_COUNT);
        }else{
            tvCartCount.setVisibility(View.GONE);
        }
        Method.sendRegistrationToServer(MainActivity.this, FirebaseInstanceId.getInstance().getToken(),Method.getAndroidID(MainActivity.this));

      if (SharedPref.getAppLaunchStatus(MainActivity.this).equalsIgnoreCase("true"))
          startActivity(new Intent(this,CreateAddressActivity.class).putExtra("type","add"));

    }

    private void getAreaList() {
        Constants.areaLists.clear();
        Constants.areaLists=new ArrayList<>();
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.area_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Constants.areaLists.clear();
                Constants.areaLists=new ArrayList<>();
                Constants.areaLists.add(new AreaList("0","Select Area","0"));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("AREA_LIST");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String name = jsonObject1.getString("area_name");
                        String delivery = jsonObject1.getString("delivery_amount");
                        Constants.areaLists.add(new AreaList(id,name,delivery));
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

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        HomeFragment fragmentHome = new HomeFragment();
        OrderFragment fragmentOrder = new OrderFragment();
        ProfileFragment fragmentProfile = new ProfileFragment();
        LoginFragment fragmentLogin = new LoginFragment();
        adapter.addFragment(fragmentHome);
        adapter.addFragment(fragmentOrder);
        if (SharedPref.getUserId(MainActivity.this)!=null && !SharedPref.getUserId(MainActivity.this).equals("")) {
            adapter.addFragment(fragmentProfile);
        }else{
            adapter.addFragment(fragmentLogin);
        }
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);
        drawer.closeDrawers();
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                //Home
                refresh_flag=false;
                viewPager.setCurrentItem(0);
                return true;

            case R.id.profile:
                //Your Profile
                refresh_flag=false;
                viewPager.setCurrentItem(2);
                return true;

            case R.id.myorder:
                //Your order listing
                refresh_flag=false;
                viewPager.setCurrentItem(1);
                return true;

            case R.id.contact_us:
                //Contact the app owner
                refresh_flag=false;
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                return true;

            case R.id.rate_app:
                //Rate app
                showSoonToast();

//                refresh_flag=false;
//                Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                // To count with Play market backstack, After pressing back button,
//                // to taken back to our application, we need to add following flags to intent.
//                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                try {
//                    startActivity(goToMarket);
//                } catch (ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
//                }
                return true;

            case R.id.share_app:
                //Share app
                showSoonToast();
//                refresh_flag=false;
//                try {
//                    Intent i = new Intent(Intent.ACTION_SEND);
//                    i.setType("text/plain");
//                    i.putExtra(Intent.EXTRA_SUBJECT, "My application menu_name");
//                    String sAux = "\n" + getResources().getString(R.string.Let_me_recommend_you_this_application) + "\n\n";
//                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName();
//                    i.putExtra(Intent.EXTRA_TEXT, sAux);
//                    startActivity(Intent.createChooser(i, "choose one"));
//                } catch (Exception e) {
//                    //e.toString();
//                }
                return true;

            case R.id.about:
                //About the app
                refresh_flag=false;
                startActivity(new Intent(MainActivity.this, AboutusActivity.class));
                return true;

            case R.id.privacy_policy:
                //Privacy Policy of the app
                refresh_flag=false;
                startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
                return true;
            case R.id.logout:
                //Logout Of the app
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(SharedPref.USER_MOBILE, "");
                editor.putString(SharedPref.USER_NAME, "");
                editor.apply();
                final String token = "0";
                Method.sendRegistrationToServer(MainActivity.this, token,Method.getAndroidID(MainActivity.this));
                //      SharedPref.clearAllPreferences(MainActivity.this);

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
                        params.put("user_id", SharedPref.getUserId(getApplicationContext()));
                        params.put("token", token);
                        params.put("mobileid", com.rndtechnosoft.fooddaily.Util.Method.getAndroidID(getApplicationContext()));
                        params.put("versioncode", Constants.VERSION);
                        return params;
                    }

                };
                //creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                //adding the string request to request queue
                requestQueue.add(stringRequest);


                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return true;
        }

    }
void showSoonToast(){
    Toast.makeText(MainActivity.this,"will be added once app launch",Toast.LENGTH_LONG).show();

}
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_shop:
                    refresh_flag=false;
                    viewPager.setCurrentItem(0);
                    try {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.home));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.action_orders:
                    refresh_flag=false;
                    viewPager.setCurrentItem(1);
                    try {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.myorders));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.action_profile:
                    refresh_flag=false;
                    viewPager.setCurrentItem(2);
                    try {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.profile));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    refresh_flag=false;
                    viewPager.setCurrentItem(0);
                    try {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.home));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
            }
            return false;
        }
    };

}
