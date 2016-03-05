package org.zenbaei.kalematAlQuraan.common.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.zenbaei.kalematAlQuraan.component.R;

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

        if (id == R.id.action_about) {
            showDialog();
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
}
