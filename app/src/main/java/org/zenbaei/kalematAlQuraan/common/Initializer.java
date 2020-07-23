package org.zenbaei.kalematAlQuraan.common;

import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

public class Initializer {

    private static Initializer initializer;
    private volatile static int  FONT_SIZE = 15; //default
    private SettingDAO settingDAO;

    private Initializer(){
    }

    public static void execute(SettingDAO settingDAO) {
        if (initializer == null) {
            initializer = new Initializer();
            initializer.settingDAO = settingDAO;
        }
        initializer.initFont();
    }

    public static void reload() {
        if (initializer != null) {
            execute(initializer.settingDAO);
        }
    }

    private void initFont(){
        FONT_SIZE = Integer.valueOf(settingDAO.findByKey(Setting.KEY_NAME.DEFAULT_FONT_SIZE));
    }

    public static int getFontSize() {
        return FONT_SIZE;
    }
}
