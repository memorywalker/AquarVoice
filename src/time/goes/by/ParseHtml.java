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
 * 解析网页的单例
 */
public class ParseHtml {
	private static ParseHtml instance = null;
	private ParseHtml() {
		
	}
	public static ParseHtml getInstance() {
		if (instance==null) {
			instance = new ParseHtml();
		}
		return instance;
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
    	if (content!=null) {
    		Elements liList = content.getElementsByTag("li");
        	for (Element link : liList) {
        		dataList.add(getVoiceListItemData(link));
        	}
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
    
    /**
     * get the real mp3 url from content page.
     * @param contentURL
     * @return
     */
    public String getVoiceMP3Url(String contentURL){
    	String mp3URL = null;
    	Document doc = null;
    	try {
			doc = Jsoup.connect(contentURL).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Element menubar = doc.getElementById("menubar");
    	if (menubar!=null) {
    		Elements aList = menubar.getElementsByTag("a");
        	mp3URL = aList.get(0).attr("href");
		}
    	
    	return mp3URL;
    }
    
    public String getVoiceDoc(String url) {
    	String html = "<html><head><title>Title</title></head>"
    			  + "<body><p id='title'>Parsed HTML into a doc.</p>" +
    			  "</body></html>";
    	Document mydoc = Jsoup.parse(html);
    	Document doc = null;
    	try {
			doc = Jsoup.connect(url).get();
			String title = doc.title();
			Element body = doc.select("#content").first();
			body.select("#menubar").first().remove();
			body.select("#playbar").first().remove();
			mydoc.title(title);
			mydoc.select("#title").first().text(title);
			mydoc.body().appendChild(body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return mydoc.html();
    }
   
}
