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

import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier;
import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier.OnVisibilityEventListener;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.InfoResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.util.ReceiverUtil;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BroadcastTextView extends TextView implements OnClickListener, OnVisibilityEventListener {
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			BroadcastTextView.this.onReceive(intent);
		}
	};
	
	private boolean mBroadcastRegistered = false;
	private IntentFilter mFilter = new IntentFilter();
	
	public BroadcastTextView(Context context) {
		this(context, null, 0);
	}
	
	public BroadcastTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BroadcastTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
		ColorStateList colors = new ColorStateList(
				 new int[][]{
                         new int[]{android.R.attr.state_pressed},
                         new int[] { android.R.attr.state_enabled}
                 },
                 new int[] {
                         ColorsResolver.getPressedColor(getContext()),
                         ColorsResolver.getTextColor(getContext())
                     });
		setTextColor(colors);
		setTextSize(TypedValue.COMPLEX_UNIT_SP, InfoResolver.getInfoTextSize(getContext()));
		
		VisibilityEventNotifier.getInstance().registerListener(this);
	}
	
	public final void stopListeningForBroadcastAction() {
		mBroadcastRegistered = false;
		ReceiverUtil.unregisterReceiverSafe(getContext(), mReceiver);
	}
	
	public final void startListeningForBroadcastAction() {
		mBroadcastRegistered = true;
		getContext().registerReceiver(mReceiver, mFilter);
	}
	
	@Override
	public void onShow() {
		if(!mBroadcastRegistered) {
			startListeningForBroadcastAction();
		}
	}
	
	@Override
	public void onHide() {
		if(mBroadcastRegistered) {
			stopListeningForBroadcastAction();
		}
	}
	
	public final void addAction(String action) {
		mFilter.addAction(action);
	}
	
	@Override
	public void onClick(View v) {
		try {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(getIntentActionName());

			getContext().startActivity(intent);

			ControlService service = (ControlService) ControlService.getInstance();
			if(service != null && service.isAttachedToWindow() && ControlService.isRunning()) {
				service.close();
			}
		} catch(ActivityNotFoundException e) {
			setOnClickListener(null);
		}
	}
	
	protected abstract String getIntentActionName();

	protected abstract void onReceive(Intent intent);

}
