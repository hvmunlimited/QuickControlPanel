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
package com.woodblockwithoutco.quickcontroldock.model.seekbar;


import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier.OnVisibilityEventListener;
import com.woodblockwithoutco.quickcontroldock.model.action.SeekBarAction;
import com.woodblockwithoutco.quickcontroldock.util.ReceiverUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;

public abstract class BaseBroadcastActionSeekBar extends BaseActionSeekBar implements OnVisibilityEventListener {

	protected IntentFilter mFilter = new IntentFilter();
	private boolean mBroadcastRegistered = false;
	protected SimpleBroadcastReceiver mReceiver = new SimpleBroadcastReceiver();

	public BaseBroadcastActionSeekBar(Context context) {
		this(context, null, 0);
	}

	public BaseBroadcastActionSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseBroadcastActionSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onShow() {
		super.onShow();
		startListeningForBroadcastAction();
	}

	@Override
	public void onHide() {
		stopListeningForBroadcastAction();
	}

	public final void addAction(String action) {
		mFilter.addAction(action);
	}

	protected abstract void onReceive(Intent broadcastIntent);

	public void stopListeningForBroadcastAction() {
		if(mBroadcastRegistered) {
			mBroadcastRegistered = false;
			ReceiverUtil.unregisterReceiverSafe(getContext(), mReceiver);
		}
	}

	public void startListeningForBroadcastAction() {
		if(!mBroadcastRegistered) {
			mBroadcastRegistered = true;
			getContext().registerReceiver(mReceiver, mFilter);
		}
	}

	protected abstract int transformProgress(int progress);
	protected abstract SeekBarAction createSeekBarAction();

	protected class SimpleBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			BaseBroadcastActionSeekBar.this.onReceive(intent);
		}

	}

}
