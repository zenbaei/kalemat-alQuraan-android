package org.zenbaei.kalematAlQuraan.component.search;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.component.R;

import java.util.List;

public class SearchListAdapter extends ArrayAdapter {

    private Context context;
    private int resource;
    private List<String[]> objects;

    public SearchListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(resource, parent, false); //row here is fav_list_item.xml

        TextView id = row.findViewById(R.id.ayahNumberTextView);
        TextView ayah = row.findViewById(R.id.kalemahTextView);
        TextView surah = row.findViewById(R.id.surahNameTextView);

        String[] strings = objects.get(position);

        id.setText(strings[0]);
        ayah.setText(strings[1]);
        surah.setText(strings[2]);

        id.setTextSize(Initializer.getFontSize());
        ayah.setTextSize(Initializer.getFontSize());
        surah.setTextSize(Initializer.getFontSize());

        setColor(id, ayah, surah);
        return row;
    }


    private void setColor(TextView id, TextView ayah, TextView tafsir) {
        id.setTextColor(Initializer.getNonAyahFontColor());
        ayah.setTextColor(Initializer.getFontColor());
        tafsir.setTextColor(Initializer.getNonAyahFontColor());
    }

}
