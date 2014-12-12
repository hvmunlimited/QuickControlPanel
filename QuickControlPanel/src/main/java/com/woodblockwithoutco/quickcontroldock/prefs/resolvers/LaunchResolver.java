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

import android.content.Context;
import android.view.Gravity;

import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.util.ScreenUtils;
import com.woodblockwithoutco.quickcontroldock.R;

public class LaunchResolver extends BasePrefsResolver {

	public static int getSwipeDetectorSize1(Context context) {
		int gravity = getSwipeDetectorAlignment(context);
		
		if(gravity == Gravity.BOTTOM) {
			int percent = getInt(context, Keys.Launch.SWIPE_DETECTOR_SIZE_1, 40);
			int maxSize = context.getResources().getDimensionPixelSize(R.dimen.detector_max_height);
			return percent * maxSize / 100;
		} else {
			int percent = getInt(context, Keys.Launch.SWIPE_DETECTOR_SIZE_2, 50);
			int maxSize = ScreenUtils.getHeightPx(context);
			return percent * maxSize / 100;
		}
	}
	
	public static int getSwipeDetectorOffset(Context context) {
		
		int gravity = getSwipeDetectorAlignment(context);
		int percent = getInt(context, Keys.Launch.SWIPE_DETECTOR_OFFSET, 0);
		
		if(gravity == Gravity.BOTTOM) {
			return percent * ScreenUtils.getWidthPx(context) / 400;
		} else {
			return percent * ScreenUtils.getHeightPx(context) / 400;
		}
	}

	public static int getSwipeDetectorSize2(Context context) {
		int gravity = getSwipeDetectorAlignment(context);
		if(gravity == Gravity.BOTTOM) {
			int percent = getInt(context, Keys.Launch.SWIPE_DETECTOR_SIZE_2, 50);
			int maxSize = ScreenUtils.getWidthPx(context);
			return percent * maxSize / 100;
		} else {
			int percent = getInt(context, Keys.Launch.SWIPE_DETECTOR_SIZE_1, 40);
			int maxSize = context.getResources().getDimensionPixelSize(R.dimen.detector_max_height);
			return percent * maxSize / 100;
		}
	}
	
	public static String getSwipeDetectorDirection(Context context) {
		String direction = getString(context, Keys.Launch.SWIPE_DETECTOR_DIRECTION, "UP");
		return direction;
	}

	public static int getSwipeDetectorAlignment(Context context) {
		String align = getString(context, Keys.Launch.SWIPE_DETECTOR_ALIGN, "MIDDLE");
		if("LEFT".equals(align)) {
			return Gravity.LEFT | Gravity.BOTTOM;
		} else if("MIDDLE".equals(align)) {
			return Gravity.BOTTOM;
		} else if("RIGHT".equals(align)) {
			return Gravity.RIGHT | Gravity.BOTTOM;
		} else {
			return Gravity.BOTTOM;
		}
	}

	public static boolean isSwipeDetectorEnabled(Context context) {
		return getBoolean(context, Keys.Launch.SWIPE_DETECTOR_ENABLED, true);
	}
	
	public static boolean isServiceEnabled(Context context) {
		return getBoolean(context, Keys.Launch.SERVICE_ENABLED, true);
	}
	
	public static boolean isVibrationEnabled(Context context) {
		return getBoolean(context, Keys.Launch.SWIPE_DETECTOR_VIBRATE, true);
	}

	public static boolean loadSwipeDetectorOnBoot(Context context) {
		return getBoolean(context, Keys.Launch.SWIPE_DETECTOR_LOAD_ON_BOOT, true);
	}
	
	public static boolean isGoogleNowGestureEnabled(Context context) {
		return getBoolean(context, Keys.Launch.SWIPE_DETECTOR_GOOGLE_NOW_GESTURE_ENABLED, true);
	}
	
	public static boolean isShortcutLaunchEnabled(Context context) {
		return getBoolean(context, Keys.Launch.LAUNCH_LAUNCHER_ICON, true);
	}
	

}
