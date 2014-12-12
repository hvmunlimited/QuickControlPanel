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
import java.util.List;

import com.woodblockwithoutco.fragment.BackButtonPreferenceFragment;
import com.woodblockwithoutco.preferencesaver.PreferenceToXmlWrapper;
import com.woodblockwithoutco.preferencesaver.XmlToPreferenceWrapper;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.BackupResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LaunchResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.util.FileUtils;
import com.woodblockwithoutco.quickcontroldock.R;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class BackupPrefsFragment extends BackButtonPreferenceFragment implements OnPreferenceChangeListener {

	private Preference mRestoreBackupPreference;
	private ListPreference mRestoreBackupListPreference;
	private BackupListUpdateTask mUpdateTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.backup_prefs);

		Preference pref = findPreference(Keys.Backup.BACKUP_NAME);
		pref.setSummary(BackupResolver.getBackupName(getActivity()));
		pref.setOnPreferenceChangeListener(this);

		mRestoreBackupPreference = findPreference("backup_restore_backup");
		mRestoreBackupListPreference = (ListPreference)findPreference(Keys.Backup.BACKUP_RESTORE_NAME);

		findPreference("backup_create_backup").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(getActivity(), BackupService.class);
				intent.putExtra("name", BackupResolver.getBackupName(getActivity()));
				getActivity().startService(intent);
				return true;
			}
		});

		findPreference("backup_restore_refresh").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mUpdateTask.cancel(true);
				mUpdateTask = new BackupListUpdateTask();
				mUpdateTask.execute();
				return true;
			}
		});

		findPreference("backup_restore_backup").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(getActivity(), RestoreService.class);
				intent.putExtra("name", BackupResolver.getRestoreBackupName(getActivity()));
				getActivity().startService(intent);
				getActivity().finish();
				return true;
			}
		});
		
		
	}

	@Override
	public void onResume() {
		super.onResume();
		mUpdateTask = new BackupListUpdateTask();
		mUpdateTask.execute();
	}

	@Override
	public void onPause() {
		super.onPause();
		mUpdateTask.cancel(true);
	}



	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
		if(preference.getKey().equals(Keys.Backup.BACKUP_NAME)) {
			if(value instanceof CharSequence) {
				preference.setSummary((CharSequence)value);
			}
		}
		return true;
	}

	private class BackupListUpdateTask extends AsyncTask<Void, Void, List<List<String>>> {

		@Override
		public void onPreExecute() {
			mRestoreBackupPreference.setEnabled(false);
			mRestoreBackupListPreference.setEnabled(false);
		}

		@Override
		protected List<List<String>> doInBackground(Void... arg) {
			return initBackupsList();
		}

		@Override
		public void onPostExecute(List<List<String>> result) {
			if(!isCancelled()) {
				List<String> entries = result.get(0);
				List<String> values = result.get(1);

				if(entries.size() != 0 && values.size() != 0) {
					if(BackupResolver.getRestoreBackupName(getActivity()).equals("-")) {
						mRestoreBackupListPreference.setValue(values.get(0));
					}
					mRestoreBackupPreference.setEnabled(true);
					mRestoreBackupListPreference.setEnabled(true);
				} else {
					entries.add(getActivity().getString(R.string.bkp_list_none_entry));
					values.add(getActivity().getString(R.string.bkp_list_none_value));
					mRestoreBackupListPreference.setValue("-");
				}

				mRestoreBackupListPreference.setEntries(entries.toArray(new String[1]));
				mRestoreBackupListPreference.setEntryValues(values.toArray(new String[1]));
			}
		}

		private List<List<String>> initBackupsList() {
			List<String> entries = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			if(!isCancelled()) {
				String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/QuickControlPanel/backups";
				File dirs = new File(dirPath);
				if(dirs.exists()) {
					File[] listOfFiles = dirs.listFiles();
					for(File f : listOfFiles) {
						if(f.isDirectory()) {
							entries.add(f.getName());
							values.add(f.getName());
						}
					}
				}
			}
			List<List<String>> result = new ArrayList<List<String>>();
			result.add(entries);
			result.add(values);

			return result;
		}
	}


	public static class RestoreService extends IntentService {

		private static final String TAG = "RestoreService";
		private static final int ID = 4295;
		private static final int ID_END = 4296;
		public static boolean isWorking = false;

		public RestoreService() {
			this(TAG);
		}

		public RestoreService(String name) {
			super(name);
		}

		@Override
		protected void onHandleIntent(Intent intent) {
			isWorking = true;
			if(intent != null) {
				boolean result = false;
				String name = intent.getStringExtra("name");
				postStartNotification(name);
				String path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/QuickControlPanel/backups/"+name;
				String prefsPath = path+"/prefs.xml";
				try {

					File filesDir = getFilesDir();
					String destPath = path+"/files";
					File filesDirSrc = new File(destPath);
					if(filesDirSrc.exists()) {
						FileUtils.copyDirectory(filesDirSrc, filesDir, true);
					}

					String xmlContents = FileUtils.readFile(prefsPath);
					XmlToPreferenceWrapper unwrapper = new XmlToPreferenceWrapper(xmlContents);
					if(unwrapper.isValid()) {
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						unwrapper.applyToPreferences(prefs);
						result = true;
					} else {
						Log.e(TAG, "Restoring preferences failed due to corrupt XmlToPreferenceWrapper");
						result = false;
					}
				} catch (IOException e) {
					Log.e(TAG, "Restoring preferences failed due to I/O error ["+e.getMessage()+"]");
					result = false;
				}

				if(result) {
					postSuccessNotification(name);
					Intent serviceIntent = new Intent(getApplicationContext(), ControlService.class);
					if(LaunchResolver.isServiceEnabled(getApplicationContext())) {
						stopService(serviceIntent);
						startService(serviceIntent);
					}
				} else {
					postFailNotification(name);
				}
			}
			isWorking = false;
		}

		private void postStartNotification(String name) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setContentTitle(getResources().getString(R.string.backup_restore_in_progress));
			builder.setContentText(getResources().getString(R.string.backup_backup_summary)+" "+name);
			builder.setSmallIcon(R.drawable.ic_notification);
			builder.setTicker(getResources().getString(R.string.backup_restore_in_progress));
			builder.setOngoing(true);
			manager.notify(ID, builder.build());
		}

		private void postSuccessNotification(String name) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(ID);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setContentTitle(getResources().getString(R.string.backup_restore_success));
			builder.setContentText(getResources().getString(R.string.backup_backup_summary)+" "+name);
			builder.setSmallIcon(R.drawable.ic_notification_success);
			builder.setTicker(getResources().getString(R.string.backup_restore_success));
			builder.setOngoing(false);
			manager.notify(ID_END, builder.build());
		}

		private void postFailNotification(String name) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(ID);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setContentTitle(getResources().getString(R.string.backup_restore_fail));
			builder.setContentText(getResources().getString(R.string.backup_backup_summary)+" "+name);
			builder.setSmallIcon(R.drawable.ic_notification_fail);
			builder.setTicker(getResources().getString(R.string.backup_restore_fail));
			builder.setOngoing(false);
			manager.notify(ID_END, builder.build());
		}

	}


	public static class BackupService extends IntentService {

		private static final String TAG = "BackupService";
		private static final int ID = 3295;
		private static final int ID_END = 3296;
		public static boolean isWorking = false;

		public BackupService() {
			this(TAG);
		}

		public BackupService(String name) {
			super(name);
		}

		@Override
		protected void onHandleIntent(Intent intent) {
			isWorking = true;
			if(intent != null) {
				boolean result = false;
				String name = intent.getStringExtra("name");
				postStartNotification(name);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				PreferenceToXmlWrapper wrapper = new PreferenceToXmlWrapper(prefs);
				if(wrapper.isValid()) {
					String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/QuickControlPanel/backups/"+name;
					File dir = new File(dirPath);
					dir.mkdirs();
					File bkpFile = new File(dir, "prefs.xml");
					try {

						wrapper.saveToFile(bkpFile);

						File filesDir = getFilesDir();
						String destPath = dirPath+"/files";
						File destDir = new File(destPath);
						destDir.mkdirs();

						FileUtils.copyDirectory(filesDir, destDir, true);
						result = true;
					} catch (IOException e) {
						Log.e(TAG, "Saving preferences failed due to I/O error ["+e.getMessage()+"]");
					}
				} else {
					Log.e(TAG, "Saving preferences failed due to corrupt PreferenceToXmlWrapper");
				}
				if(result) {
					postSuccessNotification(name);
				} else {
					postFailNotification(name);
				}
			}
			isWorking = false;
		}

		private void postStartNotification(String name) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setContentTitle(getResources().getString(R.string.backup_backup_in_progress));
			builder.setContentText(getResources().getString(R.string.backup_backup_summary)+" "+name);
			builder.setSmallIcon(R.drawable.ic_notification_success);
			builder.setTicker(getResources().getString(R.string.backup_backup_in_progress));
			builder.setOngoing(true);
			manager.notify(ID, builder.build());
		}

		private void postSuccessNotification(String name) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(ID);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setContentTitle(getResources().getString(R.string.backup_backup_success));
			builder.setContentText(getResources().getString(R.string.backup_backup_summary)+" "+name);
			builder.setSmallIcon(R.drawable.ic_notification_success);
			builder.setTicker(getResources().getString(R.string.backup_backup_success));
			builder.setOngoing(false);
			manager.notify(ID_END, builder.build());
		}

		private void postFailNotification(String name) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(ID);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setContentTitle(getResources().getString(R.string.backup_backup_fail));
			builder.setContentText(getResources().getString(R.string.backup_backup_summary)+" "+name);
			builder.setSmallIcon(R.drawable.ic_notification_fail);
			builder.setTicker(getResources().getString(R.string.backup_backup_fail));
			builder.setOngoing(false);
			manager.notify(ID_END, builder.build());
		}
	}
}


