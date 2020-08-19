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
        this.pendingIntent = PendingIntent.getBroadcast(ctx, 0, notificationIntent, 0);
        this.alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

    }

    public void addAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 42);
        if (alarmManager != null) {
            Log.d("Alarm","Add alarm on: " + calendar.getTimeInMillis());
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1 * 60 * 1000, pendingIntent);
        }
    }

    public void removeAlarm() {
        if (alarmManager!= null) {
            alarmManager.cancel(pendingIntent);
            Log.d("Alarm", "Alarm is canceled");
        }
    }
}
