package org.zenbaei.kalematAlQuraan.component.menu;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.core.view.MenuItemCompat;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.setting.adapter.SettingArrayAdaptar;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

import java.util.List;



public class FavActivity extends BaseActivity {

    private SettingDAO settingDAO;
    private SearchView searchView;
    private ListView listview;
    private SettingArrayAdaptar adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        settingDAO = new SettingDAO(this);
        fillView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
        }
        listview.setAdapter(adapter);
    }

    private void fillView() {
        listview = (ListView)findViewById(R.id.favListView);
        List<Setting> settings = settingDAO.findAllValByKey(Setting.KEY_NAME.FAVOURITE);

        adapter = new SettingArrayAdaptar
                (FavActivity.this,
                        R.layout.fav_list_item,
                        settings);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnClickListener());

        TextView emptyFav = (TextView) findViewById(R.id.emptyFav);
        if (settings.isEmpty()) {
            emptyFav.setVisibility(View.VISIBLE);
        } else {
            emptyFav.setVisibility(View.INVISIBLE);
        }

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((AppCompatCheckBox) view).isChecked();
        for (int i = 0; i < listview.getChildCount(); i++) {
            RelativeLayout layout = (RelativeLayout) listview.getChildAt(i);
            AppCompatCheckBox checkBox = (AppCompatCheckBox) layout.getChildAt(3);
            if (checkBox != null) {
                checkBox.setChecked(checked);
            }
        }
    }

    private void resetCheckAllButton() {
        AppCompatCheckBox checkBox = (AppCompatCheckBox)findViewById(R.id.favAllCheckbox);
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        }
    }

    public void onDeleteButtonClick(View view) {
        boolean deleted = false;
        resetCheckAllButton();
        for (int i = 0; i < listview.getChildCount(); i++) {
            RelativeLayout layout = (RelativeLayout) listview.getChildAt(i);
            TextView _id = (TextView) layout.getChildAt(0);
            AppCompatCheckBox checkBox = (AppCompatCheckBox) layout.getChildAt(3);
            if (checkBox != null && checkBox.isChecked()) {
                Setting setting = new Setting();
                setting.setId(Long.parseLong(_id.getText().toString()));
                settingDAO.delete(setting);
                deleted = true;
            }
        }
        if (deleted) {
            fillView();
            Toast.makeText(this, R.string.deleted, Toast.LENGTH_SHORT).show();
        }
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



    class OnClickListener implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RelativeLayout layout = (RelativeLayout)view;
            TextView _id = (TextView) layout.getChildAt(0);
            if (_id == null) {
                return;
            }
            AppCompatCheckBox checkBox = (AppCompatCheckBox) layout.getChildAt(3);
            boolean isChecked = checkBox.isChecked() ? false : true;
            checkBox.setChecked(isChecked);
        }
    }
}
