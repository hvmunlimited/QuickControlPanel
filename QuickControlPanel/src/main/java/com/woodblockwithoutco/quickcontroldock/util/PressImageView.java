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
package com.woodblockwithoutco.quickcontroldock.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PressImageView extends ImageView {
	
	public interface OnPressedStateChangeListener {
		public void onPressedStateChange(ImageView v, boolean pressed);
	}
	
	private OnPressedStateChangeListener mListener;

	public PressImageView(Context context) {
		this(context, null, 0);
	}
	
	public PressImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public  PressImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setOnPressedStateChangeListener(OnPressedStateChangeListener l) {
		mListener = l;
	}
	
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if(mListener != null) mListener.onPressedStateChange(this, pressed);
	}
}
