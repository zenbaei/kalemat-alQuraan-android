package org.zenbaei.kalematAlQuraan.common.db;

/**
 * Created by zenbaei on 7/15/17.
 */

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.database.MatrixCursor;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.language.entity.Language;
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
 * This creates/opens the database.
 */
public class AppSqliteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "KalematDatabase";
    public static final String DATABASE_NAME = "kalemat_alQuraan";
    public static final int DATABASE_VERSION = 3;

    //The columns we'll include in the dictionary table
    public static final String KALEMAH = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String SURAH = SearchManager.SUGGEST_COLUMN_TEXT_2;
    public static final String AYAH_NUMBER = Ayah.NUMBER_COLUMN;
    public static final String TAFSIR = Tafsir.TAFSIR_COLUMN;
    public static final String KALEMAH_ABSTRACTD = "KALEMAH_ABSTRACTED";

    public static final String FTS_VIRTUAL_TABLE = "FTSdictionary";

    private static final String INSERT_FONT_SIZE_STAT = "INSERT INTO SETTINGS (key, value) VALUES ('DEFAULT_FONT_SIZE', '15')";
    private static final String INSERT_FONT_COLOR_STAT = "INSERT INTO SETTINGS (key, value) VALUES ('FONT_COLOR', '-48060')"; //red
    private static final String INSERT_BACKGROUND_COLOR_STAT = "INSERT INTO SETTINGS (key, value) VALUES ('BACKGROUND_COLOR', '-32')"; //yellow
    private static final String INSERT_NIGHT_MODE_STAT = "INSERT INTO SETTINGS (key, value) VALUES ('NIGHT_MODE', 'false')";

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

    public AppSqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mHelperContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mDatabase = db;
        executeDatabaseDropStatements();
        executeDatabaseCreateStatements();
        loadKalemat();
        insertDefaultValues();
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

    private void insertDefaultValues() {
        Log.d("insertDefaultFontSize", INSERT_FONT_SIZE_STAT);
        mDatabase.execSQL(INSERT_FONT_SIZE_STAT);
        mDatabase.execSQL(INSERT_FONT_COLOR_STAT);
        mDatabase.execSQL(INSERT_BACKGROUND_COLOR_STAT);
        mDatabase.execSQL(INSERT_NIGHT_MODE_STAT);
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

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}