package com.rndtechnosoft.fooddaily.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rndtechnosoft.fooddaily.R;

public class NotificationDetailActivity extends AppCompatActivity {

    TextView txtTitle, txtMessage;
    ImageView imageView;
    Toolbar toolbar;
    String title,message,image;
    boolean isFromNotification=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitle(Html.fromHtml("<b>"+getResources().getString(R.string.details)+"</b>"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtTitle = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);
        txtMessage = (TextView) findViewById(R.id.textMessage);

        Bundle i = getIntent().getExtras();
        title = i.getString("title");
        message = i.getString("message");
        image = i.getString("image");
        isFromNotification = i.getBoolean("notification");

        txtTitle.setText(title);
        txtMessage.setText(message);
        Glide.with(NotificationDetailActivity.this).load(image).thumbnail(Glide.with(NotificationDetailActivity.this).load(R.drawable.loading)).into(imageView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(isFromNotification){
            Intent intent = new Intent(NotificationDetailActivity.this, NotificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }
    }
}
