package com.bankscene.bes.welllinkbank.view.powerwebview.webviewutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpTools {
	public Context mContext;

	public static boolean isNetAvailable(Context context) {
		boolean isNetAvailable = false;
		ConnectivityManager nManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = nManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			isNetAvailable = networkInfo.isAvailable();
		}
		return isNetAvailable;
	}
	/**
	 * 判断手机当前网络开关状态 wifi gprs
	 * 
	 * @return boolean
	 * */
	public boolean IsNetWork() {
		// 判断手机当前网络开关状态 wifi gprs
		boolean isnetwork = true;
		try {
			ConnectivityManager cm = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			boolean isWifiConnected = cm.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ? true
					: false;
			boolean isGprsConnected = cm.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ? true
					: false;
			if (isWifiConnected || isGprsConnected) {
				isnetwork = true;
			} else {
				isnetwork = false;
			}
		} catch (Exception e) {
			isnetwork = true;
		}
		return isnetwork;
	}
}
