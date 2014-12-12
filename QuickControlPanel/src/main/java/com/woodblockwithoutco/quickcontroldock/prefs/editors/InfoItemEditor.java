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

import java.util.List;

import android.content.Context;


import com.woodblockwithoutco.quickcontroldock.model.text.InfoItemType;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.InfoResolver;

public class InfoItemEditor extends BasePrefsEditor {
	
	private final static int FLAG_USES_TIME_ITEM = 1 << 0;
	private final static int FLAG_USES_DATE_ITEM = 1 << 1;
	private final static int FLAG_USES_BATTERY_ITEM = 1 << 2;
	
	public static boolean saveInfoItemsOrder(Context context, List<String> order) {
		StringBuilder orderBuilder = new StringBuilder();
		for(String s : order) {
			try {
				InfoItemType.valueOf(s);
				orderBuilder.append(s).append(":");
			} catch(IllegalArgumentException e) {
				throw new IllegalArgumentException("Trying to save unknown info item type!");
			}	
		}
		orderBuilder.deleteCharAt(orderBuilder.length() - 1);
		return putString(context, Keys.Info.INFO_ITEMS_STRING, orderBuilder.toString());
	}
	
	public static boolean setInfoItemEnabled(Context context, InfoItemType type, boolean enabled) {
		int flags = InfoResolver.getInfoItemsFlags(context);
		int flag = FLAG_USES_TIME_ITEM;
		switch(type) {
		case BATTERY:
			flag = FLAG_USES_BATTERY_ITEM;
			break;
		case DATE:
			flag = FLAG_USES_DATE_ITEM;
			break;
		case TIME:
			flag = FLAG_USES_TIME_ITEM;
			break;
		}
		
		if(enabled) {
			flags |= flag;
		} else {
			flags &= ~flag;
		}
		
		return putInt(context, Keys.Info.INFO_ITEMS_ENABLED, flags);
	}

}
