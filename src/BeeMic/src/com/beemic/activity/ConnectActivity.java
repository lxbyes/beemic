package com.beemic.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.beemic.R;
import com.beemic.common.Message;
import com.beemic.common.Session;

/**
 * 程序入口<code>Activity</code><br>
 * 进入程序时根据程序的状态判断界面的样式<br>
 * 
 * @see Activity
 * @author Leckie
 * @version 2014-04-01
 */
public class ConnectActivity extends Activity {

	private final static String AUTH_CODE = "AUTH_CODE";// 放在二维码信息头部，用以标识是本程序生成的二维码

	private Session session;

	// 主题按钮
	private ImageView imageView;

	// 第一个按钮，起功能的作用
	private Button funcButton;

	private static Socket socket = null;

	/*
	 * 程序的连接状态*********************************************** value status * 0
	 * wifi未连接 * 1 wifi已连接但未与远程电脑建立连接 * 2 已与远程电脑建立连接 *
	 * ***********************************************
	 */
	private int connectStatus = 0;

	private boolean hasConnect = false;

	// 第二个按钮，查看已保存的录音列表
	// private Button viewButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		imageView = (ImageView) findViewById(R.id.ib001);
		funcButton = (Button) findViewById(R.id.bnt001);
		// viewButton = (Button) findViewById(R.id.bnt002);
		session = Session.getSession();

		funcButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (connectStatus) {
				case 0:
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));// 调用系统wifi连接
					break;
				case 1:
					Intent intent = new Intent();
					intent.setClass(ConnectActivity.this,
							MipcaActivityCapture.class);// 扫描二维码获取连接信息
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivityForResult(intent, 1);// 要求返回信息
					break;
				case 2:
					if(socket != null) {
						start();
					} else {
						break;
					}
					
					Intent intent2 = new Intent(ConnectActivity.this,
							RecordActivity.class);
					intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//startActivity(intent2);
					startActivityForResult(intent2, 2);// 要求返回信息
					break;
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == 2) {
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");
				/*Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_SHORT).show();*/
				String[] strArr = result.split(",");
				if (strArr != null && AUTH_CODE.equals(strArr[0])) {
					session.setIp(strArr[1]);
					session.setTcpPort(Integer.parseInt(strArr[2]));
				}
				if(session.getIp() != null) {
					connect();
				} else {
					break;
				}
				int connInfo = 9999;
				if (connInfo != 0) {
					hasConnect = true;
					session.setUdpPort(connInfo);// 设置远程udp端口
				} else {
					Toast.makeText(getApplicationContext(), "获取远程连接错误！IP:" + session.getIp() + ":" + session.getTcpPort(),
							Toast.LENGTH_SHORT).show();
					try {
						Thread.sleep(Toast.LENGTH_SHORT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					finish();
				}
			}
			break;
		case 2:
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		load();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (socket != null && !socket.isOutputShutdown()) {
			PrintStream ps = null;
			try {
				ps = new PrintStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			ps.print(Message.EXIT);
		}
	}

	/**
	 * 通过对系统状态判断来加载界面的模块
	 */
	@SuppressLint("NewApi")
	public void load() {
		String[] text = { "连接Wifi", "点击扫描" ,"开始录音"};
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (info.isConnected()) {
			connectStatus = 1;// wifi 已连接
			if (hasConnect) {
				connectStatus = 2;// 已经和远程电脑取得连接
			}
		} else {
			connectStatus = 0;
			hasConnect = false;
		}

		imageView.setImageDrawable(getResources().getDrawable(
				R.drawable.status0 + connectStatus));
		/*funcButton.setBackground(getResources().getDrawable(
				R.drawable.func0 + connectStatus));*/
		funcButton.setText(text[connectStatus]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 建立连接<br>
	 */
	private void connect() {
		new Thread(new ConnectThread()).start();
	}
	
	private void start() {
		new Thread(new StartThread()).start();
	}
	
	/**
	 * 开始通信线程定义<br>
	 * @author Leckie
	 * @date 2014-10-9
	 */
	class StartThread implements Runnable {

		@Override
		public void run() {
			PrintStream ps = null;
			try {
				ps = new PrintStream(socket.getOutputStream());
				//is = socket.getInputStream();
				Log.i("Test", session.getIp()+":"+session.getUdpPort());
			} catch (Exception e) {
				e.printStackTrace();
			}
			ps.print(Message.START);
		}
		
	}
	
	/**
	 * 连接PC端线程定义<br>
	 */
	class ConnectThread implements Runnable {

		@Override
		public void run() {
			PrintStream ps = null;
			InputStream is = null;
			try {
				InetAddress address = InetAddress.getLocalHost();
				Log.i("TEST--------------->", session.getIp() + ":" + session.getTcpPort());
				Log.i("TEST--------------->", address.getHostAddress());
				socket = new Socket(session.getIp(), session.getTcpPort());
				ps = new PrintStream(socket.getOutputStream());
				is = socket.getInputStream();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ps.print(Message.CONNECT);
			byte[] buffer = new byte[Message.MESSAGE_BUFFER_SIZE];
			try {
				is.read(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
