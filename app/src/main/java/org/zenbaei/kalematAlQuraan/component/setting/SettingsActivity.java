package org.zenbaei.kalematAlQuraan.component.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.common.notification.NotificationReceiver;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

public class SettingsActivity extends BaseActivity {

    private TextView text;
    private SettingDAO settingDAO;
    private AppCompatCheckBox nightModeCheckBox, enableNotificationCheckBox;
    private ScrollView container;
    private SearchView searchView;
    TextView chooseBackCol, chooseFontCol, chooseFontSize;
    Slider slider;
    NotificationReceiver notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingDAO = new SettingDAO(this);
        notification = new NotificationReceiver();

        this.text = findViewById(R.id.fontText);
        slider = findViewById(R.id.fontSlider);
        slider.addOnChangeListener(new SliderListener());

        nightModeCheckBox = (AppCompatCheckBox) findViewById(R.id.night_mode_checkbox);
        enableNotificationCheckBox = (AppCompatCheckBox) findViewById(R.id.enable_ntf_checkbox);
        container = (ScrollView) findViewById(R.id.font_actv_container);
        chooseBackCol = (TextView) findViewById(R.id.choose_background_color);
        chooseFontCol = (TextView) findViewById(R.id.choose_font_color);
        chooseFontSize = (TextView) findViewById(R.id.choose_font_size);

        setCurrentFontSize();
        setNightMode();
        setFontAndBackground();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
        }
        setFontAndBackground();
    }

    @Override
    public void setFontAndBackground() {
        container.setBackgroundColor(Initializer.getBackgroundColor());
        text.setTextColor(Initializer.getFontColor());
        chooseBackCol.setTextColor(Initializer.getNonAyahFontColor());
        chooseFontCol.setTextColor(Initializer.getNonAyahFontColor());
        chooseFontSize.setTextColor(Initializer.getNonAyahFontColor());
        nightModeCheckBox.setTextColor(Initializer.getNonAyahFontColor());
        enableNotificationCheckBox.setTextColor(Initializer.getNonAyahFontColor());
        int flag = Paint.HINTING_OFF;
        if (nightModeCheckBox.isChecked()) {
            flag = Paint.STRIKE_THRU_TEXT_FLAG;
        }
        chooseBackCol.setPaintFlags(flag);
        chooseFontCol.setPaintFlags(flag);
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

    private void setCurrentFontSize() {
        //get current fontColor fontSize
        slider.setValue(Initializer.getFontSize());
    }

    private void setNightMode() {
        boolean isChecked = Initializer.getBackgroundColor() == getResources().getColor(R.color.darkGray) ?
                true : false;
        nightModeCheckBox.setChecked(isChecked);
    }

    private void changeFontSize(float val) {
        text.setTextSize(val);
        int fontSize = Math.round(val);
        settingDAO.update(Setting.KEY_NAME.DEFAULT_FONT_SIZE, String.valueOf(fontSize));

    }

    public void changeFontColor(View view) {
        if (!nightModeCheckBox.isChecked()) {
            int color = ((TextView) view).getTextColors().getDefaultColor();
            text.setTextColor(color);
            settingDAO.update(Setting.KEY_NAME.FONT_COLOR, String.valueOf(color));
        } else {
            Toast toast = Toast.makeText(this, R.string.disable_night_mode, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void changeBackgroundColor(View view) {
        if (!nightModeCheckBox.isChecked()) {
            int color = ((TextView) view).getTextColors().getDefaultColor();
            container.setBackgroundColor(color);
            settingDAO.update(Setting.KEY_NAME.BACKGROUND_COLOR, String.valueOf(color));
        } else {
            Toast.makeText(this, R.string.disable_night_mode, Toast.LENGTH_SHORT).show();
        }
    }

    public void toggleNightMode(View view) {
        int flag =  Paint.HINTING_OFF;
        int fontColor, background;
        if (nightModeCheckBox.isChecked()) {
            background = getResources().getColor(R.color.darkGray);
            fontColor = getResources().getColor(R.color.white);
            flag = Paint.STRIKE_THRU_TEXT_FLAG;
        } else {
            background = getResources().getColor(R.color.yellow);
            fontColor = getResources().getColor(R.color.red);
        }
        chooseBackCol.setPaintFlags(flag);
        chooseFontCol.setPaintFlags(flag);
        settingDAO.update(Setting.KEY_NAME.FONT_COLOR, String.valueOf(fontColor));
        settingDAO.update(Setting.KEY_NAME.BACKGROUND_COLOR, String.valueOf(background));
        Initializer.reInitVariables();
        setFontAndBackground();
    }

    public void onToggleNotification(View view) {
        if (enableNotificationCheckBox.isChecked()) {
            notification.onReceive(getApplicationContext(), null);
        } else {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Initializer.reInitVariables();
    }

    class SliderListener implements Slider.OnChangeListener {
        @Override
        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
            changeFontSize(value);
        }
    }
}