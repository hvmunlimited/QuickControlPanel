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
package com.woodblockwithoutco.quickcontroldock.model.impl.buttons;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import com.woodblockwithoutco.quickcontroldock.model.buttons.BaseToggleButton;
import com.woodblockwithoutco.quickcontroldock.model.buttons.ButtonType;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.LockNowAction;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;

public class LockNowButton extends BaseToggleButton {
	
	public LockNowButton(Context context) {
		this(context, null, 0);
	}
	
	public LockNowButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LockNowButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mAction = new LockNowAction(context.getApplicationContext());
		initDrawable(ButtonType.LOCK_NOW);
		setVisualStateActive();
	}
	
	@Override
	public boolean onLongClick(View v) {
		try {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			intent.setPackage("com.android.settings");
			intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.DeviceAdminSettings"));

			getContext().startActivity(intent);

			ControlService service = (ControlService) ControlService.getInstance();
			if(service != null && ControlService.isRunning()) {
				service.close();
			}
		} catch(ActivityNotFoundException e) {
			setOnLongClickListener(null);
		}
		return true;
	}

	@Override
	protected String getIntentActionName() {
		return "";
	}

}
