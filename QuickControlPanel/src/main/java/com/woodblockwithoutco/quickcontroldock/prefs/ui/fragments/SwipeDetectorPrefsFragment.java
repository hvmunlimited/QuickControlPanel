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

import com.curvefish.widget.preference.SliderPreference;
import com.woodblockwithoutco.fragment.BackButtonPreferenceFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.GeneralResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LaunchResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.ui.ViewService;
import com.woodblockwithoutco.quickcontroldock.R;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class SwipeDetectorPrefsFragment extends BackButtonPreferenceFragment {

	private final static int HOLO_COLOR = 0xFF33B5E5;
	
	public static boolean isActive = false;
	
	private SliderPreference mDetectorHeightPref;
	private SliderPreference mDetectorWidthPref;


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.swipe_detector_prefs);
		
		mDetectorHeightPref = (SliderPreference) findPreference(Keys.Launch.SWIPE_DETECTOR_SIZE_1);
		mDetectorWidthPref = (SliderPreference) findPreference(Keys.Launch.SWIPE_DETECTOR_SIZE_2);
		
		findPreference("dimens_swipe_detector_align").setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if("MIDDLE".equals(newValue)) {
					mDetectorWidthPref.setTitle(R.string.swipe_detector_width_pref);
					mDetectorHeightPref.setTitle(R.string.swipe_detector_height_pref);
				} else {
					mDetectorWidthPref.setTitle(R.string.swipe_detector_height_pref);
					mDetectorHeightPref.setTitle(R.string.swipe_detector_width_pref);
				}
				return true;
			}
		});
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		
		if(LaunchResolver.getSwipeDetectorAlignment(getActivity()) != Gravity.BOTTOM) {
			mDetectorWidthPref.setTitle(R.string.swipe_detector_height_pref);
			mDetectorHeightPref.setTitle(R.string.swipe_detector_width_pref);
		} else {
			mDetectorWidthPref.setTitle(R.string.swipe_detector_width_pref);
			mDetectorHeightPref.setTitle(R.string.swipe_detector_height_pref);
		}
		
		ViewService service = ControlService.getInstance();
		if(ControlService.isRunning() && service != null) {
			View view = service.getSecondaryView();
			if(view != null && service.isSecondaryAttachedToWindow() && LaunchResolver.isSwipeDetectorEnabled(getActivity())) ((LinearLayout)view.findViewById(R.id.swipe_detector)).setBackgroundColor(HOLO_COLOR);
		}
		isActive = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		ViewService service = ControlService.getInstance();
		if(ControlService.isRunning() && service != null) {
			View view = service.getSecondaryView();
			boolean isDebugColor = GeneralResolver.isSwipeDetectorDebugVisible(getActivity());
			int colorId = isDebugColor ? R.color.detector_debug_color : R.color.detector_color;
			if(view != null && service.isSecondaryAttachedToWindow()) ((LinearLayout)view.findViewById(R.id.swipe_detector)).setBackgroundColor(getResources().getColor(colorId));
		}
		isActive = false;
	}
}
