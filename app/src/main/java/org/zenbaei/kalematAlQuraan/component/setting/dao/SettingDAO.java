package org.zenbaei.kalematAlQuraan.component.setting.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.common.dao.AbstractDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting.KEY_NAME;

/**
 * Created by zenbaei on 4/9/17.
 */

public class SettingDAO extends AbstractDAO<Setting> {

    private static final String FIND_BY_KEY_STAT = "SELECT value FROM SETTINGS WHERE key = '%s'";
    private static final String UPDATE_STAT = "UPDATE SETTINGS SET value = '%s' WHERE key = '%s'";
    private static final String INSERT_STAT = "INSERT INTO SETTINGS (key, value) VALUES ('%s', '%s')";

    public SettingDAO(Context context) {
        super(context, new Setting());
    }


    @Override
    protected Setting cursorToEntity(Cursor cursor) {
        Setting setting = new Setting();
        setting.setKey(cursor.getString(0));
        setting.setValue(cursor.getString(1));
        return setting;
    }

    public void update(KEY_NAME key, String value) {
        Log.d("Update Settings table", String.format("Key [%s] - Value [%s]", key.name(), value));
        String stat = String.format(UPDATE_STAT, value, key.name());
        Log.d("Update Statement", stat);
        getWritableDatabase().execSQL(stat);
    }

    public String findByKey(KEY_NAME key) {
        String query = String.format(FIND_BY_KEY_STAT, key.name());
        Log.d("FindByKey", query);
        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        boolean exists = cursor.moveToFirst();
        if (!exists) {
            Log.i(this.getClass().getSimpleName(), "Key " + key.name() + " does not exist in Settings table");
            return "";
        }

        String value = cursor.getString(0);
        cursor.close();
        return value == null ? "" : value;
    }

    public void insert(KEY_NAME key, String value) {
        Log.d(this.getClass().getSimpleName(), String.format("Key [%s] - Value [%s]", key.name(), value));
        String stat = String.format(INSERT_STAT, key.name(), value);
        Log.d("Insert Statement", stat);
        getWritableDatabase().execSQL(stat);

    }
}
