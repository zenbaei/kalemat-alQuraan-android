package org.zenbaei.kalematAlQuraan.common.notification;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import org.zenbaei.kalematAlQuraan.component.R;

public class NotificationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);

        TextView ayah = (TextView) findViewById(R.id.notification_ayah);
        TextView tafsir = (TextView) findViewById(R.id.notification_tafsir);
        TextView index = (TextView) findViewById(R.id.notification_index);

        ayah.setText(" \"" + getIntent().getStringExtra("ayah") + "\"");
        tafsir.setText(getIntent().getStringExtra("tafsir") + ".");
        index.setText(getIntent().getStringExtra("index"));
    }

    public void closeDialog(View view) {
        finish();
    }
}
