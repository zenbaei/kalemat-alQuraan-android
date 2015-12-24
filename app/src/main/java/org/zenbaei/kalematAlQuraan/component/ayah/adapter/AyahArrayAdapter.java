package org.zenbaei.kalematAlQuraan.component.ayah.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;

/**
 * Created by Islam on 12/17/2015.
 */
public class AyahArrayAdapter extends ArrayAdapter<Ayah> {

    private Context context;
    private int resource;
    private Ayah[] objects;

    public AyahArrayAdapter(Context context,
                            int resource,
                            Ayah[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(resource, parent, false);
        TextView number = (TextView)
                row.findViewById(R.id.ayahNumberTextView);
        TextView kalemah = (TextView)
                row.findViewById(R.id.kalemahTextView);
        TextView surah = (TextView)
                row.findViewById(R.id.surahNameTextView);
        number.setText(String.valueOf(objects[position].getNumber()));
        kalemah.setText(objects[position].getKalemah());
        surah.setText(objects[position].getSurah().getName());
        return row;
    }
}
