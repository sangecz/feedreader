package cz.cvut.marekp11.feedreader.update.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cz.cvut.marekp11.feedreader.update.helpers.MyAlarm;

public class BootCompleteBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootCompleteBC", "running");
        MyAlarm.setRepeatingUpdate(context);
    }
}
