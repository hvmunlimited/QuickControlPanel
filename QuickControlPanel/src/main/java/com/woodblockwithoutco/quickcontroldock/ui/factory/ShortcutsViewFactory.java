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


import com.woodblockwithoutco.quickcontroldock.model.group.ShortcutContainer;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ShortcutsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.SettingsActivity;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class ShortcutsViewFactory {

	private Context mContext;
	
	public ShortcutsViewFactory(Context context) {
		mContext = context;
	}
	
	@SuppressWarnings("deprecation")
	public LinearLayout getShortcutsSectionView() {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		LinearLayout result = new LinearLayout(mContext);
		result.setGravity(Gravity.CENTER);
		result.setOrientation(LinearLayout.VERTICAL);
		result.setLayoutParams(params);

		result.addView(getShortcutsScrollView());
		
		LayerDrawable bgDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.section_bg);
		GradientDrawable mainDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_main);
		GradientDrawable shadowDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_shadow);
		mainDrawable.setColor(ColorsResolver.getSectionMainBackgroundColor(mContext));
		shadowDrawable.setColor(ColorsResolver.getSectionShadowBackgroundColor(mContext));

		result.setBackgroundDrawable(bgDrawable);
		
		return result;
	}
	
	public HorizontalScrollView getShortcutsScrollView() {
		HorizontalScrollView scrollView = (HorizontalScrollView) LayoutInflater.from(mContext).inflate(R.layout.buttons_container, null, false);
		RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		scrollParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		scrollView.setLayoutParams(scrollParams);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setVerticalScrollBarEnabled(false);
		
		LinearLayout container = (LinearLayout) scrollView.findViewById(R.id.toggles_container);
		FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		containerParams.gravity = Gravity.START;
		container.setLayoutParams(containerParams);
		
		int size = LinearLayout.LayoutParams.WRAP_CONTENT;
		int margin = ShortcutsResolver.getShortcutsDistance(mContext);
		LinearLayout.LayoutParams shortcutParams = new LinearLayout.LayoutParams(size, size);
		shortcutParams.setMargins(margin, 0, margin, 0);
		
		List<String> shortcuts = ShortcutsResolver.getShortcutsOrder(mContext);
		boolean atLeastOneShortcutFound = false;
		for(String s : shortcuts) {
			String[] split = s.split("/");
			ShortcutContainer shortcut = new ShortcutContainer(mContext);
			shortcut.init(split[0], split[1]);
			shortcut.setLayoutParams(shortcutParams);
			container.addView(shortcut);
			atLeastOneShortcutFound = true;
		}
		
		if(!atLeastOneShortcutFound) {
			ShortcutContainer shortcut = new ShortcutContainer(mContext);
			shortcut.init(mContext.getPackageName(), SettingsActivity.class.getName());
			shortcut.setLayoutParams(shortcutParams);
			container.addView(shortcut);
		}
		
		return scrollView;
	}
}
