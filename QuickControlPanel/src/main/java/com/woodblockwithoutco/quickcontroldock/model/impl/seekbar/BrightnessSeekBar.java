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
package com.woodblockwithoutco.quickcontroldock.model.impl.seekbar;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier.OnVisibilityEventListener;
import com.woodblockwithoutco.quickcontroldock.model.action.SeekBarAction;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.BrightnessSeekBarAction;
import com.woodblockwithoutco.quickcontroldock.model.seekbar.BaseLocalBroadcastActionSeekBar;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;
import com.woodblockwithoutco.quickcontroldock.resource.Res;
import com.woodblockwithoutco.quickcontroldock.R;

public class BrightnessSeekBar extends BaseLocalBroadcastActionSeekBar implements OnVisibilityEventListener {
	
	private final int mMinBrightness;
	
	public static View getSeekBarContainer(Context context) {
		View result = LayoutInflater.from(context).inflate(R.layout.brightness_bar_layout, null);
		initContainer(context, result, Res.drawable.brightness_full);
		return result;
	}

	
	public BrightnessSeekBar(Context context) {
		this(context, null, 0);
	}
	
	public BrightnessSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	private boolean isAutoBrightnessEnabled() {
		return Settings.System.getInt(
				getContext().getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) 
				== 
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
	}
	
	private void determineState() {
		if(isAutoBrightnessEnabled()) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}
		
	
	public BrightnessSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mMinBrightness = TogglesResolver.getMinimumBrightness(getContext());
		addAction(getContext().getPackageName()+".BRIGHTNESS_CHANGED");
		addAction(getContext().getPackageName()+".BRIGHTNESS_MODE_CHANGED");
	}

	@Override
	protected int transformProgress(int progress) {
		if(progress < mMinBrightness) {
			return mMinBrightness;
		}

		return progress;
	}

	@Override
	protected SeekBarAction createSeekBarAction() {
		return new BrightnessSeekBarAction(getContext());
	}


	@Override
	protected void onReceive(Intent broadcastIntent) {
		String action = broadcastIntent.getAction();
		if((getContext().getPackageName() + ".BRIGHTNESS_CHANGED").equals(action)) {
			setProgress(mAction.getCurrentValue());
		} else if((getContext().getPackageName() + ".BRIGHTNESS_MODE_CHANGED").equals(action)) {
			determineState();
		}
	}


	@Override
	public void onShow() {
		super.onShow();
		determineState();
	}


	@Override
	public void onHide() {
		super.onHide();
		//������ �� ������
	}
}
