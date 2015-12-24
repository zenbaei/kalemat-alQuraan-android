package org.zenbaei.kalematAlQuraan.component.surah.dao;

import android.content.Context;
import android.database.Cursor;

import org.zenbaei.kalematAlQuraan.common.dao.AbstractDAO;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;

/**
 * Created by Islam on 11/9/2015.
 */
public class SurahDAO extends AbstractDAO<Surah> {


    public SurahDAO(Context context) {
        super(context, new Surah());
    }

    @Override
    protected Surah cursorToEntity(Cursor cursor) {
        Surah surah = new Surah();
        surah.setId(cursor.getLong(0));
        surah.setName(cursor.getString(1));
        return surah;
    }
}

