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
import android.content.Intent;
import android.provider.Settings;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;


public class SettingsAction extends ToggleAction {
	
	private Context mContext;
	
	public SettingsAction(Context context) {
		mContext = context;
	}

	@Override
	protected void performActionOn() {
		Intent intent = new Intent(Settings.ACTION_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		
		ControlService service = (ControlService) ControlService.getInstance();
		if(service != null && ControlService.isRunning()) {
			service.close();
		}
	}

	@Override
	protected void performActionOff() {
		performActionOn();
	}

	@Override
	public boolean isStateOn() {
		return true;
	}
	
	@Override
	public boolean isStateOff() {
		return false;
	}
}
