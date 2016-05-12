package cz.cvut.marekp11.feedreader.update.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import cz.cvut.marekp11.feedreader.update.broadcasts.MyBroadcastReceiver;

public class MyAlarm {

    public static void setRepeatingUpdate(Context ctx) {
        AlarmManager am =
                (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        long interval = AlarmManager.INTERVAL_HOUR / 60;
        long time = System.currentTimeMillis();  // spust "hned"
        Intent intent = new Intent(ctx, MyBroadcastReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, interval, pendingIntent); //nastaveni opakovaneho alarmu

    }
}
