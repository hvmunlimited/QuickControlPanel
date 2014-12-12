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
package com.woodblockwithoutco.quickcontroldock.prefs.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.woodblockwithoutco.activity.BackButtonPreferenceActivity;
import com.woodblockwithoutco.quickcontroldock.global.app.AssistantLauncherActivity;
import com.woodblockwithoutco.quickcontroldock.global.app.LauncherActivity;
import com.woodblockwithoutco.quickcontroldock.global.holder.ConstantHolder;
import com.woodblockwithoutco.quickcontroldock.notification.QcpNotificationListenerService;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys.Launch;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LaunchResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.AboutPrefsFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.BackupPrefsFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.ColorsPrefsFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.GeneralPrefsFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.InfoPanelPrefsFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.ShortcutsPrefsFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.SwipeDetectorPrefsFragment;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class SettingsActivity extends BackButtonPreferenceActivity{

	private PrefsChangeListener mPrefsListener;
	private Intent mServiceIntent;
	

	private static final Set<String> AVAILABLE_EVAL = new HashSet<String>(
			Arrays.asList(
					SwipeDetectorPrefsFragment.class.getName(),
					ShortcutsPrefsFragment.class.getName(),
					InfoPanelPrefsFragment.class.getName(),
					GeneralPrefsFragment.class.getName(),
					AboutPrefsFragment.class.getName(),
					ColorsPrefsFragment.class.getName()
					));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		mServiceIntent = new Intent(SettingsActivity.this, ControlService.class);

		if(!ControlService.isRunning() && LaunchResolver.isServiceEnabled(getApplicationContext())) {
			startService(new Intent(getApplicationContext(), ControlService.class));
		}

		mPrefsListener = new PrefsChangeListener();
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.prefs_headers, target);
	}

	@Override
	public void onHeaderClick(Header header, int position) {
		if(ConstantHolder.getIsEval()) {
			if(!isAvailableEval(header.fragment)) {
				Toast.makeText(getApplicationContext(), R.string.only_in_full_version, Toast.LENGTH_SHORT).show();
			} else {
				super.onHeaderClick(header, position);
			}
		} else {
			super.onHeaderClick(header, position);
		}
	}

	private boolean isAvailableEval(String name) {
		return AVAILABLE_EVAL.contains(name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(ConstantHolder.getIsEval()) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.buy_menu, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_buy:
	        	Intent intent = new Intent(Intent.ACTION_VIEW); 
	        	intent.setData(Uri.parse("market://details?id=com.woodblockwithoutco.quickcontroldock")); 
	        	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


	@Override
	protected boolean isValidFragment(String fragmentName) {
		//probably requires further investigations. 
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		stopListeningForPrefsChanges();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(BackupPrefsFragment.RestoreService.isWorking) {
			finish();
		} else {
			startListeningForPrefsChanges();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class PrefsChangeListener implements OnSharedPreferenceChangeListener {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

			if(key.startsWith("backup")) {
				return;
			}
			
			if(key.startsWith("notifications")) {
				QcpNotificationListenerService service = (QcpNotificationListenerService) QcpNotificationListenerService.getInstance();
				if(service != null) {
					service.updateSettings();
				}
			}
			
			if(key.equals(Keys.Notifications.NOTIFICATIONS_HIDE_QCP_FOREGROUND)) {
				QcpNotificationListenerService service = (QcpNotificationListenerService) QcpNotificationListenerService.getInstance();
				if(service != null) {
					service.setIgnoreForegroundModeNotification(prefs.getBoolean(key, true));
				}
				return;
			}

			if(!Keys.Launch.SWIPE_DETECTOR_GOOGLE_NOW_GESTURE_ENABLED.equals(key) && !Keys.Launch.LAUNCH_LAUNCHER_ICON.equals(key)) {
				if(LaunchResolver.isServiceEnabled(getApplicationContext())) {
					stopService(mServiceIntent);
					startService(mServiceIntent);
				} else {
					stopService(mServiceIntent);
				}

				if(!Launch.SERVICE_ENABLED.equals(key)) return;
			}
			

			PackageManager pm  = getApplicationContext().getPackageManager();
			ComponentName compName = new ComponentName(getApplicationContext(), AssistantLauncherActivity.class);
			int state = 0;
			if(LaunchResolver.isGoogleNowGestureEnabled(getApplicationContext()) && LaunchResolver.isServiceEnabled(getApplicationContext())) {
				state = PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
			} else {
				state = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
			}
			pm.setComponentEnabledSetting(compName, state, PackageManager.DONT_KILL_APP);
			
			if(LaunchResolver.isServiceEnabled(getApplicationContext()) && LaunchResolver.isShortcutLaunchEnabled(getApplicationContext())) {
				state = PackageManager.COMPONENT_ENABLED_STATE_DEFAULT; 
			} else {
				state = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
			}
			compName = new ComponentName(getApplicationContext(), LauncherActivity.class);
			pm.setComponentEnabledSetting(compName, state, PackageManager.DONT_KILL_APP);
		}
	}
	
	public void stopListeningForPrefsChanges() {
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).unregisterOnSharedPreferenceChangeListener(mPrefsListener);
	}
	
	public void startListeningForPrefsChanges() {
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(mPrefsListener);
	}

}
