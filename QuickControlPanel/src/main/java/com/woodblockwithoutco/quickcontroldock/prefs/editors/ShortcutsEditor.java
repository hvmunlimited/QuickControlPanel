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

import java.io.File;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.woodblockwithoutco.quickcontroldock.prefs.Keys;

public class ShortcutsEditor extends BasePrefsEditor {

	public static boolean setCustomIconEnabled(Context context, String className, boolean enabled) {
		if(enabled) {
			return putBoolean(context, className+"_icon", true);
		} else {
			String path = context.getFilesDir() + File.separator + className + ".png";
			File file = new File(path);
			if(file.exists()) {
				if(!file.delete()) {
					Log.e("ShortcutsEditor", "Can't delete custom icon file!");
				}
			}
			return remove(context, className+"_icon");
		}
	}
	
	public static boolean saveShortcuts(Context context, List<String> shortcutsList) {
		StringBuilder builder = new StringBuilder();
		for(String s : shortcutsList) {
			builder.append(s).append(":");
		}

		if(builder.length() > 0) {
			builder.delete(builder.length() - 1, builder.length());
		}
		
		String s = builder.toString();
		return putString(context, Keys.Shortcuts.SHORTCUTS_ORDER, s);
	}
}
