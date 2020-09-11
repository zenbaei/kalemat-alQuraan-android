package org.zenbaei.kalematAlQuraan.component.ayah.dao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.common.dao.AbstractDAO;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;
import org.zenbaei.kalematAlQuraan.component.tafsir.entity.Tafsir;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Islam on 11/13/2015.
 */
public class AyahDAO extends AbstractDAO<Ayah> {

    public AyahDAO(Context context) {
        super(context, new Ayah());
    }

    @Override
    protected Ayah cursorToEntity(Cursor cursor) {
        Ayah ayah = new Ayah();
        ayah.setId(cursor.getLong(0));
        ayah.setKalemah(cursor.getString(1));
        ayah.setNumber(cursor.getLong(2));
        ayah.setSurah(new Surah(cursor.getLong(3)));
        return ayah;
    }

    public List<Ayah> findBySurahIdLanguageId(long surahId, long languageId, int from, int to) {
        return findByAsList(Ayah.SURAH_ID_COLUMN, surahId, languageId, from, to, false);
    }

    public List<Ayah> findBySurahIdLanguageId(long surahId, long languageId) {
        return findByAsListNoLimit(Ayah.SURAH_ID_COLUMN, surahId, languageId, false);
    }

    private List<Ayah> findByAsList(String column, Object value, long languageId, int from, int to, boolean like) {
        List<Ayah> ayahList;
        String query = String.format("SELECT ayah.%s, ayah.%s, ayah.%s, tafsir.%s FROM %s ayah,%s tafsir WHERE %s AND %s AND %s ORDER BY %s LIMIT %s, %s ;",
                Ayah.ID_COLUMN, Ayah.NUMBER_COLUMN, Ayah.KALEMAH_COLUMN, Tafsir.TAFSIR_COLUMN, Ayah.TABLE_NAME, Tafsir.TABLE_NAME,
                String.format("ayah.%s %s", column, like ? "LIKE '%" + value + "%'" : "= " + value),
                String.format("ayah.%s = tafsir.%s", Ayah.ID_COLUMN, Tafsir.AYAH_ID_COLUMN),
                String.format("tafsir.%s = %s", Tafsir.LANGUAGE_ID_COLUMN, languageId),
                "ayah._ID",
                from,
                to
        );
        Log.i(AyahDAO.class.getSimpleName(), query);

        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        //TODO: if cursor is empty then return
        ayahList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ayah ayah = new Ayah();
            Tafsir tafsir = new Tafsir();
            ayah.setTafsir(tafsir);

            ayah.setId(cursor.getLong(0));
            ayah.setNumber(cursor.getLong(1));
            ayah.setKalemah(cursor.getString(2));
            tafsir.setTafsir(cursor.getString(3));

            ayahList.add(ayah);

            cursor.moveToNext();
        }

        cursor.close();

