package org.zenbaei.kalematAlQuraan.component.tafsir.entity;

import org.zenbaei.kalematAlQuraan.common.entity.AbstractEntity;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.language.entity.Language;

/**
 * Created by Islam on 11/16/2015.
 */
public class Tafsir extends AbstractEntity {

    public static final String TABLE_NAME = "TAFSIR";
    public static final String TAFSIR_COLUMN = "TAFSIR";
    public static final String LANGUAGE_ID_COLUMN = "LANGUAGE_ID";
    public static final String AYAH_ID_COLUMN = "AYAH_ID";
    public static final String DATABASE_CREATE = String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, FOREIGN KEY(%s) REFERENCES %s(_id), FOREIGN KEY(%s) REFERENCES %s(_id));", TABLE_NAME, TAFSIR_COLUMN, AYAH_ID_COLUMN, LANGUAGE_ID_COLUMN, AYAH_ID_COLUMN, Ayah.TABLE_NAME, LANGUAGE_ID_COLUMN, Language.TABLE_NAME);
    public static final String DATABASE_DROP = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
    public static final String[] COLUMNS = {ID_COLUMN, TAFSIR_COLUMN, AYAH_ID_COLUMN, LANGUAGE_ID_COLUMN};

    private String tafsir;

    //join column
    private Ayah ayah;

    private Language language;

    public Tafsir() {
    }

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    static {
        databaseCreateStatements.put(3, DATABASE_CREATE);
        databaseDropStatements.put(2, DATABASE_DROP);
    }

    public String getTafsir() {
        return tafsir;
    }

    public void setTafsir(String tafsir) {
        this.tafsir = tafsir;
    }

    public Ayah getAyah() {
        return ayah;
    }

    public void setAyah(Ayah ayah) {
        this.ayah = ayah;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Tafsir{" +
                "tafsir='" + tafsir + '\'' +
                '}';
    }
}
