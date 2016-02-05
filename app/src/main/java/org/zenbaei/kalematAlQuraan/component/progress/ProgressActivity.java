package org.zenbaei.kalematAlQuraan.component.progress;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.db.KalematDatabase;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.author.AuthorActivity;
import org.zenbaei.kalematAlQuraan.component.ayah.view.AyahActivity;
import org.zenbaei.kalematAlQuraan.component.surah.view.SurahActivity;

/**
 * Created by Islam on 2/5/2016.
 */
public class ProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView progressText;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        MyTask myTask = new MyTask();
        myTask.execute();
    }

    class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressText.setText(getString(R.string.loading_data));
        }

        @Override
        protected String doInBackground(String... params) {
            KalematDatabase k = new KalematDatabase(getApplicationContext());
            return "completed";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getApplicationContext(), AuthorActivity.class);
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
