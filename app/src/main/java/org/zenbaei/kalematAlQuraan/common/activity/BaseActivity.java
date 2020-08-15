package org.zenbaei.kalematAlQuraan.common.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.author.IntroActivity;
import org.zenbaei.kalematAlQuraan.component.menu.FavActivity;
import org.zenbaei.kalematAlQuraan.component.setting.SettingsActivity;
import org.zenbaei.kalematAlQuraan.utils.AndroidDatabaseManager;

/**
 * Created by Islam on 2/7/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ClipboardManager clipboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /* to make icon visible
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        return super.onMenuOpened(featureId, menu);
    }*/

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
            case R.id.action_share:
                shareLink();
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
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void showDbManager() {
        Intent dbManager = new Intent(getApplicationContext(),AndroidDatabaseManager.class);
        startActivity(dbManager);
    }

    private void shareLink() {
        Toast.makeText(BaseActivity.this, getResources().getText(R.string.share_link), Toast.LENGTH_SHORT).show();
        copyToClipboard(getResources().getText(R.string.app_link).toString());
    }

    public void copyToClipboard(String text) {
        ClipData clip = ClipData.newPlainText("tafsir", text);
        clipboard.setPrimaryClip(clip);
    }

    public abstract void setFontAndBackground();

}
