package org.zenbaei.kalematAlQuraan.component.setting.entity;

import org.zenbaei.kalematAlQuraan.common.entity.AbstractEntity;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;

/**
 * Created by zenbaei on 4/9/17.
 */

public class Setting extends AbstractEntity {

    private String key;
    private String value;
    public static final String TABLE_NAME = "SETTINGS";
    public static final String KEY_COLUMN = "key";
    public static final String VALUE_COLUMN = "value";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS SETTINGS (_id INTEGER PRIMARY KEY, key TEXT, value TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS SETTINGS;";
    public static final String[] COLUMNS = {ID_COLUMN, KEY_COLUMN, VALUE_COLUMN};
    public enum KEY_NAME {DEFAULT_FONT_SIZE, LAST_READ_PAGE, LAST_READ_SURAH_ID, LAST_READ_SURAH_NAME}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
