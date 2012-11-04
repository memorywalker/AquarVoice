/**
 * 
 */
package time.goes.by;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import time.goes.by.data.DBHelper;
import time.goes.by.data.VoiceDataBaseDefine;
import time.goes.by.data.VoiceListItemData;

import android.app.Service;
import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

/**
 * @author Edison
 * 自定义列表adapter
 */
public class VoiceListAdapter extends BaseAdapter {
	LayoutInflater layoutInflater;
	List<Object> list;
	private int layoutResID;
	private Context mContext;
	/**
	 * 
	 */
	public VoiceListAdapter(Context context, List<Object> list, 
			int layoutResID) {
		mContext = context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.layoutResID = layoutResID;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final VoiceListItemData data = (VoiceListItemData) list.get(position);
		if (convertView==null) {
			convertView = layoutInflater.inflate(layoutResID, parent, false);
			holder = new ViewHolder();
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
			holder.descriptView = (TextView) convertView.findViewById(R.id.description);
			holder.rateBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
			
			holder.rateBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
				
				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
					// TODO Auto-generated method stub
					DBHelper dbHelper = new DBHelper(mContext);
					dbHelper.updateRating(data.id, rating);
					dbHelper.close();
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.titleView.setText(data.title);
		holder.descriptView.setText(data.type);
		if (data.isDownload==VoiceDataBaseDefine.DOWNLOAD_STATUS_YES) {
			holder.rateBar.setVisibility(View.VISIBLE);
		} else {
			holder.rateBar.setVisibility(View.GONE);
		}
		return convertView;
	}

	// TODO 
	static class ViewHolder {
		TextView titleView;
		TextView descriptView;
		RatingBar rateBar;
	}

}
