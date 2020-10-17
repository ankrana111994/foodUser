package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.MainActivity;
import com.rndtechnosoft.fooddaily.Activity.MenuDetailActivity;
import com.rndtechnosoft.fooddaily.Fragment.HomeFragment;
import com.rndtechnosoft.fooddaily.Model.HomeMenuList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

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

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.ViewHolder> {

    private Activity activity;
    ArrayList<HomeMenuList> homeMenuLists;
    String cat_id;
    HomeFragment fragment;
    String endtime ="";
    String starttime ="";

    public HomeMenuAdapter(Activity activity, ArrayList<HomeMenuList> homeMenuLists, String cat_id, HomeFragment fragment) {
        this.activity = activity;
        this.homeMenuLists = homeMenuLists;
        this.cat_id = cat_id;
        this.fragment = fragment;
    }

    @Override
    public HomeMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.home_menu_item, parent, false);

        return new HomeMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeMenuAdapter.ViewHolder holder, final int position) {

        Glide.with(activity).load(homeMenuLists.get(position).getImage()).thumbnail(Glide.with(activity).load(R.drawable.loading)).centerCrop().into(holder.imgCategory);

        if (homeMenuLists.get(position).getVariantLists().get(0).getStatus().equalsIgnoreCase("false")){
            holder.tvPlus.setVisibility(View.VISIBLE);
            holder.tvMinus.setVisibility(View.GONE);
        }else{
            holder.tvPlus.setVisibility(View.GONE);
            holder.tvMinus.setVisibility(View.VISIBLE);
        }

        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(25);
//        gd.setStroke(2, Color.RED);
           // gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1), 12, 16);
        }else{
            gd.setColor(Color.WHITE);
            gd.setCornerRadius(25);
          //  gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1));
        }
        holder.cat_lay.setBackground(gd);
        holder.tvCategory.setText(homeMenuLists.get(position).getName());
        holder.tvPrice.setText(SharedPref.getCurrency(activity)+homeMenuLists.get(position).getVariantLists().get(0).getPrice());
        holder.cat_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MenuDetailActivity.class);
                intent.putExtra("menu_image",homeMenuLists.get(position).getImage());
                intent.putExtra("menu_name",homeMenuLists.get(position).getName());
                intent.putExtra("menu_id",homeMenuLists.get(position).getId());
                intent.putExtra("cat_id",cat_id);
                activity.startActivity(intent);
            }
        });

        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start_time = homeMenuLists.get(position).getFood_opening_time();
                String end_time = homeMenuLists.get(position).getFood_closing_time();
                if (!start_time.equalsIgnoreCase("") && !start_time.equalsIgnoreCase("0") && start_time!=null) {
                    try {
                        Date time1 = new SimpleDateFormat("HH:mm").parse(start_time);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(time1);
                        calendar1.add(Calendar.DATE, 1);

                        Date time2 = new SimpleDateFormat("HH:mm").parse(end_time);
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
                            addtocart(holder,cat_id,homeMenuLists.get(position).getId(),homeMenuLists.get(position).getName(),homeMenuLists.get(position).getImage(),homeMenuLists.get(position).getVariantLists().get(0).getId(),homeMenuLists.get(position).getVariantLists().get(0).getVolume(),homeMenuLists.get(position).getVariantLists().get(0).getPrice(),Constants.INCREMENT);
                        } else {
                            //Toast.makeText(activity, "Delivery not available", Toast.LENGTH_SHORT).show();
                            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                            Date _24HourStart = _24HourSDF.parse(homeMenuLists.get(position).getFood_opening_time());
                            Date _24HourEnd = _24HourSDF.parse(homeMenuLists.get(position).getFood_closing_time());
                            starttime=_12HourSDF.format(_24HourStart);
                            endtime=_12HourSDF.format(_24HourEnd);
                            String msg =  String.format(homeMenuLists.get(position).getFood_time_msg(), homeMenuLists.get(position).getName(), starttime+" - "+endtime);
                            AlertDialog dialog=new AlertDialog.Builder(activity).setTitle(R.string.app_name).setMessage(msg)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                            TextView textView=dialog.findViewById(android.R.id.message);
                            Typeface face= null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                face = activity.getResources()
                                        .getFont(R.font.calibri_regular);
                            }
                            textView.setTypeface(face);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    //Add to cart
                    addtocart(holder,cat_id,homeMenuLists.get(position).getId(),homeMenuLists.get(position).getName(),homeMenuLists.get(position).getImage(),homeMenuLists.get(position).getVariantLists().get(0).getId(),homeMenuLists.get(position).getVariantLists().get(0).getVolume(),homeMenuLists.get(position).getVariantLists().get(0).getPrice(),Constants.INCREMENT);
                }


            }
        });

        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocart(holder,cat_id,homeMenuLists.get(position).getId(),homeMenuLists.get(position).getName(),homeMenuLists.get(position).getImage(),homeMenuLists.get(position).getVariantLists().get(0).getId(),homeMenuLists.get(position).getVariantLists().get(0).getVolume(),homeMenuLists.get(position).getVariantLists().get(0).getPrice(),Constants.DECREMENT);
            }
        });

    }

    private void addtocart(final ViewHolder holder, final String cat_id, final String id, final String name, final String image, final String variant_id, final String volume, final String price, final String operation) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.add_cart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("MANAGE_CART");
                            String message = jsonObject1.getString("message");
                            MainActivity mainActivity = (MainActivity) activity;
                            mainActivity.cartcount();
                            if (jsonObject1.getString("error").equalsIgnoreCase("false")){
                                holder.tvPlus.setVisibility(View.GONE);
                                holder.tvMinus.setVisibility(View.VISIBLE);
                            }else{
                                holder.tvPlus.setVisibility(View.VISIBLE);
                                holder.tvMinus.setVisibility(View.GONE);
                            }
//                            fragment.onResume();
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", SharedPref.getUserId(activity));
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
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return homeMenuLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory;
        TextView tvCategory,tvPrice, tvPlus, tvMinus;
        CardView cat_lay;

        public ViewHolder(View itemView) {
            super(itemView);

            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvPlus = (TextView) itemView.findViewById(R.id.tvPlus);
            tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
            cat_lay = (CardView) itemView.findViewById(R.id.cat_lay);

        }
    }
}