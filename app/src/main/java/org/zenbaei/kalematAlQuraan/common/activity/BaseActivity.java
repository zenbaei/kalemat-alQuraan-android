package org.zenbaei.kalematAlQuraan.common.activity;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.author.IntroActivity;
import org.zenbaei.kalematAlQuraan.component.menu.FavActivity;
import org.zenbaei.kalematAlQuraan.component.menu.FontActivity;
import org.zenbaei.kalematAlQuraan.utils.AndroidDatabaseManager;

/**
 * Created by Islam on 2/7/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                showDialog();
                break;
            case R.id.action_goToIntro:
                goToIntro();
                break;
            case R.id.action_goToFav:
                goToFav();
                break;
            case R.id.action_goToFontPreferences:
                goToFontPreferences();
                break;
            /*
            case R.id.action_showDbManager:
                showDbManager();
                break;
             */
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.about))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.close), null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void goToIntro() {
        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
        startActivity(intent);
    }

    private  void goToFav() {
        Intent intent = new Intent(getApplicationContext(), FavActivity.class);
        startActivity(intent);
    }

    private void goToFontPreferences() {
        Intent intent = new Intent(getApplicationContext(), FontActivity.class);
        startActivity(intent);
    }

    private void showDbManager() {
        Intent dbManager = new Intent(getApplicationContext(),AndroidDatabaseManager.class);
        startActivity(dbManager);
    }

    public abstract void setFontAndBackground();

}
