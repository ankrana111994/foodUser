package com.rndtechnosoft.fooddaily.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rndtechnosoft.fooddaily.Activity.CouponActivity;
import com.rndtechnosoft.fooddaily.Activity.MenuDetailActivity;
import com.rndtechnosoft.fooddaily.Model.CouponList;
import com.rndtechnosoft.fooddaily.R;
import com.rndtechnosoft.fooddaily.Util.Method;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CouponCodeAdapter extends RecyclerView.Adapter<CouponCodeAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<CouponList> couponLists;
    private Method method;
    String amount;

    public CouponCodeAdapter(Activity activity, ArrayList<CouponList> couponLists,String amount) {
        this.activity = activity;
        this.couponLists = couponLists;
        method = new Method(activity);
        this.amount = amount;
    }

    @Override
    public CouponCodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.coupon_code_list, parent, false);

        return new CouponCodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CouponCodeAdapter.ViewHolder holder, final int position) {

        holder.txtcode.setText(couponLists.get(position).getCoupon_code());
        Glide.with(activity).load(couponLists.get(position).getImage()).thumbnail(Glide.with(activity).load(R.drawable.loading)).into(holder.coupon_image);
        holder.title.setText(couponLists.get(position).getTitle());
        holder.desc.setText(couponLists.get(position).getTnc());
        holder.tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final BottomSheetDialog dialog = new BottomSheetDialog(activity);
                    dialog.setContentView(R.layout.bottom_dialog);
                    TextView cancelBtn = dialog.findViewById(R.id.cancel);
                    Button doneBtn = dialog.findViewById(R.id.done);
                    TextView term_condition = dialog.findViewById(R.id.term_condition);
                    term_condition.setText(couponLists.get(position).getTnc());
                    dialog.show();

                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponActivity context=(CouponActivity) activity;
                context.getDiscount(holder.txtcode.getText().toString(),amount);
            }
        });

    }

    @Override
    public int getItemCount() {
        return couponLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtcode,title, desc;
        RelativeLayout tnc;
        TextView apply_button;
        CircleImageView coupon_image;

        public ViewHolder(View itemView) {
            super(itemView);

            txtcode = itemView.findViewById(R.id.tvCouponcode);
            title = itemView.findViewById(R.id.tvTitle);
            desc = itemView.findViewById(R.id.desc);
            tnc = itemView.findViewById(R.id.tnc);
            apply_button = itemView.findViewById(R.id.apply_button);
            coupon_image = itemView.findViewById(R.id.coupon_image);

        }
    }
}

