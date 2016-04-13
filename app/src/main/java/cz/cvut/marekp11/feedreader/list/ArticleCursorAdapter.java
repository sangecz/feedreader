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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ArticleCursorAdapter extends CursorAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	
	public ArticleCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.list_item, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		((TextView) view.findViewById(R.id.headline)).setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(TITLE))));
		((TextView) view.findViewById(R.id.preview)).setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(TEXT))));

        view.setOnClickListener(detailListener);
		int idColumnIndex = cursor.getColumnIndex(ID);
		view.setTag(cursor.getString(idColumnIndex));
	}


	private View.OnClickListener detailListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
            ItemActivity.start(mContext, (String) v.getTag());
		}
	};

}
