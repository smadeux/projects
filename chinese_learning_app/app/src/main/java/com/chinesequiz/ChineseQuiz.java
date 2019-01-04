package com.chinesequiz;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ChineseQuiz extends Application {
    public static final String CHANNEL_ID = "study";
    @Override
    public void onCreate() {
         super.onCreate();
    }
    private void  CreateNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel studyChannel = new NotificationChannel(CHANNEL_ID,"Study Channel",NotificationManager.IMPORTANCE_HIGH);
            studyChannel.setDescription("Notification reminder to study.");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(studyChannel);
        }
        else{

        }
    }
}
