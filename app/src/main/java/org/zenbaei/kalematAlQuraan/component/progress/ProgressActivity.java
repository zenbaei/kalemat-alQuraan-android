package org.zenbaei.kalematAlQuraan.component.progress;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;

import android.widget.ProgressBar;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.notification.Alarm;
import org.zenbaei.kalematAlQuraan.common.notification.NotificationReceiver;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;
import org.zenbaei.kalematAlQuraan.component.surah.business.SurahService;
import org.zenbaei.kalematAlQuraan.component.surah.view.SurahActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Islam on 2/5/2016.
 */
public class ProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView progressText;
    private Handler handler = new Handler();
    private SettingDAO settingDAO;
    private static int ALARM_HOUR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingDAO = new SettingDAO(this);

        setContentView(R.layout.progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
        addLastReadPageFeature();
        int hour = Integer.valueOf(getResources().getString(R.string.alarm_hour));
        int minutes = Integer.valueOf(getResources().getString(R.string.alarm_minutes));
        new Alarm(getApplicationContext()).addAlarm(hour, minutes);
    }



    private void addLastReadPageFeature() {
        String lastReadPage = settingDAO.findByKey(Setting.KEY_NAME.LAST_READ_PAGE);

        // lastReadPage key is inserted in db?
        if (!lastReadPage.isEmpty()) {
            return;
        }

        settingDAO.insert(Setting.KEY_NAME.LAST_READ_PAGE, "0");
        settingDAO.insert(Setting.KEY_NAME.LAST_READ_SURAH_ID, "0");
        settingDAO.insert(Setting.KEY_NAME.LAST_READ_SURAH_NAME, "");
    }
}
