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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.woodblockwithoutco.quickcontroldock.model.buttons.BaseMediaDualStateButton;
import com.woodblockwithoutco.quickcontroldock.resource.Res;
import com.woodblockwithoutco.remotecontroller.MediaCommand;

public class PlayPauseButton extends BaseMediaDualStateButton {

	public PlayPauseButton(Context context) {
		this(context, null, 0);
	}
	
	public PlayPauseButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public PlayPauseButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setDrawableStateToSecond();
	}

	@Override
	protected Drawable getSecondMediaDrawable() {
		return Res.drawable.play_song;
	}

	@Override
	protected Drawable getMediaDrawable() {
		return Res.drawable.pause_song;
	}

	@Override
	protected MediaCommand getMediaCommand() {
		return MediaCommand.PLAY_PAUSE;
	}
	
	@Override
	protected boolean isMasterButton() {
		return true;
	}

}
