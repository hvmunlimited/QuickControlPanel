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

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.FlashlightService;

public class FlashlightAction extends ToggleAction {
	
	private Context mContext;
	private final Intent mTurnOnIntent;
	private final Intent mTurnOffIntent;
	
	public FlashlightAction(Context context) {
		mContext = context;
		mTurnOnIntent = new Intent(mContext.getPackageName() + ".START_FLASH");
		mTurnOffIntent = new Intent(mContext.getPackageName() + ".STOP_FLASH");
	}
	
	@Override
	protected void performActionOn() {
			mContext.startService(mTurnOnIntent);
	}

	@Override
	protected void performActionOff() {
		mContext.startService(mTurnOffIntent);
	}

	@Override
	public boolean isStateOn() {
		return FlashlightService.isRunning();
	}

	@Override
	public boolean isStateOff() {
		return !isStateOn();
	}
}
