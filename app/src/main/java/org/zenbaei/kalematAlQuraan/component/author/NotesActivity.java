package org.zenbaei.kalematAlQuraan.component.author;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;
import org.zenbaei.kalematAlQuraan.component.surah.view.SurahActivity;

/**
 * Created by Islam on 2/5/2016.
 */
public class NotesActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);
        addGestureListner();
    }

    private void addGestureListner() {
        RelativeLayout myView = (RelativeLayout) findViewById(R.id.notesRoot);

        myView.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override

            public void onSwipeDown() {

                // Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();

            }


            @Override

            public void onSwipeRight() {
                startNextActivity();
            }


            @Override

            public void onSwipeUp() {
                //Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
            }


            @Override

            public void onSwipeLeft() {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
            }

        });
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
        Intent intent = new Intent(getApplicationContext(), SurahActivity.class);
        startActivity(intent);
    }
}
