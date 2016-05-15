package cz.cvut.marekp11.feedreader.item;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.list.ListActivity;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class ItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FEED_LOADER = 2;
    private static final String TWO_PANE = "two_pane";
    private static final String TAG = ItemFragment.class.getSimpleName();
    private ScrollView mFragmentView;
    private String mArticleId;
    private Cursor mData;
    private String mLink;
    private boolean mTwoPane;

    public static ItemFragment newInstance(String id, boolean twoPane) {

        Bundle args = new Bundle();
        args.putString(ID, id);
        args.putBoolean(TWO_PANE, twoPane);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(FEED_LOADER, null, this);

        setActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = (ScrollView) inflater.inflate(R.layout.fragment_item, container, false);

        if(getArguments() != null) {
            mArticleId = getArguments().getString(ID);
            mTwoPane = getArguments().getBoolean(TWO_PANE);
        }

        return mFragmentView;
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        Log.d(TAG, "mTwoPane=" + mTwoPane);

        if(!mTwoPane) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mTwoPane) {
                        getActivity().finish();
                    }
                }
            });
        } else {
            toolbar.setNavigationIcon(null);
            toolbar.getMenu().clear();
        }

        ActionBar actionBar =  ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null && !mTwoPane){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (mData == null) {
                    Toast.makeText(getActivity(), R.string.toast_entry_not_loaded, Toast.LENGTH_LONG).show();
                    return true;
                }

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.article_share_text) + mLink);
                Intent chooser = Intent.createChooser(share, getString(R.string.article_share));
                startActivity(chooser);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillData(Cursor cursor){
        if (cursor != null) {
            cursor.moveToFirst();

            TextView headlineTV = (TextView) mFragmentView.findViewById(R.id.item_headline);
            Spanned titleStr = Html.fromHtml(cursor.getString(cursor.getColumnIndex(TITLE)));
            headlineTV.setText(titleStr);

            mLink = cursor.getString(cursor.getColumnIndex(LINK));

            TextView contentTV = (TextView) mFragmentView.findViewById(R.id.item_content);
            contentTV.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(TEXT))));
            // enable links
            contentTV.setMovementMethod(LinkMovementMethod.getInstance());
            contentTV.setLinksClickable(true);
        }
    }

    // loader's callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(!mArticleId.equals(ListActivity.EMPTY_ARTICLE_ID)) {
            return new CursorLoader(getActivity(), Uri.withAppendedPath(FeedReaderContentProvider.CONTENT_URI_ARTICLES, mArticleId),
                    null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        mData = data;
        if (data != null && mData.moveToFirst()) {
            fillData(data);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        // Do nothing.
    }

}
