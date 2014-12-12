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
package com.woodblockwithoutco.quickcontroldock.global.holder;

import android.view.WindowManager;

public class ConstantHolder {
	
	private static final boolean IS_EVAL = false;
	private static final boolean IS_DEBUG = false;
	
	private static final int NORMAL_TYPE = WindowManager.LayoutParams.TYPE_PHONE;
	private static final int LOCKSCREEN_TYPE = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
	
	private static final int PANEL_VIEW_FLAGS = WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
	private static final int DETACH_PANEL_VIEW_FLAGS = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
	
	public static int getPanelViewFlags() {
		return PANEL_VIEW_FLAGS;
	}
	
	public static int getDetachedPanelViewFlags() {
		return DETACH_PANEL_VIEW_FLAGS;
	}
	
	public static boolean getIsEval() {
		return IS_EVAL;
	}
	
	public static boolean getIsDebug() {
		return IS_DEBUG;
	}
	
	public static int getNormalType() {
		return NORMAL_TYPE;
	}
	
	public static int getLockscreenType() {
		return LOCKSCREEN_TYPE;
	}

}
