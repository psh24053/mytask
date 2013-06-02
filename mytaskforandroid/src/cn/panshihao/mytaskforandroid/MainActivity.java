package cn.panshihao.mytaskforandroid;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.shntec.saf.SAFUtils;

import android.app.ActionBar;
import android.app.AlertDialog;
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
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity  {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
        
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
        
        initEvent();
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
