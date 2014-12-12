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

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;

import com.woodblockwithoutco.quickcontroldock.global.holder.RemoteControllerHolder;
import com.woodblockwithoutco.quickcontroldock.model.action.SeekBarAction;
import com.woodblockwithoutco.remotecontroller.RemoteController;

public class ScrubberAction implements SeekBarAction {
	
	private int mDuration = 0;
	private static final long ONE_SECOND_DELAY = 1000;
	private static final long SEEK_DELAY = 30;
	private RemoteController mRemoteController;
	private TextView mCurrentPositionTextView;
	private TextView mDurationTextView;
	private Handler mHandler;
	private SeekBar mPosSeekBar;
	private SimpleDateFormat mFormat;
	private Runnable mUpdateActor = new Runnable() {
		@Override
		public void run() {
			update();
			mHandler.removeCallbacks(this);
			mHandler.postDelayed(this, ONE_SECOND_DELAY);
		}
	};
	
	private FutureSeekToRunnable mSeekActor = new FutureSeekToRunnable();
	private int mProgress;
	
	public ScrubberAction(SeekBar sb) {
		
		mHandler = new Handler();
		
		mRemoteController = RemoteControllerHolder.getInstance();
		mPosSeekBar = sb;
		
		String skeleton = "";
		if (mDuration >= 3600000) {
            skeleton = "kk:mm:ss";
        } else {
            skeleton = "mm:ss";
        }
        mFormat = new SimpleDateFormat(skeleton, Locale.getDefault());
        mFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
	}
	
	public void attachCurrentPositionTextView(TextView tv) {
		mCurrentPositionTextView = tv;
	}
	
	public void attachDurationTextView(TextView tv) {
		mDurationTextView = tv;
	}

	@Override
	public void performAction(int progress) {
		mProgress = progress;
		mHandler.postDelayed(mSeekActor, SEEK_DELAY);
	}

	@Override
	public void performPostChangeAction(int progress) {
		startPositionUpdate();
		return;
	}

	@Override
	public void performPreChangeAction(int progress) {
		stopPositionUpdate();
		return;
	}

	@Override
	public int getMaxValue() {
		return mDuration;
	}

	@Override
	public int getCurrentValue() {
		return (int) (mRemoteController.getPosition() / 1000);
	}
	
	public void setDuration(long duration) {
		mDuration = (int) (duration);
		
		
		String skeleton = "";
		if (mDuration >= 3600000) {
            skeleton = "kk:mm:ss";
        } else {
            skeleton = "mm:ss";
        }
        mFormat = new SimpleDateFormat(skeleton, Locale.getDefault());
        mFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        
        mDurationTextView.setText(mFormat.format(mDuration * 1000));
	}
	
	public void startPositionUpdate() {
		mHandler.post(mUpdateActor);
	}
	
	public void stopPositionUpdate() {
		mHandler.removeCallbacks(mUpdateActor);
	}
	
	public void update() {
		mPosSeekBar.setProgress(getCurrentValue());
		mCurrentPositionTextView.setText(mFormat.format(getCurrentValue() * 1000));
	}
	
	private class FutureSeekToRunnable implements Runnable {

		@Override
		public void run() {
			mRemoteController.seekTo(mProgress * 1000);
			mCurrentPositionTextView.setText(mFormat.format(mProgress * 1000));
		}
		
	}

}
