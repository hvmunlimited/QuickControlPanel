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

import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.SparseArray;

public class FilterableStateListDrawable extends StateListDrawable {

	private int mChildrenCount = 0;
	private SparseArray<ColorFilter> mFilterMap;
	private SparseArray<ColorFilter> mBgFilterMap;

	public FilterableStateListDrawable() {
		super();
		mFilterMap = new SparseArray<ColorFilter>();
		mBgFilterMap = new SparseArray<ColorFilter>();
	}

	@Override
	public void addState(int[] stateSet, Drawable drawable) {
		super.addState(stateSet, drawable);
		mChildrenCount++;
	}


	public void addState(int[] stateSet, Drawable drawable, ColorFilter colorFilter, ColorFilter bgColorFilter) {
		int currChild = mChildrenCount;
		addState(stateSet, drawable);
		mFilterMap.put(currChild, colorFilter);
		mBgFilterMap.put(currChild, bgColorFilter);
	}

	@Override
	public boolean selectDrawable(int idx) {

		boolean result = super.selectDrawable(idx);

		if(getCurrent() != null) {
			LayerDrawable drawable = (LayerDrawable) getCurrent();
			int count = drawable.getNumberOfLayers();
			for(int index = 0; index < count; index++) {
				Drawable d = drawable.getDrawable(index);
				if(index == 0){
					d.setColorFilter(getBgColorFilterForIdx(idx));
					continue;
				}
				d.setColorFilter(getColorFilterForIdx(idx));	
			}
		}

		return result;
	}

	private ColorFilter getBgColorFilterForIdx(int idx) {
		return mBgFilterMap != null ? mBgFilterMap.get(idx) : null;
	}

	private ColorFilter getColorFilterForIdx(int idx) {
		return mFilterMap != null ? mFilterMap.get(idx) : null;
	}
}
