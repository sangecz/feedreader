package cz.cvut.marekp11.feedreader.item;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cz.cvut.marekp11.feedreader.R;

/**
 * Created by sange on 22/03/16.
 */
public class ItemFragment extends Fragment {

    private static final String STYLE = "body { margin:0; padding:0; } h1 {font-size: 1.2em;} ";
    private String mHeadline;
    private String mContent;

    public static ItemFragment newInstance(String headline, String content) {

        Bundle args = new Bundle();

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        fragment.mHeadline = headline;
        fragment.mContent = content;
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

        TextView headlineTV = (TextView) fragmentView.findViewById(R.id.item_headline);
        headlineTV.setText(Html.fromHtml(mHeadline).toString());

        TextView contentTV = (TextView) fragmentView.findViewById(R.id.item_content);
        contentTV.setText(Html.fromHtml(mContent));

        return fragmentView;
    }
}
