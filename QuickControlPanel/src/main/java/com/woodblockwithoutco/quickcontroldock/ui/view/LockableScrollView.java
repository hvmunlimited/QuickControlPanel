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
package com.woodblockwithoutco.quickcontroldock.ui.view;

import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.GeneralResolver;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView {
	
	private boolean mIsLocked = false;

	public LockableScrollView(Context context) {
		this(context, null, 0);
	}
	
	public LockableScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	
	public LockableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setLocked(boolean locked) {
		mIsLocked = locked;
	}
	
	public boolean isLocked() {
		return mIsLocked;
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mIsLocked) return super.onTouchEvent(ev);
                return false; 
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsLocked) return false;
        else return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int height = getMeasuredHeight();
        final int width = getMeasuredWidth();

        float factor = Math.max(0, 1 - 1.1f * GeneralResolver.getPanelSpanPercentage(getContext()));
        int newHeight = (int)(factor * height);
        
        setMeasuredDimension(width, newHeight);
    }

}
