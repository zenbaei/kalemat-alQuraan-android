package org.zenbaei.kalematAlQuraan.common.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class Alarm {

    private Context ctx;

    public Alarm (Context ctx) {
        this.ctx = ctx;
    }

    public void addAlarm(int hourOfDay, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 42);

        Intent intent = new Intent(ctx, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            Log.d("Alarm","Current time: " + Calendar.getInstance().getTimeInMillis());
            Log.d("Alarm","Add alarm on: " + calendar.getTimeInMillis());
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1 * 60 * 1000, pendingIntent);
        } else {
            Log.d("Alarm","AlarmManager is Null");
        }
    }

    public void removeAlarm() {

    }
}
