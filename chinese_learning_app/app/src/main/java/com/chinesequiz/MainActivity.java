package com.chinesequiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import static android.view.MotionEvent.EDGE_LEFT;
import static com.chinesequiz.Util.ACTIVITY_FINISHED;
import static com.chinesequiz.Util.LOG_OUT;

/**
 * This is the main class of our application.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Handler handler;
    private DrawerLayout drawer;

    private float lastTranslate = 0.0f;
    private ConstraintLayout CL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureDesign();

        //Constraint layout with all of the activity items
        CL = findViewById(R.id.CL_main);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("Lessons");
        setSupportActionBar(tb);

        Intent i = getIntent();
        drawer = findViewById(R.id.drawer_layout);


        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == LOG_OUT) {
                    finish();
                }
            }
        };

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
        GridView lessonGv = findViewById(R.id.lesson_grid);
        final LessonAdapter adapter = new LessonAdapter(this);
        lessonGv.setAdapter(adapter);

        lessonGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LessonActivity.lessonObj = (Map<String, Object>) adapter.getItem(position);
                startActivity(new Intent(MainActivity.this, LessonActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_alarm:
                startActivity(new Intent(MainActivity.this, SetNotificationActivity.class));
                break;
            case R.id.quiz_icon:
                startActivity(new Intent(MainActivity.this, QuizMain.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //Set things up for when the back button is pressed
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_tutorial:
                startActivity(new Intent(this, TutorialActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_lessons:
                Toast.makeText(this,"You're already here!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_quizzes:
                startActivity(new Intent(this, QuizMain.class));
                //animation in between activities
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_note:
                startActivity(new Intent(this, SetNotificationActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                MainActivity.handler.sendEmptyMessage(LOG_OUT);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
