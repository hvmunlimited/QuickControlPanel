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

import com.woodblockwithoutco.quickcontroldock.model.PanelType;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;

import android.content.Context;
import android.util.Log;

public class PanelsOrderResolver extends BasePrefsResolver {

	private final static String DEFAULT_PANELS_ORDER = 
			PanelType.SHORTCUTS.name()+":"+
					PanelType.MUSIC+":"+
					PanelType.TOGGLES.name()+":"+
					PanelType.INFO.name();
	private static final String TAG = "PanelsOrderResolver";

	public static List<String> getPanelsOrder(Context context) {
		String unparsedString = getString(context, Keys.PanelsOrder.PANELS_ORDER, DEFAULT_PANELS_ORDER);
		String[] split = unparsedString.split(":");
		if(split.length < 4) {
			Log.e(TAG, "Returning fallback order");
			unparsedString = DEFAULT_PANELS_ORDER;
			split = unparsedString.split(":");
		}
		
		List<String> result = new ArrayList<String>(4);
		for(String s : split) {
			result.add(s);
		}
		return result;
	}


}
