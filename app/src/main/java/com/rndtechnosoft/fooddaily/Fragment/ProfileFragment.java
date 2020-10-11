package com.rndtechnosoft.fooddaily.Fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.Activity.AddressActivity;
import com.rndtechnosoft.fooddaily.Activity.ForgotPasswordActivity;
import com.rndtechnosoft.fooddaily.Activity.ProfileEditActivity;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Constants;
import com.rndtechnosoft.fooddaily.Util.Method;
import com.rndtechnosoft.fooddaily.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    CircleImageView profile_image;
    TextView username,u_name,amount,email,mobile,location,address,gender,dob,doa;
    AVLoadingIndicatorView progressBar;
    String name;
    String email_id;
    String plocation;
    String pdoa;
    String pdob;
    String pgender;
    String pmobile;
    String pimage;
    String pwallet;
    String paddress;
    SwipeRefreshLayout pullToRefresh;
    LinearLayout scratchCard,address_lay,editprofile_lay,reward_layout,email_layout,phone_layout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorWhite));
        int col = getResources().getColor(R.color.colorAccent);
        pullToRefresh.setColorSchemeColors(col,col,col);
        profile_image=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.user_name);
        u_name=view.findViewById(R.id.u_name);
        amount=view.findViewById(R.id.amount);
        email=view.findViewById(R.id.email);
        mobile=view.findViewById(R.id.mobile);
        location=view.findViewById(R.id.location);
        address=view.findViewById(R.id.address);
        gender=view.findViewById(R.id.gender);
        dob=view.findViewById(R.id.dob);
        doa=view.findViewById(R.id.doa);
        progressBar=view.findViewById(R.id.progresbar_profile);
        scratchCard=view.findViewById(R.id.scratchCard);
        address_lay=view.findViewById(R.id.address_lay);
        editprofile_lay=view.findViewById(R.id.editprofile_lay);
        reward_layout=view.findViewById(R.id.reward_layout);
        email_layout=view.findViewById(R.id.email_layout);
        phone_layout=view.findViewById(R.id.phone_layout);

        //Set details from preference
        username.setText(SharedPref.getUserName(getActivity()));
        u_name.setText("@"+SharedPref.getMobileNumber(getActivity()));
        mobile.setText(SharedPref.getMobileNumber(getActivity()));

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });

        scratchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ScratchCardActivity.class));
            }
        });

        address_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressActivity.class));
            }
        });

        editprofile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileEditActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GradientDrawable gd = new GradientDrawable();
        if (SharedPref.getDASHED(getActivity()).equalsIgnoreCase("1")) {
            gd.setColor(getResources().getColor(R.color.colorWhite));
            gd.setCornerRadius(25);
            gd.setStroke(4, getResources().getColor(R.color.colorGrey1), 12, 16);
        }else{
            gd.setColor(getResources().getColor(R.color.colorWhite));
            gd.setCornerRadius(25);
            gd.setStroke(4, getResources().getColor(R.color.colorGrey1));
        }
        reward_layout.setBackground(gd);
        editprofile_lay.setBackground(gd);
        address_lay.setBackground(gd);
        email_layout.setBackground(gd);
        phone_layout.setBackground(gd);
        if (Method.haveNetworkConnection(getActivity())) {
            getData();
        } else {
            pullToRefresh.setRefreshing(false);
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.hide();
        }
    }

    public void getData() {

        progressBar.hide();
        final String userid= SharedPref.getUserId(getActivity());
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.view_profile+userid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String res = new String(response);
                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("USER_PROFILE");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        name = object.getString("name");
                        email_id = object.getString("email");
                        pmobile = object.getString("mobile");
                        pgender = object.getString("gender");
                        pimage = object.getString("image");
                        pwallet = object.getString("wallet");
                        pdob = object.getString("dob");
                        pdoa = object.getString("doa");
                        paddress = object.getString("address");
                        plocation = object.getString("location");

                        SharedPref.setPreference(SharedPref.USER_NAME,name, getActivity());
                        SharedPref.setPreference(SharedPref.USER_MOBILE,pmobile,getActivity());
                        SharedPref.setPreference(SharedPref.USER_ID,userid,getActivity());
                        SharedPref.setPreference(SharedPref.USER_GENDER,pgender,getActivity());
                        SharedPref.setPreference(SharedPref.USER_EMAIL,email_id,getActivity());
                        SharedPref.setPreference(SharedPref.USER_IMAGE,pimage,getActivity());
                        SharedPref.setPreference(SharedPref.WALLET,pwallet,getActivity());
                    }

                    if (!name.equals(""))
                        username.setText(name);
                    if (!email_id.equals(""))
                        email.setText(email_id);
                    if (!pmobile.equals("")) {
                        mobile.setText(pmobile);
                        u_name.setText("@"+pmobile);
                    }
                    if (!pgender.equals(""))
                        gender.setText(pgender);
                    if (!pimage.equals(""))
                        Glide.with(getActivity()).load(pimage).thumbnail(Glide.with(getActivity()).load(R.drawable.loading)).into(profile_image);
                    if (!pwallet.equals(""))
                        amount.setText(pwallet);
                    if (!pdob.equals(""))
                        dob.setText(pdob);
                    if (!pdoa.equals(""))
                        doa.setText(pdoa);
                    if (!paddress.equals(""))
                        address.setText(paddress);
                    if (!plocation.equals(""))
                        location.setText(plocation);

                    pullToRefresh.setRefreshing(false);
                    progressBar.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(VideoDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                requestQueue.stop();
                progressBar.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);
    }

}
