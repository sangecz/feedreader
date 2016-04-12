package cz.cvut.marekp11.feedreader.list;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import cz.cvut.marekp11.feedreader.R;

public class TaskFragment extends Fragment  {

	public static final String TAG = TaskFragment.class.getSimpleName();
	private TaskCallbacks mCallbacks;
	private UselessAsyncTask mTask;
	private boolean mRunning;

	public static interface TaskCallbacks {
		void onPreExecute();

		void onProgressUpdate(int percent);

		void onCancelled();

		void onPostExecute();
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
	
	
	public void executeTask(){
		mTask = new UselessAsyncTask();
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

	private class UselessAsyncTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			mRunning = true;
			if (mCallbacks != null) {
				mCallbacks.onPreExecute();
			}
		}

		@Override
		protected Void doInBackground(Void... none) {
			for (int i = 0; !isCancelled() && i < 10; i++) {
				SystemClock.sleep(2000);
				publishProgress(i);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... percent) {
			if (mCallbacks != null) {
				mCallbacks.onProgressUpdate(percent[0]);
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
		protected void onPostExecute(Void none) {
			mRunning = false;
			if (mCallbacks != null) {
				mCallbacks.onPostExecute();
			}
		}
	}
}
