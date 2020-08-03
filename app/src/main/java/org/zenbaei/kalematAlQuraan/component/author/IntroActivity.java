package org.zenbaei.kalematAlQuraan.component.author;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GestureDetectorCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;

/**
 * Created by Islam on 2/5/2016.
 */
public class IntroActivity extends BaseActivity {

    private GestureDetectorCompat mDetector;
    ScrollView container;
    TextView allah, author, date, note, noteText, intro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        container = (ScrollView) findViewById(R.id.introRoot);
        allah = (TextView) findViewById(R.id.allah);
        intro = (TextView) findViewById(R.id.intro);
        author = (TextView) findViewById(R.id.author_name);
        date = (TextView) findViewById(R.id.date);
        note = (TextView) findViewById(R.id.notes_1);
        noteText = (TextView) findViewById(R.id.note_text);

        addGestureListner();

        setFontAndBackground();
    }

    private void addGestureListner() {
        container.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override
            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(), R.string.authorOnSwingLeftHint, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(getApplicationContext(), R.string.authorOnSwingLeftHint, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        setFontAndBackground();
    }

    @Override
    public void setFontAndBackground() {
        container.setBackgroundColor(Initializer.getBackgroundColor());
        allah.setTextColor(Initializer.getFontColor());
        intro.setTextColor(Initializer.getNonAyahFontColor());
        author.setTextColor(Initializer.getNonAyahFontColor());
        date.setTextColor(Initializer.getNonAyahFontColor());
        note.setTextColor(Initializer.getFontColor());
        noteText.setTextColor(Initializer.getNonAyahFontColor());
    }
}
