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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.db.KalematDatabase;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.search.SearchHandlerActivity;

/**
 * Created by Islam on 12/24/2015.
 */
public class SingleAyahActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ayah);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

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

            surah.setText(String.format("%s %s",getString(R.string.surah), cursor.getString(sIndex)));
            number.setText(cursor.getString(nIndex));
            kalemah.setText(cursor.getString(kIndex));
            tafsir.setText(cursor.getString(tIndex));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, SearchHandlerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
