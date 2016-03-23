package cz.cvut.marekp11.feedreader.list;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.DataStorage;
import cz.cvut.marekp11.feedreader.item.ItemActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment {


    public static ListFragment newInstance() {

        Bundle args = new Bundle();

        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ScrollView fragmentView = (ScrollView) inflater.inflate(R.layout.fragment_list, container, false);
        LinearLayout listLL = (LinearLayout) fragmentView.findViewById(R.id.listLL);

        for (int i = 0; i < DataStorage.ARTICLES_CNT; i++) {
            LinearLayout listItemLL = (LinearLayout) inflater.inflate(R.layout.list_item, null);

            final String headline = DataStorage.getNthHeadline(i);
            final String content = DataStorage.getNthContent(i);

            listItemLL.setOnClickListener(new LinearLayout.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemActivity.start(getActivity(), headline, content);
                }
            });

            TextView headlineTV = (TextView) listItemLL.findViewById(R.id.headline);
            headlineTV.setText(Html.fromHtml(headline).toString());

            TextView previewTV = (TextView) listItemLL.findViewById(R.id.preview);
            previewTV.setText(Html.fromHtml(content).toString());

            View line = new View(getActivity());
            line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            line.setBackgroundResource(R.color.gray);

            listLL.addView(listItemLL);
            listLL.addView(line);
        }
        return fragmentView;
    }

}
