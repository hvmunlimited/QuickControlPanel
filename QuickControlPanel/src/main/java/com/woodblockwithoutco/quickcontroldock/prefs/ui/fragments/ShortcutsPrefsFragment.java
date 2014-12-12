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

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.woodblockwithoutco.fragment.BackButtonPreferenceFragment;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ShortcutsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.ShortcutsPickDialog;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.util.icon.IconPackNameParser;
import com.woodblockwithoutco.quickcontroldock.util.icon.IconPackRetriever;
import com.woodblockwithoutco.quickcontroldock.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;


public class ShortcutsPrefsFragment extends BackButtonPreferenceFragment {

	private final static String PICK_DIALOG_TAG = "ShortcutsPickDialog";
	private ShortcutsPickDialog mPickShortcutsDialog;
	private ListPreference mIconPackPref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.shortcuts_prefs);

		findPreference("pick_shortcuts").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference pref) {
				if(mPickShortcutsDialog == null) {
					mPickShortcutsDialog = new ShortcutsPickDialog();
				}
				mPickShortcutsDialog.show(getFragmentManager(), PICK_DIALOG_TAG);
				return true;
			}
		});

		mIconPackPref = (ListPreference) findPreference(Keys.Shortcuts.SHORTCUTS_EXTERNAL_ICON_PACK);

		findPreference("apply_icon_pack").setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference pref) {

				if(!ShortcutsResolver.isExternalIconPackUsed(getActivity())) return true;

				String title = getString(R.string.icon_pack_please_wait);
				String message = getString(R.string.icon_pack_icon_pack_loading);
				final ProgressDialog pd = ProgressDialog.show(getActivity(), title, message, true, false);
				new AsyncTask<Void, Void, String>() {

					@Override
					public void onPreExecute() {
						pd.show();
					}

					@Override
					protected String doInBackground(Void... args) {
						try {
							IconPackNameParser.load(getActivity(), ShortcutsResolver.getExternalIconPackName(getActivity()));
						} catch (NameNotFoundException e) {
							PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(Keys.Shortcuts.SHORTCUTS_EXTERNAL_ICON_PACK, "none").commit();
							return "[PACKAGE_MISSING]";
						} catch (XmlPullParserException e) {
							PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(Keys.Shortcuts.SHORTCUTS_EXTERNAL_ICON_PACK, "none").commit();
							return "[XML_PULL_PARSER_EXCEPTION]";
						} catch(IOException e) {
							return "[IO_EXCEPTION]";
						}
						return null;
					}

					@Override
					public void onPostExecute(String result) {
						String toastMessage;
						if(result == null) {
							Intent serviceIntent = new Intent(getActivity().getApplicationContext(), ControlService.class);
							getActivity().stopService(serviceIntent);
							getActivity().startService(serviceIntent);
							toastMessage = getActivity().getString(R.string.icon_pack_apply_success);
						} else {
							toastMessage = getActivity().getString(R.string.icon_pack_apply_fail)+result;
						}
						Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
						pd.dismiss();
					}
				}.execute();

				return true;

			}

		});

	}

	@Override
	public void onResume() {
		super.onResume();
		new AsyncTask<Void, Void, Void>() {

			private List<String> mPkgs;
			private List<String> mPkgNames;

			@Override
			protected void onPreExecute() {
				mIconPackPref.setEnabled(false);
			}

			@Override
			protected Void doInBackground(Void... params) {
				mPkgs = IconPackRetriever.getIconPackages(getActivity());
				mPkgNames = IconPackRetriever.getIconPackNames(getActivity(), mPkgs);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				mIconPackPref.setEnabled(true);
				mIconPackPref.setEntries(mPkgNames.toArray(new String[1]));
				mIconPackPref.setEntryValues(mPkgs.toArray(new String[1]));
			}

		}.execute();
	}
}
