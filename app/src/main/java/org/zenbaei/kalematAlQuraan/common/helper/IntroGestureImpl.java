package org.zenbaei.kalematAlQuraan.common.helper;

import android.app.Application;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import org.zenbaei.kalematAlQuraan.component.R;

/**
 * Created by zenbaei on 5/6/17.
 */

public class IntroGestureImpl extends GestureDetector.SimpleOnGestureListener {

    private Context context;

    public IntroGestureImpl(Context ctx) {
        context = ctx;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toast.makeText(context, R.string.backHint, Toast.LENGTH_SHORT).show();
        return true;
    }
}
