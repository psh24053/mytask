package cn.panshihao.mytaskforandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.mytaskforandroid.adapter.WorkGalleryAdapter;
import cn.panshihao.mytaskforandroid.model.Work;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.shntec.saf.SAFException;
import com.shntec.saf.SAFRunnerAdapter;
import com.shntec.saf.SAFUtils;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity  {

	private AlertDialog loadWaitDialog;
	private Gallery main_work_gallery;
	private WorkGalleryAdapter galleryAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);

		setBehindContentView(R.layout.layout_menu_left);
		
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
		
		
		SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidth(SAFUtils.dp2px(10, this));
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffset(SAFUtils.dp2px(80, this));
        sm.setFadeDegree(0.35f);
        sm.setMode(SlidingMenu.LEFT);
        
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
        
        initEvent();
        
        main_work_gallery = (Gallery) findViewById(R.id.main_work_gallery);
        
        new LoadWorkListRunnerAdapter().execute("");
        
	}
	/**
	 * 载入Work列表
	 * @author Panshihao
	 *
	 */
	private class LoadWorkListRunnerAdapter extends SAFRunnerAdapter<String, Integer, JSONObject>{

		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			openWaitDialog();
		}
		
		@Override
		public JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONObject prm = new JSONObject();
			JSONObject response = new JSONObject();
			try {
				prm.put("start", 0);
				prm.put("count", 1000);
				response = MyTaskForAndroid.request(100, prm);
			} catch (SAFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return response;
		}
		@Override
		public void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			// 返回值不为空
			if(result != null){
				boolean res = false;
				try {
					res = result.getBoolean("res");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 获取work列表失败
				if(!res){
					loadWorkListFaild();
				}else{
					JSONObject pld = null;
					try {
						pld = result.getJSONObject("pld");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(pld != null){
						try {
							changeViewData(pld.getJSONArray("list"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						loadWorkListFaild();
					}
				}
				
				
			}
			closeWaitDialog();
			
			System.out.println("result -> "+result);
			
		}
		
	}
	/**
	 * 开启等待dialog
	 */
	public void openWaitDialog(){
		if(loadWaitDialog == null){
			Builder builder = new Builder(this);
			builder.setTitle("请稍候");
			builder.setMessage("\n正在加载...\n");
			loadWaitDialog = builder.create();
			loadWaitDialog.setCanceledOnTouchOutside(false);
		}
		if(!isFinishing()){
			loadWaitDialog.show();
		}
	}
	/**
	 * 关闭等待dialog
	 */
	public void closeWaitDialog(){
		if(!isFinishing()){
			loadWaitDialog.hide();
		}
	}
	/**
	 * 根据数据，改变界面
	 * @param list
	 */
	public void changeViewData(JSONArray list){
		
		List<Work> data = new ArrayList<Work>();
		
		for(int i = 0 ; i < list.length() ; i ++){
			try {
				JSONObject json = list.getJSONObject(i);
				Work work = new Work();
				work.setTarget_count(json.getInt("target_count"));
				work.setWork_createtime(json.getLong("work_createtime") * 1000);
				work.setWork_createtimevalue(json.getString("work_createtimevalue"));
				work.setWork_desc(json.getString("work_desc"));
				work.setWork_everyday(json.getInt("work_everyday"));
				work.setWork_id(json.getInt("work_id"));
				work.setWork_img(json.getString("work_img"));
				work.setWork_lasttime(json.getLong("work_lasttime") * 1000);
				work.setWork_name(json.getString("work_name"));
				work.setWork_status(json.getInt("work_status"));
				data.add(work);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		if(galleryAdapter == null){
			galleryAdapter = new WorkGalleryAdapter(data, this);
			main_work_gallery.setAdapter(galleryAdapter);
		}else{
			galleryAdapter.setData(data);
			galleryAdapter.notifyDataSetChanged();
		}
		
		
		
	}
	
	
	
	/**
	 * 加载work列表失败
	 */
	public void loadWorkListFaild(){
		Builder builder = new Builder(this);
		builder.setTitle("错误");
		builder.setMessage("\n加载work列表失败\n");
		builder.setNegativeButton("重新加载", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				new LoadWorkListRunnerAdapter().execute("");
			}
		});
		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
	}
	
	/**
	 * 初始化事件
	 */
	public void initEvent(){
		MenuClickListener menuClickListener = new MenuClickListener();
		
		findViewById(R.id.menu_left_today).setOnClickListener(menuClickListener);
		findViewById(R.id.menu_left_wait).setOnClickListener(menuClickListener);
		findViewById(R.id.menu_left_complete).setOnClickListener(menuClickListener);
		findViewById(R.id.menu_left_history).setOnClickListener(menuClickListener);
		findViewById(R.id.menu_left_about).setOnClickListener(menuClickListener);
		findViewById(R.id.menu_left_settings).setOnClickListener(menuClickListener);
	}
	
	private class MenuClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.menu_left_today:
				
				break;

			default:
				break;
			}
		}
		
	}
	

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			getSlidingMenu().toggle();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == event.KEYCODE_MENU){
			getSlidingMenu().toggle();	
			return true;
		}else if(keyCode == event.KEYCODE_BACK){
			if(getSlidingMenu().isMenuShowing()){
				getSlidingMenu().showContent();
				return true;
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("退出");
				builder.setMessage("\n你真的要退出"+getResources().getString(R.string.app_name)+"吗？\n");
				builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
				builder.setNeutralButton("取消", null);
				builder.show();
				return true;
			}
		}
		
		
		return super.onKeyDown(keyCode, event);
	}
}
