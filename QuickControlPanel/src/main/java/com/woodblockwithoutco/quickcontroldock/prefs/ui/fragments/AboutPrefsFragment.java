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

import java.util.Random;

import com.woodblockwithoutco.fragment.BackButtonPreferenceFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.LicenseDialog;
import com.woodblockwithoutco.quickcontroldock.util.ClipboardUtil;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.widget.Toast;

public class AboutPrefsFragment extends BackButtonPreferenceFragment {
	
	private static final int TAP_COUNT = 5;
	private static final long TAP_DELAY_MILLIS = 1000;
	
	private static final String TAG = "AboutPrefsFragment";
	private static final String OPEN_SOURCE_LICENSES_TAG = "OpenSourceLicensesDialog";
	private int mVersionCode = 0;
	private String mVersionName = "";
	private LicenseDialog mLicenseDialog;
	
	
	private Random mRandom = new Random();
	private Handler mHandler = new Handler();
	private int mTapCount = 0;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mTapCount = 0;
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.about_prefs);
		
		Preference appVersion = findPreference("about_app_version"); 
		Preference appBuild = findPreference("about_app_build");
		
		try {
			PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			appVersion.setSummary(pInfo.versionName);
			appBuild.setSummary(""+pInfo.versionCode);
			mVersionCode = pInfo.versionCode;
			mVersionName = pInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Something went really wrong - our own package is missing");
		}
		
		OnPreferenceClickListener l = new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mHandler.postDelayed(mRunnable, TAP_DELAY_MILLIS);
				mTapCount++;
				if(mTapCount == TAP_COUNT) Toast.makeText(getActivity(), getRandomStringForSomeBraveSoul(), Toast.LENGTH_LONG).show();
				return true;
			}
		};
		
		appVersion.setOnPreferenceClickListener(l);
		findPreference("about_app_name").setOnPreferenceClickListener(l);
		appBuild.setOnPreferenceClickListener(l);
		
		findPreference("about_copy_theme_kit_link").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				ClipboardUtil.copyToClipboard(getActivity().getApplicationContext(), getActivity().getResources().getText(R.string.theme_kit_link).toString());
				return true;
			}
		});
		
		findPreference("send_feedback").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "wryyy906@gmail.com", null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Quick Control Panel v"+mVersionName+" [build "+mVersionCode+"]");
				startActivity(Intent.createChooser(emailIntent, getActivity().getResources().getString(R.string.about_feedback)));
				return true;
			}
		});
		
		mLicenseDialog = new LicenseDialog();
		
		findPreference("open_source_licenses").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mLicenseDialog.show(getFragmentManager(), OPEN_SOURCE_LICENSES_TAG);
				return true;
			}
		});
	}
	
	private String getRandomStringForSomeBraveSoul() {
		int number = mRandom.nextInt(10);
		String[] forBraveOnes = getActivity().getResources().getStringArray(R.array.for_brave_souls);
		return forBraveOnes[number];
	}
}
