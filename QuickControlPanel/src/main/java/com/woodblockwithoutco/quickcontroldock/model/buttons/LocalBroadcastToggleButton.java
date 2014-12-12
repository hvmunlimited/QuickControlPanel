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

import com.woodblockwithoutco.quickcontroldock.util.ReceiverUtil;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;

public abstract class LocalBroadcastToggleButton extends BroadcastToggleButton {

	
	public LocalBroadcastToggleButton(Context context) {
		this(context, null, 0);
	}
	
	public LocalBroadcastToggleButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public LocalBroadcastToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void startListeningForBroadcast(IntentFilter filter) {
		LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
	}

	@Override
	public void stopListeningForBroadcast() {
		ReceiverUtil.unregisterLocalReceiverSafe(getContext(), mReceiver);
	}
}
