package cz.cvut.marekp11.feedreader.update;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntryImpl;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

import java.util.ArrayList;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.update.helpers.DataHolder;
import cz.cvut.marekp11.feedreader.update.helpers.MyWakeLockHelper;
import cz.cvut.marekp11.feedreader.update.helpers.NetworkStatus;
import cz.cvut.marekp11.feedreader.update.helpers.RssAtomFeedRetriever;

import static cz.cvut.marekp11.feedreader.data.DbConstants.ID;
import static cz.cvut.marekp11.feedreader.data.DbConstants.LINK;
import static cz.cvut.marekp11.feedreader.data.DbConstants.TEXT;
import static cz.cvut.marekp11.feedreader.data.DbConstants.TITLE;

// FIXME too much work on main thread

public class UpdateService extends Service {

    private static final String TAG = "UpdateService";

    public interface TaskCallbacks {

        void onPreExecute();
        void onNoConnection();
        void onCancelled();
        void onPostExecute(boolean failed);
    }

    private boolean mFailed = false;
    public static boolean sRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startUpdate();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public boolean startUpdate() {
        if(!sRunning) {
            MyWakeLockHelper.lock(getApplicationContext());

            if (!NetworkStatus.isNetworkAvailable(getApplicationContext())) {
                if(getCallback() != null) {
                    getCallback().onNoConnection();
                }
                return false;
            }

            sRunning = true;
            mFailed = false;
            Log.d(TAG, " mTask.execute()");
            DownloadAndParseFeedsAsyncTask mTask = new DownloadAndParseFeedsAsyncTask();
            mTask.execute();
        }
        return sRunning;
    }

    private TaskCallbacks getCallback() {
        return (TaskCallbacks) DataHolder.getInstance().retrieve(DataHolder.LIST_FRAGMENT_ID);
    }


    private class DownloadAndParseFeedsAsyncTask extends AsyncTask<Void, ContentValues, Void> {

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            if (getCallback() != null) {
                getCallback().onPreExecute();
            }
        }

        @Override
        protected Void doInBackground(Void... none) {
//            Log.d(TAG, "doInBackground");
//            for (int i = 0; i < 3; i++) {
//                Log.d("doInBackground", i + "  count");
//                SystemClock.sleep(10000);
//            }

            // get URLs from DB
            ArrayList<String> feedUrls = extractFeedUrlsAsList();
            if (feedUrls == null || feedUrls.isEmpty()) {
                return null;
            }

            // download
            RssAtomFeedRetriever rssAtomFeedRetriever = new RssAtomFeedRetriever();

            // parsing & filling CV
            StringBuilder sb = new StringBuilder(10);
            ArrayList<ContentValues> contentValues = new ArrayList<>();
            for(String url : feedUrls) {
                try {
                    SyndFeed feed = rssAtomFeedRetriever.getMostRecentNews(url);
                    ArrayList<SyndEntryImpl> entries = (ArrayList<SyndEntryImpl>) feed.getEntries();

                    parseEntries(sb, contentValues, entries);
                } catch (RuntimeException e) {
                    mFailed = true;
                }
            }

            insertData(contentValues);

            return null;
        }

        private void parseEntries(StringBuilder sb, ArrayList<ContentValues> contentValues, ArrayList<SyndEntryImpl> entries) {
            for (SyndEntryImpl e : entries) {
                ContentValues cv = new ContentValues();
                cv.put(TITLE, e.getTitle());

                sb.setLength(0);
                sb.append(e.getDescription().getValue());
                sb.append(getString(R.string.link_prefix));
                sb.append(e.getLink());
                sb.append(getString(R.string.link_postfix));
                cv.put(TEXT, sb.toString());

                cv.put(LINK, e.getLink());

//                publishProgress(cv);

                contentValues.add(cv);
            }
        }

        @Override
        protected void onProgressUpdate(ContentValues... cv) {
//            insertFeed(cv[0]);
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled");

            sRunning = false;
            if (getCallback() != null) {
                getCallback().onCancelled();
            }
            MyWakeLockHelper.release();
            stopSelf();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onPostExecute(Void val) {
            Log.d(TAG, "onPostExecute");

            sRunning = false;
            if (getCallback() != null) {
                getCallback().onPostExecute(mFailed);
            }
            MyWakeLockHelper.release();
            stopSelf();
        }

        private void insertData(ArrayList<ContentValues> contentValues) {
            if (!contentValues.isEmpty()) {
                getContentResolver().delete(FeedReaderContentProvider.CONTENT_URI_ARTICLES, null, null);
            }

            for (ContentValues cv : contentValues) {
                insertFeed(cv);
            }
        }

        private void insertFeed(ContentValues cv) {
            getContentResolver().insert(FeedReaderContentProvider.CONTENT_URI_ARTICLES, cv);
        }


        private ArrayList<String> extractFeedUrlsAsList() {
            Cursor cursor = getContentResolver().query(FeedReaderContentProvider.CONTENT_URI_FEEDS,
                    new String[]{ID, TITLE, TEXT}, null, null, null);

            ArrayList<String> list = new ArrayList<>();
            int textColumnIndex = 0;
            if (cursor != null) {
                textColumnIndex = cursor.getColumnIndex(TEXT);
                while (cursor.moveToNext()) {
                    String url = cursor.getString(textColumnIndex);
                    list.add(url);
                }
                cursor.close();
            }

            return list;
        }
    }

}
