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
package com.woodblockwithoutco.quickcontroldock.model.text;

import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ShortcutsResolver;
import com.woodblockwithoutco.quickcontroldock.util.AppShortcutResolver;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

public class ShortcutTitle extends TextView {

	public ShortcutTitle(Context context) {
		this(context, null, 0);
	}
	
	public ShortcutTitle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ShortcutTitle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setTextColor(ColorsResolver.getTextColor(getContext()));
		int size = ShortcutsResolver.getShortcutTextSize(getContext());
		setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		setGravity(Gravity.CENTER);
	}
	
	public void init(String pkgName, String className) {
		AppShortcutResolver resolver = new AppShortcutResolver(getContext());
		String title = resolver.getLabel(pkgName, className);
		setText(title);
	}

}
