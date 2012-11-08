/**
 * 
 */
package time.goes.by.data;

import android.os.Parcel;
import android.os.Parcelable;
import time.goes.by.DownloadHelper;
import time.goes.by.ParseHtml;

/**
 * @author Edison
 * @Date  Sep 2, 2012
 * one voice data item
 */
public class VoiceListItemData implements Parcelable{
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
	
	public VoiceListItemData(Parcel source) {
		id = source.readString();
		title = source.readString();
		type = source.readString();
		contentURL = source.readString();
		lrcURL = source.readString();
		translateURL = source.readString();
		voiceFile = source.readString();
		contentFile = source.readString();
		lrcFile = source.readString();
		isDownload = source.readInt();
		mp3URL = source.readString();
		rating = source.readFloat();
	}

	public String saveContentFile() {
		String content = ParseHtml.getInstance().getVoiceDoc(contentURL);
		// .html is needed ohterwiese webview will throw StringIndexOutOfBoundsException
		String fileName = FileHelper.APP_PATH_DOC + title + ".html";
		FileHelper.writeToFile(content, fileName);
		return fileName;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(type);
		dest.writeString(contentURL);
		dest.writeString(lrcURL);
		dest.writeString(translateURL);
		dest.writeString(voiceFile);
		dest.writeString(contentFile);
		dest.writeString(lrcFile);
		dest.writeInt(isDownload);
		dest.writeString(mp3URL);
		dest.writeFloat(rating);
		
	}
	public static final Parcelable.Creator<VoiceListItemData> CREATOR = new Parcelable.Creator<VoiceListItemData>() {   
        @Override  
        public VoiceListItemData createFromParcel(Parcel source) {   
        	VoiceListItemData data = new VoiceListItemData(source);   
            return data;   
        }   
  
        @Override  
        public VoiceListItemData[] newArray(int size) {   
            // TODO Auto-generated method stub   
            return new VoiceListItemData[size];   
        }
	}; 
}
