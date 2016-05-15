package cz.cvut.marekp11.feedreader.item;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cz.cvut.marekp11.feedreader.R;

public class ItemActivity extends AppCompatActivity {

    public static void start(Context activity, String id) {
        Intent intent = new Intent(activity, ItemActivity.class);
        intent.putExtra(ID, id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

//        setActionBar();

        if(savedInstanceState == null) {
            Intent i = getIntent();
            String itemId = i.getStringExtra(ID);

            // startovan z aktivity -> neni twoPane
            ItemFragment fragment = ItemFragment.newInstance(itemId, false);
            getFragmentManager().beginTransaction()
                    .add(R.id.container_item, fragment)
                    .commit();
        }
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
}
