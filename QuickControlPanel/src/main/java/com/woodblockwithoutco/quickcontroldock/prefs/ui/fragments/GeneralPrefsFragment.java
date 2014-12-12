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
package com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.woodblockwithoutco.fragment.BackButtonPreferenceFragment;
import com.woodblockwithoutco.preferencesaver.PreferenceToXmlWrapper;
import com.woodblockwithoutco.quickcontroldock.global.holder.ConstantHolder;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.PrefsKeysDialog;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.PrefsXmlDialog;
import com.woodblockwithoutco.quickcontroldock.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class GeneralPrefsFragment extends BackButtonPreferenceFragment {

	private LoadIconPackAppsTask mLoadPacksListTask;
	private ListPreference mPackPreference;

	private PrefsKeysDialog mPrefsKeysDialog;
	private PrefsXmlDialog mXmlPrefsDialog;

	private static final String IS_PACK_KEY = "com.woodblockwithoutco.IS_ICON_PACK";
	protected static final String TAG = "PrefsKeysDialog";
	protected static final String TAG_XML = "PrefsXmlDialog";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.misc_prefs);

		mPackPreference = (ListPreference) findPreference(Keys.ExternalResources.EXTERNAL_RESOURCE_PACKAGE);

		mPrefsKeysDialog = new PrefsKeysDialog();
		mXmlPrefsDialog = new PrefsXmlDialog();


		findPreference("force_crash").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				String date = DateFormat.getTimeFormat(getActivity()).format(Calendar.getInstance().getTime());
				throw new RuntimeException("App has been crashed manually["+date+"]");
			}
		});

		findPreference("list_prefs").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mPrefsKeysDialog.show(getFragmentManager(), TAG);
				return true;
			}
		});

		findPreference("list_prefs_xml").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mXmlPrefsDialog.show(getFragmentManager(), TAG_XML);
				return true;
			}
		});

		findPreference("save_prefs_xml").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				saveXmlToFile();
				return true;
			}
		});
		
		findPreference("post_notification_dismissable").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext());
				int id = 0;
				final int MAX_NOTIFICATIONS = 50;
				Random r = new Random();
				id = 10000 + r.nextInt(MAX_NOTIFICATIONS);
				builder.setContentTitle("TEST");
				builder.setOngoing(false);
				builder.setContentText("TEST TEXT " + id);
				builder.setDefaults(Notification.DEFAULT_SOUND);
				builder.setSmallIcon(R.drawable.ic_notification);
				NotificationManager mgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
				mgr.notify(id, builder.build());
				return true;
			}
		});
		
		findPreference("post_notification_dismissable_delay").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			private Handler mHandler = new Handler();
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext());
						int id = 0;
						final int MAX_NOTIFICATIONS = 50;
						Random r = new Random();
						id = 10000 + r.nextInt(MAX_NOTIFICATIONS);
						builder.setContentTitle("TEST");
						builder.setOngoing(false);
						builder.setDefaults(Notification.DEFAULT_SOUND);
						builder.setContentText("TEST TEXT " + id);
						builder.setSmallIcon(R.drawable.ic_notification);
						NotificationManager mgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
						mgr.notify(id, builder.build());
					}
					
				}, 5000);
				
				return true;
			}
		});
		
		

		if(!ConstantHolder.getIsDebug()) {
			Preference debugCat = findPreference("debug_cat");
			getPreferenceScreen().removePreference(debugCat);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mLoadPacksListTask = new LoadIconPackAppsTask();
		mLoadPacksListTask.execute();
	}

	@Override
	public void onPause() {
		super.onPause();
		mLoadPacksListTask.cancel(false);
	}

	private void saveXmlToFile() {
		PreferenceToXmlWrapper wrapper = new PreferenceToXmlWrapper(PreferenceManager.getDefaultSharedPreferences(getActivity()));
		File file = new File(getActivity().getExternalFilesDir(null), "prefs.xml");
		if (file != null) {
			if(!file.exists()) {
				try {
					boolean result = file.createNewFile();
					if(!result) {
						Toast.makeText(getActivity(), R.string.saving_debug_xml_fail, Toast.LENGTH_LONG).show();
						return;
					}
				} catch (IOException e) {
					Toast.makeText(getActivity(), R.string.saving_debug_xml_fail, Toast.LENGTH_LONG).show();
					return;
				}
			}
			try {
				wrapper.saveToFile(file);
			} catch (IOException e) {
				Toast.makeText(getActivity(), R.string.saving_debug_xml_fail, Toast.LENGTH_LONG).show();
				return;
			}
		}
		Toast.makeText(getActivity(), getActivity().getExternalFilesDir(null)+"/prefs.xml", Toast.LENGTH_LONG).show();

	}

	private class LoadIconPackAppsTask extends AsyncTask<Void, Void, Void> {

		private static final String TAG = "GeneralPrefsFragment";
		private List<String> mResultEntries = new ArrayList<String>();
		private List<String> mResultEntryValues = new ArrayList<String>();
		private boolean mIsCurrentPackExists = false;

		@Override
		public void onPreExecute() {
			if(mPackPreference != null) {
				mPackPreference.setEnabled(false);
				mPackPreference.setSummary(R.string.general_external_icon_pack_loading);
			}
		}

		@Override
		protected Void doInBackground(Void... args) {
			Context context = getActivity();
			if(context != null) {
				mResultEntryValues.add("-");
				mResultEntries.add(context.getResources().getString(R.string.general_icon_pack_none));
				PackageManager pm = context.getPackageManager();
				List<PackageInfo> packs = pm.getInstalledPackages(0);
				for(PackageInfo info : packs) {
					try {
						ApplicationInfo appInfo;
						appInfo = pm.getApplicationInfo(info.packageName, PackageManager.GET_META_DATA);
						if(appInfo != null) {
							Bundle metadata = appInfo.metaData;
							if(isValidPackage(metadata)) {
								String pkgName = appInfo.packageName;
								if(pkgName.equals(mPackPreference.getValue())) mIsCurrentPackExists = true;
								mResultEntryValues.add(pkgName);
								String appName = (String) appInfo.loadLabel(pm);
								mResultEntries.add(appName);
							}
						}
					} catch (NameNotFoundException e) {
						Log.e(TAG, "Package "+info.packageName+" went missing right in the process of reading metadata");
					}
				}
			}
			return null;
		}

		@Override
		public void onPostExecute(Void result) {
			if(!isCancelled()) {
				if(mPackPreference != null) {
					mPackPreference.setEntries(mResultEntries.toArray(new String[mResultEntries.size()]));
					mPackPreference.setEntryValues(mResultEntryValues.toArray(new String[mResultEntryValues.size()]));
					mPackPreference.setEnabled(true);
					if(!mIsCurrentPackExists) mPackPreference.setValue("-");
					mPackPreference.setSummary("%s");
				}
			}
		}

		private boolean isValidPackage(Bundle metadata) {
			if(metadata == null) return false;
			return metadata.getBoolean(IS_PACK_KEY);
		}

	}
}
