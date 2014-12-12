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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.woodblockwithoutco.quickcontroldock.model.text.InfoItemType;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;

import android.content.Context;

public class InfoResolver extends BasePrefsResolver {

	private final static String DEFAULT_DATE_FORMAT = "E, dd/MM";

	private final static int FLAG_USES_TIME_ITEM = 1 << 0;
	private final static int FLAG_USES_DATE_ITEM = 1 << 1;
	private final static int FLAG_USES_BATTERY_ITEM = 1 << 2;
	private final static int DEFAULT_FLAGS = FLAG_USES_TIME_ITEM |
			FLAG_USES_DATE_ITEM |
			FLAG_USES_BATTERY_ITEM;
	private static final int DEFAULT_TEXT_SIZE = 16;


	public static boolean isInfoPanelEnabled(Context context) {
		return getBoolean(context, Keys.Info.INFO_ENABLED, true);
	}

	public static SimpleDateFormat getDateFormat(Context context) {
		String dateFormat = getString(context, Keys.Info.DATE_FORMAT, DEFAULT_DATE_FORMAT);


		SimpleDateFormat format = null;
		try {
			format = new SimpleDateFormat(dateFormat, Locale.getDefault());
		} catch(IllegalArgumentException e) {
			format = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
		}

		return format;
	}

	public static boolean isInfoItemEnabled(Context context, InfoItemType type) {
		int flags = getInt(context, Keys.Info.INFO_ITEMS_ENABLED, DEFAULT_FLAGS);
		int flag = FLAG_USES_TIME_ITEM;
		switch(type) {
		case BATTERY:
			flag  = FLAG_USES_BATTERY_ITEM;
			break;
		case DATE:
			flag = FLAG_USES_DATE_ITEM;
			break;
		case TIME:
			flag = FLAG_USES_TIME_ITEM;
			break;
		}
		return (flags & flag) != 0;
	}
	
	public static List<String> getInfoItemsOrderListAsString(Context context) {
		List<String> result = new ArrayList<String>();
		final String defaultOrder = InfoItemType.DATE.name() + ":" +
				InfoItemType.TIME.name() + ":" +
				InfoItemType.BATTERY.name();
		
		String orderString = getString(context, Keys.Info.INFO_ITEMS_STRING, defaultOrder);
		
		String[] splitOrder = orderString.split(":");
		for(String s : splitOrder) {
			result.add(s);
		}
		
		return result;
	}
	
	public static List<InfoItemType> getInfoItemsOrderList(Context context) {
		List<InfoItemType> result = new ArrayList<InfoItemType>();
		List<String> resultAsString = getInfoItemsOrderListAsString(context);
		for(String s : resultAsString) {
			InfoItemType type = InfoItemType.valueOf(s);
			if(isInfoItemEnabled(context, type)) {
				result.add(type);
			}
		}
		return result;
	}
	
	public static int getInfoItemsFlags(Context context) {
		return getInt(context, Keys.Info.INFO_ITEMS_ENABLED, DEFAULT_FLAGS);
	}
	
	public static int getInfoTextSize(Context context) {
		return getInt(context, Keys.Info.INFO_ITEMS_SIZE, DEFAULT_TEXT_SIZE);
	}

}
