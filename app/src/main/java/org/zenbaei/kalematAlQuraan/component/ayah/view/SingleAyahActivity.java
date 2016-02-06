package org.zenbaei.kalematAlQuraan.component.ayah.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.db.KalematDatabase;
import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.author.IntroActivity;
import org.zenbaei.kalematAlQuraan.component.search.SearchHandlerActivity;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;
import org.zenbaei.kalematAlQuraan.component.surah.view.SurahActivity;

/**
 * Created by Islam on 12/24/2015.
 */
public class SingleAyahActivity extends AppCompatActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ayah);
        process();
        addGestureListner();
    }

    private void addGestureListner() {
        RelativeLayout myView = (RelativeLayout) findViewById(R.id.singleAyahRoot);

        myView.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override

            public void onSwipeDown() {

                // Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();

            }


            @Override

            public void onSwipeLeft() {
            }


            @Override

            public void onSwipeUp() {
            }


            @Override

            public void onSwipeRight() {
                finish();
            }

        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        searchView.setQuery("", false);
        searchView.setIconified(true);
        process();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            showDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.about))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.thanks), null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void process() {
        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (cursor == null) {
            finish();
        } else {
            cursor.moveToFirst();

            TextView surah = (TextView) findViewById(R.id.singleAyahSurah);
            TextView number = (TextView) findViewById(R.id.singleAyahNumber);
            TextView kalemah = (TextView) findViewById(R.id.singleAyahKalemah);
            TextView tafsir = (TextView) findViewById(R.id.singleAyahTafsir);

            int sIndex = cursor.getColumnIndexOrThrow(KalematDatabase.SURAH);
            int nIndex = cursor.getColumnIndexOrThrow(KalematDatabase.AYAH_NUMBER);
            int kIndex = cursor.getColumnIndexOrThrow(KalematDatabase.KALEMAH);
            int tIndex = cursor.getColumnIndexOrThrow(KalematDatabase.TAFSIR);

            surah.setText(getString(R.string.surah, new Object[]{cursor.getString(sIndex)}));
            number.setText(cursor.getString(nIndex));
            kalemah.setText(cursor.getString(kIndex));
            tafsir.setText(cursor.getString(tIndex));
        }
    }

    public void back(View view){
        finish();
    }
}
