package cz.cvut.marekp11.feedreader.list;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.update.helpers.DataHolder;
import cz.cvut.marekp11.feedreader.update.helpers.MyAlarm;
import cz.cvut.marekp11.feedreader.update.UpdateService;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.feed.FeedListActivity;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class ListFragment extends android.app.ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, UpdateService.TaskCallbacks {

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
        MyAlarm.setRepeatingUpdate(getActivity().getApplicationContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(mAdapter);
    }

    private ProgressBar mProgressBar;
    private ImageView mProgressBarImg;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);

        initActionBar();

        toggleProgressBarSpin(UpdateService.sRunning);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        mProgressBar = (ProgressBar) toolbar.findViewById(R.id.progress_bar);
        // set spinning wheel's color
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.gray),
                android.graphics.PorterDuff.Mode.SRC_IN);

        mProgressBarImg = (ImageView) toolbar.findViewById(R.id.progress_bar_img);
        mProgressBarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressBarImg.setVisibility(View.VISIBLE);

        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        ((ListActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void update() {
        getActivity().startService(new Intent(getActivity(), UpdateService.class));
    }

    private void toggleProgressBarSpin(boolean spin) {
        if (spin) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBarImg.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBarImg.setVisibility(View.VISIBLE);
        }
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
    public void onStart() {
        super.onStart();
        // set weakreference for service
        setCallback(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        setCallback(null);
    }

    private void setCallback(Object o) {
        DataHolder.getInstance().save(DataHolder.LIST_FRAGMENT_ID, o == null ? null : o);
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

    /**
     * TASK CALLBACKS
     */

    @Override
    public void onPreExecute() {
        toggleProgressBarSpin(true);
        Toast.makeText(getActivity(), getString(R.string.toast_refresh), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoConnection() {
        toggleProgressBarSpin(false);
        Toast.makeText(getActivity(), getString(R.string.toast_no_internet), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelled() {
        toggleProgressBarSpin(false);
        interruptedUpdateNotice();
    }

    @Override
    public void onPostExecute(boolean failed) {
        toggleProgressBarSpin(false);
        if (failed) {
            interruptedUpdateNotice();
        }
    }

    private void interruptedUpdateNotice() {
        Toast.makeText(getActivity(), getString(R.string.toast_refresh_failed), Toast.LENGTH_LONG).show();
    }


    public interface FragmentListListener {
        public void show();
    }
}
