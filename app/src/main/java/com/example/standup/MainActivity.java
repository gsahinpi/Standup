package com.example.standup;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private NotificationManager mNotificationManager;
       private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(this, MyReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //Toggle button
        ToggleButton alarmToggle=findViewById(R.id.alarmToggle);

        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);

        alarmToggle.setChecked(alarmUp);

        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            String toastMessage;
            if(isChecked){
                //Set the toast message for the "on" case.
                //deliverNotification(MainActivity.this);
                long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES/15;
                long triggerTime = SystemClock.elapsedRealtime()
                        + repeatInterval;

//If the Toggle is turned on, set the repeating alarm with a 15 minute interval
                if (alarmManager != null) {
                    alarmManager.setInexactRepeating
                            (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerTime, repeatInterval, notifyPendingIntent);
                }

                toastMessage = "Stand Up Alarm On!";
            } else {
                //Set the toast message for the "off" case.
                mNotificationManager.cancelAll();
                if (alarmManager != null) {
                    alarmManager.cancel(notifyPendingIntent);
                }
                toastMessage = "Stand Up Alarm Off!";
            }

            //Show a toast to say the alarm is turned on or off.
            Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_SHORT)
                    .show();
        }
    });
        createNotificationChannel();
    }//on main

    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }//createchannle


}
