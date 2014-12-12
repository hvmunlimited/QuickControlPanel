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

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.AttributeSet;

import com.woodblockwithoutco.quickcontroldock.model.text.BroadcastTextView;

public class BatteryTextView extends BroadcastTextView {
	
	private StringBuilder mBuilder = new StringBuilder();
	
	public BatteryTextView(Context context) {
		this(context, null, 0);
	}
	
	public BatteryTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BatteryTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addAction(Intent.ACTION_BATTERY_CHANGED);
	}

	@Override
	protected String getIntentActionName() {
		return Intent.ACTION_POWER_USAGE_SUMMARY;
	}

	@Override
	protected void onReceive(Intent intent) {
		
		int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		if(batteryLevel > -1) {
			mBuilder.append(batteryLevel);
		} else {
			mBuilder.append("??");
		}
		
		mBuilder.append("%");
		setText(mBuilder.toString());
		
		int length = mBuilder.length();
		if(length > 0) mBuilder.delete(0, length);
	}
}
