package time.goes.by;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.umeng.analytics.MobclickAgent;
import time.goes.by.data.DBHelper;
import time.goes.by.data.FileHelper;
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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private long myDownloadReference = 0;
	private Context mContext;
	private List<Object> dataList;
	private ListView listView;
	private VoiceListAdapter adapter;
	VoiceListItemData currentItemData;
	VoiceListItemData downloadingItemData;
	DBHelper dbHelper;
	String fileName = "";
	String filePath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onError(this);
        mContext = this;
        setContentView(R.layout.activity_main);
        dataList = new ArrayList<Object>();
        dbHelper = new DBHelper(mContext);
        
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(receiver, filter);
		
		listView = (ListView) findViewById(R.id.listview);
		adapter = new VoiceListAdapter(mContext, dataList, R.layout.voice_list_item);
		listView.setAdapter(adapter);
		//trigger a dialog to download or delete this record.
		listView.setOnItemLongClickListener(itemLongClickListener);
		//click into play UI
		listView.setOnItemClickListener(listOnItemClickListener);
		// post a task to read data list from database.
		new ReadDBTask().execute("");
    }
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			// when download is completed
			if (reference == myDownloadReference) {
				Toast.makeText(mContext, "Download ok", Toast.LENGTH_SHORT).show();
				dbHelper.updateDownloadStatus(downloadingItemData.id, VoiceDataBaseDefine.DOWNLOAD_STATUS_YES, filePath);
				new ReadDBTask().execute("");
			}
		}
	};
    
    public long download(String urlStr, String fileName){
    	DownloadManager downloadManager = (DownloadManager)
    			getSystemService(Context.DOWNLOAD_SERVICE);
		// urlStr="http://down.51voa.com/201208/se-am-neil-armstrong-great-animal-orchestra-and-kris-allen-31aug12.mp3";
    	Uri uri = Uri.parse(urlStr);
    	DownloadManager.Request req = new Request(uri);
    	Uri destUri = Uri.parse(fileName);
    	req.setDestinationUri(destUri);
    	//req.setDestinationInExternalPublicDir(path, filename);
    	req.setTitle(fileName)
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
    	// 自定义事件 
		String urlStr = "http://www.51voa.com/VOA_Special_English/";
		List<Object> objList = ParseHtml.getInstance().getDownloadDataList(urlStr);
		if (objList!=null) {
			dbHelper.insertDataList(objList);
			Toast.makeText(mContext, "refresh completed!", Toast.LENGTH_SHORT).show();
			//update the listView from database
			new ReadDBTask().execute("");
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
        	dataList.clear();
        	dataList.addAll(result);
        	adapter.notifyDataSetChanged();
        }
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	MobclickAgent.onResume(mContext);
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	MobclickAgent.onPause(mContext);
    }
    
    AdapterView.OnItemClickListener listOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			MobclickAgent.onEvent(mContext, "ItemClick");
			currentItemData = (VoiceListItemData) adapter.getItem(position);
			if (currentItemData.mp3URL!=null) {
				Intent intent = new Intent(mContext, PlayActivity.class);
				intent.putExtra(VoiceDataBaseDefine.MAP_KEY_MP3_FILE, currentItemData.voiceFile);
				intent.putExtra(VoiceDataBaseDefine.MAP_KEY_CONTENT, currentItemData.contentFile);
				startActivity(intent);
			} else {
				Toast.makeText(mContext, "MP3 not downloaded yet!!!", Toast.LENGTH_SHORT).show();
			}
			
		}
	};
	
	AdapterView.OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			MobclickAgent.onEvent(mContext, "ItemLongClick");
			downloadingItemData = (VoiceListItemData) adapter.getItem(position);
			if (1==downloadingItemData.isDownload) {
				Toast.makeText(mContext, "This Item has downloaded.", Toast.LENGTH_SHORT).show();
			} else{
				String mp3URL = null;
				//mp3 url has fetched or not.
				if (downloadingItemData.mp3URL==null) {
					mp3URL = ParseHtml.getInstance().getVoiceMP3Url(downloadingItemData.contentURL);
				} else {
					mp3URL = downloadingItemData.mp3URL;
				}
				
				if (mp3URL!=null) {
					dbHelper.updateStringField(downloadingItemData.id, DBHelper.KEY_MP3_URL_COLUMN, mp3URL);
					downloadingItemData.mp3URL = mp3URL;					
					filePath = "file://"+FileHelper.APP_PATH_VOICE + downloadingItemData.title.trim()+".mp3";
					myDownloadReference = download(mp3URL, filePath);
					
					String contentFile = downloadingItemData.saveContentFile();
					dbHelper.updateStringField(downloadingItemData.id, DBHelper.KEY_CONTENT_FILE_COLUMN, contentFile);
				} else {
					Toast.makeText(mContext, "MP3 URL error!!!", Toast.LENGTH_SHORT).show();
				}
				
			}
			return true; //avoid OnItemClick
		}
	};
}
