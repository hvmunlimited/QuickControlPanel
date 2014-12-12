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
package com.woodblockwithoutco.quickcontroldock.model.seekbar;

import java.lang.reflect.Field;

import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier;
import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier.OnVisibilityEventListener;
import com.woodblockwithoutco.quickcontroldock.model.action.SeekBarAction;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsSeekBar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public abstract class BaseActionSeekBar extends SeekBar implements OnSeekBarChangeListener, OnVisibilityEventListener {

	private static final String TAG = "ActionSeekBar";
	protected SeekBarAction mAction;

	public BaseActionSeekBar(Context context) {
		this(context, null, 0);
	}

	public BaseActionSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseActionSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mAction = createSeekBarAction();

		setMax(mAction.getMaxValue());
		setProgress(mAction.getCurrentValue());

		setOnSeekBarChangeListener(this);

		if(!ColorsResolver.isSeekbarColorDisabled(getContext())) {
			int progressColor = ColorsResolver.getSeekBarProgressColor(getContext());
			int thumbColor = ColorsResolver.getSeekBarThumbColor(getContext());

			Drawable thumbDrawable = getThumbCompat();
			Rect thumbRect = thumbDrawable.getBounds();

			thumbDrawable.setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
			setThumb(thumbDrawable);
			getThumbCompat().setBounds(thumbRect); 

			Drawable progressDrawable = getProgressDrawable();
			Rect progressRect = progressDrawable.getBounds();
			progressDrawable.setColorFilter(progressColor, PorterDuff.Mode.SRC_ATOP);
			setProgressDrawable(progressDrawable);
			getProgressDrawable().setBounds(progressRect);
		}

		VisibilityEventNotifier.getInstance().registerListener(this);
	}

	@Override
	public void onShow() {
		setProgress(mAction.getCurrentValue());
	}

	@Override
	public void onHide() {
	}



	@SuppressLint("NewApi")
	private Drawable getThumbCompat() {
		try {
			return getThumb();
		} catch(NoSuchMethodError e) {
			try {
				Field thumbField = AbsSeekBar.class.getDeclaredField("mThumb");
				thumbField.setAccessible(true);
				return (Drawable) thumbField.get(this);
			} catch (NoSuchFieldException e1) {
				Log.e(TAG, "Can't find thumb drawable field");
			} catch (IllegalAccessException e1) {
				Log.e(TAG, "Illegal access to thumb drawable field!");
			} catch (IllegalArgumentException e1) {
				Log.e(TAG, "Can't find thumb drawable field in given object!");
			}
		}
		return null;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(fromUser) {
			mAction.performAction(transformProgress(progress));
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		mAction.performPreChangeAction(transformProgress(seekBar.getProgress()));
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mAction.performPostChangeAction(transformProgress(seekBar.getProgress()));
	}

	protected static void initContainer(Context context, View container, Drawable icon) {
		if(!(container instanceof LinearLayout)) {
			throw new IllegalArgumentException("Incorrect view container!");
		}

		ImageView im = (ImageView) container.findViewById(R.id.seekbar_icon);
		im.setImageDrawable(icon);
		im.setColorFilter(ColorsResolver.getSeekbarIconColor(context));

	}


	protected abstract int transformProgress(int progress);
	protected abstract SeekBarAction createSeekBarAction();

}
