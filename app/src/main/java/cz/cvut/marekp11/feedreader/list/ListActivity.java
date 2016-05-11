package cz.cvut.marekp11.feedreader.list;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import cz.cvut.marekp11.feedreader.R;

public class ListActivity extends AppCompatActivity implements ListFragment.FragmentListListener {

    private ProgressBar mProgressBar;
    private ImageView mProgressBarImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initFragments(savedInstanceState);

        initActionBar();
    }

    private void initFragments(Bundle savedInstanceState) {
        FragmentManager fm = getFragmentManager();

        // TODO
//        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TASK_FRAGMET_TAG);
//        if (mTaskFragment == null) {
//            mTaskFragment = new TaskFragment();
//            fm.beginTransaction().add(mTaskFragment, TASK_FRAGMET_TAG).commit();
//        }

        if(savedInstanceState == null) {
            ListFragment fragment = ListFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.container_list, fragment).commit();
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
//                startUpdate(v);
            }
        });

        toggleProgressBarSpin(false);

        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
    }

//    private void start() {
//
//        if(!mService.isRunning()){
//            if(NetworkStatus.isNetworkAvailable(this)) {
//                mService.startUpdate();
////                toggleProgressBarSpin(true);
//                Toast.makeText(ListActivity.this, getString(R.string.toast_refresh), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(ListActivity.this, getString(R.string.toast_no_internet), Toast.LENGTH_LONG).show();
//            }
//        } else {
//            toggleProgressBarSpin(true);
//        }
//    }

    private void toggleProgressBarSpin(boolean spin) {
        if(spin) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBarImg.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBarImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showItem(String id) {

    }
}
