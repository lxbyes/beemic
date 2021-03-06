package com.beemic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.beemic.R;

/**
 * 启动动画<br>
 * 
 * @author Leckie
 * @version 2014-04-18
 */
public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟的时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * requestWindowFeature(Window.FEATURE_NO_TITLE); // 无title
		 * 
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
		 */		
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(SplashActivity.this,
						ConnectActivity.class);
				startActivity(intent);
				finish();//结束当前acitvity
			};
		}, SPLASH_DISPLAY_LENGHT);
	}
	
	/**
	 * 重写返回按钮事件，使返回按钮"失效"<br>
	 */
	@Override
	public void onBackPressed() {
		//Do nothing!
	}
}
