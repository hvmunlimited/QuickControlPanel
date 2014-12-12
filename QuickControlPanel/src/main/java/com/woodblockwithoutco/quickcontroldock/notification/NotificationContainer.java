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
package com.woodblockwithoutco.quickcontroldock.notification;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class NotificationContainer extends LinearLayout implements OnGestureListener {

	private GestureDetector mDetector;
	private OnGestureListener2 mGestureListener;
	private boolean mShouldReturnToOriginalPosition = true;

	public NotificationContainer(Context context) {
		this(context, null, 0);
	}
	
	public NotificationContainer(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public NotificationContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setClickable(true);
		mDetector = new GestureDetector(context, this);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		super.onTouchEvent(e);
		
		if(e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
			if(mGestureListener != null) {
				mGestureListener.onTouchEnd();
			}
		}
		
		return mDetector.onTouchEvent(e);
	}
	
	
	@Override
	public boolean shouldDelayChildPressedState() {
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		if(mGestureListener != null) return mGestureListener.onDown(e);
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if(mGestureListener != null) return mGestureListener.onFling(e1, e2, velocityX, velocityY);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if(mGestureListener != null) mGestureListener.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if(mGestureListener != null) return mGestureListener.onScroll(e1, e2, distanceX, distanceY);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		if(mGestureListener != null) mGestureListener.onShowPress(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if(mGestureListener != null) return mGestureListener.onSingleTapUp(e);
		return false;
	}
	
	public void setOnGestureListener(OnGestureListener2 l) {
		mGestureListener = l;
	}
	
	public void setShouldReturnToOriginalPosition(boolean shouldReturn) {
		mShouldReturnToOriginalPosition = shouldReturn;
	}
	
	public boolean shouldReturnToOriginalPosition() {
		return mShouldReturnToOriginalPosition;
	}
	

}
