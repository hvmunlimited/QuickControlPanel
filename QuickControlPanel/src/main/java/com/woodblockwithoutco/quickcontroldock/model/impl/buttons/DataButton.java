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

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;

import com.woodblockwithoutco.quickcontroldock.model.buttons.ButtonType;
import com.woodblockwithoutco.quickcontroldock.model.buttons.LocalBroadcastToggleButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.DataAction;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.DataLollipopAction;

public class DataButton extends LocalBroadcastToggleButton {
	
	private boolean mPreviousStateIsEnabled = false;
	
	public DataButton(Context context) {
		this(context, null, 0);
	}
	
	public DataButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DataButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			mAction = new DataLollipopAction(context.getApplicationContext());
		} else {
			mAction = new DataAction(context.getApplicationContext());
		}
		
		addBroadcastAction(context.getPackageName() + ".DATA_BUTTON_CLICKED");
		initDrawable(ButtonType.DATA);
		mPreviousStateIsEnabled = mAction.isStateOn();
	}
	
	@Override
	public void onReceive(Intent intent) {
		if(mPreviousStateIsEnabled) {
			mPreviousStateIsEnabled = false;
			setVisualStateIdle();
		} else {
			mPreviousStateIsEnabled = true;
			setVisualStateActive();
		}
	}

	@Override
	protected String getIntentActionName() {
		return Settings.ACTION_DATA_ROAMING_SETTINGS;
	}
}
