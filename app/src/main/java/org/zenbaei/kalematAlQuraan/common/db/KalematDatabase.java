package org.zenbaei.kalematAlQuraan.common.db;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.tafsir.entity.Tafsir;

import java.util.HashMap;

/**
 * Created by Islam on 11/14/2015.
 *
 * adb forward tcp:1970 localabstract:android.sdk.conroller
 */
public class KalematDatabase {

    private AppSqliteOpenHelper mDatabaseOpenHelper;
    private static final HashMap<String, String> mColumnMap = buildColumnMap();
    private static final String LIKE_QUERY = " LIKE ?";
    private static final String LIKE_OPERATOR = "%";
    private static final String WORD_MATCHES_SELECTION = AppSqliteOpenHelper.KALEMAH_ABSTRACTD + LIKE_QUERY;
    private static final String ID_MATCH_SELECTION = "rowid = ?";

    /*
   * Instantiates an open helper for the provider's SQLite data repository
   * Do not do database creation and upgrade here.
   */
    public KalematDatabase(Context context) {
        mDatabaseOpenHelper = new AppSqliteOpenHelper(context);
    }

    public SQLiteDatabase getWritableDatabase() {
        return mDatabaseOpenHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return mDatabaseOpenHelper.getReadableDatabase();
    }


    /**
     * Returns a Cursor over all words that match the given query
     *
     * @param query   The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all words that match, or null if none found.
     */
    public Cursor getWordMatches(String query, String[] columns) {
                //" MATCH ?";
        String[] selectionArgs = new String[]{LIKE_OPERATOR + query + LIKE_OPERATOR};
            //new String[]{"*" + query + "*"};

        return query(WORD_MATCHES_SELECTION, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE <KEY_WORD> MATCH 'query*'
         * which is an FTS3 search for the query text (plus a wildcard) inside the word column.
         *
         * - "rowid" is the unique id for all rows but we need this value for the "_id" column in
         *    order for the Adapters to work, so the columns need to make "_id" an alias for "rowid"
         * - "rowid" also needs to be used by the SUGGEST_COLUMN_INTENT_DATA alias in order
         *   for suggestions to carry the proper intent data.
         *   These aliases are defined in the DictionaryProvider when queries are made.
         * - This can be revised to also search the definition text with FTS3 by changing
         *   the selection clause to use FTS_VIRTUAL_TABLE instead of KEY_WORD (to search across
         *   the entire table, but sorting the relevance could be difficult.
         */
    }


    /**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param rowId   id of word to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
    public Cursor getWord(String rowId, String[] columns) {
        String[] selectionArgs = new String[]{rowId};
        return query(ID_MATCH_SELECTION, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE rowid = <rowId>
         */
    }

    /**
     * Performs a database query.
     *
     * @param selection     The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns       The columns to return
     * @return A Cursor over all rows matching the query
     */
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(AppSqliteOpenHelper.FTS_VIRTUAL_TABLE);
        builder.setProjectionMap(mColumnMap);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        /* for test purpose
        String sql = "SELECT rowid AS _id, suggest_text_1, suggest_text_2, rowid AS suggest_intent_data_id FROM FTSdictionary " +
                "WHERE KALEMAH_ABSTRACTED LIKE '%s'";
        String parameter = selectionArgs.length == 0 ? "" : selectionArgs[0];
        String sqlWithParameter = String.format(sql, LIKE_OPERATOR + parameter + LIKE_OPERATOR);
                //"(KALEMAH_ABSTRACTED MATCH '*')";
        Cursor cursor = mDatabaseOpenHelper.getReadableDatabase().rawQuery(sqlWithParameter, null);
        */

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            mDatabaseOpenHelper.getReadableDatabase().close();
            return null;
        }
        return cursor;
    }

    /**
     * Builds a map for all columns that may be requested, which will be given to the
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */
    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(AppSqliteOpenHelper.KALEMAH, AppSqliteOpenHelper.KALEMAH);
        map.put(AppSqliteOpenHelper.SURAH, AppSqliteOpenHelper.SURAH);
        map.put(AppSqliteOpenHelper.AYAH_NUMBER, AppSqliteOpenHelper.AYAH_NUMBER);
        map.put(AppSqliteOpenHelper.TAFSIR, AppSqliteOpenHelper.TAFSIR);
        map.put(AppSqliteOpenHelper.KALEMAH_ABSTRACTD, AppSqliteOpenHelper.KALEMAH_ABSTRACTD);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }

}
