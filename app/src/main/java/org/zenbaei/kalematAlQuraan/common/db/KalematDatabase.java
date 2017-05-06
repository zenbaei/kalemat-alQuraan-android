package org.zenbaei.kalematAlQuraan.common.db;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.language.entity.Language;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;
import org.zenbaei.kalematAlQuraan.component.tafsir.entity.Tafsir;
import org.zenbaei.kalematAlQuraan.utils.ArabicUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Islam on 11/14/2015.
 *
 * adb forward tcp:1970 localabstract:android.sdk.conroller
 */
public class KalematDatabase {

    private static final String TAG = "KalematDatabase";
    public static final String DATABASE_NAME = "kalemat_alQuraan";
    public static final int DATABASE_VERSION = 2;

    //The columns we'll include in the dictionary table
    public static final String KALEMAH = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String SURAH = SearchManager.SUGGEST_COLUMN_TEXT_2;
    public static final String AYAH_NUMBER = Ayah.NUMBER_COLUMN;
    public static final String TAFSIR = Tafsir.TAFSIR_COLUMN;
    public static final String KALEMAH_ABSTRACTD = "KALEMAH_ABSTRACTED";

    private static final String FTS_VIRTUAL_TABLE = "FTSdictionary";
    private static final HashMap<String, String> mColumnMap = buildColumnMap();
    private final KalematOpenHelper mDatabaseOpenHelper;
    private static final String INSERT_FONT_SIZE_STAT = "INSERT INTO SETTINGS (key, value) VALUES ('DEFAULT_FONT_SIZE', '15')";

    /*
   * Instantiates an open helper for the provider's SQLite data repository
   * Do not do database creation and upgrade here.
   */
    public KalematDatabase(Context context) {
        mDatabaseOpenHelper = new KalematOpenHelper(context);
    }

    public SQLiteDatabase getWritableDatabase() {
        return mDatabaseOpenHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return mDatabaseOpenHelper.getReadableDatabase();
    }


    /**
     * Builds a map for all columns that may be requested, which will be given to the
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */
    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KALEMAH, KALEMAH);
        map.put(SURAH, SURAH);
        map.put(AYAH_NUMBER, AYAH_NUMBER);
        map.put(TAFSIR, TAFSIR);
        map.put(KALEMAH_ABSTRACTD, KALEMAH_ABSTRACTD);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }


    /**
     * Returns a Cursor over all words that match the given query
     *
     * @param query   The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all words that match, or null if none found.
     */
    public Cursor getWordMatches(String query, String[] columns) {
        String selection = KALEMAH_ABSTRACTD + " MATCH ?";
        String[] selectionArgs = new String[]{"*" + query + "*"};

        return query(selection, selectionArgs, columns);

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
        String selection = "rowid = ?";
        String[] selectionArgs = new String[]{rowId};

        return query(selection, selectionArgs, columns);

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
        builder.setTables(FTS_VIRTUAL_TABLE);
        builder.setProjectionMap(mColumnMap);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    /**
     * This creates/opens the database.
     */
    private static class KalematOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        /* Note that FTS3 does not support column constraints and thus, you cannot
         * declare a primary key. However, "rowid" is automatically used as a unique
         * identifier, so when making requests, we will use "_id" as an alias for "rowid"
         */
        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        KALEMAH + ", " +
                        SURAH + "," +
                        Ayah.NUMBER_COLUMN + "," +
                        Tafsir.TAFSIR_COLUMN + "," +
                        KALEMAH_ABSTRACTD +
                        ");";

        KalematOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            executeDatabaseDropStatements();
            executeDatabaseCreateStatements();
            loadKalemat();
            insertDefaultFontSize();
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            onCreate(db);
        }

        /**
         * Starts a thread to load the database table with words
         */
        private void loadKalemat() {
            try {
                //loadWords();
                insertData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
           /* new Thread(new Runnable() {
                public void run() {
                    try {
                        //loadWords();
                        insertData();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();*/
        }

        private void executeDatabaseCreateStatements() {
            Log.d(TAG, "Creating Tables...");

            Log.d(TAG, FTS_TABLE_CREATE);
            mDatabase.execSQL(FTS_TABLE_CREATE);

            Log.d(TAG, Surah.DATABASE_CREATE);
            mDatabase.execSQL(Surah.DATABASE_CREATE);

            Log.d(TAG, Ayah.DATABASE_CREATE);
            mDatabase.execSQL(Ayah.DATABASE_CREATE);

            Log.d(TAG, Tafsir.DATABASE_CREATE);
            mDatabase.execSQL(Tafsir.DATABASE_CREATE);

            Log.d(TAG, Language.DATABASE_CREATE);
            mDatabase.execSQL(Language.DATABASE_CREATE);

            Log.d("Creating SETTINGS table", Setting.CREATE_TABLE);
            mDatabase.execSQL(Setting.CREATE_TABLE);
        }

        private void executeDatabaseDropStatements() {
            Log.d(TAG, "Dropping Tables...");

            String virutalTable = String.format("DROP TABLE IF EXISTS %s;", FTS_VIRTUAL_TABLE);
            Log.d(TAG, virutalTable);
            mDatabase.execSQL(virutalTable);

            Log.d(TAG, Tafsir.DATABASE_DROP);
            mDatabase.execSQL(Tafsir.DATABASE_DROP);

            Log.d(TAG, Language.DATABASE_DROP);
            mDatabase.execSQL(Language.DATABASE_DROP);

            Log.d(TAG, Ayah.DATABASE_DROP);
            mDatabase.execSQL(Ayah.DATABASE_DROP);

            Log.d(TAG, Surah.DATABASE_DROP);
            mDatabase.execSQL(Surah.DATABASE_DROP);

            Log.d("Drop SETTINGS table", Setting.DROP_TABLE);
            mDatabase.execSQL(Setting.DROP_TABLE);
        }

        private void insertDefaultFontSize() {
            Log.d("insertDefaultFontSize", INSERT_FONT_SIZE_STAT);
            mDatabase.execSQL(INSERT_FONT_SIZE_STAT);
        }

        private void insertData() throws IOException {
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(mHelperContext.getResources().openRawResource(R.raw.kalemat), "UTF-8"));

            String str = null;
            while ((str = in.readLine()) != null) {
                if (str.isEmpty() || str.contains("--"))
                    continue;
                // Log.d(TAG, str);
                mDatabase.execSQL(str);
            }

            addWordsToSuggestionsTable();
        }
