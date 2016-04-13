package cz.cvut.marekp11.feedreader.feed;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.feed.dialogs.ClosableDialogFragment;
import cz.cvut.marekp11.feedreader.feed.dialogs.CustomLayoutDialogFragment;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class FeedListActivity extends AppCompatActivity implements FeedCursorAdapter.FeedDeleteListener,
        ClosableDialogFragment.ClosableDialogFragmentListener, CustomLayoutDialogFragment.CustomLayoutDialogFragmentListener {

    private static final String CLOSABLE_DIALOG = ClosableDialogFragment.class.getSimpleName();
    private static final String CUSTOM_DIALOG = CustomLayoutDialogFragment.class.getSimpleName();

    public static void start(Context activity) {
        Intent intent = new Intent(activity, FeedListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setActionBar();

        if(savedInstanceState == null) {
            FeedListFragment fragment = FeedListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_feed, fragment)
                    .commit();
        }
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_feed);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
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
        d.show(getFragmentManager(), CUSTOM_DIALOG);
    }

    private void insertContentValue(ContentValues cv) {
        getContentResolver().insert(FeedReaderContentProvider.CONTENT_URI_FEEDS, cv);
    }

    @Override
    public void deleteFeedClicked(String id) {
        // shows delete dialog
        ClosableDialogFragment d = ClosableDialogFragment.newInstance(id);
        d.show(getFragmentManager(), CLOSABLE_DIALOG);
    }

    @Override
    public void onDeleteBtnClicked(String id) {
        // deletes feed
        Uri uri = Uri.parse(FeedReaderContentProvider.CONTENT_URI_FEEDS + "/" + id);
        getContentResolver().delete(uri, null, null);
    }


    @Override
    public void onAddUrlBtnClicked(String url) {
        if(url.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_empty_url), Toast.LENGTH_LONG).show();
            return;
        }

        try {
            URL url1 = new URL(url.trim());
            ContentValues cv = new ContentValues();
            // TODO nacist title v TaskFragmentu
            cv.put(TITLE, "");
            cv.put(TEXT, url1.toString());
            insertContentValue(cv);
        } catch (MalformedURLException e) {
            Toast.makeText(this, getString(R.string.toast_bad_url), Toast.LENGTH_LONG).show();
        }
    }
}
