package com.chinesequiz;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;
    TextView emailText,passwordText;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SharedPreferences pref;
    private String email;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //animate login screen
        introAnimations();

        initValue();
        configureDesign();

        email = pref.getString("Email", "");
        if(!email.equals("")) {
            password = pref.getString("Password", "");
            authenticateWithEmailAndPassword();
        }
        else {
            if(mAuth.getCurrentUser() != null){
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    private void initValue() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    private void configureDesign() {
        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        emailText = (TextView) findViewById(R.id.emailText);
        passwordText = (TextView) findViewById(R.id.passwordText);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showProgressDialog("Loading..", LoginActivity.this);
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                authenticateWithEmailAndPassword();
            }
        });

        Button googleSignInBtn = (Button) findViewById(R.id.googleSignInBtn);
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void authenticateWithEmailAndPassword() {
        if(email.equals("") || password.equals("")) {
            Util.showToast("Please provide Email and Password.", LoginActivity.this);
            Util.hideProgressDialog();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                        Util.hideProgressDialog();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        pref.edit().putString("Email", email).apply();
                        pref.edit().putString("Password", password).apply();
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Util.showToast("New User created.", LoginActivity.this);
                                            Util.hideProgressDialog();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                            pref.edit().putString("Email", email).apply();
                                            pref.edit().putString("Password", password).apply();
                                        } else {
                                            Util.showToast("Authentication failed.", LoginActivity.this);
                                            Util.hideProgressDialog();
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Util.showToast("Authentication failed.", LoginActivity.this);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Util.showProgressDialog("Loading..", LoginActivity.this);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Util.showToast("Authentication success.", LoginActivity.this);
                            Util.hideProgressDialog();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Util.showToast("Authentication failed.", LoginActivity.this);
                            Util.hideProgressDialog();
                        }
                    }
                });
    }

    /**
     *  Defines and plays the animations that will run when the app is opened.
     *  @author Scott Madeux 12/5/2018
     */
    private void introAnimations() {

        //create objects to be animated
        ImageView logo = findViewById(R.id.img_logo);
        TextView title = findViewById(R.id.txt_title);
        android.support.constraint.ConstraintLayout fields = findViewById(R.id.input_cl);
        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        Button googlesignInBtn = (Button) findViewById(R.id.googleSignInBtn);

        //define animations using ObjectAnimator
        ObjectAnimator slideInLogo = ObjectAnimator.ofFloat(logo, "y", -400f, 136f);
        slideInLogo.setDuration(2000);
        ObjectAnimator slideInTitle = ObjectAnimator.ofFloat(title, "y", -400f, 490f);
        slideInTitle.setDuration(2000);
        ObjectAnimator fadeInCL = ObjectAnimator.ofFloat(fields, View.ALPHA, 0f, 1f);
        fadeInCL.setDuration(2000);
        ObjectAnimator fadeInButton1 = ObjectAnimator.ofFloat(signInBtn, View.ALPHA, 0f, 1f);
        fadeInButton1.setDuration(2000);
        ObjectAnimator fadeInButton2 = ObjectAnimator.ofFloat(googlesignInBtn, View.ALPHA, 0f, 1f);
        fadeInButton2.setDuration(2000);

        //set and play animations together
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(slideInTitle, slideInLogo, fadeInCL, fadeInButton1, fadeInButton2);
        animSet.start();

    }
}
