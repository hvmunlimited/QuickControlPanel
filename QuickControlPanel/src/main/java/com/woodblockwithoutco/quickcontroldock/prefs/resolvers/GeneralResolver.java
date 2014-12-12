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


import com.woodblockwithoutco.quickcontroldock.prefs.Keys;
import com.woodblockwithoutco.quickcontroldock.util.ScreenUtils;

import android.content.Context;
import android.content.res.Configuration;

public class GeneralResolver extends BasePrefsResolver {

	public static int getPanelsOffset(Context context) {
		if(ScreenUtils.getScreenOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
			float offset = (float) getInt(context, Keys.General.PANELS_OFFSET, 0) / 100;
			return (int) (offset * ScreenUtils.getHeightPx(context));
		} else {
			return 0;
		}
	}
	
	public static int getDragHandlerWidth(Context context) {
		float width = (float) getInt(context, Keys.General.DRAG_HANDLER_WIDTH, 15) / 100;
		return (int) (width * ScreenUtils.getWidthPx(context));
	}
	
	public static boolean isSameLayoutForLandscape(Context context) {
		return getBoolean(context, Keys.General.PANELS_SAME_LAYOUT_LANDSCAPE, false);
	}
	
	public static float getPanelSpanPercentage(Context context) {
		if(ScreenUtils.getScreenOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
			float offset = (float) getInt(context, Keys.General.PANEL_SPAN, 0) / 100;
			return offset;
		} else {
			float offset = (float) getInt(context, Keys.General.PANEL_SPAN_LANDSCAPE, 0) / 100;
			return offset;
		}
	}
	
	public static int getPanelSpan(Context context) {
		if(ScreenUtils.getScreenOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
			float offset = (float) getInt(context, Keys.General.PANEL_SPAN, 0) / 100;
			return (int) (offset * ScreenUtils.getHeightPx(context));
		} else {
			float offset = (float) getInt(context, Keys.General.PANEL_SPAN_LANDSCAPE, 0) / 100;
			return (int) (offset * ScreenUtils.getHeightPx(context));
		}
	}
	
	public static boolean isSwipeDetectorDebugVisible(Context context) {
		return getBoolean(context, Keys.Debug.DEBUG_SWIPE_DETECTOR_VISIBLE, false);
	}
	
	public static boolean isForceForegroundEnabled(Context context) {
		return getBoolean(context, Keys.General.FORCE_FORGEROUND, true);
	}
	
	public static int getPanelsMargin(Context context) {
		int dpSize = getInt(context, Keys.General.PANELS_MARGIN, 8);
		return ScreenUtils.dipToPixels(context, dpSize) / 2;
	}

	public static float getDimAmount(Context context) {
		float amount = (float) getInt(context, Keys.General.DIM_AMOUNT, 30) / 100;
		if(amount == 0) amount = 0.01f;
		return amount;
	}

	public static boolean isCloseOnSpanTouch(Context context) {
		return getBoolean(context, Keys.General.CLOSE_ON_SPAN_TOUCH, false);
	}
}
