package cz.cvut.marekp11.feedreader.item;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import cz.cvut.marekp11.feedreader.R;

public class ItemActivity extends AppCompatActivity {

    private static final String HEADLINE = "headline";
    private static final String CONTENT = "content";

    public static void start(Activity activity, String headline, String content) {
        Intent intent = new Intent(activity, ItemActivity.class);
        intent.putExtra(HEADLINE, headline);
        intent.putExtra(CONTENT, content);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        setActionBar();

        if(savedInstanceState == null) {
            Intent i = getIntent();
            String headline = i.getStringExtra(HEADLINE);
            String content = i.getStringExtra(CONTENT);

            ItemFragment fragment = ItemFragment.newInstance(headline, content);
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
        // Inflate the menu; this adds items to the action bar if it is present.
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
