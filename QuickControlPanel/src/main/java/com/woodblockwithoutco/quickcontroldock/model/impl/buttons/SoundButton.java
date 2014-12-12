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
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;


import com.woodblockwithoutco.quickcontroldock.model.buttons.BroadcastTriToggleButton;
import com.woodblockwithoutco.quickcontroldock.model.buttons.ButtonType;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.SoundAction;

public class SoundButton extends BroadcastTriToggleButton {
	
	public SoundButton(Context context) {
		this(context, null, 0);
	}
	
	public SoundButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SoundButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mAction = new SoundAction(context.getApplicationContext());
		addBroadcastAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
		initDrawable(ButtonType.SOUND);
	}
	
	@Override
	public void onReceive(Intent intent) {
		setVisualState(mAction.getCurrentState());
	}

	@Override
	protected String getIntentActionName() {
		return Settings.ACTION_SOUND_SETTINGS;
	}
}
