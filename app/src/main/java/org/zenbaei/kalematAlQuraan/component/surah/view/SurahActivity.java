package org.zenbaei.kalematAlQuraan.component.surah.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.author.IntroActivity;
import org.zenbaei.kalematAlQuraan.component.author.NotesActivity;
import org.zenbaei.kalematAlQuraan.component.ayah.adapter.AyahArrayAdapter;
import org.zenbaei.kalematAlQuraan.component.ayah.business.AyahService;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.ayah.view.AyahActivity;
import org.zenbaei.kalematAlQuraan.component.search.SearchHandlerActivity;
import org.zenbaei.kalematAlQuraan.component.surah.business.SurahService;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Islam on 12/11/2015.
 */
public class SurahActivity extends AppCompatActivity {

    private List<Surah> surahList;

    private SearchView searchView;

    private ArrayAdapter surahArrayAdapter;

    private ListView listView;

    private EditText surahSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surah);

        listView = (ListView) findViewById(R.id.surahListView);

        setSurahListView();
        setEditTextListener();
        addGestureListner();
    }

    private void setEditTextListener() {
        surahSearchText = (EditText) findViewById(R.id.surahSearch);
        surahSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSurahList(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterSurahList(CharSequence s) {
        if (s.toString().trim().isEmpty()) {
            listView.setAdapter(surahArrayAdapter);
            return;
        }

        List<Surah> filteredSurahList = new ArrayList<>();

        for (Surah surah : surahList) {
            if (surah.getName().contains(s.toString().trim()))
                filteredSurahList.add(surah);
        }

        ArrayAdapter<Surah> adapter = new ArrayAdapter<Surah>(this,
                R.layout.text_view, filteredSurahList);
        listView.setAdapter(adapter);
    }

    private void addGestureListner() {
        ListView myView = (ListView) findViewById(R.id.surahListView);

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
                //Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
            }


            @Override

            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        searchView.setQuery("", false);
        searchView.setIconified(true);
        listView.setAdapter(surahArrayAdapter);
        surahSearchText.setText(null);
    }

    private void setSurahListView() {
        SurahService surahService = new SurahService(this);
        surahList = surahService.findAll();

        surahArrayAdapter = new ArrayAdapter<Surah>(this,
                R.layout.text_view, surahList);
        listView.setAdapter(surahArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startKalemahTafsirActivity(((TextView) view).getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
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

    private void startKalemahTafsirActivity(String selectedSurah) {
        if (selectedSurah == null || selectedSurah.isEmpty()) {
            Log.e("SurahActivity", "Selected Surah has null value.");
        }

        Surah surah = null;
        for (Surah s : surahList) {
            if (s.toString().equals(selectedSurah))
                surah = s;
        }

        if (surah == null) {
            Log.e("SurahActivity", String.format("Surah name %s is not found.", selectedSurah));
        }

        Intent intent = new Intent(getApplicationContext(), AyahActivity.class);
        intent.putExtra("surahId", surah.getId());
        intent.putExtra("surahName", surah.getName());
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
        startActivity(intent);
    }
}
