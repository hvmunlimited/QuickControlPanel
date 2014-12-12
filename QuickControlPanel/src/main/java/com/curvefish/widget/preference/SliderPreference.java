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

package com.curvefish.widget.preference;

import com.curvefish.widget.Utils;
import com.woodblockwithoutco.quickcontroldock.R;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;

public class SliderPreference extends Preference implements OnSeekBarChangeListener {
	private final static int MAX_SLIDER_VALUE = 100;
	private final static int MIN_SLIDER_VALUE = 1;
	private final static int INITIAL_VALUE = 50;

	private final static int MAX_INTERNAL = 100;

	private int mValue = INITIAL_VALUE; //actual value
	private int mMinValue = MIN_SLIDER_VALUE;
	private int mMaxValue = MAX_SLIDER_VALUE;

	private OnSeekBarChangeListener mExternalListener;
	private TextView mCurrentValueTextView;
	private StringBuilder mCurrentValueBuilder;
	private String mUnits;


	public SliderPreference(Context context) {
		super(context);
		setWidgetLayoutResource(R.layout.slider_preference);
	}

	public SliderPreference(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.preferenceStyle);
	}

	public SliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SliderPreference, defStyle, 0);
		mMinValue = a.getInt(R.styleable.SliderPreference_minValue, MIN_SLIDER_VALUE);
		mMaxValue = a.getInt(R.styleable.SliderPreference_maxValue, MAX_SLIDER_VALUE);
		mUnits = a.getString(R.styleable.SliderPreference_units);
		a.recycle();

		setWidgetLayoutResource(R.layout.slider_preference);
		
		mCurrentValueBuilder = new StringBuilder();
		
	}

	public void setOnSeekBarPrefChangeListener(OnSeekBarChangeListener listener) {
		mExternalListener = listener;
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);

		if (view instanceof LinearLayout) {
			LinearLayout ll = (LinearLayout) view;
			ll.setOrientation(LinearLayout.VERTICAL);
		}

		View frame = view.findViewById(android.R.id.widget_frame);
		if (frame != null) {
			ViewGroup.LayoutParams lp = frame.getLayoutParams();
			lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		}


		SeekBar bar = (SeekBar)view.findViewById(R.id.slider);
		bar.setMax(MAX_INTERNAL);

		bar.setProgress(transformValue(mValue));
		bar.setOnSeekBarChangeListener(this);
		
		mCurrentValueTextView = (TextView)view.findViewById(R.id.current);
		mCurrentValueTextView.setText(getValueFormatted(mValue));
	}

	private int transformValue(int value) {
		return (value - mMinValue) * MAX_INTERNAL / (mMaxValue - mMinValue);
	}

	private int detransformValue(int transValue) {
		return (mMaxValue-mMinValue) * transValue / MAX_INTERNAL + mMinValue;
	}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		mCurrentValueTextView.setText(getValueFormatted(detransformValue(progress)));
		if(mExternalListener != null) mExternalListener.onProgressChanged(seekBar, progress, fromUser);
	}


	public void onStartTrackingTouch(SeekBar seekBar) {
		if(mExternalListener != null) mExternalListener.onStartTrackingTouch(seekBar);
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		mValue = detransformValue(seekBar.getProgress());
		persistInt(mValue);
		if(mExternalListener != null) mExternalListener.onStopTrackingTouch(seekBar);
	}


	@Override 
	protected Object onGetDefaultValue(TypedArray ta,int index) {
		int dValue = (int)ta.getInt(index, INITIAL_VALUE);
		return (int) Utils.clamp(dValue, 0, MAX_SLIDER_VALUE);
	}


	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		mValue = defaultValue != null ? (Integer)defaultValue : INITIAL_VALUE;

		if (!restoreValue) {
			persistInt(mValue);
		} else {
			mValue = getPersistedInt(mValue);
		}
		
		
	}

	private String getValueFormatted(int value) {
		return mCurrentValueBuilder.
		delete(0, mCurrentValueBuilder.length()).
		append(value).append(mUnits == null ? "" : mUnits).toString();
	}

}