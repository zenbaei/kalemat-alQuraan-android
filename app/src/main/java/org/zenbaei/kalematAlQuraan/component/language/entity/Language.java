package org.zenbaei.kalematAlQuraan.component.language.entity;

import org.zenbaei.kalematAlQuraan.common.entity.AbstractEntity;

import java.util.List;

/**
 * Created by Islam on 11/16/2015.
 */
public class Language extends AbstractEntity {

    public static final String TABLE_NAME = "LANGUAGES";
    public static final String LANGUAGE_COLUMN = "LANGUAGE";
    public static final String DATABASE_CREATE = String.format("CREATE TABLE %s (_ID INTEGER PRIMARY KEY, %s TEXT NOT NULL);", TABLE_NAME, LANGUAGE_COLUMN);
    public static final String DATABASE_DROP = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
    public static final String[] COLUMNS = {ID_COLUMN, LANGUAGE_COLUMN};

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    static {
        databaseCreateStatements.put(4, DATABASE_CREATE);
        databaseDropStatements.put(1, DATABASE_DROP);
    }
}
