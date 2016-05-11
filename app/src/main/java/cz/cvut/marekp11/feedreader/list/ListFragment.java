package cz.cvut.marekp11.feedreader.list;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.feed.FeedListActivity;
import cz.cvut.marekp11.feedreader.item.ItemActivity;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class ListFragment extends android.app.ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ARTICLE_LOADER = 1;

    private FragmentListListener mListener;
    private ArticleCursorAdapter mAdapter;

    public static ListFragment newInstance() {

        Bundle args = new Bundle();

        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        try {
            mListener = (FragmentListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.must_implement) + ListFragment.FragmentListListener.class.getSimpleName());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_config_feeds) {
            FeedListActivity.start(getActivity());
            return true;
        }
        if (id == R.id.action_prefs) {
            Toast.makeText(getActivity(), getString(R.string.action_prefs), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // loader's callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ARTICLE_LOADER:
                return new CursorLoader(getActivity(), FeedReaderContentProvider.CONTENT_URI_ARTICLES, new String[] { ID, TITLE, TEXT }, null,
                        null, null);
            default:
                break;
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case ARTICLE_LOADER:
                if (mAdapter == null) {
                    mAdapter = new ArticleCursorAdapter(getActivity(), cursor);
                    setListAdapter(mAdapter);
                }
                mAdapter.changeCursor(cursor);
                break;

            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case ARTICLE_LOADER:
                mAdapter.changeCursor(null);
                break;

            default:
                break;
        }
    }

    public interface FragmentListListener {
        public void showItem(String id);
    }
}
