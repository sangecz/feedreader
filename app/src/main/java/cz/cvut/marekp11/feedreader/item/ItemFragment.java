package cz.cvut.marekp11.feedreader.item;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

public class ItemFragment extends Fragment {

    public static ItemFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(ID, id);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView fragmentView = (ScrollView) inflater.inflate(R.layout.fragment_item, container, false);

        if(getArguments() != null) {
            String id = getArguments().getString(ID);
            fillData(id, fragmentView);
        }

        return fragmentView;
    }

    private void fillData(String id, View fragmentView){
        Activity activity = getActivity();

        if(activity != null) {
            Cursor cursor = activity.getContentResolver().query(Uri.withAppendedPath(FeedReaderContentProvider.CONTENT_URI, id), new String[]{TEXT, TITLE}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                TextView headlineTV = (TextView) fragmentView.findViewById(R.id.item_headline);
                headlineTV.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(TITLE))));

                TextView contentTV = (TextView) fragmentView.findViewById(R.id.item_content);
                contentTV.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(TEXT))));
                // enable links
                contentTV.setMovementMethod(LinkMovementMethod.getInstance());
                contentTV.setLinksClickable(true);

                cursor.close();
            }
        }
    }
}
