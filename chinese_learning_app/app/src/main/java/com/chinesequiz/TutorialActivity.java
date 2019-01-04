package com.chinesequiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.firebase.auth.FirebaseAuth;
import com.shockwave.pdfium.PdfDocument;
import java.util.List;

import static com.chinesequiz.Util.LOG_OUT;

/**
 *  This class loads in the tutorial pdf file.
 */
public class TutorialActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "tutorial.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    private DrawerLayout drawer;

    private float lastTranslate = 0.0f;
    private ConstraintLayout CL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);

        //Constraint layout with all of the activity items
        CL = findViewById(R.id.CL_tutorial);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("Tutorial");
        setSupportActionBar(tb);

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

        pdfView= findViewById(R.id.pdfView);
        displayFromAsset(TAG);
    }

    private void displayFromAsset(String assetFileName) {
        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
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
                Toast.makeText(this,"You're already here!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_lessons:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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
                startActivity(new Intent(TutorialActivity.this, LoginActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
