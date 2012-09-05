package time.goes.by;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import time.goes.by.data.DBHelper;
import time.goes.by.data.VoiceDataBaseDefine;
import time.goes.by.data.VoiceListItemData;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private long myDownloadReference = 0;
	private Context mContext;
	private BroadcastReceiver receiver;
	private ParseHtml parseHtml;
	private List<Object> dataList;
	private ListView listView;
	private VoiceListAdapter adapter;
	VoiceListItemData currentItemData;
	DBHelper dbHelper;
	String fileName = "";
	String filePath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        dataList = new ArrayList<Object>();
        Button writeBtn = (Button) findViewById(R.id.writeBtn);
        dbHelper = new DBHelper(mContext);
        writeBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				
				
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
					dbHelper.updateDownloadStatus(currentItemData.id, 1, filePath);
					currentItemData.isDownload = 1;
					currentItemData.voiceFile = filePath;
				}
			}
		};
		
		registerReceiver(receiver, filter);
		
		parseHtml = new ParseHtml();
		
		listView = (ListView) findViewById(R.id.listview);
		adapter = new VoiceListAdapter(mContext, dataList, R.layout.voice_list_item);
		listView.setAdapter(adapter);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentItemData = (VoiceListItemData) adapter.getItem(position);
				if (1==currentItemData.isDownload) {
					Toast.makeText(mContext, "This Item has downloaded.", Toast.LENGTH_SHORT).show();
				} else{
					String mp3URL = null;
					//mp3 url has fetched or not.
					if (currentItemData.mp3URL==null) {
						mp3URL = ParseHtml.getVoiceMP3Url(currentItemData.contentURL);
					} else {
						mp3URL = currentItemData.mp3URL;
					}
					
					if (mp3URL!=null) {
						dbHelper.updateMP3URL(currentItemData.id, mp3URL);
						currentItemData.mp3URL = mp3URL;
						String path = "AquarVoice";
						fileName = currentItemData.title.trim()+".mp3";
						myDownloadReference = download(mp3URL, path, fileName);
					} else {
						Toast.makeText(mContext, "MP3 URL error!!!", Toast.LENGTH_SHORT).show();
					}
					
				}
				return true; //avoid OnItemClick
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentItemData = (VoiceListItemData) adapter.getItem(position);
				if (currentItemData.mp3URL!=null) {
					Intent intent = new Intent(mContext, PlayActivity.class);
					intent.putExtra(VoiceDataBaseDefine.MAP_KEY_MP3_FILE, currentItemData.voiceFile);
					startActivity(intent);
				} else {
					Toast.makeText(mContext, "MP3 not downloaded yet!!!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		// post a task to read data list from database.
		new ReadDBTask().execute("");
		
    }
    
    public long download(String urlStr, String path, String filename){
    	DownloadManager downloadManager = (DownloadManager)
    			getSystemService(Context.DOWNLOAD_SERVICE);
		// urlStr="http://down.51voa.com/201208/se-am-neil-armstrong-great-animal-orchestra-and-kris-allen-31aug12.mp3";
    	File downloadDir = Environment.getExternalStoragePublicDirectory(path);
    	if (!downloadDir.exists()) {
    		downloadDir.mkdirs();
		}
        //get the entire file path include the name.
    	filePath = downloadDir.toString()+"/"+filename;
    	Uri uri = Uri.parse(urlStr);
    	DownloadManager.Request req = new Request(uri);
    	
    	req.setDestinationInExternalPublicDir(path, filename);
    	req.setTitle(filename)
    	.setShowRunningNotification(true);
    	//req.allowScanningByMediaScanner();
    	return downloadManager.enqueue(req);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_settings:
    		break;
    	case R.id.menu_refresh:
    		refreshList();
    		break;
    	default:
    			
    	}
    	return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    protected void onDestroy() {
    	unregisterReceiver(receiver);
    	dbHelper.close();
    	super.onDestroy();
    }
    
    public void refreshList(){
		String urlStr = "http://www.51voa.com/VOA_Special_English/";
		List<Object> objList = parseHtml.getDownloadDataList(urlStr);
		if (objList!=null) {
			dbHelper.insertDataList(objList);
			Toast.makeText(mContext, "refresh completed!", Toast.LENGTH_SHORT).show();
			//update the listView
			dataList.clear();
			dataList.addAll(objList);
			adapter.notifyDataSetChanged();
		} else {
			Toast.makeText(mContext, "refresh error!", Toast.LENGTH_SHORT).show();
		}
		
		
		
    }
    
    /**
     * An asyncTask for read datalist from database.
     * @author Edison
     * @Date  Sep 3, 2012
     */
    private class ReadDBTask extends AsyncTask<String, Void, List<Object>> {
        /** The system calls this to perform work in a worker thread and
          * delivers it the parameters given to AsyncTask.execute() */
        protected List<Object> doInBackground(String... urls) {
            return dbHelper.getDataList();
        }
        
        /** The system calls this to perform work in the UI thread and delivers
          * the result from doInBackground() */
        protected void onPostExecute(List<Object> result) {
        	dataList.addAll(result);
        	adapter.notifyDataSetChanged();
        }
    }
    
    
    
}
