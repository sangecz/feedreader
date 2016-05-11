package cz.cvut.marekp11.feedreader.feed;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.text.TextUtils;
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

    private static class ViewHolder {

        TextView title;
        TextView url;
        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.headline);
            url = (TextView) view.findViewById(R.id.url);
        }

    }
    private int mIdColumn;
    private int mTitleColumn;
    private int mUrlColumn;
    private LayoutInflater mInflater;
    private FeedDeleteListener mListener;

    public FeedCursorAdapter(Context context, Cursor cursor, int flags, FeedDeleteListener listener) {
        super(context, cursor, flags);
        mInflater = LayoutInflater.from(context);
        // FIXME ne naprimo
        mListener = listener;
        initColumns(cursor);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        Cursor oldCursor = super.swapCursor(newCursor);
        initColumns(newCursor);
        return oldCursor;
    }

    private void initColumns(Cursor cursor) {
        if (cursor != null) {
            mTitleColumn = cursor.getColumnIndex(TITLE);
            mUrlColumn = cursor.getColumnIndex(TEXT);
            mIdColumn = cursor.getColumnIndex(ID);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.list_feed, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        view.setOnClickListener(getDeleteListener(cursor.getString(mIdColumn)));

        holder.title.setText(Html.fromHtml(cursor.getString(mTitleColumn)));

        String summary = cursor.getString(mUrlColumn);
        if (!TextUtils.isEmpty(summary)) {
            holder.url.setText(summary);
            holder.url.setVisibility(View.VISIBLE);
        } else {
            holder.url.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener getDeleteListener(final String id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteFeedClicked(id);
            }
        };
    }


    public interface FeedDeleteListener {
        public void deleteFeedClicked(String id);
    }
}


