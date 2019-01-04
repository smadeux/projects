package com.chinesequiz;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;


import static com.chinesequiz.Util.LOG_OUT;

/**
 * Activity to picks time that notifications will be issued
 */
public class SetNotificationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NotificationManager notificationManager;
    private TimePicker notificationTime;
    private DrawerLayout drawer;
    private ConstraintLayout CL;

    AlarmManager mAlarmManager;
    final long intervalPeriod = 60*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        configureDesign();

        //Constraint layout with all of the activity items
        CL = findViewById(R.id.notificationLayout);

        //Create toolbar and set the title
        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("Alarm");
        setSupportActionBar(tb);

        //Create navigation drawer object
        Intent i = getIntent();
        drawer = findViewById(R.id.drawer_layout);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Creates menu button in top left
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.open_nav_drawer, R.string.close_nav_drawer)
        {
            //Brings menu to the front when it slides out and bring the activity to the front when the menu goes away
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {

                navigationView.setTranslationX(0);
                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    CL.bringToFront();
                }
                else {
                    drawer.bringToFront();
                }
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureDesign() {
        //Old Code Left in for kicks and later reference
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorAccent)));
//        getSupportActionBar().setTitle("Alarm");

        //Create time picker
        notificationTime = (TimePicker)findViewById(R.id.notificationTimePicker);

        //notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Button setNotificationBtn = (Button) findViewById(R.id.setNotificationBtn);
        setNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Old Code Left in for kicks and later reference
//                NotificationCompat.Builder notification = new NotificationCompat.Builder(SetNotificationActivity.this);
//                notification.setSmallIcon(R.drawable.ic_launcher_background);
//                notification.setContentTitle("Chinese Quiz");
//                notification.setContentText("Chinese Quiz");
//                notification.setContentText("Make sure you study for the chinese quiz");
//                notification.setPriority(NotificationCompat.PRIORITY_HIGH);
//                notification.setCategory(NotificationCompat.CATEGORY_ALARM);
//
//                int hour = notificationTime.getCurrentHour();
//                int minute = notificationTime.getCurrentMinute();
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR, hour);
//                calendar.set(Calendar.MINUTE, minute);
//                Date notificationDate = calendar.getTime();
//
//                PendingIntent contentIntent = PendingIntent.getActivity(SetNotificationActivity.this, 0,
//                        new Intent(SetNotificationActivity.this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//                notification.setContentIntent(contentIntent);
//                notification.setAutoCancel(true);
//                scheduleNotification(notification.build(), notificationDate.getTime());

                //Use NotificationPublisher to create notification
                //Also schedule that notification
                NotificationChannel notificationChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel("default",
                            "primary", NotificationManager.IMPORTANCE_DEFAULT);

                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (manager != null) manager.createNotificationChannel(notificationChannel);

                    mAlarmManager=(AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);
                    PendingIntent intent=PendingIntent.getBroadcast(getApplicationContext(),1234,
                            new Intent(getApplicationContext(),NotificationPublisher.class),0);

                    mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+intervalPeriod, intent);
                }

                Toast.makeText(SetNotificationActivity.this,"Alarm is created!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Old Code Left in for kicks and later reference
//    private void scheduleNotification(Notification notification, long eventStartTime) {
//        Intent notificationIntent = new Intent(SetNotificationActivity.this, NotificationPublisher.class);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long futureInMillis = eventStartTime;
//        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
//    }

    //When logout button is pressed
    public void logout(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SetNotificationActivity.this);
        pref.edit().putString("Email", "");
        pref.edit().putString("Password", "");

        FirebaseAuth.getInstance().signOut();
        finish();
        MainActivity.handler.sendEmptyMessage(LOG_OUT);
        //startActivity(new Intent(SetNotificationActivity.this, LoginActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Set things up for when the back button is pressed
    //If menu drawer is open and back is pressed, then close it
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    //What happens based on which menu item is selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_tutorial:
                startActivity(new Intent(this, TutorialActivity.class));
                //Transition in between Activities
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_lessons:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_quizzes:
                startActivity(new Intent(this, QuizMain.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_note:
                Toast.makeText(this,"You're already here!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                MainActivity.handler.sendEmptyMessage(LOG_OUT);
                startActivity(new Intent(SetNotificationActivity.this, LoginActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
