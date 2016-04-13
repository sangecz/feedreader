package cz.cvut.marekp11.feedreader.feed;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class FeedListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int FEED_LOADER = 2;

    private FeedCursorAdapter.FeedDeleteListener mListener;
    private FeedListActivity mActivity;
    private FeedCursorAdapter mAdapter;
    public static FeedListFragment newInstance() {
        return new FeedListFragment();
    }

    public FeedListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView fragmentView = (ListView) inflater.inflate(R.layout.fragment_feed, container, false);
        initList(fragmentView);
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            if(context instanceof Activity) {
                mActivity = (FeedListActivity) context;
                mListener = (FeedCursorAdapter.FeedDeleteListener) mActivity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.must_implement) + FeedCursorAdapter.FeedDeleteListener.class.getSimpleName());
        }
    }

    private void initList(View v) {
        ListView mListView = (ListView) v.findViewById(R.id.database_content_feed);
        mAdapter = new FeedCursorAdapter(mActivity, null, 0, mListener);
        mListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(FEED_LOADER, null, this);
    }

    // loader's callbacks


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case FEED_LOADER:
                return new CursorLoader(mActivity, FeedReaderContentProvider.CONTENT_URI_FEEDS,
                        new String[] { ID, TITLE, TEXT }, null,
                        null, null);
            default:
                break;
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case FEED_LOADER:
                mAdapter.swapCursor(cursor);
                break;

            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case FEED_LOADER:
                mAdapter.swapCursor(null);
                break;

            default:
                break;
        }
    }

}
