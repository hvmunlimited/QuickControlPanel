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
package com.woodblockwithoutco.quickcontroldock.util;


import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class AppShortcutResolver {

	private PackageManager mPackageManager;
	private Drawable mMissingAppDrawable;
	private Context mContext;

	public AppShortcutResolver(Context context) {
		mContext = context;
		mPackageManager = context.getPackageManager();
		mMissingAppDrawable = context.getResources().getDrawable(R.drawable.missing_app);
		mMissingAppDrawable.setColorFilter(ColorsResolver.getActiveColor(context), PorterDuff.Mode.SRC_ATOP);
	}

	
	public String getLabel(String packageName, String className) {
		try {
			ComponentName componentName = new ComponentName(packageName, className);
			ActivityInfo info = mPackageManager.getActivityInfo(componentName, 0);
			return info.loadLabel(mPackageManager).toString();
		} catch (NameNotFoundException e) {
			return mContext.getResources().getString(R.string.shortcuts_app_not_found);
		}
	}

	
	public Drawable getIcon(String packageName, String className) {
		try {
			ComponentName componentName = new ComponentName(packageName, className);
			return mPackageManager.getActivityIcon(componentName);
		} catch (NameNotFoundException e) {
			return mMissingAppDrawable;
		}
	}

	
	public Intent getIntent(String packageName, String className) {
		if (packageName.equals("null") || className.equals("null")) {
			return null;
		}

		if(!isPackageExist(packageName)) {
			//return null;
		}

		Intent intent = new Intent(packageName);
		intent.setPackage(packageName);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setComponent(new ComponentName(packageName, className));
		return intent;
	}

	private boolean isPackageExist(String packageName) {
		try {
			mPackageManager.getPackageInfo(packageName, 0);
			return true;
		} catch(NameNotFoundException e) {
			return false;
		}
	}
}
