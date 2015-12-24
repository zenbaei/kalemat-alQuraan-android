package org.zenbaei.kalematAlQuraan.common.entity;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Islam on 11/9/2015.
 */
public abstract class AbstractEntity {

    public static final String ID_COLUMN = BaseColumns._ID;
    protected long id;

    public static TreeMap<Integer, String> databaseCreateStatements = new TreeMap<>();
    public static TreeMap<Integer, String> databaseDropStatements = new TreeMap<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "id=" + id +
                '}';
    }

    public abstract String[] getColumns();

    public abstract String getTableName();
}
