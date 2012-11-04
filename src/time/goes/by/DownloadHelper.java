/**
 * 
 */
package time.goes.by;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import time.goes.by.data.FileHelper;


import android.os.Environment;
import android.util.Log;

/**
 * @author Edison
 * Internet网络文件下载辅助类
 */
public class DownloadHelper {
	private static String TAG="DownloadHelper";

	public static int downloadFile(String urlStr, String filePath, String fileName) {
		InputStream inputStream = getInputStreamFromUrl(urlStr);
		if (inputStream != null) {
			return FileHelper.writeToSdCard(inputStream, filePath, fileName);
		} else {
			return 0;
		}
	}

	/**
	 * 从指定的URL中获取输入流
	 * 
	 * @param urlStr
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStreamFromUrl(String urlStr) {
		
		HttpURLConnection httpUrlConnection = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			
			int responseCode = httpUrlConnection.getResponseCode();
			if (responseCode==HttpURLConnection.HTTP_OK) {
				inputStream = httpUrlConnection.getInputStream();
			}
		} catch (MalformedURLException e) {
			Log.d(TAG, "Malformed URL Exception");
		} catch (IOException e) {
			Log.d(TAG, "IO Exception");
			e.printStackTrace();
		}
		
		return inputStream;
	}

	public static String getHTMLSting(String url) throws URISyntaxException, ClientProtocolException, IOException{
		URI uri = new URI(url);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		BasicResponseHandler responseHandler = new BasicResponseHandler();
		String content = httpClient.execute(httpGet, responseHandler);
		content = new String(content.getBytes("ISO-8859-1"),"UTF-8");
		return content;
	}
	
	
}
