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
package com.woodblockwithoutco.quickcontroldock.model.impl.actions;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.content.LocalBroadcastManager;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleTriAction;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;

public class ScreenTimeoutAction extends ToggleTriAction {

	private final int mFirstTimeout;
	private final int mSecondTimeout;
	private final int mThirdTimeout;
	private ContentResolver mResolver;
	private Context mContext;
	private final Intent mClickIntent;

	public ScreenTimeoutAction(Context context) {
		mContext = context;
		mResolver = context.getContentResolver();
		String modes = TogglesResolver.getTimeoutModes(context);
		if(modes.equals("15s/30s/1m")) {
			mFirstTimeout = 15 * 1000;
			mSecondTimeout = 30 * 1000;
			mThirdTimeout = 60 * 1000;
		} else {
			mFirstTimeout = 30 * 1000;
			mSecondTimeout = 60 * 1000;
			mThirdTimeout = 2 * 60 * 1000;
		}
		
		mClickIntent = new Intent(mContext.getPackageName()+".TIMEOUT_BUTTON_CLICKED");
	}
	
	@Override
	public State getCurrentState() {
		int timeout = getTimeout();
		if(timeout == mFirstTimeout) {
			return State.FIRST;
		} else if(timeout == mSecondTimeout) {
			return State.SECOND;
		} else if(timeout == mThirdTimeout) {
			return State.THIRD;
		}
		return State.SECOND;
	}

	@Override
	protected void performFirstAction() {
		saveTimeout(mFirstTimeout);
	}

	@Override
	protected void performSecondAction() {
		saveTimeout(mSecondTimeout);
	}

	@Override
	protected void performThirdAction() {
		saveTimeout(mThirdTimeout);
	}
	
	private void saveTimeout(int timeout) {
		
		new AsyncTask<Integer, Void, Void>() {

			@Override
			protected Void doInBackground(Integer... args) {
				Settings.System.putInt(mResolver, Settings.System.SCREEN_OFF_TIMEOUT, args[0]);
				return null;
			}
			
			@Override
			public void onPostExecute(Void result) {
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(mClickIntent);
			}
			
		}.execute(timeout);

	}
	
	private int getTimeout() {
		try {
			return Settings.System.getInt(mResolver, Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			return mSecondTimeout;
		}
	}

}
