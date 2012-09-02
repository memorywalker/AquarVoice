/**
 * 
 */
package time.goes.by;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpUriRequest;

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
			return writeToSdCard(inputStream, filePath, fileName);
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

	/**
	 * @param inStream
	 * @param filePath
	 * @param fileName
	 * @return 0 存储文件失败，1存储文件成功，-1文件已经存在
	 */
	public static int writeToSdCard(InputStream inStream, String filePath,
			String fileName) {
		int retVal = 0;
		// file name
		// String filePath = "/AquarVoice/";
		// String fileName = "test.txt";
		// check for sdcard
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			// sdCardDirectory
			File sdCardDir = Environment.getExternalStorageDirectory();
			// file object , true for append to exist file.
			File saveFile = null;
			FileOutputStream outStream = null;
			// create file
			try {
				File path = new File(sdCardDir.getAbsolutePath() + filePath);
				if (!path.exists()) {// must check the path first
					path.mkdirs();
				}
				saveFile = new File(path, fileName);
				if (saveFile.exists()) {
					return -1; // 文件已存在
				} else {
					outStream = new FileOutputStream(saveFile);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// write InputStream to file.
			if (outStream != null) {
				try {
					byte buffer[] = new byte[128];
					do {
						int length = (inStream.read(buffer));
						if (length != -1) {
							outStream.write(buffer, 0, length);
						} else {
							break;
						}
					} while (true);
					retVal = 1;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						outStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
		return retVal;
	}
}
