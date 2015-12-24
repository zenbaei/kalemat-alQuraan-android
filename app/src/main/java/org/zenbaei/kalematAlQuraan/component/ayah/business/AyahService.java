package org.zenbaei.kalematAlQuraan.component.ayah.business;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.component.ayah.dao.AyahDAO;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;

import java.util.List;

/**
 * Created by Islam on 12/11/2015.
 */
public class AyahService {

    private AyahDAO ayahDAO;

    public AyahService(Context context) {
        ayahDAO = new AyahDAO(context);
    }

    public List<Ayah> findBySurahIdLanguageId(long surahId, long languageId, int from, int to) {
        return ayahDAO.findBySurahIdLanguageId(surahId, languageId, from, to);
    }

    public List<Ayah> findByAyahNumberLanguageId(long number, long languageId, Integer from, Integer to, boolean like) {
        return ayahDAO.findByAyahNumberLanguageId(number, languageId, from, to, like);
    }

    public List<Ayah> findByKalemahLanguageId(String kalemah, long languageId, Integer from, Integer to, boolean like) {
        return ayahDAO.findByKalemahLanguageId(kalemah, languageId, from, to, like);
    }

    public int getCountBySurahId(long surahId, boolean like) {
        return ayahDAO.getCountBySurahId(surahId, like);
    }

    public int getCountByAyahNumber(long ayahNumber, boolean like) {
        return ayahDAO.getCountByAyahNumber(ayahNumber, like);
    }

    public int getCountByKalemah(String kalemah, boolean like) {
        return ayahDAO.getCountByKalemah(kalemah, like);
    }


    public List<Ayah> findAyahJoinSurahByNumberOrKalemahAsList(String query) {
        try {
            Long number = Long.parseLong(query.trim());
            return ayahDAO.findAyahJoinSurahByNumberAsList(number, false);
        } catch (NumberFormatException ex) {
            Log.d(AyahService.class.getSimpleName(), query + " is not a valid number.");
        }
        return ayahDAO.findAyahJoinSurahByKalemahAsList(query.trim(), true);
    }

    public Cursor findAyahJoinSurahByNumberOrKalemah(String query) {
        try {
            Long number = Long.parseLong(query.trim());
            return ayahDAO.findAyahJoinSurahByNumber(number, false);
        } catch (NumberFormatException ex) {
            Log.d(AyahService.class.getSimpleName(), query + " is not a valid number.");
        }
        return ayahDAO.findAyahJoinSurahByKalemah(query.trim(), true);
    }
}
