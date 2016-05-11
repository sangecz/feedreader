package cz.cvut.marekp11.feedreader.item;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class ItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FEED_LOADER = 2;
    private ScrollView mFragmentView;
    private String articleId;
    private Cursor mData;
    private String mLink;

    public static ItemFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(ID, id);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = (ScrollView) inflater.inflate(R.layout.fragment_item, container, false);

        if(getArguments() != null) {
            articleId = getArguments().getString(ID);
        }

        return mFragmentView;
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
            headlineTV.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(TITLE))));

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
        return new CursorLoader(getActivity(), Uri.withAppendedPath(FeedReaderContentProvider.CONTENT_URI_ARTICLES, articleId),
                null, null, null, null);
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
