package org.zenbaei.kalematAlQuraan.component.author;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.GestureDetectorCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.common.helper.IntroGestureImpl;
import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;

/**
 * Created by Islam on 2/5/2016.
 */
class AuthorActivity extends BaseActivity{

    private GestureDetectorCompat mDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author);
        mDetector = new GestureDetectorCompat(this, new IntroGestureImpl(getApplicationContext()));
        addGestureListner();
    }

    private void addGestureListner() {
        RelativeLayout myView = (RelativeLayout) findViewById(R.id.authorRoot);

        myView.setOnTouchListener(new OnSwipeTouchListener(this) {


            @Override
            public void onSwipeLeft() {
                displayToast();
            }

            @Override
            public void onSwipeRight() {
                startNextActivity();
            }

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return super.onTouch(view, motionEvent);
            }

        });
    }

    private void displayToast() {
        Toast.makeText(AuthorActivity.this, R.string.authorOnSwingLeftHint, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_search).setVisible(false);

        return true;
    }

    public void next(View view){
        startNextActivity();
    }

    private void startNextActivity(){
        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
        startActivity(intent);
    }

}
