package cz.cvut.marekp11.feedreader.list;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.item.ItemActivity;
import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class ListActivity extends AppCompatActivity implements
        TaskFragment.TaskCallbacks {

    public static final String TASK_FRAGMET_TAG = "task";
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

        // TEST
        deleteDatabase();
        fillDatabase();
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
            mTaskFragment.executeTask();
            toggleProgressBarSpin(true);
            Toast.makeText(ListActivity.this, getString(R.string.action_refresh), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, getString(R.string.action_config_feeds), Toast.LENGTH_SHORT).show();
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
        Log.d(TAG, "started");
        toggleProgressBarSpin(true);
    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute() {
        Log.d(TAG, "finished");
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

    /////////////
    /// TESTING
    /////////////
    private void fillDatabase() {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, "Korespondencni seminar");
        cv.put(TEXT, "Muze se zucastnit opravdu kazdy student...");
        insertContentValue(cv);
        cv.put(TITLE, "Den otevrenych dveri");
        cv.put(TEXT, "Dne 12.12.2013. se kona jiz paty...");
        insertContentValue(cv);
        cv.put(TITLE, "Novy predmet");
        cv.put(TEXT, "Muze se zucastnit opravdu kazdy studeze se zucastnit opravdu kazdze se zucastnit opravdu kazdze se zucastnit opravdu kazd");
        insertContentValue(cv);
        cv.put(TITLE, "Semestr zacal");
        cv.put(TEXT, "Muze se zucastnit opravdu kazdy studeze se zucastnit opravdu kazdze se zucastnit opravdu kazd");
        insertContentValue(cv);
        cv.put(TITLE, "Konec semestru");
        cv.put(TEXT, "Muze se zucastnit opravdu kazdy studeze <a href=\"http://google.com\">kunda</a>se zucastnit ucastnit opravdu kazdy studeze se zucastnit opravdu kazdze se zucastnit opravdu kaucastnit opravdu kazdy studeze se zucastnit opravdu kazdze se zucastnit opravdu kaucastnit opravdu kazdy studeze se zucastnit opravdu kazdze se zucastnit opravdu kaucastnit opravdu kazdy studeze se zucastnit opravdu kazdze se zucastnit opravdu kaopravdu kazdze se zucastnit opravdu kazdze se zucastnit opravdu kazdze se zucastnit opravdu kazd");
        insertContentValue(cv);
        cv.put(TITLE, "Studijni oddeleni");
        cv.put(TEXT, "Muze se zucastnit opravdu kazdy stude");
        insertContentValue(cv);
        cv.put(TITLE, "Volba dekana");
        cv.put(TEXT, "Vitezem se stava...");
        insertContentValue(cv);
    }

    private void insertContentValue(ContentValues cv) {
        getContentResolver().insert(FeedReaderContentProvider.CONTENT_URI, cv);
    }

    private void deleteDatabase() {
        getContentResolver().delete(FeedReaderContentProvider.CONTENT_URI, null, null);
    }
}
