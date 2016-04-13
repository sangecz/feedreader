package cz.cvut.marekp11.feedreader.list;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int ARTICLE_LOADER = 1;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ListView fragmentView = (ListView) inflater.inflate(R.layout.fragment_list, container, false);
        initList(fragmentView);
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (FragmentListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.must_implement) + ListFragment.FragmentListListener.class.getSimpleName());
        }
    }

    private void initList(View v) {
        ListView mListView = (ListView) v.findViewById(R.id.database_content);
        mAdapter = new ArticleCursorAdapter(getActivity(), null, 0);
        mListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
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
                mAdapter.swapCursor(cursor);
                break;

            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case ARTICLE_LOADER:
                mAdapter.swapCursor(null);
                break;

            default:
                break;
        }
    }

    public interface FragmentListListener {
        public void showItem(String id);
    }
}
