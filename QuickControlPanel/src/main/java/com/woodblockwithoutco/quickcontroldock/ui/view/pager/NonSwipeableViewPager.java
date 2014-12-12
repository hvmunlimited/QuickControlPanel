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
package com.woodblockwithoutco.quickcontroldock.ui.view.pager;

import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {
	
	private boolean mIsSwipeEnabled = true;

    public NonSwipeableViewPager(Context context) {
        this(context, null, 0);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }
    
    public NonSwipeableViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        
        if(attrs != null) {
        	TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NonSwipeableViewPager, defStyle, 0);
        	mIsSwipeEnabled = a.getBoolean(R.styleable.NonSwipeableViewPager_enableSwipe, false);
        	a.recycle();
        }
    }
    
    public void setSwipeEnabled(boolean swipeEnabled) {
    	mIsSwipeEnabled = swipeEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	
    	if(!mIsSwipeEnabled) return false;
        
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	if(!mIsSwipeEnabled) return false;
    	
        // Never allow swiping to switch between pages
        return super.onTouchEvent(event);
    }
    
}
