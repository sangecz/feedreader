package cz.cvut.marekp11.feedreader.data;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;
import android.database.sqlite.SQLiteDatabase;

public class ArticleTable {

	public static final String TABLE_ARTICLE = "articleTable";

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_ARTICLE
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
		onCreate(db);
	}
	

}
