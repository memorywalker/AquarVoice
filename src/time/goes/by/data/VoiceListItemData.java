/**
 * 
 */
package time.goes.by.data;

/**
 * @author Edison
 * @Date  Sep 2, 2012
 * one voice data item
 */
public class VoiceListItemData {
	public String id;  // id in the database
	public String title;   //
	public String type;
	public String contentURL;
	public String lrcURL;
	public String translateURL;
	public String voiceFile;
	public String contentFile;
	public String lrcFile;
	public int isDownload; // 1 for downloaded, 0 not downloaded
	public String mp3URL;
	public Float rating;
	
	public VoiceListItemData() {
		// TODO Auto-generated constructor stub
	}
	
}
