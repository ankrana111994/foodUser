package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.fooddaily.Activity.CategoryDetailActivity;
import com.rndtechnosoft.fooddaily.Activity.MenuDetailActivity;
import com.rndtechnosoft.fooddaily.Model.MenuList;
import com.rndtechnosoft.fooddaily.R;
import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryMenuAdapter extends RecyclerView.Adapter<CategoryMenuAdapter.ViewHolder> {

    private Activity activity;
    private List<MenuList> menuLists;
    String endtime ="";
    String starttime ="";

    public CategoryMenuAdapter(Activity activity, List<MenuList> menuLists) {
        this.activity = activity;
        this.menuLists = menuLists;
    }

    @Override
    public CategoryMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.new_menu_item, parent, false);
        return new CategoryMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryMenuAdapter.ViewHolder holder, final int position) {

        Glide.with(activity).load(menuLists.get(position).getImage()).thumbnail(Glide.with(activity).load(R.drawable.loading).centerCrop()).centerCrop().into(holder.imgMenu);
//        Glide.with(activity).load(menuLists.get(position).getFood_type_icon()).centerCrop().into(holder.food_type);
        holder.tvName.setText(menuLists.get(position).getName());
        if(menuLists.get(position).getDes().equalsIgnoreCase("") || menuLists.get(position).getDes()==null)
            holder.tvDesc.setVisibility(View.GONE);

        holder.tvDesc.setText(Html.fromHtml(menuLists.get(position).getDes()));
        holder.tvPrice.setText(Html.fromHtml(SharedPref.getCurrency(activity)+menuLists.get(position).getVariantLists().get(0).getPrice()));

        starttime="";
        endtime="";
        if (!menuLists.get(position).getFood_opening_time().equalsIgnoreCase("0") && !menuLists.get(position).getFood_closing_time().equalsIgnoreCase("0")) {

            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            try {
                Date _24HourStart = _24HourSDF.parse(menuLists.get(position).getFood_opening_time());
                Date _24HourEnd = _24HourSDF.parse(menuLists.get(position).getFood_closing_time());
                starttime=_12HourSDF.format(_24HourStart);
                endtime=_12HourSDF.format(_24HourEnd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(activity).equalsIgnoreCase("1")) {
            gd.setColor(activity.getResources().getColor(R.color.colorWhite));
            gd.setCornerRadius(25);
           // gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1), 12, 16);
        }else{
            gd.setColor(activity.getResources().getColor(R.color.colorWhite));
            gd.setCornerRadius(25);
          //  gd.setStroke(4, activity.getResources().getColor(R.color.colorGrey1));
        }
        holder.linearLayout.setBackground(gd);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MenuDetailActivity.class);
                intent.putExtra("menu_image",menuLists.get(position).getImage());
                intent.putExtra("menu_name",menuLists.get(position).getName());
                intent.putExtra("menu_id",menuLists.get(position).getId());
                intent.putExtra("cat_id",menuLists.get(position).getCat_id());
                activity.startActivity(intent);
            }
        });

        holder.tvQty.setText(menuLists.get(position).getVariantLists().get(0).getQty());

        holder.imgIncr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start_time = menuLists.get(position).getFood_opening_time();
                String end_time = menuLists.get(position).getFood_closing_time();
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
                            int qty = Integer.parseInt(holder.tvQty.getText().toString());
                            holder.tvQty.setText(String.valueOf(qty+1));

                            //add to cart or update cart
                            addtocart(menuLists.get(position).getCat_id(),menuLists.get(position).getId(),menuLists.get(position).getName(),menuLists.get(position).getImage(),menuLists.get(position).getVariantLists().get(0).getId(),menuLists.get(position).getVariantLists().get(0).getVolume(),menuLists.get(position).getVariantLists().get(0).getPrice(), Constants.INCREMENT);
                        } else {
                            //Toast.makeText(activity, "Delivery not available", Toast.LENGTH_SHORT).show();
                            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                            Date _24HourStart = _24HourSDF.parse(menuLists.get(position).getFood_opening_time());
                            Date _24HourEnd = _24HourSDF.parse(menuLists.get(position).getFood_closing_time());
                            starttime=_12HourSDF.format(_24HourStart);
                            endtime=_12HourSDF.format(_24HourEnd);
                            String msg =  String.format(menuLists.get(position).getFood_time_msg(), menuLists.get(position).getName(), starttime+" - "+endtime);
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
                    int qty = Integer.parseInt(holder.tvQty.getText().toString());
                    holder.tvQty.setText(String.valueOf(qty+1));

                    //add to cart or update cart
                    addtocart(menuLists.get(position).getCat_id(),menuLists.get(position).getId(),menuLists.get(position).getName(),menuLists.get(position).getImage(),menuLists.get(position).getVariantLists().get(0).getId(),menuLists.get(position).getVariantLists().get(0).getVolume(),menuLists.get(position).getVariantLists().get(0).getPrice(), Constants.INCREMENT);
                }
            }
        });

        holder.imgDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.tvQty.getText().toString());
                if (qty!=0) {
                    holder.tvQty.setText(String.valueOf(qty - 1));

                    //add to cart or update cart
                    addtocart(menuLists.get(position).getCat_id(), menuLists.get(position).getId(), menuLists.get(position).getName(), menuLists.get(position).getImage(), menuLists.get(position).getVariantLists().get(0).getId(), menuLists.get(position).getVariantLists().get(0).getVolume(), menuLists.get(position).getVariantLists().get(0).getPrice(), Constants.DECREMENT);
                }
            }
        });

