package org.zenbaei.kalematAlQuraan.component.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

public class FontSizeActivity extends AppCompatActivity {

    private TextView text;
    private SettingDAO settingDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size);
        settingDAO = new SettingDAO(this);

        this.text = findViewById(R.id.fontText);
        Slider slider = findViewById(R.id.fontSlider);
        slider.addOnChangeListener(new SliderListener());
        setCurrentFontSize(slider);
    }

    private void setCurrentFontSize(Slider slider) {
        //get current font size
        slider.setValue(Initializer.getFontSize());
    }

    private void changeFontSize(float val) {
        text.setTextSize(val);
        settingDAO.update(Setting.KEY_NAME.DEFAULT_FONT_SIZE, String.valueOf(Math.round(val)));
        Initializer.reload();
    }


    class SliderListener implements Slider.OnChangeListener {
        @Override
        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
            changeFontSize(value);
        }
    }
}