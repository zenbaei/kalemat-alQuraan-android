package org.zenbaei.kalematAlQuraan.component.ayah.entity;

import org.zenbaei.kalematAlQuraan.common.entity.AbstractEntity;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;
import org.zenbaei.kalematAlQuraan.component.tafsir.entity.Tafsir;

/**
 * Created by Islam on 11/13/2015.
 */
public class Ayah extends AbstractEntity {

    public static final String TABLE_NAME = "AYAH";
    public static final String KALEMAH_COLUMN = "KALEMAH";
    public static final String NUMBER_COLUMN = "NUMBER";
    public static final String SURAH_ID_COLUMN = "SURAH_ID";
    public static final String DATABASE_CREATE = String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY, %s TEXT NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, FOREIGN KEY(%s) REFERENCES %s(_id));", TABLE_NAME, KALEMAH_COLUMN, NUMBER_COLUMN, SURAH_ID_COLUMN, SURAH_ID_COLUMN, Surah.TABLE_NAME);
    public static final String DATABASE_DROP = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
    public static final String[] COLUMNS = {ID_COLUMN, KALEMAH_COLUMN, NUMBER_COLUMN, SURAH_ID_COLUMN};

    private String kalemah;

    private long number;

    //join column
    private Surah surah;

    private Tafsir tafsir;

    public Ayah() {
    }

    public Ayah(String kalemah, long number, Surah surah) {
        this.kalemah = kalemah;
        this.number = number;
        this.surah = surah;
    }

    public String getKalemah() {
        return kalemah;
    }

    public void setKalemah(String kalemah) {
        this.kalemah = kalemah;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Surah getSurah() {
        return surah;
    }

    public void setSurah(Surah surah) {
        this.surah = surah;
    }

    @Override
    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public Tafsir getTafsir() {
        return tafsir;
    }

    public void setTafsir(Tafsir tafsir) {
        this.tafsir = tafsir;
    }

    @Override
    public String toString() {
        return "Ayah{" +
                "kalemah='" + kalemah + '\'' +
                ", number=" + number +
                ", tafsir=" + tafsir +
                '}';
    }
}
