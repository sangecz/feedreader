package cz.cvut.marekp11.feedreader.connectivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntryImpl;

import java.util.ArrayList;

import cz.cvut.marekp11.feedreader.R;

import static cz.cvut.marekp11.feedreader.data.DbConstants.TEXT;
import static cz.cvut.marekp11.feedreader.data.DbConstants.TITLE;

public class TaskFragment extends Fragment  {

	public static final String TAG = TaskFragment.class.getSimpleName();
	private TaskCallbacks mCallbacks;
	private ArrayList<String> mFeedUrls;
	private DownloadAndParseFeedsAsyncTask mTask;
	private boolean mRunning;
	private boolean mFailed;

	public static interface TaskCallbacks {
		void onPreExecute();

		void onProgressUpdate(int percent);

		void onNewFeed(ContentValues cv);

		void onCancelled();

		void onPostExecute(ArrayList<ContentValues> cv, boolean failed);

	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		try {
			if(context instanceof Activity) {
				mCallbacks = (TaskCallbacks) context;
			}
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
	
	
	public void executeTask(ArrayList<String> feedUrls){
		mFeedUrls = feedUrls;
		mFailed = false;
		if(mFeedUrls != null && !mFeedUrls.isEmpty()) {
			mTask = new DownloadAndParseFeedsAsyncTask();
		}
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

	private class DownloadAndParseFeedsAsyncTask extends AsyncTask<Void, ContentValues, ArrayList<ContentValues>> {

		@Override
		protected void onPreExecute() {
			mRunning = true;
			if (mCallbacks != null) {
				mCallbacks.onPreExecute();
			}
		}

		@Override
		protected ArrayList<ContentValues> doInBackground(Void... none) {

			// download
			RssAtomFeedRetriever rssAtomFeedRetriever = new RssAtomFeedRetriever();

			// parsing & filling CV
			StringBuilder sb = new StringBuilder(10);
			ArrayList<ContentValues> contentValues = new ArrayList<>();

			for(String url : mFeedUrls) {
				try {
					SyndFeed feed = rssAtomFeedRetriever.getMostRecentNews(url);
					ArrayList<SyndEntryImpl> entries = (ArrayList<SyndEntryImpl>) feed.getEntries();

					parseEntries(sb, contentValues, entries);
				} catch (RuntimeException e) {
					mFailed = true;
				}
			}

			return contentValues;
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
//				sb.append(getString(R.string.links_prefix));
//				for (Object link : e.getLinks()) {
//					sb.append(getString(R.string))
//					sb.append((String) link);
//				}
//				sb.append(getString(R.string.links_postfix));

                cv.put(TEXT, sb.toString());

                contentValues.add(cv);
            }
		}

		@Override
		protected void onProgressUpdate(ContentValues... cv) {
			if (mCallbacks != null) {
				mCallbacks.onNewFeed(cv[0]);
			}
		}

		@Override
		protected void onCancelled() {
			mRunning = false;
			if (mCallbacks != null) {
				mCallbacks.onCancelled();
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ContentValues> cv) {
			mRunning = false;
			if (mCallbacks != null) {
				mCallbacks.onPostExecute(cv, mFailed);
			}
		}
	}
}
