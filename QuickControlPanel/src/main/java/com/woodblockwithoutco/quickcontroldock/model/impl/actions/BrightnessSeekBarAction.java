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
package com.woodblockwithoutco.quickcontroldock.model.impl.actions;

import android.content.Context;
import android.provider.Settings;
import android.view.WindowManager;

import com.woodblockwithoutco.quickcontroldock.model.action.SeekBarAction;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;

public class BrightnessSeekBarAction implements SeekBarAction {

	private Context mContext;
	
	public BrightnessSeekBarAction(Context context) {
		mContext = context;
		
	}

	@Override
	public void performAction(int progress) {
		ControlService service = (ControlService) ControlService.getInstance();
		if(ControlService.isRunning() && service != null && service.isAttachedToWindow()) {
			WindowManager.LayoutParams params = service.getServiceViewLayoutParams();
			params.screenBrightness = (float) progress / getMaxValue();
			service.setServiceViewLayoutParams(params);
		}
	}

	@Override
	public void performPostChangeAction(int progress) {
		Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
	}

	@Override
	public void performPreChangeAction(int progress) {
		return;
	}

	@Override
	public int getMaxValue() {
		return 255;
	}

	@Override
	public int getCurrentValue() {
		return Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 128);
	}

}
