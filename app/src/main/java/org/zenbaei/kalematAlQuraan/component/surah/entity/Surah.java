package org.zenbaei.kalematAlQuraan.component.surah.entity;

import org.zenbaei.kalematAlQuraan.common.entity.AbstractEntity;

/**
 * Created by Islam on 11/9/2015.
 */
public class Surah extends AbstractEntity {

    public static final String TABLE_NAME = "SURAH";
    public static final String NAME_COLUMN = "NAME";
    public static final String DATABASE_CREATE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL);", TABLE_NAME, ID_COLUMN, NAME_COLUMN);
    public static final String DATABASE_DROP = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
    private static final String[] COLUMNS = {ID_COLUMN,
            NAME_COLUMN};

    private String name;

    static {
        databaseCreateStatements.put(1, DATABASE_CREATE);
        databaseDropStatements.put(4, DATABASE_DROP);
    }

    public Surah() {
    }

    public Surah(long id) {
        super.id = id;
    }

    public Surah(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
