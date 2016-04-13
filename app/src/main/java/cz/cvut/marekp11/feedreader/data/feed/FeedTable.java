package cz.cvut.marekp11.feedreader.data.feed;

import android.database.sqlite.SQLiteDatabase;

import static cz.cvut.marekp11.feedreader.data.DbConstants.ID;
import static cz.cvut.marekp11.feedreader.data.DbConstants.TEXT;
import static cz.cvut.marekp11.feedreader.data.DbConstants.TITLE;

public class FeedTable {

	public static final String TABLE_FEED = "feedTable";

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_FEED
			+ "(" 
			+ ID + " integer primary key autoincrement, "
			+ TITLE + " text not null, "
			+ TEXT + " text null "
			+ ");";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		dropAndCreateTable(db);
	}
	
	public static void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropAndCreateTable(db);
	}	
	
	public static void dropAndCreateTable(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED);
		onCreate(db);
	}
	

}
