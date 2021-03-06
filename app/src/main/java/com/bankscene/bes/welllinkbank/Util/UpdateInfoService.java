package com.bankscene.bes.welllinkbank.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import com.bankscene.bes.welllinkbank.biz.UpdateInfo;

public class UpdateInfoService {
	ProgressDialog progressDialog;
	Handler handler;
	Context context;
	UpdateInfo updateInfo;
	
	public UpdateInfoService(Context context){
		this.context=context;
	}
	
//	public UpdateInfo getUpDateInfo() throws Exception {
//		String path = GetServerUrl.getUrl() + "/update.txt";
//		StringBuffer sb = new StringBuffer();
//		String line = null;
//		BufferedReader reader = null;
//		try {
//			// ����һ��url����
//			URL url = new URL(path);
//			// ͨ�^url���󣬴���һ��HttpURLConnection�������ӣ�
//			HttpURLConnection urlConnection = (HttpURLConnection) url
//					.openConnection();
//			// ͨ��HttpURLConnection���󣬵õ�InputStream
//			reader = new BufferedReader(new InputStreamReader(
//					urlConnection.getInputStream()));
//			// ʹ��io����ȡ�ļ�
//			while ((line = reader.readLine()) != null) {
//				sb.append(line);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (reader != null) {
//					reader.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		String info = sb.toString();
//		UpdateInfo updateInfo = new UpdateInfo();
//		updateInfo.setVersion(info.split("&")[1]);
//		updateInfo.setDescription(info.split("&")[2]);
//		updateInfo.setUrl(info.split("&")[3]);
//		this.updateInfo=updateInfo;
//		return updateInfo;
//	}

	
	public boolean isNeedUpdate(){
			String new_version = updateInfo.getVersion(); // ���°汾�İ汾��
			//��ȡ��ǰ�汾��
			String now_version="";
			try {
				PackageManager packageManager = context.getPackageManager();
				PackageInfo packageInfo = packageManager.getPackageInfo(
						context.getPackageName(), 0);
				now_version= packageInfo.versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if (new_version.equals(now_version)) {
				return false;
			} else {
				return true;
			}
	}
	
	
	public void downLoadFile(final String url,final ProgressDialog pDialog,Handler h){
		progressDialog=pDialog;
		handler=h;
		new Thread() {
			public void run() {        
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength();   //��ȡ�ļ���С
                                        progressDialog.setMax(length);                            //���ý��������ܳ���
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"Test.apk");
						fileOutputStream = new FileOutputStream(file);
						//����ǻ���������һ�ζ�ȡ10�����أ���Ū��С�˵㣬��Ϊ�ڱ��أ�������ֵ̫��һ�¾���������,
						//������progressbar��Ч����
                        byte[] buf = new byte[10];   
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {       
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							progressDialog.setProgress(process);       //������ǹؼ���ʵʱ���½����ˣ�
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}
	
	void down() {
		handler.post(new Runnable() {
			public void run() {
				progressDialog.cancel();
				update();
			}
		});
	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "Test.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	
}
