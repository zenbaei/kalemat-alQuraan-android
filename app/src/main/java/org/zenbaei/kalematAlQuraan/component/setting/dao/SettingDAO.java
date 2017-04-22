package org.zenbaei.kalematAlQuraan.component.setting.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.common.dao.AbstractDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

/**
 * Created by zenbaei on 4/9/17.
 */

public class SettingDAO extends AbstractDAO<Setting> {

    private static final String FIND_BY_KEY_STAT = "SELECT value FROM SETTINGS WHERE key = '%s'";
    private static final String UPDATE_FONT_SIZE_STAT = "UPDATE SETTINGS SET value = '%s' WHERE key = 'DEFAULT_FONT_SIZE'";

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

    public void update(String key, String value) {
        Log.d("Update Settings table", String.format("Key [%s] - Value [%s]", key, value));
        String stat = String.format(UPDATE_FONT_SIZE_STAT, value);
        Log.d("Update Statement", stat);
        getWritableDatabase().execSQL(stat);
    }

    public String findByKey(String key) {
        String query = String.format(FIND_BY_KEY_STAT, key);
        Log.d("FindByKey", query);
        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        boolean exists = cursor.moveToFirst();
        if (!exists) {
            throw new IllegalStateException("DEFAULT FONT SIZE is not inserted into db.");
        }

        String value = cursor.getString(0);
        cursor.close();
        return value == null ? "" : value;
    }

}
