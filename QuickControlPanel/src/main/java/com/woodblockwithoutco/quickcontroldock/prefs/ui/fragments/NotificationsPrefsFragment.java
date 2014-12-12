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

import com.woodblockwithoutco.fragment.BackButtonPreferenceFragment;
import com.woodblockwithoutco.quickcontroldock.R;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.IgnoredAppsDialog;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

public class NotificationsPrefsFragment extends BackButtonPreferenceFragment {
	
	protected static final String DIALOG_TAG = "IGNORED_APPS_DIALOG";
	private IgnoredAppsDialog mIgnoredAppsDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.notifications_prefs);
		
		findPreference("notifications_ignore").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if(mIgnoredAppsDialog == null) {
					mIgnoredAppsDialog = new IgnoredAppsDialog();
				}
				
				mIgnoredAppsDialog.show(getFragmentManager(), DIALOG_TAG);
				return true;
			}
			
		});
	}
}
