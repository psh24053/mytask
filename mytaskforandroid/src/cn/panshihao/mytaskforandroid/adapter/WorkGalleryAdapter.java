package cn.panshihao.mytaskforandroid.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shntec.saf.SAFConfig;
import com.shntec.saf.SAFException;
import com.shntec.saf.SAFImageCompress;
import com.shntec.saf.SAFImageLoader;
import com.shntec.saf.SAFRunner;
import com.shntec.saf.SAFUtils;

import cn.panshihao.mytaskforandroid.R;
import cn.panshihao.mytaskforandroid.model.Work;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * work gallery adapter
 * @author Panshihao
 *
 */
public class WorkGalleryAdapter extends BaseAdapter {

	private Context context;
	private List<Work> data;
	private Map<Integer, View> viewCache = new HashMap<Integer, View>();
	private LayoutInflater inflater;
	
	public WorkGalleryAdapter(List<Work> data, Context context){
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View itemView = viewCache.get(position);
		
		if(itemView == null){
			
			Work work = data.get(position);
			itemView = inflater.inflate(R.layout.item_work, null);
		
			
			LayoutParams params = new LayoutParams(SAFUtils.dp2px(130, context), SAFUtils.dp2px(120, context));
			
			itemView.setLayoutParams(params);
			itemView.setTag(work);
			ImageView image = (ImageView) itemView.findViewById(R.id.item_work_image);
			TextView text = (TextView) itemView.findViewById(R.id.item_work_text);
			
			text.setText(work.getWork_name());
			
			String imageUrl = null;
			try {
				imageUrl = SAFConfig.getInstance().getString("Server", "") + work.getWork_img();
			} catch (SAFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new SAFImageLoader(image, SAFUtils.dp2px(130, context), SAFUtils.dp2px(120, context)).execute(imageUrl);
			
			
			viewCache.put(position, itemView);
			
			
		}
		
		return itemView;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<Work> getData() {
		return data;
	}

	public void setData(List<Work> data) {
		this.data = data;
		this.viewCache.clear();
	}


	
	
	

}
