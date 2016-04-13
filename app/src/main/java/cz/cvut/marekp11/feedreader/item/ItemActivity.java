package cz.cvut.marekp11.feedreader.item;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

        setActionBar();

        if(savedInstanceState == null) {
            Intent i = getIntent();
            String itemId = i.getStringExtra(ID);

            ItemFragment fragment = ItemFragment.newInstance(itemId);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {

            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, getString(R.string.article_share_text));
            startActivity(Intent.createChooser(share, getString(R.string.article_share)));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
