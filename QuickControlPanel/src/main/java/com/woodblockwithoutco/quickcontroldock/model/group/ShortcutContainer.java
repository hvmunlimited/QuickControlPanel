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
package com.woodblockwithoutco.quickcontroldock.model.group;

import java.util.List;


import com.woodblockwithoutco.quickcontroldock.global.holder.AppShortcutResolverHolder;
import com.woodblockwithoutco.quickcontroldock.model.buttons.ShortcutImage;
import com.woodblockwithoutco.quickcontroldock.model.text.ShortcutTitle;
import com.woodblockwithoutco.quickcontroldock.prefs.editors.ShortcutsEditor;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ShortcutsResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ShortcutContainer extends LinearLayout implements OnClickListener {

	private Intent mClickIntent;
	private int mPressedColor;

	public ShortcutContainer(Context context) {
		this(context, null, 0);
	}

	public ShortcutContainer(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShortcutContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOrientation(LinearLayout.VERTICAL);
		mPressedColor = ColorsResolver.getPressedColor(getContext());
	}
	
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if(pressed) {
			setBackgroundColor(mPressedColor);
		} else {
			setBackgroundColor(0x00000000);
		}
	}

	public void init(String pkgName, String className) {
		setOnClickListener(this);
		setClickable(true);
		setGravity(Gravity.CENTER);
		ShortcutImage img = new ShortcutImage(getContext());
		img.init(pkgName, className);
		int size = ShortcutsResolver.getShortcutSize(getContext());
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
		img.setLayoutParams(params);
		
		int padding = ShortcutsResolver.getShortcutsPadding(getContext());
		img.setPadding(padding, padding, padding, padding);
		
		addView(img);

		if(ShortcutsResolver.isShortcutTitleEnabled(getContext())) {
			ShortcutTitle title = new ShortcutTitle(getContext());
			title.setSingleLine();
			title.setEllipsize(TruncateAt.END);
			title.init(pkgName, className);
			LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(size, LinearLayout.LayoutParams.WRAP_CONTENT);
			title.setLayoutParams(titleParams);
			addView(title);
		}

		mClickIntent = AppShortcutResolverHolder.getInstance().getIntent(pkgName, className);
	}

	@Override
	public void onClick(View v) {
		if(mClickIntent != null) {
			try {
				
				getContext().startActivity(mClickIntent);
				
				ControlService service = (ControlService) ControlService.getInstance();
				if(service != null && service.isAttachedToWindow() && ControlService.isRunning()) {
					service.close();
				}
			} catch(ActivityNotFoundException e) {
				ViewParent parent = getParent();
				if(parent != null && parent instanceof ViewGroup) {
					ViewGroup parentView = (ViewGroup) parent;
					parentView.removeView(this);
					ComponentName compName = mClickIntent.getComponent();
					if(compName != null) {
						String className = compName.getClassName();
						String pkgName = compName.getPackageName();
						List<String> shortcuts = ShortcutsResolver.getShortcutsOrder(getContext());
						shortcuts.remove(pkgName+"/"+className);
						ShortcutsEditor.saveShortcuts(getContext(), shortcuts);
					}
				}
			}
		}
	}

}
