package org.zenbaei.kalematAlQuraan.common.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;

import java.util.Calendar;

public class Alarm {

    private Context ctx;
    private Intent notificationIntent;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private SettingDAO settingDAO;

    public Alarm (Context ctx) {
        this.ctx = ctx;
        this.notificationIntent = new Intent(ctx, NotificationReceiver.class);
        this.pendingIntent = PendingIntent.getBroadcast(ctx, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        this.alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        this.settingDAO = new SettingDAO(ctx);
    }

    public void addAlarm() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 11);
        //calendar1.set(Calendar.MINUTE, 10);
        int interval = getInterval();

        if (interval == 0) {
            removeAlarm();
            return;
        }

        if (alarmManager != null) {
            Log.d("Alarm","Add alarm on: " + calendar1.getTimeInMillis());
            alarmManager.setInexactRepeating(AlarmManager.RTC, calendar1.getTimeInMillis(), interval, pendingIntent);
        }
    }

    public void removeAlarm() {
        if (alarmManager!= null) {
            alarmManager.cancel(pendingIntent);
            Log.d("Alarm", "Alarm is canceled");
        }
    }

    private int getInterval() {
        String number = settingDAO.getNotificationNumber();
        int minutes = 60;
        int seconds = 60;
        int millis = 1000;
        int hours = 0;
        switch (number) {
            case "1":
                hours = 24;
                break;
            case "2":
                hours =  12;
                break;
            case "3":
                hours = 8;
                break;
            case "4":
                hours = 6;
                break;
        }
        return hours * minutes * millis; // * seconds
    }

    //NOT WORKING RIGHT
    private boolean isWorking() {
        return (PendingIntent.getBroadcast(ctx, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE) != null);
    }
}
