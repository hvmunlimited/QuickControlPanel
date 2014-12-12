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
package com.woodblockwithoutco.quickcontroldock.util.icon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.woodblockwithoutco.quickcontroldock.R;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

public class IconPackRetriever {

	private final static String NOVA_CATEGORY = "com.teslacoilsw.launcher.THEME";
	private final static String APEX_CATEGORY = "com.anddoes.launcher.THEME";


	public static List<String> getIconPackages(Context context) {
		List<String> result = new ArrayList<String>();
		Set<String> preResult = new HashSet<String>();

		final Intent apexThemeIntent = new Intent(Intent.ACTION_MAIN);
		apexThemeIntent.addCategory(APEX_CATEGORY);
		
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> themes = pm.queryIntentActivities(apexThemeIntent, 0);
		for(ResolveInfo i : themes) {
			if(i.activityInfo != null) {
				preResult.add(i.activityInfo.packageName);
			}
		}
		
		final Intent novaThemeIntent = new Intent(Intent.ACTION_MAIN);
		novaThemeIntent.addCategory(NOVA_CATEGORY);
		themes = pm.queryIntentActivities(novaThemeIntent, 0);
		for(ResolveInfo i : themes) {
			if(i.activityInfo != null) {
				preResult.add(i.activityInfo.packageName);
			}
		}
	

		result.add("none");
		
		result.addAll(preResult);
		return result;
	}
	
	public static List<String> getIconPackNames(Context context, List<String> pkgs) {
		PackageManager pm = context.getPackageManager();
		List<String> result = new ArrayList<String>();
		result.add(context.getResources().getString(R.string.shortcut_launcher_icon_pack_none));
		for(String s : pkgs) {
			if(s.equals("none")) continue;
			try {
				result.add(pm.getApplicationLabel(pm.getApplicationInfo(s, 0)).toString());
			} catch (NameNotFoundException e) {
				result.add(s);
			}
		}
		return result;
	}

}
