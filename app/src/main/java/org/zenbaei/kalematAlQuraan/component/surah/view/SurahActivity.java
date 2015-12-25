package org.zenbaei.kalematAlQuraan.component.surah.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.adapter.AyahArrayAdapter;
import org.zenbaei.kalematAlQuraan.component.ayah.business.AyahService;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.ayah.view.AyahActivity;
import org.zenbaei.kalematAlQuraan.component.search.SearchHandlerActivity;
import org.zenbaei.kalematAlQuraan.component.surah.business.SurahService;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;

import java.util.List;

/**
 * Created by Islam on 12/11/2015.
 */
public class SurahActivity extends AppCompatActivity {

    private List<Surah> surahList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surah);
        setSurahListView();
    }

    private void setSurahListView() {
        ListView listView = (ListView) findViewById(R.id.surahListView);

        SurahService surahService = new SurahService(this);
        surahList = surahService.findAll();

        ArrayAdapter<Surah> adapter = new ArrayAdapter<Surah>(this,
                R.layout.text_view, surahList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startKalemahTafsirActivity(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startKalemahTafsirActivity(int position) {
        Surah surah = surahList.get(position);
        Intent intent = new Intent(getApplicationContext(), AyahActivity.class);
        intent.putExtra("surahId", surah.getId());
        intent.putExtra("surahName", surah.getName());
        startActivity(intent);
    }
}
