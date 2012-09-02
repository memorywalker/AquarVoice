/**
 * 
 */
package time.goes.by;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import time.goes.by.data.VoiceDataBaseDefine;
import time.goes.by.data.VoiceListItemData;

/**
 * @author Edison
 *
 */
public class ParseHtml {
	public ParseHtml() {
		
	}
	
	public List<Object> getDownloadDataList(String urlStr){
		List<Object> dataList = new ArrayList<Object>();
    	Document doc = null;
		try {
			doc = Jsoup.connect(urlStr).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Element content = doc.getElementById("blist");
    	Elements liList = content.getElementsByTag("li");
    	for (Element link : liList) {
    		dataList.add(getVoiceListItemData(link));
    	}
    	return dataList;
    }
    
    public VoiceListItemData getVoiceListItemData(Element link){
    	String preStr = "http://www.51voa.com";
    	VoiceListItemData data = new VoiceListItemData();
    	//获取对应的子连接
    	Elements aList = link.getElementsByTag("a");
    	int count = aList.size();
    	if (count==4) {
    		data.type = aList.get(0).ownText();
    		data.lrcURL = preStr + aList.get(1).attr("href");
    		data.translateURL = preStr + aList.get(2).attr("href");
    		data.contentURL = preStr + aList.get(3).attr("href");
    		data.title = aList.get(3).ownText();
		} else if (count==3) {
			data.type = aList.get(0).ownText();
    		data.lrcURL = preStr + aList.get(1).attr("href");
    		data.contentURL = preStr + aList.get(2).attr("href");
    		data.title = aList.get(2).ownText();
		} else if (count==2) {
			data.type = aList.get(0).ownText();
			data.contentURL = preStr + aList.get(1).attr("href");
    		data.title = aList.get(1).ownText();
		}
		return data;
    }
   
}
