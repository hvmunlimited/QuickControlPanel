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
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.SettingsActivity;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;

public class ColorsPrefsFragment extends BackButtonPreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.colors_prefs);
		
		findPreference("color_apply_white_theme").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				getSettingsActivity().stopListeningForPrefsChanges();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				prefs.edit().putInt(Keys.Color.IDLE_COLOR, ColorsStorage.White.DEFAULT_IDLE_COLOR).commit();
				prefs.edit().putInt(Keys.Color.ACTIVE_COLOR, ColorsStorage.White.DEFAULT_ACTIVE_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PRESSED_COLOR, ColorsStorage.White.DEFAULT_PRESSED_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_BACKGROUND_COLOR, ColorsStorage.White.DEFAULT_PANEL_BACKGROUND_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECTION_BG_COLOR, ColorsStorage.White.DEFAULT_SECTION_BACKGROUND_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECTION_SHADOW_COLOR, ColorsStorage.White.DEFAULT_SECTION_SHADOW_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECOND_BACKGROUND_COLOR, ColorsStorage.White.DEFAULT_PANEL_BACKGROUND_COLOR_SECONDARY).commit();
				prefs.edit().putInt(Keys.Color.TEXT_COLOR, ColorsStorage.White.DEFAULT_INFO_TEXT_COLOR).commit();
				prefs.edit().putInt(Keys.Color.TOGGLE_BG_COLOR, ColorsStorage.White.DEFAULT_IDLE_BG_COLOR).commit();
				
				getSettingsActivity().startListeningForPrefsChanges();
				
				Intent intent = new Intent(getActivity(), ControlService.class);
				getActivity().stopService(intent);
				getActivity().startService(intent);
				
				getActivity().onBackPressed();
				
				return false;
			}
		});
		
		findPreference("color_apply_material_theme").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				getSettingsActivity().stopListeningForPrefsChanges();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				prefs.edit().putInt(Keys.Color.IDLE_COLOR, ColorsStorage.Material.DEFAULT_IDLE_COLOR).commit();
				prefs.edit().putInt(Keys.Color.ACTIVE_COLOR, ColorsStorage.Material.DEFAULT_ACTIVE_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PRESSED_COLOR, ColorsStorage.Material.DEFAULT_PRESSED_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_BACKGROUND_COLOR, ColorsStorage.Material.DEFAULT_PANEL_BACKGROUND_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECTION_BG_COLOR, ColorsStorage.Material.DEFAULT_SECTION_BACKGROUND_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECTION_SHADOW_COLOR, ColorsStorage.Material.DEFAULT_SECTION_SHADOW_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECOND_BACKGROUND_COLOR, ColorsStorage.Material.DEFAULT_PANEL_BACKGROUND_COLOR_SECONDARY).commit();
				prefs.edit().putInt(Keys.Color.TEXT_COLOR, ColorsStorage.Material.DEFAULT_INFO_TEXT_COLOR).commit();
				prefs.edit().putInt(Keys.Color.TOGGLE_BG_COLOR, ColorsStorage.Material.DEFAULT_IDLE_BG_COLOR).commit();
				
				getSettingsActivity().startListeningForPrefsChanges();
				
				Intent intent = new Intent(getActivity(), ControlService.class);
				getActivity().stopService(intent);
				getActivity().startService(intent);
				
				getActivity().onBackPressed();
				
				return false;
			}
		});
		
		findPreference("color_apply_black_theme").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				getSettingsActivity().stopListeningForPrefsChanges();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				prefs.edit().putInt(Keys.Color.IDLE_COLOR, ColorsStorage.Black.DEFAULT_IDLE_COLOR).commit();
				prefs.edit().putInt(Keys.Color.ACTIVE_COLOR, ColorsStorage.Black.DEFAULT_ACTIVE_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PRESSED_COLOR, ColorsStorage.Black.DEFAULT_PRESSED_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_BACKGROUND_COLOR, ColorsStorage.Black.DEFAULT_PANEL_BACKGROUND_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECTION_BG_COLOR, ColorsStorage.Black.DEFAULT_SECTION_BACKGROUND_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECTION_SHADOW_COLOR, ColorsStorage.Black.DEFAULT_SECTION_SHADOW_COLOR).commit();
				prefs.edit().putInt(Keys.Color.PANEL_SECOND_BACKGROUND_COLOR, ColorsStorage.Black.DEFAULT_PANEL_BACKGROUND_COLOR_SECONDARY).commit();
				prefs.edit().putInt(Keys.Color.TEXT_COLOR, ColorsStorage.Black.DEFAULT_INFO_TEXT_COLOR).commit();
				prefs.edit().putInt(Keys.Color.TOGGLE_BG_COLOR, ColorsStorage.Black.DEFAULT_IDLE_BG_COLOR).commit();
				
				getSettingsActivity().startListeningForPrefsChanges();
				
				Intent intent = new Intent(getActivity(), ControlService.class);
				getActivity().stopService(intent);
				getActivity().startService(intent);
				
				getActivity().onBackPressed();
				
				return false;
			}
		});
	}
	
	private static final class ColorsStorage {
		
		private static final class White {
			public static final int DEFAULT_IDLE_COLOR = 0xFF909090;
			public static final int DEFAULT_ACTIVE_COLOR = 0xFF202020;
			public static final int DEFAULT_PRESSED_COLOR = 0xB0DFDFDF;
			public static final int DEFAULT_PANEL_BACKGROUND_COLOR = 0xDFFFFFFF;
			public static final int DEFAULT_SECTION_BACKGROUND_COLOR = 0xFFFFFFFF;
			public static final int DEFAULT_SECTION_SHADOW_COLOR = 0xFFC0C0C0;
			public static final int DEFAULT_PANEL_BACKGROUND_COLOR_SECONDARY = 0xDFFFFFFF;
			public static final int DEFAULT_INFO_TEXT_COLOR = 0xFF000000;
			public static final int DEFAULT_IDLE_BG_COLOR = 0x00000000;
		}
		
		private static final class Black {
			public static final int DEFAULT_IDLE_COLOR = 0xFF606060;
			public static final int DEFAULT_ACTIVE_COLOR = 0xFFFFFFFF;
			public static final int DEFAULT_PRESSED_COLOR = 0xB0DFDFDF;
			public static final int DEFAULT_PANEL_BACKGROUND_COLOR = 0xDF000000;
			public static final int DEFAULT_SECTION_BACKGROUND_COLOR = 0x00000000;
			public static final int DEFAULT_SECTION_SHADOW_COLOR = 0x00000000;
			public static final int DEFAULT_PANEL_BACKGROUND_COLOR_SECONDARY = 0xDF000000;
			public static final int DEFAULT_INFO_TEXT_COLOR = 0xFFFFFFFF;
			public static final int DEFAULT_IDLE_BG_COLOR = 0x00000000;
		}
		
		private static final class Material {
			public static final int DEFAULT_IDLE_COLOR = 0xFF78909C;
			public static final int DEFAULT_ACTIVE_COLOR = 0xFFFFFFFF; 
			public static final int DEFAULT_PRESSED_COLOR = 0xB0455A64;
			public static final int DEFAULT_PANEL_BACKGROUND_COLOR = 0xDF263238;
			public static final int DEFAULT_SECTION_BACKGROUND_COLOR = 0x00000000;
			public static final int DEFAULT_SECTION_SHADOW_COLOR = 0x00000000;
			public static final int DEFAULT_PANEL_BACKGROUND_COLOR_SECONDARY = 0xDF000000;
			public static final int DEFAULT_INFO_TEXT_COLOR = 0xFFECEFF1;
			public static final int DEFAULT_IDLE_BG_COLOR = 0x00000000;
		}
	}
	
	private SettingsActivity getSettingsActivity() {
		return (SettingsActivity) getActivity();
	}
}
