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
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.InfoItemSortDialog;
import com.woodblockwithoutco.quickcontroldock.R;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

public class InfoPanelPrefsFragment extends BackButtonPreferenceFragment {
	
	private final static String SORT_DIALOG_TAG = "InfoItemSortDialog";
	private InfoItemSortDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.info_prefs);
		mDialog = new InfoItemSortDialog();
		findPreference("info_order_click").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mDialog.show(getFragmentManager(), SORT_DIALOG_TAG);
				return true;
			}
		});
	}
}
