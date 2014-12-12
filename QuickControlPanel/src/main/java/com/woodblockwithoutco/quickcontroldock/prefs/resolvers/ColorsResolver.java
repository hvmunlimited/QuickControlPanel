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

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

public class ColorsResolver extends BasePrefsResolver {
	
	public static final int DEFAULT_IDLE_COLOR = 0xFF909090;
	public static final int DEFAULT_ACTIVE_COLOR = 0xFF202020;
	public static final int DEFAULT_PRESSED_COLOR = 0xB0DFDFDF;
	public static final int DEFAULT_PANEL_BACKGROUND_COLOR = 0xDFFFFFFF;
	public static final int DEFAULT_SECTION_BACKGROUND_COLOR = 0xFFFFFFFF;
	public static final int DEFAULT_SECTION_SHADOW_COLOR = 0xFFC0C0C0;
	public static final int DEFAULT_PANEL_BACKGROUND_COLOR_SECONDARY = 0xDFFFFFFF;
	public static final int DEFAULT_SEEKBAR_COLOR = 0xFF33B5E5;
	public static final int DEFAULT_INFO_TEXT_COLOR = 0xFF000000;
	public static final int DEFAULT_IDLE_BG_COLOR = 0x00000000;
	
	public static int getTextColor(Context context) {
		return getInt(context, Keys.Color.TEXT_COLOR, DEFAULT_INFO_TEXT_COLOR);
	}
	
	public static int getIdleColor(Context context) {
		return getInt(context, Keys.Color.IDLE_COLOR, DEFAULT_IDLE_COLOR);
	}
	
	public static int getActiveColor(Context context) {
		return getInt(context, Keys.Color.ACTIVE_COLOR, DEFAULT_ACTIVE_COLOR);
	}
	
	public static int getPressedColor(Context context) {
		return getInt(context, Keys.Color.PRESSED_COLOR, DEFAULT_PRESSED_COLOR);
	}
	
	public static int getPanelBackgroundColor(Context context) {
		return getInt(context, Keys.Color.PANEL_BACKGROUND_COLOR, DEFAULT_PANEL_BACKGROUND_COLOR);
	}
	
	public static int getPanelBackgroundSecondaryColor(Context context) {
		return getInt(context, Keys.Color.PANEL_SECOND_BACKGROUND_COLOR, DEFAULT_PANEL_BACKGROUND_COLOR_SECONDARY);
	}
	
	public static int getSectionMainBackgroundColor(Context context) {
		return getInt(context, Keys.Color.PANEL_SECTION_BG_COLOR, DEFAULT_SECTION_BACKGROUND_COLOR);
	}
	
	public static int getSectionShadowBackgroundColor(Context context) {
		return getInt(context, Keys.Color.PANEL_SECTION_SHADOW_COLOR, DEFAULT_SECTION_SHADOW_COLOR);
	}
	
	public static int getSeekBarProgressColor(Context context) {
		return getInt(context, Keys.Color.SEEKBAR_PROGRESS_COLOR, DEFAULT_SEEKBAR_COLOR);
	}
	
	public static int getSeekBarThumbColor(Context context) {
		if(getBoolean(context, Keys.Color.SEEKBAR_THUMB_PROGRESS_SAME_COLOR, true)) {
			return getSeekBarProgressColor(context);
		}
		
		return getInt(context, Keys.Color.SEEKBAR_THUMB_COLOR, DEFAULT_SEEKBAR_COLOR);
	}
	
	public static Drawable getBackgroundDrawable(Context context) {
		String orientationString = getString(context, "colors_gradient_direction", "no_gradient");
		if("no_gradient".equals(orientationString)) {
			ColorDrawable drawable = new ColorDrawable(getPanelBackgroundColor(context));
			return drawable;
		}
		
		Orientation orientation = Orientation.valueOf(orientationString);
		GradientDrawable gradientBackground=new GradientDrawable(orientation, 
				new int[] { 
				getPanelBackgroundColor(context), 
				getPanelBackgroundSecondaryColor(context)
				});
		return gradientBackground;
	}

	public static int getIdleBgColor(Context context) {
		return getInt(context, Keys.Color.TOGGLE_BG_COLOR, DEFAULT_IDLE_BG_COLOR);
	}

	public static boolean isSeekbarColorDisabled(Context context) {
		return getBoolean(context, Keys.Color.DISABLE_SEEKBAR_COLORING, false);
	}
	
	
	public static int getSeekbarIconColor(Context context) {
		boolean isSameColor = getBoolean(context, Keys.Color.SEEKBAR_ICON_COLOR_ACTIVE, true);
		if(isSameColor) {
			return getActiveColor(context);
		} else {
			return getInt(context, Keys.Color.SEEKBAR_ICON_COLOR, DEFAULT_ACTIVE_COLOR);
		}
		
	}

}
