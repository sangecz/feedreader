package cz.cvut.marekp11.feedreader.update.helpers;


import android.content.Context;
import android.os.PowerManager;

public class MyWakeLockHelper {
    private static final String LOCK_NAME = "cz.cvut.and";
    private static PowerManager.WakeLock sWakeLock = null;

    public static synchronized void lock(Context context) {
        if (sWakeLock == null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            sWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME);
            // release uvolni vsechny acquire
            sWakeLock.setReferenceCounted(false);
        }
        sWakeLock.acquire();
    }
    public static synchronized void release() {
        if (sWakeLock != null) {
            sWakeLock.release();
        }
    }
}

