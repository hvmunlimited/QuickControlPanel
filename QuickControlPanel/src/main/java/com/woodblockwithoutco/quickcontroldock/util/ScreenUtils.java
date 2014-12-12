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
package com.woodblockwithoutco.quickcontroldock.util;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class ScreenUtils {

	public static int dipToPixels(Context context, float dipValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static float pixelsToDip(Context context, int pixels) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels, metrics);
	}

	public static int getWidthPx(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		return p.x;
	}

	public static int getHeightPx(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		return p.y;
	}

	public static int getScreenOrientation(Context context) {
		return context.getResources().getConfiguration().orientation;
	}

	public static int getIconSize(Context context) {
		switch (context.getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_MEDIUM:
			return 64;
		case DisplayMetrics.DENSITY_HIGH:
			return 96;
		case DisplayMetrics.DENSITY_XHIGH:
			return 144;
		default:
			return 96;
		}
	}

	public static int getScreenOrientationLegacy(WindowManager windowManager) {
		int rotation = windowManager.getDefaultDisplay().getRotation();
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int orientation;
		// if the device's natural orientation is portrait:
		if ((rotation == Surface.ROTATION_0
				|| rotation == Surface.ROTATION_180) && height > width ||
				(rotation == Surface.ROTATION_90
				|| rotation == Surface.ROTATION_270) && width > height) {
			switch(rotation) {
			case Surface.ROTATION_0:
				orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				break;
			case Surface.ROTATION_90:
				orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;
			case Surface.ROTATION_180:
				orientation =
				ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				break;
			case Surface.ROTATION_270:
				orientation =
				ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				break;
			default:
				orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				break;              
			}
		}
		// if the device's natural orientation is landscape or if the device
		// is square:
		else {
			switch(rotation) {
			case Surface.ROTATION_0:
				orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;
			case Surface.ROTATION_90:
				orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				break;
			case Surface.ROTATION_180:
				orientation =
				ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				break;
			case Surface.ROTATION_270:
				orientation =
				ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				break;
			default:
				orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;              
			}
		}

		return orientation;
	}

	public static int getNavigationBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

}
