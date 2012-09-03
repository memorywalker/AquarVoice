/**
 * 
 */
package time.goes.by.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author Edison
 * @Date  Sep 2, 2012
 * Êý¾Ý¿â¸¨ÖúÀà
 */
public class DBHelper extends SQLiteOpenHelper{
	private SQLiteDatabase db;
	
	// The index (key) column name for use in where clauses.
	public static final String KEY_ID = "_id";
	// The name and column index of each column in your database.
	// These should be descriptive.
	public static final String KEY_VOICE_TITLE_COLUMN = "VOICE_TITLE"; 
	public static final String KEY_VOICE_TYPE_COLUMN = "VOICE_TYPE";
	public static final String KEY_CONTENT_URL_COLUMN = "CONTENT_URL";
	public static final String KEY_LRC_URL_COLUMN = "LRC_URL";
	public static final String KEY_TRANSLATE_URL_COLUMN = "TRANSLATE_URL";
	public static final String KEY_VOICE_FILE_COLUMN = "VOICE_FILE";
	public static final String KEY_CONTENT_FILE_COLUMN = "CONTENT_FILE";
	public static final String KEY_LRC_FILE_COLUMN = "LRC_FILE";
	public static final String KEY_IS_DOWNLOAD_COLUMN = "IS_DOWNLOAD";
	
	private static final String DATABASE_NAME = "voice.db";
	private static final String DATABASE_TABLE = "voice_list";
	private static final int DATABASE_VERSION = 1;
	
	// SQL Statement to create a new database.
	private static final String DATABASE_CREATE = "create table " +
	  DATABASE_TABLE + " (" + KEY_ID +
	  " integer primary key autoincrement, " +
	  KEY_VOICE_TITLE_COLUMN + " text not null, " +
	  KEY_VOICE_TYPE_COLUMN + " text, " +
	  KEY_CONTENT_URL_COLUMN + " text, " +
	  KEY_LRC_URL_COLUMN + " text, " +
	  KEY_TRANSLATE_URL_COLUMN + " text, " +
	  KEY_VOICE_FILE_COLUMN + " text, " +
	  KEY_CONTENT_FILE_COLUMN + " text, " +
	  KEY_LRC_FILE_COLUMN + " text, " +
	  KEY_IS_DOWNLOAD_COLUMN + " text);";
	
	public DBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = getReadableDatabase();
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w("DBHelper", "Upgrading frome version" + 
				oldVersion + " to " + newVersion + ", which will destory all old data");
		db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
		//Create a new one
		onCreate(db);
	}
	
	public void insert(Object obj) {
		VoiceListItemData data = (VoiceListItemData)obj;
		//create a new row
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_VOICE_TITLE_COLUMN, data.title);
		newValues.put(KEY_VOICE_TYPE_COLUMN, data.type);
		newValues.put(KEY_CONTENT_URL_COLUMN, data.contentURL);
		newValues.put(KEY_LRC_URL_COLUMN, data.lrcURL);
		newValues.put(KEY_TRANSLATE_URL_COLUMN, data.translateURL);
		newValues.put(KEY_VOICE_FILE_COLUMN, data.voiceFile);
		newValues.put(KEY_CONTENT_FILE_COLUMN, data.contentFile);
		newValues.put(KEY_LRC_FILE_COLUMN, data.lrcFile);
		newValues.put(KEY_IS_DOWNLOAD_COLUMN, data.isDownload);
		
		db.insert(DATABASE_TABLE, null, newValues);
	}
	
	public Cursor queryAllColumns(){
		String[] columns = {
			KEY_ID,KEY_VOICE_TITLE_COLUMN,KEY_VOICE_TYPE_COLUMN,
			KEY_CONTENT_URL_COLUMN,KEY_LRC_URL_COLUMN,KEY_TRANSLATE_URL_COLUMN,
			KEY_VOICE_FILE_COLUMN,KEY_CONTENT_FILE_COLUMN,KEY_LRC_FILE_COLUMN,
			KEY_IS_DOWNLOAD_COLUMN
		};
		return db.query(DATABASE_TABLE, columns, null, null, null, null, null);
	}
	
	public List<Object> getDataList() {
		Cursor cursor = queryAllColumns();
		List<Object> dataList = new ArrayList<Object>();
		
		//find each columns index
		int KEY_ID_INDEX = cursor.getColumnIndexOrThrow(KEY_ID);
		int KEY_VOICE_TITLE_INDEX = cursor.getColumnIndexOrThrow(KEY_VOICE_TITLE_COLUMN);
		int KEY_VOICE_TYPE_INDEX = cursor.getColumnIndexOrThrow(KEY_VOICE_TYPE_COLUMN);
		int KEY_CONTENT_URL_INDEX = cursor.getColumnIndexOrThrow(KEY_CONTENT_URL_COLUMN);
		int KEY_LRC_URL_INDEX = cursor.getColumnIndexOrThrow(KEY_LRC_URL_COLUMN);
		int KEY_TRANSLATE_URL_INDEX = cursor.getColumnIndexOrThrow(KEY_TRANSLATE_URL_COLUMN);
		int KEY_VOICE_FILE_INDEX = cursor.getColumnIndexOrThrow(KEY_VOICE_FILE_COLUMN);
		int KEY_CONTENT_FILE_INDEX = cursor.getColumnIndexOrThrow(KEY_CONTENT_FILE_COLUMN);
		int KEY_LRC_FILE_INDEX = cursor.getColumnIndexOrThrow(KEY_LRC_FILE_COLUMN);
		int KEY_IS_DOWNLOAD_INDEX = cursor.getColumnIndexOrThrow(KEY_IS_DOWNLOAD_COLUMN);
		
		while (cursor.moveToNext()) {
			VoiceListItemData data = new VoiceListItemData();
			data.id = cursor.getString(KEY_ID_INDEX);
			data.title = cursor.getString(KEY_VOICE_TITLE_INDEX);
			data.type = cursor.getString(KEY_VOICE_TYPE_INDEX);
			data.contentURL = cursor.getString(KEY_CONTENT_URL_INDEX);
			data.lrcURL = cursor.getString(KEY_LRC_URL_INDEX);
			data.translateURL = cursor.getString(KEY_TRANSLATE_URL_INDEX);
			data.voiceFile = cursor.getString(KEY_VOICE_FILE_INDEX);
			data.contentFile = cursor.getString(KEY_CONTENT_FILE_INDEX);
			data.lrcFile = cursor.getString(KEY_LRC_FILE_INDEX);
			data.isDownload = cursor.getInt(KEY_IS_DOWNLOAD_INDEX);
			
			dataList.add(data);
		}
		
		cursor.close();
		return dataList;
	}
		
	public int insertDataList(List<Object> dataList){
		int count = 0;
		for (Object obj : dataList) {
			VoiceListItemData data = (VoiceListItemData) obj;
			insert(data);
			count++;
		}
		return count;
	}
	
	public void updateDownloadStatus(String id, int isDownload){
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_IS_DOWNLOAD_COLUMN, isDownload);
		String where = KEY_ID +"="+id;
		db = getWritableDatabase();
		db.update(DATABASE_TABLE, newValues, where, null);
	}
	
}
  
