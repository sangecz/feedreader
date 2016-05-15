package cz.cvut.marekp11.feedreader.list;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.item.ItemActivity;
import cz.cvut.marekp11.feedreader.item.ItemFragment;

public class ListActivity extends AppCompatActivity
        implements MyListFragment.FragmentListListener,
        ArticleCursorAdapter.ItemClickedListener
{

    private static final String TAG = ListActivity.class.getSimpleName();
    public static final String EMPTY_ARTICLE_ID = "empty_article";
//    private static final int MAX_BACK_STACK_CNT = 5;
//    private static int sPopBackStackCnt = MAX_BACK_STACK_CNT;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (findViewById(R.id.container_item) != null) {
            mTwoPane = true;
        }

        initFragments(savedInstanceState);
    }

    private void initFragments(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            MyListFragment fragment = MyListFragment.newInstance();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.container_list, fragment);

            if(mTwoPane) {
                ItemFragment itemFragment = ItemFragment.newInstance(EMPTY_ARTICLE_ID, true);
                transaction.replace(R.id.container_item, itemFragment);
//                increaseBackStackCnt();
            }

            transaction.commit();
        }
    }

    @Override
    public void clickedAt(String id) {
        if(!mTwoPane) {
            ItemActivity.start(this, id);
        } else {
            // startovan rovnou -> twoPane
            ItemFragment fragment = ItemFragment.newInstance(id, true);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container_item, fragment).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void onBackPressed() {
//        decreaseBackStackCnt();
        if(!mTwoPane || getFragmentManager().getBackStackEntryCount() <= 0){// || sPopBackStackCnt <= 0) {
            super.onBackPressed();
        }

        getFragmentManager().popBackStack();
    }

//    private void decreaseBackStackCnt() {
//        if(sPopBackStackCnt > 0) {
//            sPopBackStackCnt--;
//        }
//    }
//
//    private void increaseBackStackCnt() {
//        if(sPopBackStackCnt < MAX_BACK_STACK_CNT) {
//            sPopBackStackCnt++;
//        }
//    }
}
