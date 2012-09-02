package time.goes.by;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private long myDownloadReference = 0;
	private Context mContext;
	private BroadcastReceiver receiver;
	private ParseHtml parseHtml;
	private List<Map<String, String>> urlMapList;
	private ListView listView;
	private SimpleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        urlMapList = new ArrayList<Map<String,String>>();
        Button writeBtn = (Button) findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String preStr = "http://www.51voa.com";
				String urlStr = "http://www.51voa.com/VOA_Special_English/";
				//myDownloadReference = download();
				urlMapList = parseHtml.getDownloadList(urlStr);
				adapter.notifyDataSetChanged();
				/*new Thread(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						String urlStr="http://down.51voa.com/201208/se-am-neil-armstrong-great-animal-orchestra-and-kris-allen-31aug12.mp3";
						String filePath = "/AquarVoice/";
						String fileName = "voa.mp3";
						//DownloadHelper.downloadFile(urlStr, filePath, fileName);
						myDownloadReference = download();
					}
				});*/
			}
		});
        
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        receiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				// when download is completed
				if (reference == myDownloadReference) {
					Toast.makeText(mContext, "Download ok", Toast.LENGTH_SHORT).show();
					
				}
			}
		};
		
		registerReceiver(receiver, filter);
		
		parseHtml = new ParseHtml();
		
		listView = (ListView) findViewById(R.id.listview);
		adapter = new SimpleAdapter(this, urlMapList, android.R.layout.simple_list_item_2,
				new String[] { ParseHtml.TITLE, ParseHtml.CONTENT }, new int[] { android.R.id.text1, android.R.id.text2 });
		listView.setAdapter(adapter);
		
    }
    
    public long download(){
    	DownloadManager downloadManager = (DownloadManager)
    			getSystemService(Context.DOWNLOAD_SERVICE);
    	String urlStr="http://down.51voa.com/201208/se-am-neil-armstrong-great-animal-orchestra-and-kris-allen-31aug12.mp3";
    	Uri uri = Uri.parse(urlStr);
    	DownloadManager.Request req = new Request(uri);
    	req.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "voa.mp3");
    	//req.allowScanningByMediaScanner();
    	return downloadManager.enqueue(req);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
    	unregisterReceiver(receiver);
    	super.onDestroy();
    }
}
