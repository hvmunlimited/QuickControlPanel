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
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;


public class GpsAction extends ToggleAction {

	private Context mContext;
	private Intent mPokeIntent;
	private LocationManager mLocationManager;

	public GpsAction(Context context) {

		mContext = context;

		mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

		mPokeIntent = new Intent();
		mPokeIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
		mPokeIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		mPokeIntent.setData(Uri.parse("3")); 
	}

	@Override
	protected void performActionOn() {
		if(isExploitAvailable()) {
			mContext.sendBroadcast(mPokeIntent);
		} else {
			startGpsSettings();
		}
	}

	@Override
	protected void performActionOff() {
		if(isExploitAvailable()) {
			mContext.sendBroadcast(mPokeIntent);
		} else {
			startGpsSettings();
		}
	}


	@Override
	public boolean isStateOn() {
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	@Override
	public boolean isStateOff() {
		return !isStateOn();
	}

	private boolean isExploitAvailable() {
		PackageManager pacman = mContext.getPackageManager();
		PackageInfo pacInfo = null;

		try {
			pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
		} catch (NameNotFoundException e) {
			return false;
		}

		if(pacInfo != null) {
			for(ActivityInfo actInfo : pacInfo.receivers){
				if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported){
					return true;
				}
			}
		}
		return false;
	}
	
	private void startGpsSettings() {
		Intent gpsSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		gpsSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ControlService service = (ControlService) ControlService.getInstance();
		if(service != null && ControlService.isRunning()) {
			service.close();
		}
		mContext.startActivity(gpsSettingsIntent);
	}
}
