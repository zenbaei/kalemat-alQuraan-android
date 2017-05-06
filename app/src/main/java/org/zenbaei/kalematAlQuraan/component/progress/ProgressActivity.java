package org.zenbaei.kalematAlQuraan.component.progress;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.db.KalematDatabase;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.author.AuthorActivity;
import org.zenbaei.kalematAlQuraan.component.ayah.view.AyahActivity;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;
import org.zenbaei.kalematAlQuraan.component.surah.business.SurahService;
import org.zenbaei.kalematAlQuraan.component.surah.view.SurahActivity;

/**
 * Created by Islam on 2/5/2016.
 */
public class ProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView progressText;
    private Handler handler = new Handler();
    private SettingDAO settingDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingDAO = new SettingDAO(this);

        setContentView(R.layout.progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        progressText.setText(getString(R.string.loading_data));

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
            Intent intent;
            String lastReadPage = settingDAO.findByKey(Setting.KEY_NAME.LAST_READ_PAGE);

            if (lastReadPage.isEmpty()) {
                settingDAO.insert(Setting.KEY_NAME.LAST_READ_PAGE, "0");
                settingDAO.insert(Setting.KEY_NAME.LAST_READ_SURAH_ID, "0");
                settingDAO.insert(Setting.KEY_NAME.LAST_READ_SURAH_NAME, "");
            } else if (Integer.valueOf(lastReadPage) > 0) {
                String lastReadSurahId = settingDAO.findByKey(Setting.KEY_NAME.LAST_READ_SURAH_ID);
                String lastReadSurahName = settingDAO.findByKey(Setting.KEY_NAME.LAST_READ_SURAH_NAME);
                intent = new Intent(getApplicationContext(), AyahActivity.class);
                intent.putExtra(Setting.KEY_NAME.LAST_READ_PAGE.name(), Integer.valueOf(lastReadPage));
                intent.putExtra(Setting.KEY_NAME.LAST_READ_SURAH_ID.name(), Integer.valueOf(lastReadSurahId));
                intent.putExtra(Setting.KEY_NAME.LAST_READ_SURAH_NAME.name(), lastReadSurahName);
                startActivity(intent);
                return;
            }

            intent = new Intent(getApplicationContext(), AuthorActivity.class);
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
