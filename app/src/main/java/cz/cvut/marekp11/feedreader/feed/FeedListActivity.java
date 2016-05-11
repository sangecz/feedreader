package cz.cvut.marekp11.feedreader.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.feed.dialogs.ClosableDialogFragment;
import cz.cvut.marekp11.feedreader.feed.dialogs.CustomLayoutDialogFragment;

public class FeedListActivity extends AppCompatActivity implements
        ClosableDialogFragment.ClosableDialogFragmentListener,
        CustomLayoutDialogFragment.CustomLayoutDialogFragmentListener,
        FeedCursorAdapter.FeedDeleteListener {

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
    public void onDeleteBtnClicked(String id) {
        getFragment().deleteFeed(id);
    }

    @Override
    public void onAddUrlBtnClicked(String url) {
        getFragment().addFeedUrl(url);
    }

    @Override
    public void deleteFeedClicked(String id) {
        getFragment().deleteFeedClicked(id);
    }

    private FeedListFragment getFragment () {
        return (FeedListFragment) getSupportFragmentManager().findFragmentById(R.id.container_feed);
    }

}