//        int numberOfColumns = 2;//menuLists.get(position).getVariantLists().size();
//        GridLayoutManager layoutManager = new GridLayoutManager(activity, numberOfColumns);
//        holder.variant_recycler.setLayoutManager(layoutManager);
//        holder.variant_recycler.setAdapter(new VariantAdapter(activity, menuLists.get(position), menuLists.get(position).getVariantLists(), holder.sub_layout, holder.topping_recycler));

//        holder.tvYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.tvYes.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
//                holder.tvYes.setTextColor(activity.getResources().getColor(R.color.colorWhite));
//                holder.submenu_layout.setVisibility(View.VISIBLE);
//            }
//        });
//
//        holder.tvNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.sub_layout.setVisibility(View.GONE);
//                holder.submenu_layout.setVisibility(View.GONE);
//            }
//        });

//        holder.topping_recycler.setLayoutManager(new LinearLayoutManager(activity));
//        holder.topping_recycler.setAdapter(new SubMenuAdapter(activity,menuLists.get(position),menuLists.get(position).getSubMenuLists()));
    }

    @Override
    public int getItemCount() {
        return menuLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgMenu,imgDesc,imgIncr;//,food_type;
        private TextView tvName, tvDesc, tvPrice, tvQty;//,tvYes,tvNo;
//        private RecyclerView variant_recycler,topping_recycler;
//        private LinearLayout sub_layout,submenu_layout;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
//            food_type = itemView.findViewById(R.id.foodtype_img);
            imgMenu = (ImageView) itemView.findViewById(R.id.imgMenu);
            imgDesc = (ImageView) itemView.findViewById(R.id.imgDesc);
            imgIncr = (ImageView) itemView.findViewById(R.id.imgIncr);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvQty = (TextView) itemView.findViewById(R.id.tvQty);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
//            tvYes = (TextView) itemView.findViewById(R.id.tvYes);
//            tvNo = (TextView) itemView.findViewById(R.id.tvNo);
//            variant_recycler = (RecyclerView) itemView.findViewById(R.id.variant_recycler);
//            topping_recycler = (RecyclerView) itemView.findViewById(R.id.topping_recycler);
//            sub_layout = (LinearLayout) itemView.findViewById(R.id.sub_layout);
//            submenu_layout = (LinearLayout) itemView.findViewById(R.id.submenu_layout);

        }
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
                            CategoryDetailActivity detailActivity = (CategoryDetailActivity) activity;
                            detailActivity.cartcount();
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

}

