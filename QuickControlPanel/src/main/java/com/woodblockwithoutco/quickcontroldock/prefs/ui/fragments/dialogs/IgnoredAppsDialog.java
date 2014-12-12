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
package com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.woodblockwithoutco.quickcontroldock.R;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.adapters.IgnoredAppsAdapter;
import com.woodblockwithoutco.quickcontroldock.util.AppRecord;
import com.woodblockwithoutco.quickcontroldock.util.AppRecordComparator;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

public class IgnoredAppsDialog extends DialogFragment {

	private static final String TAG = "IgnoredAppsDialog";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		PackageManager pm = getActivity().getPackageManager();
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		List<AppRecord> apps = new ArrayList<AppRecord>();
		for(PackageInfo info : packages) {
			try {
				if(pm.getApplicationInfo(info.packageName, 0).enabled) {
					String title = info.applicationInfo.loadLabel(pm).toString();
					AppRecord appRecord = new AppRecord(info.packageName, title);
					apps.add(appRecord);
				}
			} catch (NameNotFoundException e) {
				Log.e(TAG, "Missing package, let's proceed");
			}
		}
		Collections.sort(apps, new AppRecordComparator());
		IgnoredAppsAdapter adapter = new IgnoredAppsAdapter(getActivity().getApplicationContext(), apps);
		builder.setAdapter(adapter, null);
		
		builder.setPositiveButton(R.string.close, null);
		
	    return builder.create();
	}
}
