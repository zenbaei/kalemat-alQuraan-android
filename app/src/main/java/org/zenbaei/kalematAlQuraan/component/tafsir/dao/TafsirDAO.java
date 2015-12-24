package org.zenbaei.kalematAlQuraan.component.tafsir.dao;

import android.content.Context;
import android.database.Cursor;

import org.zenbaei.kalematAlQuraan.common.dao.AbstractDAO;
import org.zenbaei.kalematAlQuraan.component.tafsir.entity.Tafsir;

/**
 * Created by Islam on 11/18/2015.
 */
public class TafsirDAO extends AbstractDAO<Tafsir> {

    public TafsirDAO(Context context) {
        super(context, new Tafsir());
    }

    @Override
    protected Tafsir cursorToEntity(Cursor cursor) {
        return null;
    }



}

