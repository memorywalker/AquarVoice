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

/**
 * @author Edison
 *
 */
public class ParseHtml {
	//每一个li下面可能有的四种数据
	public static final String TYPE = "TYPE";
	public static final String LRC = "LRC";
	public static final String TRANSLATE = "TRANSLATE";
	public static final String CONTENT = "CONTENT";
	public static final String TITLE = "TITLE";
	
	private String[] has4Item = {TYPE,LRC,TRANSLATE,CONTENT};
	private String[] has3Item = {TYPE,LRC,CONTENT};
	private String[] has2Item = {TYPE,CONTENT};
	
	public ParseHtml() {
		
	}
	
	public List<Map<String, String>> getDownloadList(String urlStr){
		List<Map<String, String>> urlMapList = new ArrayList<Map<String, String>>();
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
    	  urlMapList.add(getRealUrlMap(link));
    	}
    	return urlMapList;
    }
    
    public Map<String, String> getRealUrlMap(Element link){
    	String preStr = "http://www.51voa.com";
    	Map<String, String> urlMap = new HashMap<String, String>();
    	//获取对应的子连接
    	Elements aList = link.getElementsByTag("a");
    	int count = aList.size();
    	if (count==4) {
    		for (int i = 0; i < count; i++) {
        		Element alink = aList.get(i);
        		String urlstr = alink.attr("href");
        		urlMap.put(has4Item[i], preStr+urlstr);        		
    		}
		} else if (count==3) {
			for (int i = 0; i < count; i++) {
        		Element alink = aList.get(i);
        		String urlstr = alink.attr("href");
        		urlMap.put(has3Item[i], preStr+urlstr);
    		}
		} else if (count==2) {
			for (int i = 0; i < count; i++) {
        		Element alink = aList.get(i);
        		String urlstr = alink.attr("href");
        		urlMap.put(has2Item[i], preStr+urlstr);
    		}
		}
    	//从最后一个连接处获取文章标题
    	urlMap.put(TITLE, aList.get(count-1).ownText());
		return urlMap;
    	
    }
   
}
