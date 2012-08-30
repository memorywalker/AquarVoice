package time.goes.by;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button writeBtn = (Button) findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				writeToSdCard();
			}
		});
    }
    
    public void writeToSdCard(){
    	//file name
        String filePath = "/AquarVoice/";
        String fileName = "test.txt";
        // check for sdcard
        boolean sdCardExist = Environment.getExternalStorageState()  
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
			//sdCardDirectory
        	File sdCardDir = Environment.getExternalStorageDirectory();
        	//file object , true for append to exist file.
        	File savePath = null;
        	File saveFile = null;
        	OutputStreamWriter outStreamWriter = null;
        	try {
        		File path = new File(sdCardDir.getAbsolutePath() + filePath);
        		if (!path.exists()) {//must check the path first
					path.mkdirs();
				}
        		saveFile = new File(path, fileName);
        		outStreamWriter = new OutputStreamWriter(new FileOutputStream(saveFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	
        	Date now = new Date();
        	String content = now.toLocaleString();
        	if (outStreamWriter!=null) {
        		try {
        			outStreamWriter.write("Time:");
        			outStreamWriter.write("\r\n");
        			outStreamWriter.write(content);
					
        			outStreamWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
