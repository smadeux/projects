package com.chinesequiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.chinesequiz.Util.LOG_OUT;

/**
 *  This class is the main page to display and run the quiz activity.
 */
public class QuizMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView questionLabel, questionCountLabel, scoreLabel;
    EditText answerEdt;
    Button submitButton;
    ProgressBar progressBar;
    ArrayList<QuizResults> questionModelArraylist;
    int currentPosition = 0;
    int numberOfCorrectAnswer = 0;

    private DrawerLayout drawer;
    LinearLayout LL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_main);

        questionCountLabel = findViewById(R.id.noQuestion);
        questionLabel = findViewById(R.id.question);
        scoreLabel = findViewById(R.id.score);

        answerEdt = findViewById(R.id.answer);
        submitButton = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progress);

        questionModelArraylist = new ArrayList<>();

        LL = findViewById(R.id.linear_lay_quiz);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("Quiz");
        setSupportActionBar(tb);

        drawer = findViewById(R.id.drawer_layout);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Creates menu button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.open_nav_drawer, R.string.close_nav_drawer)
        {
            //Brings menu to the front
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {

                navigationView.setTranslationX(0);
                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    LL.bringToFront();
                }
                else {
                    drawer.bringToFront();
                }
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setUpQuestion();
        setData();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        answerEdt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                Log.e("event.getAction()",event.getAction()+"");
                Log.e("event.keyCode()",keyCode+"");
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    checkAnswer();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * This function will check to see if the user input on the quiz is correct or incorrect and will also display feedback.
     */
    public void checkAnswer(){
        String answerString  = answerEdt.getText().toString().trim();

        if(answerString.equalsIgnoreCase(questionModelArraylist.get(currentPosition).getAnswer())){
            numberOfCorrectAnswer ++;

            new SweetAlertDialog(QuizMain.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Nice job!")
                    .setContentText("Correct Answer")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            currentPosition ++;
                            setData();
                            answerEdt.setText("");
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }else {

            new SweetAlertDialog(QuizMain.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Incorrect Answer")
                    .setContentText("The correct answer is : "+questionModelArraylist.get(currentPosition).getAnswer())
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            currentPosition ++;
                            setData();
                            answerEdt.setText("");
                        }
                    })
                    .show();
        }

        int x = ((currentPosition+1) * 100) / questionModelArraylist.size();
        progressBar.setProgress(x);
    }

    /**
     * This function adds and sets the quiz questions being asked to the user.
     */
    public void setUpQuestion(){
        questionModelArraylist.add(new QuizResults("What is 1+1 ? ","2"));
        questionModelArraylist.add(new QuizResults("What is 6*8 ? ","48"));
        questionModelArraylist.add(new QuizResults("What is 12/3 ? ","4"));
//        questionModelArraylist.add(new QuizResults("Who is the best professor at BYUI - EG: 'Last Name' ? ","Burton"));
    }

    /**
     * This function displays and sets up the end part of the quiz.
     */
    public void setData(){

        if(questionModelArraylist.size()>currentPosition) {

            questionLabel.setText(questionModelArraylist.get(currentPosition).getQuestionString());
            scoreLabel.setText("Score :" + numberOfCorrectAnswer + "/" + questionModelArraylist.size());
            questionCountLabel.setText("Question No : " + (currentPosition + 1));

        }else{

            new SweetAlertDialog(QuizMain.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("You have successfully completed the quiz")
                    .setContentText("Your score is : "+ numberOfCorrectAnswer + "/" + questionModelArraylist.size())
                    .setConfirmText("Restart")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();
                            currentPosition = 0;
                            numberOfCorrectAnswer = 0;
                            progressBar.setProgress(0);
                            setData();
                        }
                    })
                    .setCancelText("Close")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_tutorial:
                startActivity(new Intent(this, TutorialActivity.class));
                //Transition in between activities
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_lessons:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_quizzes:
                Toast.makeText(this,"You're already here!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_note:
                startActivity(new Intent(this, SetNotificationActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                MainActivity.handler.sendEmptyMessage(LOG_OUT);
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
