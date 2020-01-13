package com.example.integration.firebaseservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.integration.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";

    PendingIntent action = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

// TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
// Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            remoteMessage.getNotification().getBody();

        }
// Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage);
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        Context context = getApplicationContext();
        Resources res = context.getResources();

        // ??????? ? Android 8, ????????? ?????? ???????????
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String CHANNEL_ID = "alex_channel";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "AlexChannel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Alex channel description");
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        }
        else
        {
            builder = new NotificationCompat.Builder(context);
        }

//
//        if (remoteMessage.getNotification().getBody().contains("doctor")) {
//
//            action = PendingIntent.getActivity(context, 0, new Intent(context, Doctor_HomeScreen.class), PendingIntent.FLAG_CANCEL_CURRENT);
//
//        }
//        else if(remoteMessage.getNotification().getBody().contains("patient"))
//        {
//            action = PendingIntent.getActivity(context, 0, new Intent(context, Patient_Home_Screen.class), PendingIntent.FLAG_CANCEL_CURRENT);
//        }
        // https://developer.android.com/reference/android/app/PendingIntent
         // Flag indicating that if the described PendingIntent already exists, the current one should be canceled before generating a new one.

      //  builder.setContentIntent(action)
        builder.setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Small text!")
                .setAutoCancel(true) // make this notification automatically dismissed when the user touches it
                .setContentTitle("Firebase")
                .setContentText(""+remoteMessage.getNotification().getBody());

        Notification notification = builder.build();

        int notificationCode = (int) (Math.random() * 1000);
        manager.notify(notificationCode, notification);

    }
}