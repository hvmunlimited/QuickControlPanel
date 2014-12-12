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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.woodblockwithoutco.quickcontroldock.model.action.SeekBarAction;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.ScrubberAction;
import com.woodblockwithoutco.quickcontroldock.model.seekbar.BaseActionSeekBar;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.R;

public class ScrubberSeekBar extends BaseActionSeekBar {

	private TextView mCurrentTv;
	private TextView mDurationTv;
	private boolean mIsUpdating = false;

	public static View getContainer(Context context) {
		View result = LayoutInflater.from(context).inflate(R.layout.scrubber_bar_layout, null);
		
		TextView durationTv = (TextView) result.findViewById(R.id.duration);
		durationTv.setTextColor(ColorsResolver.getTextColor(context));
		durationTv.setAlpha(0.5f);
		
		TextView currentTv = (TextView) result.findViewById(R.id.current);
		currentTv.setTextColor(ColorsResolver.getTextColor(context));
		currentTv.setAlpha(0.5f);
		
		ScrubberSeekBar scrubber = (ScrubberSeekBar) result.findViewById(R.id.seek_bar);
		scrubber.attachCurrentPosTextView(currentTv);
		scrubber.attachDurationTextView(durationTv);
		return result;
	}

	public ScrubberSeekBar(Context context) {
		this(context, null, 0);
	}

	public ScrubberSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrubberSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setEnabled(false);
	}

	@Override
	protected int transformProgress(int progress) {
		return progress;
	}

	@Override
	protected SeekBarAction createSeekBarAction() {
		return new ScrubberAction(this);
	}


	public void attachCurrentPosTextView(TextView tv) {
		mCurrentTv = tv;
		getAction().attachCurrentPositionTextView(tv);
	}

	public void attachDurationTextView(TextView tv) {
		mDurationTv = tv;
		getAction().attachDurationTextView(tv);
	}

	public void startPositionUpdate() {
		if(!mIsUpdating) {
			getAction().startPositionUpdate();
			mIsUpdating = true;
		}
	}

	public void stopPositionUpdate() {
		if(mIsUpdating) {
			mIsUpdating = false;
			if(isEnabled()) getAction().stopPositionUpdate();
		}
	}

	private ScrubberAction getAction() {
		return (ScrubberAction) mAction;
	}

	public void requestHide() {
		requestVisibility(View.GONE);
	}

	public void requestShow() {
		requestVisibility(View.VISIBLE);
	}

	private void requestVisibility(int visibility) {
		mDurationTv.setVisibility(visibility);
		mCurrentTv.setVisibility(visibility);
		setVisibility(visibility);
	}

	public void setDuration(long duration) {
		if(duration > 0) {
			setMax((int)duration);
			getAction().setDuration(duration);
			setEnabled(true);
		} else {
			setMax(0);
			setEnabled(false);
		}
	}

}
