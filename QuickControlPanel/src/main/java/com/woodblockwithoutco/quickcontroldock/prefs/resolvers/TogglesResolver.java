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

import com.woodblockwithoutco.quickcontroldock.model.buttons.ButtonType;
import com.woodblockwithoutco.quickcontroldock.model.seekbar.SeekbarType;
import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.util.ScreenUtils;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

public class TogglesResolver extends BasePrefsResolver {

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

	private static final int FLAGS_DEFAULT_BUTTONS = 
			FLAG_USES_DATA_BUTTON |
			FLAG_USES_WIFI_BUTTON |
			FLAG_USES_BLUETOOTH_BUTTON |
			FLAG_USES_SOUND_BUTTON |
			FLAG_USES_ROTATE_BUTTON |
			FLAG_USES_FLASH_BUTTON |
			FLAG_USES_AIRPLANE_BUTTON |
			FLAG_USES_GPS_BUTTON |
			FLAG_USES_SETTINGS_BUTTON |
			FLAG_USES_FLASH_BUTTON |
			FLAG_USES_SYNC_BUTTON | 
			FLAG_USES_WIFI_AP_BUTTON |
			FLAG_USES_AUTO_BRIGHTNESS_BUTTON |
			FLAG_USES_SCREEN_TIMEOUT_BUTTON;

	private static final int FLAGS_DEFAULT_SEEKBARS = FLAG_USES_MEDIA_SEEKBAR |
			FLAG_USES_RINGER_SEEKBAR |
			FLAG_USES_BRIGHTNESS_SEEKBAR;

	private static final boolean checkButton(Context context, int flag) {
		return (getInt(context, Keys.Toggles.BUTTONS_USED, FLAGS_DEFAULT_BUTTONS) & flag) != 0;
	}
	
	public static boolean isLongClickEnabled(Context context) {
		return getBoolean(context, Keys.Toggles.ENABLE_LONG_CLICK, true);
	}

	public static int getButtonsFlags(Context context) {
		return getInt(context, Keys.Toggles.BUTTONS_USED, FLAGS_DEFAULT_BUTTONS);
	}

	public static boolean checkButton(Context context, ButtonType buttonType) {

		boolean result = false;
		switch(buttonType) {
		case AIRPLANE:
			result = isAirplaneButtonEnabled(context);
			break;
		case BLUETOOTH:
			result = isBluetoothButtonEnabled(context);
			break;
		case DATA:
			result = isDataButtonEnabled(context);
			break;
		case FLASHLIGHT:
			result = isFlashButtonEnabled(context);
			break;
		case GPS:
			result = isGpsButtonEnabled(context);
			break;
		case LOCK_NOW:
			result = isLockNowButtonEnabled(context);
			break;
		case ROTATE:
			result = isRotateButtonEnabled(context);
			break;
		case SETTINGS:
			result = isSettingsButtonEnabled(context);
			break;
		case SOUND:
			result = isSoundButtonEnabled(context);
			break;
		case SYNC:
			result = isSyncButtonEnabled(context);
			break;
		case WIFI:
			result = isWifiButtonEnabled(context);
			break;
		case WIFI_AP:
			result = isWifiApButtonEnabled(context);
			break;
		case AUTO_BRIGHTNESS:
			result = isAutoBrightnessButtonEnabled(context);
			break;
		case SCREEN_TIMEOUT:
			result = isScreenTimeoutButtonEnabled(context);
			break;
		}
		return result;
	}

