package org.zenbaei.kalematAlQuraan.common.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class Alarm {

    private Context ctx;
    Intent notificationIntent;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    public Alarm (Context ctx) {
        this.ctx = ctx;
        this.notificationIntent = new Intent(ctx, NotificationReceiver.class);
        this.pendingIntent = PendingIntent.getBroadcast(ctx, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        this.alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

    }

    public void addAlarm() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 11);
        //calendar1.set(Calendar.MINUTE, 10);

        if (alarmManager != null) {
            Log.d("Alarm","Add alarm on: " + calendar1.getTimeInMillis());
            alarmManager.setInexactRepeating(AlarmManager.RTC, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        }
    }

    public void removeAlarm() {
        if (alarmManager!= null) {
            alarmManager.cancel(pendingIntent);
            Log.d("Alarm", "Alarm is canceled");
        }
    }
}
