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
import android.media.AudioManager;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleTriAction;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;


public class SoundAction extends ToggleTriAction {

	private AudioManager mAudioManager;

	public SoundAction(Context context) {
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		String modes = TogglesResolver.getSoundModes(context);
		if(!modes.contains("normal")) {
			excludeActionForState(State.FIRST);
		}
		
		if(!modes.contains("vibrate")) {
			excludeActionForState(State.SECOND);
		}
		
		if(!modes.contains("silent")) {
			excludeActionForState(State.THIRD);
		}
	}

	@Override
	protected void performFirstAction() {
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}

	@Override
	protected void performSecondAction() {
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	}

	@Override
	protected void performThirdAction() {
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

	@Override
	public State getCurrentState() {
		State currentState = State.FIRST;
		switch(mAudioManager.getRingerMode()) {
			case AudioManager.RINGER_MODE_NORMAL:
				currentState = State.FIRST;
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				currentState = State.SECOND;
				break;
			case AudioManager.RINGER_MODE_SILENT:
				currentState = State.THIRD;
				break;
		}
		return currentState;
	}

}
