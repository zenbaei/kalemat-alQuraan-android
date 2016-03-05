package org.zenbaei.kalematAlQuraan.component.author;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;

/**
 * Created by Islam on 2/5/2016.
 */
public class AuthorActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author);
        addGestureListner();
    }

    private void addGestureListner() {
        RelativeLayout myView = (RelativeLayout) findViewById(R.id.authorRoot);



        myView.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override

            public void onSwipeDown() {
            }


            @Override

            public void onSwipeLeft() {
                displayToast();
            }


            @Override

            public void onSwipeUp() {
                //Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
            }


            @Override

            public void onSwipeRight() {
                startNextActivity();
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
