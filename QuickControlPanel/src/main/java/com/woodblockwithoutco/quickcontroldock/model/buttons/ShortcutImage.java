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
package com.woodblockwithoutco.quickcontroldock.model.buttons;

import java.io.File;

import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ShortcutsResolver;
import com.woodblockwithoutco.quickcontroldock.util.AppShortcutResolver;
import com.woodblockwithoutco.quickcontroldock.util.icon.IconPackNameParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ShortcutImage extends ImageView {
	
	private static final String TAG = "ShortcutImage";

	public ShortcutImage(Context context) {
		this(context, null, 0);
	}
	
	public ShortcutImage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ShortcutImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void init(String pkg, String className) {
		
		AppShortcutResolver resolver = new AppShortcutResolver(getContext());
		
		boolean isCustomIconUsed = ShortcutsResolver.isCustomIconUsed(getContext(), className);
		Drawable icon = null;
		if(isCustomIconUsed) {
			icon = getCustomIcon(className);
			if(icon != null) {
				setImageDrawable(icon);
			} else {
				Log.e(TAG, "Missing custom icon for class "+className);
			}
		} else if(ShortcutsResolver.isExternalIconPackUsed(getContext())) {
			icon = IconPackNameParser.getIconForComponent(getContext(), pkg+"/"+className);
			if(icon != null) {
				setImageDrawable(icon);
			} else {
				Log.e(TAG, "Missing custom icon in external icon pack for class "+className);
			}
		}
		
		if(icon == null) {
			icon = resolver.getIcon(pkg, className);
			setImageDrawable(icon);
		}
	}
	
	private Drawable getCustomIcon(String className) {
		String path = getContext().getFilesDir() + File.separator + className + ".png";
		Bitmap decoded = BitmapFactory.decodeFile(path);
		if(decoded != null) {
			BitmapDrawable result = new BitmapDrawable(getResources(), decoded);
			return result;
		}
		
		return null;
	}

}
