package cz.cvut.marekp11.feedreader.connectivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

public class UpdateService extends Service {

    // Registered callbacks
    private TaskCallbacks mCallback;
    public void setCallbacks(TaskCallbacks callbacks) {
        this.mCallback = callbacks;
    }

    public interface TaskCallbacks {

        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute(boolean failed);
    }

    private boolean mFailed;
    private static boolean isRunning = false;
    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {

        public UpdateService getService() {
            return UpdateService.this;
        }

    }
    private Handler mPostHandler;
    private Runnable mRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("serviceThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        mPostHandler = new Handler(looper);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        if(mRunnable == null) {
            mRunnable = getRunnable();
        }

        return binder;
    }

    private Runnable getRunnable() {
        return new Runnable() {

            public void run() {

                // TODO
                int i = 0;
                while (i++ < 10) {
                    Log.d("UpdateService", i + "");
                    SystemClock.sleep(1000);
                }

                done();
            }
        };
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPostHandler.removeCallbacks(mRunnable);

        return super.onUnbind(intent);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void startUpdate() {
        if (mCallback != null) {
            mCallback.onPreExecute();
        }
        mFailed = false;
        if (!isRunning) {
            isRunning = true;
            mPostHandler.post(mRunnable);
        }
    }

    private void done() {
        isRunning = false;
        if (mCallback != null) {
            mCallback.onPostExecute(mFailed);
        }
        stopSelf();
    }

}
