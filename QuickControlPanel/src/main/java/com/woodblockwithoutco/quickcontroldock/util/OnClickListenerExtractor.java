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

import java.lang.reflect.Field;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class OnClickListenerExtractor {
	
	private static final String TAG = null;
	private Field mOnClickListenerField;
	private Field mListenerInfoField;

	public OnClickListenerExtractor() throws ClassNotFoundException, NoSuchFieldException {
		Class<?> clickInfoClass = Class.forName("android.view.View$ListenerInfo");
		
		mListenerInfoField = View.class.getDeclaredField("mListenerInfo");
		mListenerInfoField.setAccessible(true);
		
		mOnClickListenerField = clickInfoClass.getDeclaredField("mOnClickListener");
		mOnClickListenerField.setAccessible(true);
	}
	
	public OnClickListener getOnClickListener(View v) {
		try {
			Object listenerInfo = mListenerInfoField.get(v);
			if(listenerInfo == null) return null;
			Object result = mOnClickListenerField.get(listenerInfo);
			return (OnClickListener) result;
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Illegal access to listener info field");
			return null;
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Trying to access listener info field in wrong object");
			return null;
		}
		
	}
}
