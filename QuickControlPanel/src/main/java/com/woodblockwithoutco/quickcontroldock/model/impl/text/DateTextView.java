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
package com.woodblockwithoutco.quickcontroldock.model.impl.text;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;

import com.woodblockwithoutco.quickcontroldock.model.text.BroadcastTextView;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.InfoResolver;

public class DateTextView extends BroadcastTextView {

	public DateTextView(Context context) {
		this(context, null, 0);
	}

	public DateTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DateTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addAction(Intent.ACTION_TIME_TICK);
		addAction(Intent.ACTION_TIMEZONE_CHANGED);
		addAction(Intent.ACTION_TIME_CHANGED);
	}
	
	@Override
	public void onShow() {
		super.onShow();
		updateDate();
	}


	@Override
	protected void onReceive(Intent intent) {
		updateDate();
	}

	private void updateDate() {
		setText(getCurrentDate());
	}

	private String getCurrentDate() {
		return InfoResolver.getDateFormat(getContext()).format(Calendar.getInstance().getTime());
	}

	@Override
	protected String getIntentActionName() {
		return Settings.ACTION_DATE_SETTINGS;
	}
	
	

}
