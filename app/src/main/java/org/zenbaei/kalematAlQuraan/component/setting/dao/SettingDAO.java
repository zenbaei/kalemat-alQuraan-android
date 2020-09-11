package org.zenbaei.kalematAlQuraan.component.setting.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.common.dao.AbstractDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting.KEY_NAME;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zenbaei on 4/9/17.
 */

public class SettingDAO extends AbstractDAO<Setting> {

    private static final String FIND_BY_KEY_STAT = "SELECT value FROM SETTINGS WHERE key = '%s'";
    private static final String FIND_BY_VAL_STAT = "SELECT value FROM SETTINGS WHERE value = '%s'";
    private static final String FIND_ALL_VAL_BY_KEY_STAT = "SELECT _id, value FROM SETTINGS WHERE key = '%s'";
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
        getWritableDatabase().close();
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
        getReadableDatabase().close();
        return value == null ? "" : value;
    }

    public List<Setting> findAllValByKey(KEY_NAME key) {
        String query = String.format(FIND_ALL_VAL_BY_KEY_STAT, key.name());
        Log.d("FindByKey", query);
        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        List<Setting> settings = new ArrayList<>();

        boolean exists = cursor.moveToFirst();
        if (!exists) {
            Log.i(this.getClass().getSimpleName(), "Key " + key.name() + " does not exist in Settings table");
            return settings;
        }

        while (!cursor.isAfterLast()) {
            Setting setting = new Setting();
            setting.setId(cursor.getLong(0));
            setting.setValue(cursor.getString(1));
            settings.add(setting);
            cursor.moveToNext();
        }

        cursor.close();
        getReadableDatabase().close();
        return settings;
    }

    public String findByValue(String value) {
        String query = String.format(FIND_BY_VAL_STAT, value);
        Log.d("FindByVal", query);
        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        boolean exists = cursor.moveToFirst();
        if (!exists) {
            Log.i(this.getClass().getSimpleName(), "Value " + value + " does not exist in Settings table");
            return "";
        }

        String result = cursor.getString(0);
        cursor.close();
        getReadableDatabase().close();
        return result == null ? "" : result;
    }

    public void insert(KEY_NAME key, String value) {
        Log.d(this.getClass().getSimpleName(), String.format("Key [%s] - Value [%s]", key.name(), value));
        String stat = String.format(INSERT_STAT, key.name(), value);
        Log.d("Insert Statement", stat);
        getWritableDatabase().execSQL(stat);
        getWritableDatabase().close();
    }

    public void insertIfNotExists(KEY_NAME key, String value) {
        if (findByValue(value).equals("")){
            insert(key, value);
        }
    }

    public boolean isNotificationEnabled() {
        String number = findByKey(KEY_NAME.NOTIFICATION_NUMBER);
        if (number.equals("0")) {
            Log.d("SettingDAO", "Notification is disabled");
            return false;
        }
        return true;
    }

    public String getNotificationNumber() {
        return findByKey(KEY_NAME.NOTIFICATION_NUMBER);
    }
}
