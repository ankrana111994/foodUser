    package com.rndtechnosoft.fooddaily.Activity;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.PorterDuff;
    import android.graphics.Typeface;
    import android.os.Bundle;
    import android.text.Html;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.RelativeLayout;
    import android.widget.ScrollView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.airbnb.lottie.LottieAnimationView;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;
    import com.bumptech.glide.Glide;
    import com.rndtechnosoft.fooddaily.Adapter.VariantMenuDetailAdapter;
    import com.rndtechnosoft.fooddaily.Model.MenuList;
    import com.rndtechnosoft.fooddaily.Model.SubMenuList;
    import com.rndtechnosoft.fooddaily.Model.VariantList;
    import com.rndtechnosoft.fooddaily.R;
    import com.rndtechnosoft.fooddaily.Util.Constants;
    import com.rndtechnosoft.fooddaily.Util.Method;
    import com.rndtechnosoft.fooddaily.Util.SharedPref;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;

    import at.blogc.android.views.ExpandableTextView;

    public class MenuDetailActivity extends AppCompatActivity {

        String menu_id, menu_name, menu_image;
        ImageView itemImage,foodType,decrease,increase;
        TextView tvToolbarTitle,tvCartCount,tvMenu,tvPrice,tvTotalPrice,tvAddtoCart,count;
        TextView button_toggle,tvDescr;
        ExpandableTextView tvDesc;
        Toolbar toolbar;
        RelativeLayout btnGotoCart,r_addcart;
        String id, name, image, food_type_icon, des, cat_id="";
        ArrayList<VariantList> variantLists;
        private MenuList menuList;
        private ImageView imgNotification,imgCart;
        private String cart_status,variant_qty,variant_id,volume,price;
        Button btnContinue;
        LottieAnimationView loader;
        ScrollView scroll_lay;
        private String food_opening_time, food_closing_time, food_time_msg;
        String endtime ="";
        String starttime ="";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_detail);

            Intent in = getIntent();
            menu_id = in.getStringExtra("menu_id");
            menu_name = in.getStringExtra("menu_name");
            menu_image = in.getStringExtra("menu_image");
            cat_id = in.getStringExtra("cat_id");

            if (cat_id==null){
                cat_id="";
            }

            toolbar =(Toolbar) findViewById(R.id.toolbar_main);
            tvToolbarTitle =(TextView) findViewById(R.id.tvToolbarTitle);
            tvAddtoCart =(TextView) findViewById(R.id.tvAddtoCart);
            count =(TextView) findViewById(R.id.count);
            tvCartCount =(TextView) findViewById(R.id.tvCartCount);
            btnContinue =(Button) findViewById(R.id.btnContinue);
            loader = findViewById(R.id.loader);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
            tvToolbarTitle.setText(Html.fromHtml("<b>"+"Detail"+"</b>"));
            setSupportActionBar(toolbar);
            findViewById(R.id.toolbar_logo).setVisibility(View.GONE);
            tvToolbarTitle.setVisibility(View.VISIBLE);

            scroll_lay = findViewById(R.id.scroll_lay);
            itemImage = findViewById(R.id.itemImage);
            foodType = findViewById(R.id.foodType);
            tvMenu = findViewById(R.id.tvMenu);
            tvPrice = findViewById(R.id.tvPrice);
            tvDesc = findViewById(R.id.tvDesc);
            tvTotalPrice = findViewById(R.id.tvTotalPrice);
            btnGotoCart=findViewById(R.id.btnGotoCart);
            r_addcart=findViewById(R.id.r_addcart);
            btnGotoCart.setVisibility(View.GONE);
            imgCart = (ImageView) findViewById(R.id.imgCart);
            imgNotification = (ImageView) findViewById(R.id.imgNotification);
            decrease = (ImageView) findViewById(R.id.decrease);
            increase = (ImageView) findViewById(R.id.increase);
            button_toggle =(TextView) findViewById(R.id.button_toggle);
            tvDescr =(TextView) findViewById(R.id.tvDescr);
          //  button_toggle.setVisibility(View.VISIBLE);
            scroll_lay.setVisibility(View.GONE);

            variantLists = new ArrayList<>();

            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuDetailActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    finish();
                }
            });

            tvMenu.setText(menu_name);
            Glide.with(MenuDetailActivity.this).load(menu_image).thumbnail(Glide.with(this).load(R.drawable.loading)).into(itemImage);

            btnGotoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuDetailActivity.this,CartActivity.class));
                }
            });

            tvAddtoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!food_opening_time.equalsIgnoreCase("") && !food_opening_time.equalsIgnoreCase("0") && food_opening_time!=null) {
                        try {
                            Date time1 = new SimpleDateFormat("HH:mm").parse(food_opening_time);
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(time1);
                            calendar1.add(Calendar.DATE, 1);

                            Date time2 = new SimpleDateFormat("HH:mm").parse(food_closing_time);
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTime(time2);
                            calendar2.add(Calendar.DATE, 1);

                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String str = sdf.format(new Date());
                            Date d = new SimpleDateFormat("HH:mm").parse(str);
                            Calendar calendar3 = Calendar.getInstance();
                            calendar3.setTime(d);
                            calendar3.add(Calendar.DATE, 1);

                            Log.d("string1->>", time1 + "");
                            Log.d("string2->>", time2 + "");
                            Log.d("date->>", d + "");
                            Date x = calendar3.getTime();
                            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                                //checkes whether the current time is between 14:49:00 and 20:11:13.
                                //Add to cart
                                r_addcart.setVisibility(View.VISIBLE);
                                tvAddtoCart.setVisibility(View.GONE);
                                increase.performClick();
                            } else {
                                //Toast.makeText(activity, "Delivery not available", Toast.LENGTH_SHORT).show();
                                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                Date _24HourStart = _24HourSDF.parse(food_opening_time);
                                Date _24HourEnd = _24HourSDF.parse(food_closing_time);
                                starttime=_12HourSDF.format(_24HourStart);
                                endtime=_12HourSDF.format(_24HourEnd);
                                String msg =  String.format(food_time_msg, menu_name, starttime+" - "+endtime);
                                AlertDialog dialog=new AlertDialog.Builder(MenuDetailActivity.this).setTitle(R.string.app_name).setMessage(msg)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();
                                TextView textView=dialog.findViewById(android.R.id.message);
                                Typeface face= null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    face = getResources()
                                            .getFont(R.font.calibri_regular);
                                }
                                textView.setTypeface(face);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //Add to cart
                        r_addcart.setVisibility(View.VISIBLE);
                        tvAddtoCart.setVisibility(View.GONE);
                        increase.performClick();
                    }
                }
            });

            increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
    //                String start_time = menuLists.get(position).getFood_opening_time();
    //                String end_time = menuLists.get(position).getFood_closing_time();
                    if (!food_opening_time.equalsIgnoreCase("") && !food_opening_time.equalsIgnoreCase("0") && food_opening_time!=null) {
                        try {
                            Date time1 = new SimpleDateFormat("HH:mm").parse(food_opening_time);
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(time1);
                            calendar1.add(Calendar.DATE, 1);

                            Date time2 = new SimpleDateFormat("HH:mm").parse(food_closing_time);
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTime(time2);
                            calendar2.add(Calendar.DATE, 1);

                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String str = sdf.format(new Date());
                            Date d = new SimpleDateFormat("HH:mm").parse(str);
                            Calendar calendar3 = Calendar.getInstance();
                            calendar3.setTime(d);
                            calendar3.add(Calendar.DATE, 1);

                            Log.d("string1->>", time1 + "");
                            Log.d("string2->>", time2 + "");
                            Log.d("date->>", d + "");
                            Date x = calendar3.getTime();
                            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                                //checkes whether the current time is between 14:49:00 and 20:11:13.
                                //Add to cart
                                int item_count = Integer.parseInt(count.getText().toString());
                                item_count=item_count+1;
                                count.setText(String.valueOf(item_count));
                                addtocart(cat_id,id,name,image,variant_id,volume,price,Constants.INCREMENT);
                            } else {
                                //Toast.makeText(activity, "Delivery not available", Toast.LENGTH_SHORT).show();
                                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                Date _24HourStart = _24HourSDF.parse(food_opening_time);
                                Date _24HourEnd = _24HourSDF.parse(food_closing_time);
                                starttime=_12HourSDF.format(_24HourStart);
                                endtime=_12HourSDF.format(_24HourEnd);
                                String msg =  String.format(food_time_msg, menu_name, starttime+" - "+endtime);
                                AlertDialog dialog=new AlertDialog.Builder(MenuDetailActivity.this).setTitle(R.string.app_name).setMessage(msg)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();
                                TextView textView=dialog.findViewById(android.R.id.message);
                                Typeface face= null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    face = getResources()
                                            .getFont(R.font.calibri_regular);
                                }
                                textView.setTypeface(face);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //Add to cart
                        int item_count = Integer.parseInt(count.getText().toString());
                        item_count=item_count+1;
                        count.setText(String.valueOf(item_count));
                        addtocart(cat_id,id,name,image,variant_id,volume,price,Constants.INCREMENT);
                    }

                }
            });

            decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item_count = Integer.parseInt(count.getText().toString());
                    if (item_count!=0) {
                        item_count = item_count - 1;
                        count.setText(String.valueOf(item_count));
                        addtocart(cat_id, id, name, image, variant_id, volume, price, Constants.DECREMENT);
                    }
                }
            });

    //        button_toggle.setOnClickListener(new View.OnClickListener()
    //        {
    //            @Override
    //            public void onClick(final View v)
    //            {
    //                if (tvDesc.isExpanded())
    //                {
    //                    tvDesc.collapse();
    //                    button_toggle.setText("more..");
    //                }
    //                else
    //                {
    //                    tvDesc.expand();
    //                    button_toggle.setText("less..");
    //                }
    //            }
    //        });

            imgNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuDetailActivity.this,NotificationActivity.class));
                }
            });

            imgCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuDetailActivity.this,CartActivity.class));
                }
            });
        }

        private void addtocart(final String cat_id, final String id, final String name, final String image, final String variant_id, final String volume, final String price, final String operation) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.add_cart,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Response
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("MANAGE_CART");
                                String message = jsonObject1.getString("message");
                                cartcount();
    //                            onResume();
                                Toast.makeText(MenuDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Error
                            Log.d("AddToCart->",error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", SharedPref.getUserId(MenuDetailActivity.this));
                    params.put("category_id", cat_id);
                    params.put("menu_id", id);
                    params.put("menu_name", name);
                    params.put("menu_image", image);
                    params.put("variant_id", variant_id);
                    params.put("variant_type", volume);
                    params.put("variant_price", price);
                    params.put("variant_qty", Constants.QUANTITY);
                    params.put("operation", operation);
                    return params;
                }
            };
            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(MenuDetailActivity.this);
            //adding the string request to request queue
            requestQueue.add(stringRequest);

        }

        @Override
        protected void onResume() {
            super.onResume();
            cartcount();
            menuList=null;
            variantLists = new ArrayList<>();
            getMenuDetail();
        }

        private void getMenuDetail() {
            loader.setVisibility(View.VISIBLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(MenuDetailActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.menu_detail+"&menu_id="+menu_id+"&user_id="+SharedPref.getUserId(MenuDetailActivity.this), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("MAIN_MENU_LIST");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            id = object.getString("id");
                            name = object.getString("name");
                            image = object.getString("image");
                            food_type_icon = object.getString("food_type_icon");
                            des = object.getString("des");
                            food_opening_time = object.getString("food_opening_time");
                            food_closing_time = object.getString("food_closing_time");
                            food_time_msg = object.getString("food_time_msg");
                            JSONArray jsonVariant = object.getJSONArray("variant_list");
                            for (int j=0; j<jsonVariant.length();j++){
                                JSONObject object1 = jsonVariant.getJSONObject(j);
                                variant_id = object1.getString("id");
                                volume = object1.getString("volume");
                                price = object1.getString("price");
                                cart_status = object1.getString("cart_status");
                                variant_qty = object1.getString("variant_qty");
                                ArrayList<SubMenuList> subMenuLists=new ArrayList<>();
    //                            JSONArray jsonSubmenu = object1.getJSONArray("sub_menu_list");
    //                            for (int k=0; k<jsonSubmenu.length();k++){
    //                                JSONObject object12 = jsonSubmenu.getJSONObject(k);
    //                                if (object12.getString("error").equals("False")) {
    //                                    String sub_id = object12.getString("id");
    //                                    String sub_menu = object12.getString("sub_menu");
    //                                    String pricesub = object12.getString("price");
    //                                    String status = object12.getString("topping_status");
    //                                    subMenuLists.add(new SubMenuList(sub_id, sub_menu, pricesub, status));
    //                                }
    //                            }
    //                            variantLists.add(new VariantList(variant_id,volume,price,cart_status,variant_qty,subMenuLists));
                            }

                            menuList=new MenuList(id,cat_id,name,image,food_type_icon,des,food_opening_time,food_closing_time,food_time_msg,variantLists);
                        }
                        int numberOfColumns = variantLists.size();
    //                    recycler_item.setLayoutManager(new GridLayoutManager(MenuDetailActivity.this,numberOfColumns));
    //                    if (subMenuLists.size()>0) {
    //                        recycler_item.setAdapter(new VariantMenuDetailAdapter(MenuDetailActivity.this, menuList, variantLists, sublayout, topping_recycler));
    //                    }else{
    //                        recycler_item.setAdapter(new VariantMenuDetailAdapter(MenuDetailActivity.this, menuList, variantLists, sublayout, topping_recycler, "false"));
    //                    }


                        tvMenu.setText(name);
                        if (des!=null && !des.equals("")) {
                            tvDesc.setText(Html.fromHtml(des));
                            tvDesc.expand();

                        }else{
                            tvDescr.setVisibility(View.GONE);
                            tvDesc.setVisibility(View.GONE);
                            button_toggle.setVisibility(View.GONE);
                        }
                        Glide.with(MenuDetailActivity.this).load(image).thumbnail(Glide.with(MenuDetailActivity.this).load(R.drawable.loading)).into(itemImage);
                        Glide.with(MenuDetailActivity.this).load(food_type_icon).thumbnail(Glide.with(MenuDetailActivity.this).load(R.drawable.loading)).into(foodType);
    if (des!=null) {
        if (des.length() < 250) {
    //                        tvDescr.setVisibility(View.GONE);
    //                        tvDesc.setVisibility(View.GONE);
            button_toggle.setVisibility(View.GONE);
        }
    }    if (cart_status!=null) {

                            if (cart_status.equalsIgnoreCase("false")) {
                                count.setText("0");
                                r_addcart.setVisibility(View.GONE);
                                tvAddtoCart.setVisibility(View.VISIBLE);
                            } else {
                                r_addcart.setVisibility(View.VISIBLE);
                                tvAddtoCart.setVisibility(View.GONE);
                                count.setText(variant_qty);
                            }
                        }
                        tvPrice.setText(SharedPref.getCurrency(MenuDetailActivity.this)+price);
                        loader.setVisibility(View.GONE);
                        scroll_lay.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        loader.setVisibility(View.GONE);
                        scroll_lay.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    requestQueue.stop();
                    scroll_lay.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                }
            });
            requestQueue.add(stringRequest);
        }

        public void cartcount() {
    //        Method.getTotalPrice(MenuDetailActivity.this,tvTotalPrice,btnGotoCart);
            Method.getCartCount(MenuDetailActivity.this,tvCartCount);
            if (Integer.parseInt(Constants.CART_COUNT)>0){
                tvCartCount.setVisibility(View.VISIBLE);
                tvCartCount.setText(Constants.CART_COUNT);
            }else{
                tvCartCount.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onSupportNavigateUp() {
            onBackPressed();
            return super.onSupportNavigateUp();
        }
    }
