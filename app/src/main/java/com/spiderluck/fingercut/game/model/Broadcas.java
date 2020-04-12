package com.spiderluck.fingercut.game.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.spiderluck.fingercut.R;
import com.spiderluck.fingercut.StartActivity;


public class Broadcas extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(Messaging.TYPE_EXTRA, 0);

        Intent intentToRepeat = new Intent(context, StartActivity.class);
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, type, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = new Messaging().getNotificationManager(context);
        Notification notification = buildNotification(context, pendingIntent, nm).build();
        nm.notify(type, notification);

    }


    public NotificationCompat.Builder buildNotification(Context context, PendingIntent pendingIntent, NotificationManager nm) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Daily Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Daily Notification");
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
        }


        Bitmap resource =  BitmapFactory.decodeResource(context.getResources(), R.drawable.spidorgame);

        return new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(context.getResources().getString(R.string.push))
                .setLargeIcon(resource)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource).setBigContentTitle(context.getResources().getString(R.string.push)))
                .setAutoCancel(true);
    }
}
