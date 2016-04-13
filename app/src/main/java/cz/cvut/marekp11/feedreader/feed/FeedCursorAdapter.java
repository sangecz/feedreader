package cz.cvut.marekp11.feedreader.feed;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cz.cvut.marekp11.feedreader.R;

import static cz.cvut.marekp11.feedreader.data.DbConstants.ID;
import static cz.cvut.marekp11.feedreader.data.DbConstants.TEXT;
import static cz.cvut.marekp11.feedreader.data.DbConstants.TITLE;

/**
 * Created by sange on 13/04/16.
 */
public class FeedCursorAdapter extends CursorAdapter{

    private LayoutInflater mInflater;
    private Context mContext;
    private FeedDeleteListener mListener;

    public FeedCursorAdapter(Context context, Cursor c, int flags, FeedDeleteListener listener) {
        super(context, c, flags);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.list_feed, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        ((TextView) view.findViewById(R.id.headline)).setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(TITLE))));
        ((TextView) view.findViewById(R.id.url)).setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(TEXT))));

        view.setOnClickListener(deleteListener);
        int idColumnIndex = cursor.getColumnIndex(ID);
        view.setTag(cursor.getString(idColumnIndex));
    }


    private View.OnClickListener deleteListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        String tag = (String) v.getTag();
        mListener.deleteFeedClicked(tag);
        }
    };

    public interface FeedDeleteListener {
        public void deleteFeedClicked(String id);
    }
}


