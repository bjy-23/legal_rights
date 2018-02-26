package com.wonders.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetCheck {
	private static String TAG = NetCheck.class.getName();

	/**
	 * net work is available
	 * 
	 * @param inContext
	 * @return
	 */
	public static boolean isNetwork(Context inContext) {
		if (isWifiActive(inContext) || isNetworkAvailable(inContext)) {
			return true;
		}
		return false;
	}

	/**
	 * wifi is available
	 * 
	 * @param inContext
	 * @return
	 */
	private static boolean isWifiActive(Context inContext) {
		WifiManager mWifiManager = (WifiManager) inContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
		if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 3G net work is available
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				return false;
			} else {
				if (info.isAvailable()) {
					return true;
				}

			}
		}
		return false;
	}

	
}
