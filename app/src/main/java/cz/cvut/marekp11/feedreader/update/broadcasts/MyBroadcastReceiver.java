package cz.cvut.marekp11.feedreader.update.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cz.cvut.marekp11.feedreader.update.helpers.MyWakeLockHelper;
import cz.cvut.marekp11.feedreader.update.UpdateService;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBroadcastReceiver", "running");
        MyWakeLockHelper.lock(context);
        context.startService(new Intent(context, UpdateService.class));
    }
}
