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
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.woodblockwithoutco.quickcontroldock.model.action.SeekBarAction;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.SoundRingerSeekBarAction;
import com.woodblockwithoutco.quickcontroldock.model.seekbar.BaseBroadcastActionSeekBar;
import com.woodblockwithoutco.quickcontroldock.resource.Res;
import com.woodblockwithoutco.quickcontroldock.R;

public class RingerSeekBar extends BaseBroadcastActionSeekBar {
	
	public static View getSeekBarContainer(Context context) {
		View result = LayoutInflater.from(context).inflate(R.layout.ringer_bar_layout, null);
		initContainer(context, result, Res.drawable.ringer);
		return result;
	}

	
	public RingerSeekBar(Context context) {
		this(context, null, 0);
	}
	
	public RingerSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	
	public RingerSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addAction("android.media.VOLUME_CHANGED_ACTION");
		addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
	}

	@Override
	protected int transformProgress(int progress) {
		return progress;
	}

	@Override
	protected SeekBarAction createSeekBarAction() {
		return new SoundRingerSeekBarAction(getContext());
	}

	@Override
	protected void onReceive(Intent broadcastIntent) {
		setProgress(mAction.getCurrentValue());
	}

}
