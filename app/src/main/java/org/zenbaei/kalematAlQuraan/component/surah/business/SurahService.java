package org.zenbaei.kalematAlQuraan.component.surah.business;

import android.content.Context;

import org.zenbaei.kalematAlQuraan.component.surah.dao.SurahDAO;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;

import java.util.List;

/**
 * Created by Islam on 12/11/2015.
 */
public class SurahService {
    private SurahDAO surahDAO;

    public SurahService(Context context) {
        surahDAO = new SurahDAO(context);
    }

    public List<Surah> findAll() {
        return surahDAO.findAll();
    }
}
