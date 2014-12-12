/*******************************************************************************
 * Copyright 2014 Alexander Leontyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.woodblockwithoutco.quickcontroldock.model.impl.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;


public class WifiApAction extends ToggleAction {

	public static final int WIFI_AP_STATE_DISABLING = 10;
	public static final int WIFI_AP_STATE_DISABLED = 11;
	public static final int WIFI_AP_STATE_ENABLING = 12;
	public static final int WIFI_AP_STATE_ENABLED = 13;
	private static final String TAG = "WifiApAction";

	private WifiManager mWifiManager;
	private Method mSetWifiApEnabledMethod;
	private Method mGetWifiApStateMethod;
	private Method mGetWifiApConfigurationMethod;

	public WifiApAction(Context context) {
		mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		try {
			mGetWifiApStateMethod = WifiManager.class.getDeclaredMethod("getWifiApState");
			mGetWifiApStateMethod.setAccessible(true);
			mGetWifiApConfigurationMethod = WifiManager.class.getDeclaredMethod("getWifiApConfiguration");
			mGetWifiApConfigurationMethod.setAccessible(true);
			mSetWifiApEnabledMethod = WifiManager.class.getDeclaredMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			mSetWifiApEnabledMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "Methods for Wi-Fi AP control are missing!");
		}
	}

	@Override
	protected void performActionOn() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				setWifiApState(true);
				return null;
			}
		}.execute();
	}

	@Override
	protected void performActionOff() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				setWifiApState(false);
				return null;
			}
		}.execute();
	}
	
	private void setWifiApState(boolean state) {
		try {
			mSetWifiApEnabledMethod.invoke(mWifiManager, (WifiConfiguration)mGetWifiApConfigurationMethod.invoke(mWifiManager), state);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Inaccessible Wi-Fi AP manipulation method");
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Illegal argument for Wi-Fi AP manipulation method");
		} catch (InvocationTargetException e) {
			Log.e(TAG, "Invalid invocation target for Wi-Fi AP manipulation method");
		}
	}
	
	private int getWifiApState() {
		int state = WIFI_AP_STATE_DISABLED;
		try {
			state = (Integer) mGetWifiApStateMethod.invoke(mWifiManager);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Inaccessible Wi-Fi AP manipulation method");
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Illegal argument for Wi-Fi AP manipulation method");
		} catch (InvocationTargetException e) {
			Log.e(TAG, "Invalid invocation target for Wi-Fi AP manipulation method");
		}
		return state;
	}

	@Override
	public boolean isStateOn() {
		return getWifiApState() == WIFI_AP_STATE_ENABLED;
	}

	@Override
	public boolean isStateOff() {
		return getWifiApState() == WIFI_AP_STATE_DISABLED;
	}

	@Override
	public boolean isStateIndefinite() {
		return !isStateOn() && !isStateOff();
	}

}
