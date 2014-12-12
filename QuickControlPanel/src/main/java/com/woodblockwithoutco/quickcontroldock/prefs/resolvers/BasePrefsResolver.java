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
package com.woodblockwithoutco.quickcontroldock.prefs.resolvers;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class BasePrefsResolver {

	protected static SharedPreferences sPrefsCache;

	public static void checkPrefsCache(Context context) {
		if(sPrefsCache == null) {
			sPrefsCache = PreferenceManager.getDefaultSharedPreferences(context);
		}
	}

	protected static int getInt(Context context, String key, int def) {
		checkPrefsCache(context);
		return sPrefsCache.getInt(key, def);
	}

	protected static boolean getBoolean(Context context, String key, boolean def) {
		checkPrefsCache(context);
		return sPrefsCache.getBoolean(key, def);
	}

	protected static float getFloat(Context context, String key, float def) {
		checkPrefsCache(context);
		return sPrefsCache.getFloat(key, def);
	}

	protected static String getString(Context context, String key, String def) {
		checkPrefsCache(context);
		return sPrefsCache.getString(key, def);
	}
	
	protected static Set<String> getStringSet(Context context, String key, Set<String> def) {
		checkPrefsCache(context);
		return sPrefsCache.getStringSet(key, def);
	}



}
