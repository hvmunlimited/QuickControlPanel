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
package com.woodblockwithoutco.quickcontroldock.prefs.editors;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class BasePrefsEditor {

	protected static SharedPreferences sPrefsCache;

	public static void refreshPrefsCache(Context context) {
		sPrefsCache = PreferenceManager.getDefaultSharedPreferences(context);
	}

	protected static boolean putInt(Context context, String key, int value) {
		refreshPrefsCache(context);
		return sPrefsCache.edit().putInt(key, value).commit();
	}
	
	protected static boolean putBoolean(Context context, String key, boolean value) {
		refreshPrefsCache(context);
		return sPrefsCache.edit().putBoolean(key, value).commit();
	}
	
	protected static boolean putFloat(Context context, String key, float value) {
		refreshPrefsCache(context);
		return sPrefsCache.edit().putFloat(key, value).commit();
	}
	
	protected static boolean putString(Context context, String key, String value) {
		refreshPrefsCache(context);
		return sPrefsCache.edit().putString(key, value).commit();
	}
	
	protected static boolean putStringSet(Context context, String key, Set<String> set) {
		refreshPrefsCache(context);
		return sPrefsCache.edit().putStringSet(key, set).commit();
	}
	
	protected static boolean remove(Context context, String key) {
		refreshPrefsCache(context);
		return sPrefsCache.edit().remove(key).commit();
	}
	

}
