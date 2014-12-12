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
package com.woodblockwithoutco.fragment;
import com.woodblockwithoutco.quickcontroldock.R;

import android.preference.PreferenceActivity;
import android.app.Activity;
import android.preference.PreferenceFragment;
import android.app.ActionBar;

public class BackButtonPreferenceFragment extends PreferenceFragment {

	
	@Override
	public void onResume() {
		super.onResume();
		if(shouldEnableHomeBackButton()) {
			setBackHomeButtonEnabled(true);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(shouldEnableHomeBackButton()) {
			setBackHomeButtonEnabled(false);
		}
	}
	
	
	private void setBackHomeButtonEnabled(boolean enabled) {
		PreferenceActivity a = getPreferenceActivity();
		if(a != null) {
			ActionBar ab = a.getActionBar();
			if(ab != null) {
				ab.setDisplayHomeAsUpEnabled(enabled);
				ab.setHomeButtonEnabled(enabled);
			}
		}
	}
	
	private boolean shouldEnableHomeBackButton() {
		PreferenceActivity a = getPreferenceActivity();
		boolean result = false;
		if(a != null) {
			result = !getActivity().getResources().getBoolean(R.bool.is_tablet);
		}
		return result;
	}
	
	private PreferenceActivity getPreferenceActivity() {
		Activity a = getActivity();
		if (!(a instanceof PreferenceActivity)) {
			return null;
		}
		
		return (PreferenceActivity) a;
	}
}
