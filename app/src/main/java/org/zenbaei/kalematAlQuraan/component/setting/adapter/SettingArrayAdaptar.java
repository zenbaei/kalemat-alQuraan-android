package org.zenbaei.kalematAlQuraan.component.setting.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

import java.util.List;
import java.util.Set;

public class SettingArrayAdaptar extends ArrayAdapter<Setting> {

        private Context context;
        private int resource;
        private List<Setting> objects;

        public SettingArrayAdaptar(Context context, int resource, List<Setting> objects) {
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
            TextView id = (TextView)
                    row.findViewById(R.id.settingId);
            TextView kalemah = (TextView)
                    row.findViewById(R.id.kalemahFav);
            TextView tafsir = (TextView)
                    row.findViewById(R.id.tafsirFav);

            Setting setting = (Setting)objects.get(position);

            id.setText(String.valueOf(setting.getId()));
            String[] kalemahAndTafsir = setting.getValue().split("#");
            kalemah.setText("\"" + kalemahAndTafsir[0] +"\": " );
            tafsir.setText(kalemahAndTafsir[1]);
            return row;
        }

}