	public static boolean isAutoBrightnessButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_AUTO_BRIGHTNESS_BUTTON);
	}
	
	public static boolean isScreenTimeoutButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_SCREEN_TIMEOUT_BUTTON);
	}
	
	public static boolean isDataButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_DATA_BUTTON);
	}

	public static boolean isWifiButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_WIFI_BUTTON);
	}

	public static boolean isBluetoothButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_BLUETOOTH_BUTTON);
	}

	public static boolean isSoundButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_SOUND_BUTTON);
	}

	public static boolean isRotateButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_ROTATE_BUTTON);
	}

	public static boolean isAirplaneButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_AIRPLANE_BUTTON);
	}

	public static boolean isGpsButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_GPS_BUTTON);
	}

	public static boolean isSettingsButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_SETTINGS_BUTTON);
	}

	public static boolean isFlashButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_FLASH_BUTTON);
	}

	public static boolean isSyncButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_SYNC_BUTTON);
	}

	public static boolean isLockNowButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_LOCK_NOW_BUTTON);
	}

	public static boolean isWifiApButtonEnabled(Context context) {
		return checkButton(context, FLAG_USES_WIFI_AP_BUTTON);
	}

	public static String getBorderType(Context context) {
		return getString(context, Keys.Toggles.BORDER_DRAWABLE, "none");
	}

	public static int getBorderCornerRadius(Context context) {
		return getInt(context, Keys.Toggles.BORDER_CORNER_RADIUS, 5);
	}

	public static int getBackgroundCornerRadius(Context context) {
		return getInt(context, Keys.Toggles.BACKGROUND_CORNER_RADIUS, 5);
	}

	public static int getBorderWidth(Context context) {
		return ScreenUtils.dipToPixels(context, getInt(context, Keys.Toggles.BORDER_WIDTH, 2));
	}

	public static String getBackgroundType(Context context) {
		return getString(context, Keys.Toggles.BACKGROUND_DRAWABLE, "circle");
	}

	public static int getButtonSize(Context context) {
		int defaultSize = 60;
		return ScreenUtils.dipToPixels(context, getInt(context, Keys.Toggles.BUTTONS_SIZE, defaultSize));
	}

	public static String getFlashlightType(Context context) {
        boolean isLollipop = VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP;
        String defaultType = isLollipop ? "camera2" : "default";
        String type = getString(context, Keys.Toggles.FLASHLIGHT_TYPE, defaultType);
        if(!isLollipop && "camera2".equals(type)) {
            type = "default";
        }
		return type;
	}

	public static int getButtonDistance(Context context) {
		return getButtonSize(context) * getInt(context, Keys.Toggles.BUTTONS_DISTANCE, 15) / 100;
	}

	public static String getSoundModes(Context context) {
		return getString(context, Keys.Toggles.SOUND_MODES, "normal|vibrate|silent");
	}

	public static List<ButtonType> getButtonsOrderList(Context context) {
		String unparsedOrder = getString(context, Keys.Toggles.BUTTONS_ORDER, getDefaultOrderString(context));
		String[] types = unparsedOrder.split(":");
		List<ButtonType> result = new ArrayList<ButtonType>(types.length);
		for(String s : types) {
			result.add(ButtonType.valueOf(s));
		}
		return result;
	}

	public static List<String> getButtonsOrderListAsString(Context context) {
		String unparsedOrder = getString(context, Keys.Toggles.BUTTONS_ORDER, getDefaultOrderString(context));
		String[] types = unparsedOrder.split(":");
		List<String> result = new ArrayList<String>(types.length);
		for(String s : types) {
			result.add(s);
		}
		return result;
	}

	private static String getDefaultOrderString(Context context) {
		String[] defaultOrderArray = context.getResources().getStringArray(R.array.toggles_keys);
		StringBuilder builder = new StringBuilder();
		for(String s : defaultOrderArray) {
			builder.
			append(s).
			append(":");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	public static boolean isTogglesEnabled(Context context) {
		return getBoolean(context, Keys.Toggles.BUTTONS_ENABLED, true);
	}

	public static int getMinimumBrightness(Context context) {
		return getInt(context, Keys.Toggles.BRIGHTNESS_MIN_VALUE, 25);
	}

	public static boolean isSeekbarUsed(Context context, SeekbarType type) {

		int flags = getInt(context, Keys.Toggles.SEEKBARS_USED, FLAGS_DEFAULT_SEEKBARS);
		switch(type) {
		case BRIGHTNESS:
			return (flags & FLAG_USES_BRIGHTNESS_SEEKBAR) != 0;
		case MEDIA_VOLUME:
			return (flags & FLAG_USES_MEDIA_SEEKBAR) != 0;
		case RINGER_VOLUME:
			return (flags & FLAG_USES_RINGER_SEEKBAR) != 0;
		}
		return false;

	}

	public static List<SeekbarType> getSeekbarOrderList(Context context) {
		final int SEEKBAR_COUNT = 3;
		final String SEEKBAR_DEFAULT_ORDER = SeekbarType.BRIGHTNESS.name()+
				":"+
				SeekbarType.MEDIA_VOLUME.name()+
				":"+
				SeekbarType.RINGER_VOLUME;
		List<SeekbarType> result = new ArrayList<SeekbarType>(SEEKBAR_COUNT);
		String orderString = getString(context, Keys.Toggles.SEEKBARS_ORDER, SEEKBAR_DEFAULT_ORDER);

		String[] splitOrder = orderString.split(":");
		for(String s : splitOrder) {
			result.add(SeekbarType.valueOf(s));
		}

		return result;
	}

	public static List<String> getSeekbarOrderAsStringList(Context context) {
		final int SEEKBAR_COUNT = 3;
		final String SEEKBAR_DEFAULT_ORDER = SeekbarType.BRIGHTNESS.name()+
				":"+
				SeekbarType.MEDIA_VOLUME.name()+
				":"+
				SeekbarType.RINGER_VOLUME;
		List<String> result = new ArrayList<String>(SEEKBAR_COUNT);
		String orderString = getString(context, Keys.Toggles.SEEKBARS_ORDER, SEEKBAR_DEFAULT_ORDER);

		String[] splitOrder = orderString.split(":");
		for(String s : splitOrder) {
			result.add(s);
		}

		return result;
	}

	public static int getSeekbarsFlags(Context context) {
		return getInt(context, Keys.Toggles.SEEKBARS_USED, FLAGS_DEFAULT_SEEKBARS);
	}
	
	public static boolean shouldCloseAfterClick(Context context) {
		return getBoolean(context, Keys.Toggles.CLOSE_AFTER_CLICK, false);
	}

	public static boolean isSeekbarsOnTop(Context context) {
		return getBoolean(context, Keys.Toggles.SEEKBARS_ON_TOP, false);
	}
	
	public static String getTimeoutModes(Context context) {
		return getString(context, Keys.Toggles.TIMEOUT_MODES, "30s/1m/2m");
	}

}
