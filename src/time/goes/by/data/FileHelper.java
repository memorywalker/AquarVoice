/**
 * 
 */
package time.goes.by.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

/**
 * @author Edison
 * File helper Class
 */
public class FileHelper {
	public static String APP_PATH = "/mnt/sdcard/AquarVoice/";
	public static String APP_PATH_VOICE = APP_PATH+"voices/";
	public static String APP_PATH_DOC = APP_PATH+"docs/";
	static {
		createFile(APP_PATH);
		createFile(APP_PATH_VOICE);
		createFile(APP_PATH_DOC);
	}
	
	public static boolean isSDCardReady() {
		boolean state = false;
		String currentState = Environment.getExternalStorageState();
		if (currentState.equals(Environment.MEDIA_MOUNTED)) {
			state = true;
		} 
		return state;
	}
	
	public static void createFile(String fileName) {
		if (isSDCardReady()) {
			File file = new File(fileName);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}
	
	public static void writeToFile(String content, String fileName) {
		File outFile = new File(fileName);
		if (!outFile.exists()) {
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileWriter fileWriter = new FileWriter(outFile);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			bufferWriter.write(content);
			bufferWriter.flush();
			bufferWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
					return -1; 
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
