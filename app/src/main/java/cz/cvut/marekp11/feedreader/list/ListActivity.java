package cz.cvut.marekp11.feedreader.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import cz.cvut.marekp11.feedreader.R;

public class ListActivity extends AppCompatActivity implements ListFragment.FragmentListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initFragments(savedInstanceState);
    }

    private void initFragments(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            ListFragment fragment = ListFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.container_list, fragment).commit();
        }
    }

    @Override
    public void show() {

    }
}
