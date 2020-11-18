/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rndtechnosoft.fooddaily.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rndtechnosoft.fooddaily.Activity.MainActivity;
import com.rndtechnosoft.fooddaily.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    String CHANNEL_ID = "my_channel_01";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    private String androidID;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token in logcat
        Log.e("token", "Refreshed token: " + refreshedToken);
        try {
            androidID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MessageResponse",""+remoteMessage.getData().get("notification_title"));
            if(!remoteMessage.getData().get("image").equals("") && remoteMessage.getData().get("image")!=null) {
                sendNotificationImagePost(remoteMessage);
                //sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("image"),remoteMessage.getData().get("type"));
            }else{
                textNoti(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"),remoteMessage.getData().get("type"));
            }
        /*int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Create a notification and set the notification channel.
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("New Message")
                    .setContentText("You've received new messages.")
                    .setSmallIcon(R.drawable.splash_screen)
                    .setChannelId(CHANNEL_ID)
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

// Issue the notification.
            mNotificationManager.notify(notifyID, notification);
        }*/

    }

    private void sendNotificationImagePost(RemoteMessage remoteMessage) {

        int getrandom =getRandomNumber(0,100);
        String title;
        String message;
        String image;
        String type="";
        title = remoteMessage.getData().get("title");
        message = remoteMessage.getData().get("message");
        image = remoteMessage.getData().get("image");


        try {
            type = remoteMessage.getData().get("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* if (image == null || image.trim().equals("")) {
            sendNotificationWithoutImage(title, message, id, ActivityHome.class);
            return;
        }*/
        Intent intent=null;
        if(type.equalsIgnoreCase("order")){
            intent = new Intent(this, MainActivity.class);
        }else{
            intent = new Intent(this, MainActivity.class);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("image", image);
        intent.putExtra("notification", true);
        intent.putExtra("type",type);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, getrandom, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = null;
        try {
            bitmap = new MyTask().execute(image.replace(" ", "%20")).get();
        } catch (InterruptedException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(getString(R.string.app_name));
        bigPictureStyle.setSummaryText(message);
        bigPictureStyle.bigPicture(bitmap);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, this.getResources().getString(R.string.app_name), importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(getString(R.string.app_name))
                .setStyle(bigPictureStyle)
                .setContentText(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(getrandom,PendingIntent.FLAG_UPDATE_CURRENT
        );
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify( getrandom/* ID of notification */, notificationBuilder.build());
    }


 /*   private void sendNotification(String title, String message, String image,String type) {
        int getrandom =getRandomNumber(0,100);
        Intent intent=null;

        if(type.equalsIgnoreCase("order")){
             intent = new Intent(this, MainActivity.class);
        }else{
             intent = new Intent(this, MainActivity.class);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("image", image);
        intent.putExtra("notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap bitmap = null;
        try {
            bitmap = new MyTask().execute(Constant_Api.image + image.replace(" ", "%20")).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(message);
        bigPictureStyle.bigPicture(bitmap);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, this.getResources().getString(R.string.app_name), importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.ic_about)
                .setContentTitle(getString(R.string.app_name))
                .setStyle(bigPictureStyle)
                .setContentText(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        *//*NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);*//*

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(getrandom,PendingIntent.FLAG_UPDATE_CURRENT
        );
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(getrandom *//* ID of notification *//*, notificationBuilder.build());
    }*/

    private void textNoti (String title,String message,String type) {

        int notifyID = 1;
        Intent intent=null;

        if(type.equalsIgnoreCase("order")){
            intent = new Intent(this, MainActivity.class);
        }else{
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type",type);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        int getrandom =getRandomNumber(0,100);
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Create a notification and set the notification channel.
            Notification notification = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

// Issue the notification.
            mNotificationManager.notify(getrandom, notification);
        }
    }

        /*Intent intent=null;

        if(type.equalsIgnoreCase("order")){
            intent = new Intent(this, MainActivity.class);
        }else{
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("notification", true);
        intent.putExtra("type",type);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                ;


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1 *//* ID of notification *//*, notificationBuilder.build());*/

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class MyTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getBitmapFromURL(params[0]);
        }
    }

    private int getRandomNumber(int i, int i1) {
        Random r = new Random();
        int low = i;
        int high = i1;
        int result = r.nextInt(high-low) + low;
        return result;
    }
}
