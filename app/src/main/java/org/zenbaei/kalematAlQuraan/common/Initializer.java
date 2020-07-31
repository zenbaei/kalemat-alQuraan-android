package org.zenbaei.kalematAlQuraan.common;

import android.graphics.Color;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

public class Initializer {

    private static Initializer initializer;
    private SettingDAO settingDAO;
    private volatile static int FONT_SIZE = 15; //default
    private volatile static int FONT_COLOR;
    private volatile static int BACKGROUND_COLOR;
    private static final int RED = -48060;
    private static final int BLACK = -16777216;

    private Initializer() {
    }

    public static void execute(SettingDAO settingDAO) {
        if (initializer == null) {
            initializer = new Initializer();
            initializer.settingDAO = settingDAO;
        }
        initializer.initVariables();
    }

    public static void reInitVariables() {
        if (initializer != null) {
            initializer.initVariables();
        }
    }

    private void initVariables() {
        FONT_SIZE = Integer.valueOf(settingDAO.findByKey(Setting.KEY_NAME.DEFAULT_FONT_SIZE));
        FONT_COLOR = Integer.valueOf(settingDAO.findByKey(Setting.KEY_NAME.FONT_COLOR));
        BACKGROUND_COLOR = Integer.valueOf(settingDAO.findByKey(Setting.KEY_NAME.BACKGROUND_COLOR));
    }

    public static int getFontSize() {
        Log.d("Initializer", "getFontSize: " + FONT_SIZE);
        return FONT_SIZE;
    }

    public static int getFontColor() {
        return FONT_COLOR;
    }

    public static int getBackgroundColor() {
        return BACKGROUND_COLOR;
    }

    public static  int getNonAyahFontColor() {
        if (getFontColor() == RED) {
            return BLACK;
        }
        return getFontColor();
    }


}
