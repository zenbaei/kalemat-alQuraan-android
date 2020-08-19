package org.zenbaei.kalematAlQuraan.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.zenbaei.kalematAlQuraan.common.notification.Alarm;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

public class StartOnBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SettingDAO settingDAO = new SettingDAO(context);
        if (settingDAO.isNotificationEnabled()) {
            new Alarm(context).addAlarm();
        }
    }
}
