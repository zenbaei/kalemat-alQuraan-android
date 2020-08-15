package org.zenbaei.kalematAlQuraan.component.surah;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;

import java.util.List;

public class FilteredSurahAdaptar extends ArrayAdapter {

    private Context context;
    private int resource;
    private List<Surah> objects;

    public FilteredSurahAdaptar(Context context, int resource, List<Surah> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(resource, parent, false); //row here is surah_list_item.xml
        TextView index = (TextView)
                row.findViewById(R.id.index);

        Surah surah = (Surah)objects.get(position);
        index.setText(surah.toString());
        index.setTextSize(Initializer.getFontSize());
        index.setTextColor(Initializer.getFontColor());
        return row;
    }
}
