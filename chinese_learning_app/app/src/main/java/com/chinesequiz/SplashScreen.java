package com.chinesequiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Simple splash screen for app start up
 * @author Scott Madeux 12/15/2018
 */
public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME = 3000;
    ImageView logo;
    TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get rid of status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Create splash screen objects
        logo = findViewById(R.id.img_logo);
        loading = findViewById(R.id.txt_load);

        //Switch to the LoginActivity and specified time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(splashIntent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();
            }
        }, SPLASH_SCREEN_TIME);
    }

//    Potential animation for splash screen
//    void playAnimation() {
//        ObjectAnimator fadeOutLogo = ObjectAnimator.ofFloat(logo, View.ALPHA, 1f, 0f);
//        fadeOutLogo.setDuration(3000);
//        ObjectAnimator fadeInLogo = ObjectAnimator.ofFloat(logo, View.ALPHA, 0f, 1f);
//        fadeInLogo.setDuration(3000);
//        ObjectAnimator fadeOutLoad = ObjectAnimator.ofFloat(loading, View.ALPHA, 1f, 0f);
//        fadeOutLoad.setDuration(2000);
//        ObjectAnimator fadeInLoad = ObjectAnimator.ofFloat(loading, View.ALPHA, 0f, 1f);
//        fadeInLoad.setDuration(2000);
//
//        AnimatorSet animSet1 = new AnimatorSet();
//        //AnimatorSet animSet2 = new AnimatorSet();
//        animSet1.playSequentially(fadeInLogo, fadeInLogo);
//        //animSet2.playSequentially(fadeInLoad, fadeOutLoad, fadeInLoad, fadeOutLoad);
//        animSet1.start();
//        //animSet2.start();
//    }
}
