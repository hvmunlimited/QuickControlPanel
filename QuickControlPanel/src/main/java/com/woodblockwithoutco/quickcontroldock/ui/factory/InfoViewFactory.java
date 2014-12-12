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
package com.woodblockwithoutco.quickcontroldock.ui.factory;

import java.util.List;

import com.woodblockwithoutco.quickcontroldock.R;
import com.woodblockwithoutco.quickcontroldock.model.impl.text.BatteryTextView;
import com.woodblockwithoutco.quickcontroldock.model.impl.text.DateTextView;
import com.woodblockwithoutco.quickcontroldock.model.impl.text.TimeTextView;
import com.woodblockwithoutco.quickcontroldock.model.text.InfoItemType;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.InfoResolver;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoViewFactory {
	
	private Context mContext;

	public InfoViewFactory(Context context) {
		mContext = context;
	}
	
	@SuppressWarnings("deprecation")
	public View getInfoView() {
		LinearLayout view = new LinearLayout(mContext);
		view.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(containerParams);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
	
		List<InfoItemType> itemsList = InfoResolver.getInfoItemsOrderList(mContext);
		for(InfoItemType type : itemsList) {
			TextView v = null;
			switch(type) {
			case BATTERY:
				v = new BatteryTextView(mContext);
				break;
			case DATE:
				v = new DateTextView(mContext);
				break;
			case TIME:
				v = new TimeTextView(mContext);
				break;
			}
			v.setGravity(Gravity.CENTER);
			v.setLayoutParams(params);
			view.addView(v);
		}
		
		view.setBackgroundDrawable(getBg());
		return view;
	}
	
	private Drawable getBg() {
		LayerDrawable bgDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.section_bg);
		GradientDrawable mainDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_main);
		GradientDrawable shadowDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_shadow);
		mainDrawable.setColor(ColorsResolver.getSectionMainBackgroundColor(mContext));
		shadowDrawable.setColor(ColorsResolver.getSectionShadowBackgroundColor(mContext));

		return bgDrawable;
	}
}
