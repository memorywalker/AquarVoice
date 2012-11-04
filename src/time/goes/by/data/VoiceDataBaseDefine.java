/**
 * 
 */
package time.goes.by.data;

/**
 * @author Edison
 * @Date  Sep 2, 2012
 */
public class VoiceDataBaseDefine {
	public static final String KEY_VOICE_DATA="key_voice_data";
	
	public static final String MAP_KEY_TYPE = "TYPE";
	public static final String MAP_KEY_LRC = "LRC";
	public static final String MAP_KEY_TRANSLATE = "TRANSLATE";
	public static final String MAP_KEY_CONTENT = "CONTENT";
	public static final String MAP_KEY_TITLE = "TITLE";
	
	public static final String KEY_ID = "_id";
	// The name and column index of each column in your database.
	// These should be descriptive.
	public static final String KEY_VOICE_TITLE_COLUMN = "VOICE_TITLE"; 
	public static final String KEY_VOICE_TYPE_COLUMN = "VOICE_TYPE";
	public static final String KEY_CONTENT_URL_COLUMN = "CONTENT_URL";
	public static final String KEY_LRC_URL_COLUMN = "LRC_URL";
	public static final String KEY_TRANSLATE_URL_COLUMN = "TRANSLATE_URL";
	
	public static final int DOWNLOAD_STATUS_YES = 1;
	public static final int DOWNLOAD_STATUS_NO = 0;
}
