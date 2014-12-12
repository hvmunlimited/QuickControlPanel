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

import com.woodblockwithoutco.quickcontroldock.model.buttons.ButtonType;
import com.woodblockwithoutco.quickcontroldock.model.seekbar.SeekbarType;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;

public class ButtonsEditor extends BasePrefsEditor {

	private static final int FLAG_USES_DATA_BUTTON = 1 << 0;
	private static final int FLAG_USES_WIFI_BUTTON = 1 << 1;
	private static final int FLAG_USES_BLUETOOTH_BUTTON = 1 << 2;
	private static final int FLAG_USES_SOUND_BUTTON = 1 << 3;
	private static final int FLAG_USES_ROTATE_BUTTON = 1 << 4;
	private static final int FLAG_USES_AIRPLANE_BUTTON = 1 << 5;
	private static final int FLAG_USES_GPS_BUTTON = 1 << 6;
	private static final int FLAG_USES_SETTINGS_BUTTON = 1 << 7;
	private static final int FLAG_USES_FLASH_BUTTON = 1 << 8;
	private static final int FLAG_USES_SYNC_BUTTON = 1 << 9;
	private static final int FLAG_USES_WIFI_AP_BUTTON = 1 << 10;
	private static final int FLAG_USES_LOCK_NOW_BUTTON = 1 << 11;
	private static final int FLAG_USES_AUTO_BRIGHTNESS_BUTTON = 1 << 12;
	private static final int FLAG_USES_SCREEN_TIMEOUT_BUTTON = 1 << 13;
	
	private static final int FLAG_USES_MEDIA_SEEKBAR = 1 << 0;
	private static final int FLAG_USES_RINGER_SEEKBAR = 1 << 1;
	private static final int FLAG_USES_BRIGHTNESS_SEEKBAR = 1 << 2;

	public static boolean saveButtonsOrder(Context context, List<String> order) {
		StringBuilder orderBuilder = new StringBuilder();
		for(String s : order) {
			try {
				ButtonType.valueOf(s);
				orderBuilder.append(s).append(":");
			} catch(IllegalArgumentException e) {
				throw new IllegalArgumentException("Trying to save unknown toggle type!");
			}	
		}
		orderBuilder.deleteCharAt(orderBuilder.length() - 1);
		return putString(context, Keys.Toggles.BUTTONS_ORDER, orderBuilder.toString());
	}
	
	public static boolean saveSeekbarsOrder(Context context, List<String> order) {
		StringBuilder orderBuilder = new StringBuilder();
		for(String s : order) {
			try {
				SeekbarType.valueOf(s);
				orderBuilder.append(s).append(":");
			} catch(IllegalArgumentException e) {
				throw new IllegalArgumentException("Trying to save unknown seekbar type!");
			}	
		}
		orderBuilder.deleteCharAt(orderBuilder.length() - 1);
		return putString(context, Keys.Toggles.SEEKBARS_ORDER, orderBuilder.toString());
	}

	public static boolean setButtonEnabled(Context context, ButtonType type, boolean enabled) {
		int flags = TogglesResolver.getButtonsFlags(context);
		int flag = FLAG_USES_DATA_BUTTON;
		switch(type) {
		case AIRPLANE:
			flag = FLAG_USES_AIRPLANE_BUTTON;
			break;
		case BLUETOOTH:
			flag = FLAG_USES_BLUETOOTH_BUTTON;
			break;
		case DATA:
			flag = FLAG_USES_DATA_BUTTON;
			break;
		case FLASHLIGHT:
			flag = FLAG_USES_FLASH_BUTTON;
			break;
		case GPS:
			flag = FLAG_USES_GPS_BUTTON;
			break;
		case LOCK_NOW:
			flag = FLAG_USES_LOCK_NOW_BUTTON;
			break;
		case ROTATE:
			flag = FLAG_USES_ROTATE_BUTTON;
			break;
		case SETTINGS:
			flag = FLAG_USES_SETTINGS_BUTTON;
			break;
		case SOUND:
			flag = FLAG_USES_SOUND_BUTTON;
			break;
		case SYNC:
			flag = FLAG_USES_SYNC_BUTTON;
			break;
		case WIFI:
			flag = FLAG_USES_WIFI_BUTTON;
			break;
		case WIFI_AP:
			flag = FLAG_USES_WIFI_AP_BUTTON;
			break;
		case AUTO_BRIGHTNESS:
			flag = FLAG_USES_AUTO_BRIGHTNESS_BUTTON;
			break;
		case SCREEN_TIMEOUT:
			flag = FLAG_USES_SCREEN_TIMEOUT_BUTTON;
			break;
		}
		
		if(enabled) {
			flags |= flag;
		} else {
			flags &= ~flag;
		}
		
		return putInt(context, Keys.Toggles.BUTTONS_USED, flags);
	}

	public static boolean setSeekbarEnabled(Context context, SeekbarType type, boolean enabled) {
		int flags = TogglesResolver.getSeekbarsFlags(context);
		int flag = FLAG_USES_BRIGHTNESS_SEEKBAR;
		
		switch(type) {
		case BRIGHTNESS:
			flag = FLAG_USES_BRIGHTNESS_SEEKBAR;
			break;
		case MEDIA_VOLUME:
			flag = FLAG_USES_MEDIA_SEEKBAR;
			break;
		case RINGER_VOLUME:
			flag = FLAG_USES_RINGER_SEEKBAR;
			break;
		}
		
		if(enabled) {
			flags |= flag;
		} else {
			flags &= ~flag;
		}
		
		return putInt(context, Keys.Toggles.SEEKBARS_USED, flags);
		
	}
}
