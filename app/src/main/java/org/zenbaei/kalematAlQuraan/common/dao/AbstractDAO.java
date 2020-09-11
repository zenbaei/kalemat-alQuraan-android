package org.zenbaei.kalematAlQuraan.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.common.db.KalematDatabase;
import org.zenbaei.kalematAlQuraan.common.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Islam on 11/13/2015.
 */
public abstract class AbstractDAO<T extends AbstractEntity> {

    // Database fields
    protected KalematDatabase kalematDatabase;
    private T entity;

    public AbstractDAO(Context context, T entity) {
        this.kalematDatabase = new KalematDatabase(context);
        this.entity = entity;
    }

    protected SQLiteDatabase getWritableDatabase() throws SQLException {
        return kalematDatabase.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() throws SQLException {
        return kalematDatabase.getReadableDatabase();
    }

    public T create(ContentValues values) {
        //values.put(SuwarDBInterface.NAME_COLUMN, name);
        long insertId = getWritableDatabase().insert(entity.getTableName(), null,
                values);
        Cursor cursor = getReadableDatabase().query(entity.getTableName(),
                entity.getColumns(), entity.ID_COLUMN + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        T entity = cursorToEntity(cursor);
        cursor.close();


        return entity;
    }


    public void delete(T entity) {
        long id = entity.getId();
        Log.i(AbstractDAO.class.getSimpleName(), String.format("Entity %s deleted with id: %s", entity.getClass().getSimpleName(), id));
        getWritableDatabase().delete(entity.getTableName(), entity.ID_COLUMN
                + " = " + id, null);

    }

    public List<T> findAll() {
        List<T> entities = new ArrayList<T>();

        Cursor cursor = getReadableDatabase().query(entity.getTableName(),
                entity.getColumns(), null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T entity = cursorToEntity(cursor);
            entities.add(entity);
            cursor.moveToNext();
        }
        // make sure to closeConnection the cursor
        cursor.close();

        return entities;
    }

    protected abstract T cursorToEntity(Cursor cursor);
}
