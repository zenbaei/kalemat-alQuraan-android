package org.zenbaei.kalematAlQuraan.component.surah.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.view.AyahActivity;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;
import org.zenbaei.kalematAlQuraan.component.surah.SurahArrayAdapter;
import org.zenbaei.kalematAlQuraan.component.surah.business.SurahService;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Islam on 12/11/2015.
 */
public class SurahActivity extends BaseActivity {

    private List<Surah> surahList;
    private SearchView searchView;
    private ArrayAdapter surahArrayAdapter;
    private ListView listView;
    private EditText surahSearchText;
    private LinearLayout indexContainer;
    private SettingDAO settingDAO;
    private TextView backToLastReadPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surah);

        settingDAO = new SettingDAO(this);

        indexContainer = (LinearLayout) findViewById(R.id.index_container);

        listView = (ListView) findViewById(R.id.surahListView);

        backToLastReadPage = findViewById(R.id.lastReadPage);
        backToLastReadPage.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        setSurahListView();
        setEditTextListener();
        addGestureListner();
        setFontAndBackground();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void setFontAndBackground() {
        backToLastReadPage.setTextColor(Initializer.getNonAyahFontColor());
        indexContainer.setBackgroundColor(Initializer.getBackgroundColor());
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
                R.layout.surah_list_item, filteredSurahList);
        listView.setAdapter(adapter);
    }

    private void addGestureListner() {
        indexContainer.setOnTouchListener(new OnSwipe(this));
        listView.setOnTouchListener(new OnSwipe(this));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("SurahActivity", "onRestart");
        if (searchView != null) { // caused null pointer crash reported
            searchView.setQuery("", false);
            searchView.setIconified(true);
        }
        listView.setAdapter(surahArrayAdapter);
        surahSearchText.setText(null);
        setFontAndBackground();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void setSurahListView() {
        SurahService surahService = new SurahService(this);
        surahList = surahService.findAll();

        surahArrayAdapter = new SurahArrayAdapter(this,
                R.layout.surah_list_item, surahList);
        listView.setAdapter(surahArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SurahActivity", view.getClass().getName());
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


    private void startKalemahTafsirActivity(String selectedSurah) {
        if (selectedSurah == null ||  selectedSurah.trim().isEmpty()) {
            Log.e("SurahActivity", "Selected Surah has null value.");
            return;
        }

        Surah surah = null;
        for (Surah s : surahList) {
            if (s.toString().equals(selectedSurah)) {
                surah = s;
                break;
            }
        }

        if (surah == null) {
            Log.e("SurahActivity", String.format("Surah name %s is not found.", selectedSurah));
            return;
        }

        Intent intent = new Intent(getApplicationContext(), AyahActivity.class);
        intent.putExtra("surahId", surah.getId());
        intent.putExtra("surahName", surah.getName());
        startActivity(intent);
    }

    public void goToLastReadPage(View view) {
        String lastReadPage = settingDAO.findByKey(Setting.KEY_NAME.LAST_READ_PAGE);

        if (Integer.valueOf(lastReadPage) == 0) {
            return;
        }

        String lastReadSurahId = settingDAO.findByKey(Setting.KEY_NAME.LAST_READ_SURAH_ID);
        String lastReadSurahName = settingDAO.findByKey(Setting.KEY_NAME.LAST_READ_SURAH_NAME);

        Intent intent = new Intent(getApplicationContext(), AyahActivity.class);
        intent.putExtra(Setting.KEY_NAME.LAST_READ_PAGE.name(), Integer.valueOf(lastReadPage));
        intent.putExtra(Setting.KEY_NAME.LAST_READ_SURAH_ID.name(), Integer.valueOf(lastReadSurahId));
        intent.putExtra(Setting.KEY_NAME.LAST_READ_SURAH_NAME.name(), lastReadSurahName);

        startActivity(intent);
    }

    class OnSwipe extends OnSwipeTouchListener {
        OnSwipe(Context context) {
            super(context);
        }

        @Override
        public void onSwipeLeft() {
            onSwipeRight();
        }


        @Override
        public void onSwipeRight() {
            Toast.makeText(SurahActivity.this, R.string.surahOnSwipeRightHint, Toast.LENGTH_SHORT).show();
        }
    }
}
