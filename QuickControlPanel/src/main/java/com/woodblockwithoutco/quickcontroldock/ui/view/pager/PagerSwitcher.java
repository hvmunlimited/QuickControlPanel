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

import java.lang.reflect.Field;

import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;

public class PagerSwitcher extends ImageView implements OnClickListener, OnPageChangeListener {

	private static enum Direction {
		LEFT,
		RIGHT
	}

	private static final String TAG = "PagerSwitcher";

	private ViewPager mControlledPager;
	private Direction mDirection = Direction.LEFT;

	private int mActiveColor;
	private int mPressedColor;

	private int mPagerId;


	public PagerSwitcher(Context context) {
		this(context, null, 0);
	}


	public PagerSwitcher(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PagerSwitcher(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);

		mActiveColor = ColorsResolver.getActiveColor(getContext());
		mPressedColor = ColorsResolver.getPressedColor(getContext());

		setColorFilter(mActiveColor);

		if(attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerSwitcher, defStyle, 0);
			mPagerId = a.getResourceId(R.styleable.PagerSwitcher_controlledPager, 0);
			int direction = a.getInt(R.styleable.PagerSwitcher_direction, 0);
			switch(direction) {
			case 0:
				mDirection = Direction.LEFT;
				break;
			case 1:
				mDirection = Direction.RIGHT;
				break;
			}
			a.recycle();
		}

		switch(mDirection) {
		case LEFT:
			setImageResource(R.drawable.ic_left);
			break;
		case RIGHT:
			setImageResource(R.drawable.ic_right);
			break;
		}

		setClickable(true);
		setLongClickable(true);
		setOnClickListener(this);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();



		if(mControlledPager == null && mPagerId != 0) {
			ViewParent parent = getParent();
			if(parent != null && parent instanceof ViewGroup) {
				ViewGroup parentView = (ViewGroup)parent;
				mControlledPager = (ViewPager) parentView.findViewById(mPagerId);
				if(mControlledPager != null) {
					OnPageChangeListener l = getPageChangeListener(mControlledPager);
					if(l == null) {
						l = new MultiPageChangeListener();
					}
					((MultiPageChangeListener)l).addOnPageChangeListener(this);
					mControlledPager.setOnPageChangeListener(l);
				} else {
					Log.e(TAG, "Can't find given ViewPager!");
				}
			} else {
				Log.e(TAG, "Can't find given ViewPager!");
			}
			checkIfNeedToHideOrShow(mControlledPager.getCurrentItem());
		}

	}

	private void checkIfNeedToHideOrShow(int page) {
		if(mControlledPager != null) {
			int max = mControlledPager.getAdapter().getCount();
			int visibility = View.VISIBLE;


			switch(mDirection) {
			case LEFT:
				if(page == 0) {
					visibility = View.INVISIBLE;
				} else {
					visibility = View.VISIBLE;
				}
				break;
			case RIGHT:
				if(page == max - 1) {
					visibility = View.INVISIBLE;
				} else {
					visibility = View.VISIBLE;
				}
				break;
			}

			setVisibility(visibility);
		}
	}

	@Override
	public void onClick(View v) {
		if(mControlledPager != null) {
			int position =  mControlledPager.getCurrentItem();
			switch(mDirection) {
			case LEFT:
				if(position - 1 >=0) mControlledPager.setCurrentItem(position - 1, true);
				break;
			case RIGHT:
				int max = mControlledPager.getAdapter().getCount();
				if(position + 1 <= max - 1) mControlledPager.setCurrentItem(position + 1, true);
				break;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch(e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setColorFilter(mPressedColor);
			break;
		case MotionEvent.ACTION_UP:
			setColorFilter(mActiveColor);
			break;
		}
		return super.onTouchEvent(e);
	}


	@Override
	public void onPageScrollStateChanged(int state) {
	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}


	@Override
	public void onPageSelected(int position) {
		checkIfNeedToHideOrShow(position);
	}

	private static OnPageChangeListener getPageChangeListener(ViewPager v) {
		try {
			Field f = ViewPager.class.getDeclaredField("mOnPageChangeListener");
			f.setAccessible(true);
			Object o;
			o = f.get(v);
			return (OnPageChangeListener) o;
		} catch (NoSuchFieldException e) {
			Log.e(TAG, "Can't find page change listener field!");
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Can't access change listener field!");
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Can't find page change listener field in given object!");
		}
		return null;
	}

}
