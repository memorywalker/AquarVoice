/**
 * 
 */
package time.goes.by;

import java.io.File;
import java.io.IOException;

import com.umeng.analytics.MobclickAgent;

import time.goes.by.data.DBHelper;
import time.goes.by.data.VoiceDataBaseDefine;
import time.goes.by.data.VoiceListItemData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author Edison
 * @Date Sep 4, 2012
 */
public class PlayActivity extends Activity {

	private Button bplay, bpause, bstop;
	private SeekBar playSeekBar;
	private MediaPlayer mp = new MediaPlayer();
	private VoiceListItemData voiceData = null;
	private Context mContext;
	String voiceFile = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_activity);
		MobclickAgent.onError(this);
		mContext = this;
		
		Intent intent = getIntent();
		voiceData = intent.getParcelableExtra(VoiceDataBaseDefine.KEY_VOICE_DATA);
		String contentFile = voiceData.contentFile;
		
		// set the content of webView
		WebView contentView = (WebView) findViewById(R.id.contentText);
		String fileURL = "file://"+contentFile;
		contentView.loadUrl(fileURL);
		RatingBar rateBar = (RatingBar) findViewById(R.id.ratingBar);
		if (voiceData.rating!=null) {
			rateBar.setRating(voiceData.rating);
		}
		rateBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				DBHelper dbHelper = new DBHelper(mContext);
				dbHelper.updateRating(voiceData.id, rating);
				dbHelper.close();
			}
		});
		
		bplay = (Button) findViewById(R.id.play);
		bpause = (Button) findViewById(R.id.pause);
		bstop = (Button) findViewById(R.id.stop);
		playSeekBar = (SeekBar) findViewById(R.id.playSeekBar);
		
		voiceFile = voiceData.voiceFile;
		File file = new File(voiceFile);
		if (voiceFile==null||(!file.exists())) {
			bplay.setClickable(false);
		}
		bplay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mp.reset();
					mp.setDataSource(voiceFile);
					mp.prepare();
					playSeekBar.setMax(mp.getDuration());
					mp.start();
					handler.post(updateThread);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mp.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						handler.removeCallbacks(updateThread);
						mp.release();
					}
				});
			}
		});

		bpause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mp != null) {
					mp.pause();
				}
			}
		});

		bstop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mp != null) {
					mp.stop();
					handler.removeCallbacks(updateThread);
				}
			}
		});
		
		playSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (fromUser) {
					mp.seekTo(progress);
				}
			}
		});
	}
	
	Handler handler = new Handler();
	Runnable updateThread = new Runnable() {
		
		@Override
		public void run() {
			if (mp.isPlaying()) {
				playSeekBar.setProgress(mp.getCurrentPosition());
				handler.postDelayed(updateThread, 100);
			}
		}
	};
	
	@Override
    protected void onDestroy() {
		handler.removeCallbacks(updateThread);
       if(mp != null)
           mp.release();
       super.onDestroy();
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(mContext);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(mContext);
	}
	
	
}
