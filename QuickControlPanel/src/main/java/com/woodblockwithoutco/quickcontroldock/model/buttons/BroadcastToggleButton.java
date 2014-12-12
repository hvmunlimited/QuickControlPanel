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


import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier;
import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier.OnVisibilityEventListener;
import com.woodblockwithoutco.quickcontroldock.util.ReceiverUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;

public abstract class BroadcastToggleButton extends BaseToggleButton implements OnVisibilityEventListener {

	protected IntentFilter mIntentFilter = new IntentFilter();
	private boolean mBroadcastRegistered = false;
	
	protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			BroadcastToggleButton.this.onReceive(intent);
		}
	};

	public BroadcastToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		VisibilityEventNotifier.getInstance().registerListener(this);
	}

	public void startListeningForBroadcast(IntentFilter filter) {
		if(!mBroadcastRegistered) {
			mBroadcastRegistered = true;
			getContext().registerReceiver(mReceiver, filter);
		}
	}

	public void stopListeningForBroadcast() {
		if(mBroadcastRegistered) {
			mBroadcastRegistered = false;
			ReceiverUtil.unregisterReceiverSafe(getContext(), mReceiver);
		}
	}

	public abstract void onReceive(Intent intent);
	
	@Override
	public void onShow() {
		startListeningForBroadcast(mIntentFilter);
		if(mAction.isStateOn()) {
			setVisualStateActive();
		} else {
			setVisualStateIdle();
		}
	}
	
	@Override
	public void onHide() {
		stopListeningForBroadcast();
	}

	protected void addBroadcastAction(String action) {
		mIntentFilter.addAction(action);
	}
}
