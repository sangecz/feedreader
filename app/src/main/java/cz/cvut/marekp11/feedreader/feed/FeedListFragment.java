package cz.cvut.marekp11.feedreader.feed;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.feed.dialogs.ClosableDialogFragment;
import cz.cvut.marekp11.feedreader.feed.dialogs.CustomLayoutDialogFragment;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class FeedListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int FEED_LOADER = 2;
    private static final String CLOSABLE_DIALOG = ClosableDialogFragment.class.getSimpleName();
    private static final String CUSTOM_DIALOG = CustomLayoutDialogFragment.class.getSimpleName();

    private FeedCursorAdapter.FeedDeleteListener mListener;
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView fragmentView = (ListView) inflater.inflate(R.layout.fragment_feed, container, false);
        initList(fragmentView);
        return fragmentView;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        try {
            mListener = (FeedCursorAdapter.FeedDeleteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.must_implement) + FeedCursorAdapter.FeedDeleteListener.class.getSimpleName());
        }
    }

    private void initList(View v) {
        ListView mListView = (ListView) v.findViewById(R.id.database_content_feed);
        mAdapter = new FeedCursorAdapter(getActivity(), null, 0, mListener);
        mListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(FEED_LOADER, null, this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_feed) {
            addFeed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFeed() {
        CustomLayoutDialogFragment d = CustomLayoutDialogFragment.newInstance();
        d.show(getActivity().getFragmentManager(), CUSTOM_DIALOG);
    }

    // loader's callback

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case FEED_LOADER:
                return new CursorLoader(getActivity(), FeedReaderContentProvider.CONTENT_URI_FEEDS,
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

    public void deleteFeed(String id) {
        // deletes feed
        Uri uri = Uri.parse(FeedReaderContentProvider.CONTENT_URI_FEEDS + "/" + id);
        getActivity().getContentResolver().delete(uri, null, null);
    }

    public void addFeedUrl(String url) {
        if(url.trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.toast_empty_url), Toast.LENGTH_LONG).show();
            return;
        }

        try {
            URL url1 = new URL(url.trim());
            ContentValues cv = new ContentValues();
            cv.put(TITLE, url1.getHost() + url1.getPath());
            cv.put(TEXT, url1.toString());
            insertContentValue(cv);

            Toast.makeText(getActivity(), getString(R.string.toast_added), Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            Toast.makeText(getActivity(), getString(R.string.toast_bad_url), Toast.LENGTH_LONG).show();
        }
    }

    private void insertContentValue(ContentValues cv) {
        getActivity().getContentResolver().insert(FeedReaderContentProvider.CONTENT_URI_FEEDS, cv);
    }

    public void deleteFeedClicked(String id) {
        // shows delete dialog
        ClosableDialogFragment d = ClosableDialogFragment.newInstance(id);
        d.show(getActivity().getFragmentManager(), CLOSABLE_DIALOG);
    }
}
