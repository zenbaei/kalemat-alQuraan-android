package org.zenbaei.kalematAlQuraan.common.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.ListAdapter;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.business.AyahService;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;
import org.zenbaei.kalematAlQuraan.component.surah.business.SurahService;
import org.zenbaei.kalematAlQuraan.component.surah.entity.Surah;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "10001";
    Context mContext;
    SettingDAO settingDAO;
    AyahService ayahService;
    SurahService surahService;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notification", "Broadcast Received");
        this.mContext = context;
        settingDAO = new SettingDAO(context);
        ayahService = new AyahService(context);
        surahService = new SurahService(context);
        createNotification();
    }

    public void createNotification() {
        int surahId = getSurahId();
        List<Surah> surahList = surahService.findAll();
        Ayah ayah = getAyah(surahId);
        String title = String.format("قوله تعالى: %s%s%s", "\"", ayah.getKalemah(), "\"") ;
        String content = String.format("%s %s، سورة %s، الآية %s", "أي",ayah.getTafsir().getTafsir(), surahList.get(surahId - 1).getName(), ayah.getNumber());
        /**
         * To launch an activity when notification pressed
         * Intent intent = new Intent(this, AlertDetails.class);
         * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         * PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(getUniqueId(), builder.build());
    }

    void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NOTIFICATION_CHANNEL_NAME";
            String description = "QURAAN_NOTIFICATION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int getUniqueId() {
        String day =  String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        int unique = Integer.valueOf(day + month + year);
        Log.d("Notification", String.valueOf(unique));
        return  unique;
    }

    private Ayah getAyah(int surahId) {
        List<Ayah> ayahList = ayahService.findBySurahIdNoLimit(surahId);
        int index = getAyahIndex(ayahList);
        return ayahList.get(index);
    }

    private int getSurahId() {
        int min = 1;
        int max = 114;
        Random random = new Random();
        return random.nextInt((max - min) + 1) + 1;
    }

    private int getAyahIndex(List ayahList) {
        // Returns number between 0- ayahList - 1
        return new Random().nextInt(ayahList.size());
    }

    private boolean isNotificationEnabled() {
        String isEnabled = settingDAO.findByKey(Setting.KEY_NAME.NOTIFICATION_ENABLED);
        if (isEnabled.equals("true")) {
            return true;
        }
        return false;
    }

    private String getContent() {
        return "";
    }
}
