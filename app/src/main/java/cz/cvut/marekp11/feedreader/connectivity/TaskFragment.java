package cz.cvut.marekp11.feedreader.connectivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntryImpl;

import java.util.ArrayList;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class TaskFragment extends Fragment  {

	public static final String TAG = TaskFragment.class.getSimpleName();
	private TaskCallbacks mCallbacks;
	private DownloadAndParseFeedsAsyncTask mTask;
	private boolean mRunning;
	private boolean mFailed;

	public interface TaskCallbacks {
		void onPreExecute();

		void onProgressUpdate(int percent);

		void onCancelled();

		void onPostExecute(boolean failed);

	}

	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);

		try {
			mCallbacks = (TaskCallbacks) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ getString(R.string.must_implement) + TaskCallbacks.class.getSimpleName());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}
	
	
	public void executeTask(){
		mFailed = false;
		mTask = new DownloadAndParseFeedsAsyncTask();
		mTask.execute();
	}
	
	public void cancelTask(){
		mTask.cancel(false);
	}

	public boolean isRunning (){
		return mRunning;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	private class DownloadAndParseFeedsAsyncTask extends AsyncTask<Void, ContentValues, Void> {

		@Override
		protected void onPreExecute() {
			mRunning = true;
			if (mCallbacks != null) {
				mCallbacks.onPreExecute();
			}
		}

		@Override
		protected Void doInBackground(Void... none) {

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

				publishProgress(cv);
				SystemClock.sleep(500);

				contentValues.add(cv);
            }
		}

		@Override
		protected void onProgressUpdate(ContentValues... cv) {
			insertFeed(cv[0]);
		}

		@Override
		protected void onCancelled() {
			mRunning = false;
			if (mCallbacks != null) {
				mCallbacks.onCancelled();
			}
		}

		@Override
		protected void onPostExecute(Void val) {
			mRunning = false;
			if (mCallbacks != null) {
				mCallbacks.onPostExecute(mFailed);
			}
		}
	}

	private void insertData(ArrayList<ContentValues> contentValues) {
		if (!contentValues.isEmpty() && getActivity() != null) {
			getActivity().getContentResolver().delete(FeedReaderContentProvider.CONTENT_URI_ARTICLES, null, null);
		}

		for (ContentValues cv : contentValues) {
			insertFeed(cv);
		}
	}

	private void insertFeed(ContentValues cv) {
		if(getActivity() != null){
			getActivity().getContentResolver().insert(FeedReaderContentProvider.CONTENT_URI_ARTICLES, cv);
		}
	}


	private ArrayList<String> extractFeedUrlsAsList() {
		if(getActivity() == null) {
			return null;
		}

		Cursor cursor = getActivity().getContentResolver().query(FeedReaderContentProvider.CONTENT_URI_FEEDS,
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
