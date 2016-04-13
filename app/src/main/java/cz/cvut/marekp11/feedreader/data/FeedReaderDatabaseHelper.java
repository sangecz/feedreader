package cz.cvut.marekp11.feedreader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "fitmag.db";
	public static final int DATABASE_VERSION = 1;

	public FeedReaderDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		ArticleTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ArticleTable.onUpgrade(db, oldVersion, newVersion);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ArticleTable.onDowngrade(db, oldVersion, newVersion);
	}
	

}
