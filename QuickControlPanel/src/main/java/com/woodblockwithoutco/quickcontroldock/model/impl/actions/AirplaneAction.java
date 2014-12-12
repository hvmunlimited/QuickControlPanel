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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;
import com.woodblockwithoutco.quickcontroldock.R;


public class AirplaneAction extends ToggleAction {

	private Context mContext;
	private final Intent mBroadcastIntentEnabled;
	private final Intent mBroadcastIntentDisabled;

	public AirplaneAction(Context context) {
		mContext = context;

		mBroadcastIntentEnabled = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		mBroadcastIntentEnabled.putExtra("state", true);

		mBroadcastIntentDisabled = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		mBroadcastIntentDisabled.putExtra("state", false);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void performActionOn() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				Settings.System.putInt(mContext.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 1);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				try {
					mContext.sendBroadcast(mBroadcastIntentEnabled);
				} catch(SecurityException e) {
					Toast.makeText(mContext, R.string.airplane_mode_unavailable, Toast.LENGTH_LONG).show();
				}
			}
		}.execute();
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void performActionOff() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				Settings.System.putInt(mContext.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				try {
					mContext.sendBroadcast(mBroadcastIntentDisabled);
				} catch(SecurityException e) {
					Toast.makeText(mContext, R.string.airplane_mode_unavailable, Toast.LENGTH_LONG).show();
				}
			}
		}.execute();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isStateOn() {
		return Settings.System.getInt(mContext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
	}

	@Override
	public boolean isStateOff() {
		return !isStateOn();
	}
}
