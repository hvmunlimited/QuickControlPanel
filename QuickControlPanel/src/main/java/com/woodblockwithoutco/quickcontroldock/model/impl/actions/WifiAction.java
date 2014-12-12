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

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;


public class WifiAction extends ToggleAction {
	
	private WifiManager mWifiManager;
	
	public WifiAction(Context context) {
		mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}

	@Override
	protected void performActionOn() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				mWifiManager.setWifiEnabled(true);
				return null;
			}
		}.execute();
	}

	@Override
	protected void performActionOff() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				mWifiManager.setWifiEnabled(false);
				return null;
			}
		}.execute();
	}

	@Override
	public boolean isStateOn() {
		return mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
	}
	
	@Override
	public boolean isStateOff() {
		return mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED;
	}
	
	@Override
	public boolean isStateIndefinite() {
		return mWifiManager.getWifiState() != WifiManager.WIFI_STATE_DISABLED && mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED;
	}
	

}
