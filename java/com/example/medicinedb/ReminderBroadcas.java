package com.example.medicinedb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcas extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notify")
//                .setSmallIcon(R.drawable.alert)
//                .setContentTitle("Reminder Take medicine")
//                .setContentText("Medicine name")
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
////
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(200,builder.build());

        mp = MediaPlayer.create(context,R.raw.tune);
        mp.start();
        Toast.makeText(context,"It's time to take those bitter medicines",Toast.LENGTH_LONG).show();
        //Log.d("Checking ","YES");
    }
}
