package org.zenbaei.kalematAlQuraan.component.progress;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.notification.Alarm;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;
import org.zenbaei.kalematAlQuraan.component.surah.business.SurahService;
import org.zenbaei.kalematAlQuraan.component.surah.view.SurahActivity;

/**
 * Created by Islam on 2/5/2016.
 */
public class ProgressActivity extends AppCompatActivity {

    private TextView progressText;
    private SettingDAO settingDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingDAO = new SettingDAO(this);

        setContentView(R.layout.progress);
        progressText = (TextView) findViewById(R.id.progressText);
        progressText.setText(getString(R.string.loading_data));
        Initializer.execute(settingDAO);
        MyTask myTask = new MyTask();
        myTask.execute();
    }

    class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                SurahService surahService = new SurahService(getApplicationContext());
                surahService.findAll();
            } catch (Exception ex) {

            }

            return "completed";
        }

        @Override
        protected void onPostExecute(String result) {
            addNewFeatures();
            Intent intent = new Intent(getApplicationContext(), SurahActivity.class);
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    private void addNewFeatures() {
        addNotificationOnFirstRun();
    }

    /**
     * To avoid adding Alarm every time Progress activity runs (every startup)
     */
    private void addNotificationOnFirstRun() {
        String notification = settingDAO.findByKey(Setting.KEY_NAME.NOTIFICATION_NUMBER);
        if (notification.isEmpty()) {
            settingDAO.insert(Setting.KEY_NAME.NOTIFICATION_NUMBER, "2");
            new Alarm(getApplicationContext()).addAlarm();
        }

     }
}