        return ayahList;
    }

    private List<Ayah> findByAsListNoLimit(String column, Object value, long languageId, boolean like) {
        List<Ayah> ayahList;
        String query = String.format("SELECT ayah.%s, ayah.%s, ayah.%s, tafsir.%s FROM %s ayah,%s tafsir WHERE %s AND %s AND %s ORDER BY %s ;",
                Ayah.ID_COLUMN, Ayah.NUMBER_COLUMN, Ayah.KALEMAH_COLUMN, Tafsir.TAFSIR_COLUMN, Ayah.TABLE_NAME, Tafsir.TABLE_NAME,
                String.format("ayah.%s %s", column, like ? "LIKE '%" + value + "%'" : "= " + value),
                String.format("ayah.%s = tafsir.%s", Ayah.ID_COLUMN, Tafsir.AYAH_ID_COLUMN),
                String.format("tafsir.%s = %s", Tafsir.LANGUAGE_ID_COLUMN, languageId),
                "ayah._ID"
        );
        Log.i(AyahDAO.class.getSimpleName(), query);

        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        //TODO: if cursor is empty then return
        ayahList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ayah ayah = new Ayah();
            Tafsir tafsir = new Tafsir();
            ayah.setTafsir(tafsir);

            ayah.setId(cursor.getLong(0));
            ayah.setNumber(cursor.getLong(1));
            ayah.setKalemah(cursor.getString(2));
            tafsir.setTafsir(cursor.getString(3));

            ayahList.add(ayah);

            cursor.moveToNext();
        }

        cursor.close();

        return ayahList;
    }

    private void findBy(String column, Object value, long languageId, int from, int to, boolean like) {
        List<Ayah> ayahList;
        String query = String.format("SELECT ayah.%s, ayah.%s, ayah.%s, tafsir.%s FROM %s ayah,%s tafsir WHERE %s AND %s AND %s ORDER BY %s LIMIT %s, %s ;",
                Ayah.ID_COLUMN, Ayah.NUMBER_COLUMN, Ayah.KALEMAH_COLUMN, Tafsir.TAFSIR_COLUMN, Ayah.TABLE_NAME, Tafsir.TABLE_NAME,
                String.format("ayah.%s %s", column, like ? "LIKE '%" + value + "%'" : "= " + value),
                String.format("ayah.%s = tafsir.%s", Ayah.ID_COLUMN, Tafsir.AYAH_ID_COLUMN),
                String.format("tafsir.%s = %s", Tafsir.LANGUAGE_ID_COLUMN, languageId),
                "ayah._ID",
                from,
                to
        );
        Log.i(AyahDAO.class.getSimpleName(), query);

        Cursor cursor = getReadableDatabase().rawQuery(query, null);

    }


    private List<Ayah> findByAsList(String column, Object value, boolean like) {
        List<Ayah> ayahList;
        String query = String.format("SELECT ayah.%s, ayah.%s, ayah.%s, surah.%s FROM %s ayah,%s surah WHERE %s AND %s ORDER BY %s;",
                Ayah.ID_COLUMN, Ayah.NUMBER_COLUMN, Ayah.KALEMAH_COLUMN, Surah.NAME_COLUMN, Ayah.TABLE_NAME, Surah.TABLE_NAME,
                String.format("ayah.%s %s", column, like ? "LIKE '%" + value + "%'" : "= " + value),
                String.format("ayah.%s = surah.%s", Ayah.SURAH_ID_COLUMN, Surah.ID_COLUMN),
                "ayah._ID"
        );
        Log.i(AyahDAO.class.getSimpleName(), query);

        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        //TODO: if cursor is empty then return
        ayahList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ayah ayah = new Ayah();
            Surah surah = new Surah();
            ayah.setSurah(surah);

            ayah.setId(cursor.getLong(0));
            ayah.setNumber(cursor.getLong(1));
            ayah.setKalemah(cursor.getString(2));
            surah.setName(cursor.getString(3));

            ayahList.add(ayah);

            cursor.moveToNext();
        }

        cursor.close();
        return ayahList;
    }

    private Cursor findBy(String column, Object value, boolean like) {
        List<Ayah> ayahList;
        String query = String.format("SELECT ayah.%s, ayah.%s, ayah.%s, surah.%s FROM %s ayah,%s surah WHERE %s AND %s ORDER BY %s;",
                Ayah.ID_COLUMN, Ayah.NUMBER_COLUMN, Ayah.KALEMAH_COLUMN, Surah.NAME_COLUMN, Ayah.TABLE_NAME, Surah.TABLE_NAME,
                String.format("ayah.%s %s", column, like ? "LIKE '%" + value + "%'" : "= " + value),
                String.format("ayah.%s = surah.%s", Ayah.SURAH_ID_COLUMN, Surah.ID_COLUMN),
                "ayah._ID"
        );
        Log.i(AyahDAO.class.getSimpleName(), query);

        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public int getCount(String column, Object value, boolean like) {
        int count = 0;
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s;", Ayah.TABLE_NAME, String.format("%s %s", column, like ? "LIKE '%" + value + "%'" : "= " + value));
        Log.i(AyahDAO.class.getSimpleName(), query);

        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);

        return count;
    }

    public List<Ayah> findByAyahNumberLanguageId(Long number, long languageId, Integer from, Integer to, boolean like) {
        return findByAsList(Ayah.NUMBER_COLUMN, number, languageId, from, to, like);
    }

    public List<Ayah> findByKalemahLanguageId(String kalemah, long languageId, Integer from, Integer to, boolean like) {
        return findByAsList(Ayah.KALEMAH_COLUMN, kalemah, languageId, from, to, like);
    }

    public int getCountByAyahNumber(long ayahNumber, boolean like) {
        return getCount(Ayah.NUMBER_COLUMN, ayahNumber, like);
    }

    public int getCountByKalemah(String kalemah, boolean like) {
        return getCount(Ayah.KALEMAH_COLUMN, kalemah, like);
    }

    public int getCountBySurahId(long surahId, boolean like) {
        return getCount(Ayah.SURAH_ID_COLUMN, surahId, like);
    }

    public List<Ayah> findAyahJoinSurahByNumberAsList(long ayahNumber, boolean like) {
        return findByAsList(Ayah.NUMBER_COLUMN, ayahNumber, like);
    }

    public List<Ayah> findAyahJoinSurahByKalemahAsList(String kalemah, boolean like) {
        return findByAsList(Ayah.KALEMAH_COLUMN, kalemah, like);
    }

    public Cursor findAyahJoinSurahByNumber(long ayahNumber, boolean like) {
        return findBy(Ayah.NUMBER_COLUMN, ayahNumber, like);
    }

    public Cursor findAyahJoinSurahByKalemah(String kalemah, boolean like) {
        return findBy(Ayah.KALEMAH_COLUMN, kalemah, like);
    }
}