package cz.cvut.marekp11.feedreader.list;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.connectivity.NetworkStatus;
import cz.cvut.marekp11.feedreader.connectivity.TaskFragment;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.feed.FeedListActivity;

public class ListActivity extends AppCompatActivity implements
        TaskFragment.TaskCallbacks {

    public static final String TASK_FRAGMET_TAG = TaskFragment.TAG;
    private static final String TAG = ListActivity.class.getSimpleName();

    private TaskFragment mTaskFragment;
    private ProgressBar mProgressBar;
    private ImageView mProgressBarImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initFragments(savedInstanceState);

        initActionBar();

        start();
    }

    private void initFragments(Bundle savedInstanceState) {
        FragmentManager fm = getFragmentManager();

        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TASK_FRAGMET_TAG);
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, TASK_FRAGMET_TAG).commit();
        }

        if(savedInstanceState == null) {
            ListFragment fragment = ListFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container_list, fragment).commit();
        }
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mProgressBar = (ProgressBar) toolbar.findViewById(R.id.progress_bar);
        // set spinning wheel's color
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.gray),
                android.graphics.PorterDuff.Mode.SRC_IN);

        mProgressBarImg = (ImageView) toolbar.findViewById(R.id.progress_bar_img);
        mProgressBarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        toggleProgressBarSpin(false);

        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
    }

    private void start() {
        if(!mTaskFragment.isRunning()){
            if(NetworkStatus.isNetworkAvailable(this)) {
                mTaskFragment.executeTask();
                toggleProgressBarSpin(true);
                Toast.makeText(ListActivity.this, getString(R.string.toast_refresh), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ListActivity.this, getString(R.string.toast_no_internet), Toast.LENGTH_LONG).show();
            }
        } else {
            toggleProgressBarSpin(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_config_feeds) {
            FeedListActivity.start(this);
            return true;
        }
        if (id == R.id.action_prefs) {
            Toast.makeText(this, getString(R.string.action_prefs), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TaskFragment.TaskCallbacks
    @Override
    public void onPreExecute() {
        toggleProgressBarSpin(true);
        deleteDatabase();
    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onNewFeed(ContentValues cv) {
        insertContentValue(cv);
    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute(ArrayList<ContentValues> cv) {
        for (ContentValues aCv : cv) {
            insertContentValue(aCv);
        }
        toggleProgressBarSpin(false);
    }

    private void toggleProgressBarSpin(boolean spin) {
        if(spin) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBarImg.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBarImg.setVisibility(View.VISIBLE);
        }
    }

    private void insertContentValue(ContentValues cv) {
        getContentResolver().insert(FeedReaderContentProvider.CONTENT_URI_ARTICLES, cv);
    }

    private void deleteDatabase() {
        getContentResolver().delete(FeedReaderContentProvider.CONTENT_URI_ARTICLES, null, null);
    }
}
