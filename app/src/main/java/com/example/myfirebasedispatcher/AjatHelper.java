package com.example.myfirebasedispatcher;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class AjatHelper {
    public static void showNotificationAjat(Context context, String title, String message, int notifyId,String CHANNEL_ID, String CHANNEL_NAME, int icon) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ContextCompat.getColor(context, android.R.color.black))
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(alarmSound);
    
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
    
                builder.setChannelId(CHANNEL_ID);
    
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
            Notification notification = builder.build();
            if (notificationManager != null) {
                notificationManager.notify(notifyId, notification);
            }
        }
    public static void toast(Context context, String pesan){
        Toast.makeText(context,pesan,Toast.LENGTH_SHORT).show();
    }
}