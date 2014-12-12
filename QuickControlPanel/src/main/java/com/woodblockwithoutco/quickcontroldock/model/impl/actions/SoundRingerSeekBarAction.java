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
import android.os.Handler;
import android.os.Message;

import com.woodblockwithoutco.quickcontroldock.model.action.SeekBarAction;

public class SoundRingerSeekBarAction implements SeekBarAction {

	private Context mContext;
	private AudioManager mAudioManager;
	private final int MAX_RINGER_VOLUME;
	private Handler mHandler;

	private final static int MSG_SET_VOLUME = 100;

	public SoundRingerSeekBarAction(Context context) {
		mContext = context;
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		MAX_RINGER_VOLUME = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		mHandler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if(msg.what == MSG_SET_VOLUME) {
					int volume = msg.arg1;
					
					if(volume > getMaxValue()) {
						volume = getMaxValue();
					}

					mAudioManager.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
					msg.recycle();
					return true;
				}
				msg.recycle();
				return false;
			}
		});
	}

	@Override
	public void performAction(int progress) {
		Message msg = Message.obtain(mHandler, MSG_SET_VOLUME, progress, 0);
		mHandler.dispatchMessage(msg);
		return;
	}

	@Override
	public void performPostChangeAction(int progress) {
		return;
	}

	@Override
	public void performPreChangeAction(int progress) {
		return;
	}

	@Override
	public int getMaxValue() {
		return MAX_RINGER_VOLUME;
	}

	@Override
	public int getCurrentValue() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
	}

}
