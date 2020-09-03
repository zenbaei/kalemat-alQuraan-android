package org.zenbaei.kalematAlQuraan.component.search;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.db.AppSqliteOpenHelper;
import org.zenbaei.kalematAlQuraan.component.R;

public class SearchCursorAdapter extends CursorAdapter {

    public SearchCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.result, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView id = view.findViewById(R.id.ayahNumberTextView);
        TextView ayah = view.findViewById(R.id.kalemahTextView);
        TextView surah = view.findViewById(R.id.surahNameTextView);

        id.setText(cursor.getString(cursor.getColumnIndex(AppSqliteOpenHelper.AYAH_NUMBER)));
        ayah.setText(cursor.getString(cursor.getColumnIndex(AppSqliteOpenHelper.KALEMAH)));
        surah.setText(cursor.getString(cursor.getColumnIndex(AppSqliteOpenHelper.SURAH)));

        setFontSize(id, ayah, surah);
        setColor(id, ayah, surah);
    }

    private void setColor(TextView id, TextView ayah, TextView surah) {
        id.setTextColor(Initializer.getNonAyahFontColor());
        ayah.setTextColor(Initializer.getFontColor());
        surah.setTextColor(Initializer.getNonAyahFontColor());
    }

    private void setFontSize(TextView id, TextView ayah, TextView surah) {
        id.setTextSize(Initializer.getFontSize());
        ayah.setTextSize(Initializer.getFontSize());
        surah.setTextSize(Initializer.getFontSize());
    }
}
