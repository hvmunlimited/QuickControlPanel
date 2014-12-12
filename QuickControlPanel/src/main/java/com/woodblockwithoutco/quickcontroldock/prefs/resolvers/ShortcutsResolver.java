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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.util.ScreenUtils;

public class ShortcutsResolver extends BasePrefsResolver {

	private static final int DEFAULT_SHORTCUTS_SIZE = 100;
	private static final int DEFAULT_SHORTCUT_TEXT_SIZE = 16;
	private static final int DEFAULT_SHORTCUT_DISTANCE = 25;
	private static final int DEFAULT_SHORTCUT_PADDING = 5;

	public static boolean isShortcutsEnabled(Context context) {
		return getBoolean(context, Keys.Shortcuts.ENABLE_SHORTCUTS, true);
	}
	
	public static int getShortcutsDistance(Context context) {
		float percent = (float) (getInt(context, Keys.Shortcuts.SHORTCUTS_DISTANCE, DEFAULT_SHORTCUT_DISTANCE)) / 100;
		return (int) (percent * getShortcutSize(context)) / 2;
	}
	
	public static boolean isShortcutTitleEnabled(Context context) {
		return getBoolean(context, Keys.Shortcuts.ENABLE_SHORTCUTS_TITLE, true);
	}
	
	public static List<String> getShortcutsOrder(Context context) {
		String shortcutsUnparsed = getString(context, Keys.Shortcuts.SHORTCUTS_ORDER, "");
		String[] splitShortcuts = shortcutsUnparsed.split(":");
		List<String> result = new ArrayList<String>();
		if(shortcutsUnparsed.isEmpty()) return result;
		for(String s : splitShortcuts) {
			result.add(s);
		}
		return result;
	}
	
	public static boolean isCustomIconUsed(Context context, String className) {
		return getBoolean(context, className+"_icon", false);
	}
	
	public static int getShortcutSize(Context context) {
		return getInt(context, Keys.Shortcuts.SHORTCUTS_SIZE, DEFAULT_SHORTCUTS_SIZE);
	}
	
	public static int getShortcutTextSize(Context context) {
		return getInt(context, Keys.Shortcuts.SHORTCUTS_TITLE_SIZE, DEFAULT_SHORTCUT_TEXT_SIZE);
	}
	
	public static boolean isExternalIconPackUsed(Context context) {
		return !getExternalIconPackName(context).equals("none");
	}
	
	public static String getExternalIconPackName(Context context) {
		return getString(context, Keys.Shortcuts.SHORTCUTS_EXTERNAL_ICON_PACK, "none");
	}

	public static int getShortcutsPadding(Context context) {
		int dp = getInt(context, Keys.Shortcuts.SHORTCUTS_PADDING, DEFAULT_SHORTCUT_PADDING);
		return ScreenUtils.dipToPixels(context, dp);
	}
}
