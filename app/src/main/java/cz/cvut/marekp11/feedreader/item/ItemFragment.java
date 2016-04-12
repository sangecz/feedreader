package cz.cvut.marekp11.feedreader.item;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.DataStorage;

public class ItemFragment extends Fragment {

    int mItemId;

    public static ItemFragment newInstance(int itemId) {

        Bundle args = new Bundle();
        args.putInt(ItemActivity.ITEM_ID, itemId);

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
            mItemId = getArguments().getInt(ItemActivity.ITEM_ID);

            TextView headlineTV = (TextView) fragmentView.findViewById(R.id.item_headline);
            headlineTV.setText(Html.fromHtml(DataStorage.getNthHeadline(mItemId)).toString());

            TextView contentTV = (TextView) fragmentView.findViewById(R.id.item_content);
            contentTV.setText(Html.fromHtml(DataStorage.getNthContent(mItemId)));
            // enable links
            contentTV.setMovementMethod(LinkMovementMethod.getInstance());
            contentTV.setLinksClickable(true);;
        }

        return fragmentView;
    }
}
