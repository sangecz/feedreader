package cz.cvut.marekp11.feedreader.list;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

import cz.cvut.marekp11.feedreader.R;
import cz.cvut.marekp11.feedreader.data.FeedReaderContentProvider;
import cz.cvut.marekp11.feedreader.item.ItemActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ArticleCursorAdapter extends CursorAdapter {


	private static class ViewHolder {

		TextView headline;

		TextView preview;
		ViewHolder(View view) {
			headline = (TextView) view.findViewById(R.id.headline);
			preview = (TextView) view.findViewById(R.id.preview);
		}

	}
	private int mHeadlineColumn;
	private int mPreviewColumn;
	private int mIdColumn;
	private LayoutInflater mInflater;
	private Context mContext;

	public ArticleCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, false);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		initColumns(cursor);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Cursor swapCursor(Cursor newCursor) {
		Cursor oldCursor = super.swapCursor(newCursor);
		initColumns(newCursor);
		return oldCursor;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = mInflater.inflate(R.layout.list_item, parent, false);
		ViewHolder holder = new ViewHolder(view);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();

		view.setOnClickListener(getDetailListener(cursor.getString(mIdColumn)));

		holder.headline.setText(Html.fromHtml(cursor.getString(mHeadlineColumn)));

		String summary = cursor.getString(mPreviewColumn);
		if (!TextUtils.isEmpty(summary)) {
			holder.preview.setText(summary);
			holder.preview.setVisibility(View.VISIBLE);
		} else {
			holder.preview.setVisibility(View.GONE);
		}
	}

	private View.OnClickListener getDetailListener(final String id) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ItemActivity.start(mContext, id);
			}
		};
	}

	private void initColumns(Cursor cursor) {
		if (cursor != null) {
			mHeadlineColumn = cursor.getColumnIndex(TITLE);
			mPreviewColumn = cursor.getColumnIndex(TEXT);
			mIdColumn = cursor.getColumnIndex(ID);
		}
	}


}
