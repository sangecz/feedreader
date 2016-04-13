package cz.cvut.marekp11.feedreader.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import cz.cvut.marekp11.feedreader.data.article.ArticleTable;
import cz.cvut.marekp11.feedreader.data.feed.FeedTable;

import static cz.cvut.marekp11.feedreader.data.DbConstants.*;

// TODO feedy Z DB
public class FeedReaderContentProvider extends ContentProvider {

	private FeedReaderDatabaseHelper mFeedReaderDbHelper;

	public static final String AUTHORITY = "cz.cvut.feedreader";

	private static final int ARTICLE_LIST = 1;
	private static final int ARTICLE_ID = 2;
	private static final int FEED_LIST = 3;
	private static final int FEED_ID = 4;

	private static final String ARTICLES_PATH = "articles";
	private static final String FEEDS_PATH = "feeds";
	public static final Uri CONTENT_URI_ARTICLES = Uri.parse("content://" + AUTHORITY + "/" + ARTICLES_PATH);
	public static final Uri CONTENT_URI_FEEDS = Uri.parse("content://" + AUTHORITY + "/" + FEEDS_PATH);

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, ARTICLES_PATH, ARTICLE_LIST);
		sURIMatcher.addURI(AUTHORITY, FEEDS_PATH, FEED_LIST);
		sURIMatcher.addURI(AUTHORITY, ARTICLES_PATH + "/#", ARTICLE_ID);
		sURIMatcher.addURI(AUTHORITY, FEEDS_PATH + "/#", FEED_ID);
	}

	@Override
	public boolean onCreate() {
		mFeedReaderDbHelper = new FeedReaderDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case ARTICLE_LIST:
				queryBuilder.setTables(ArticleTable.TABLE_ARTICLE);
				break;
			case ARTICLE_ID:
				queryBuilder.setTables(ArticleTable.TABLE_ARTICLE);
				queryBuilder.appendWhere(ID + "=" + uri.getLastPathSegment());
				break;
			case FEED_LIST:
				queryBuilder.setTables(FeedTable.TABLE_FEED);
				break;
			case FEED_ID:
				queryBuilder.setTables(FeedTable.TABLE_FEED);
				queryBuilder.appendWhere(ID + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = mFeedReaderDbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = mFeedReaderDbHelper.getWritableDatabase();
		long id = 0;
		boolean articles = false;
		switch (uriType) {
			case ARTICLE_LIST:
				id = sqlDB.insert(ArticleTable.TABLE_ARTICLE, null, values);
				articles = true;
				break;
			case FEED_LIST:
				id = sqlDB.insert(FeedTable.TABLE_FEED, null, values);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		if(articles) {
			return Uri.parse(ARTICLES_PATH + "/" + id);
		} else {
			return Uri.parse(FEEDS_PATH + "/" + id);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = mFeedReaderDbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
			case ARTICLE_LIST:
				rowsDeleted = sqlDB.delete(ArticleTable.TABLE_ARTICLE, selection, selectionArgs);
				break;
			case ARTICLE_ID:
				String id = uri.getLastPathSegment();
				rowsDeleted = sqlDB.delete(ArticleTable.TABLE_ARTICLE, ID + "=" + id, null);
				break;
			case FEED_LIST:
				rowsDeleted = sqlDB.delete(FeedTable.TABLE_FEED, selection, selectionArgs);
				break;
			case FEED_ID:
				String id2 = uri.getLastPathSegment();
				rowsDeleted = sqlDB.delete(FeedTable.TABLE_FEED, ID + "=" + id2, null);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

}