/*
        private void loadWords() throws IOException {
            Log.d(TAG, "Loading words...");
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.definitions);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, "-");
                    if (strings.length < 2) continue;
                    long id = addWord(strings[0].trim(), strings[1].trim());
                    if (id < 0) {
                        Log.e(TAG, "unable to add word: " + strings[0].trim());
                    }
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "DONE loading words.");
        }
*/

        /**
         * Add a word to the dictionary.
         *
         * @return rowId or -1 if failed
         */
        public void addWordsToSuggestionsTable() {
            Log.d(TAG, String.format("Inserting into %s table...", FTS_VIRTUAL_TABLE));
            for (ContentValues initialValues : getKalematView()) {
                // Log.d(TAG, String.format("Kalemah= %s, Surah= %s, Number= %s, Tafsir= %s", initialValues.get(KALEMAH), initialValues.get(SURAH), initialValues.get(AYAH_NUMBER), initialValues.get(TAFSIR)));
                mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
            }
        }

        private List<ContentValues> getKalematView() {
            String sql = String.format("SELECT a.%s, s.%s, a.%s, t.%s FROM %s a,%s s, %s t WHERE %s AND %s ORDER BY %s",
                    Ayah.KALEMAH_COLUMN,
                    Surah.NAME_COLUMN,
                    Ayah.NUMBER_COLUMN,
                    Tafsir.TAFSIR_COLUMN,
                    Ayah.TABLE_NAME,
                    Surah.TABLE_NAME,
                    Tafsir.TABLE_NAME,
                    String.format("a.%s = s.%s", Ayah.SURAH_ID_COLUMN, Surah.ID_COLUMN),
                    String.format("a.%s = t.%s", Ayah.ID_COLUMN, Tafsir.AYAH_ID_COLUMN),
                    String.format("a.%s, a.%s", Ayah.SURAH_ID_COLUMN, Ayah.ID_COLUMN)
            );
            Log.d(TAG, sql);
            return getKalematView(mDatabase.rawQuery(sql, null));
        }

        private List<ContentValues> getKalematView(Cursor cursor) {
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }

            List<ContentValues> list = new ArrayList<>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(KALEMAH, cursor.getString(0));
                initialValues.put(SURAH, cursor.getString(1));
                initialValues.put(AYAH_NUMBER, String.valueOf(cursor.getLong(2)));
                initialValues.put(TAFSIR, cursor.getString(3));
                initialValues.put(KALEMAH_ABSTRACTD, ArabicUtils.normalize(cursor.getString(0)));
                list.add(initialValues);
                cursor.moveToNext();
            }

            cursor.close();
            return list;
        }
    }

}
