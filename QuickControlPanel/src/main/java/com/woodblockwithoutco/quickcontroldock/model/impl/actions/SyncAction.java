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
import android.support.v4.content.LocalBroadcastManager;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;


public class SyncAction extends ToggleAction {

	private final Intent mClickIntent;
	private Context mContext;

	public SyncAction(Context context) {
		mContext = context;
		mClickIntent = new Intent(context.getPackageName()+".SYNC_BUTTON_CLICKED");
	}

	@Override
	protected void performActionOn() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... args) {
				ContentResolver.setMasterSyncAutomatically(true);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(mClickIntent);
			}
		}.execute();
	}

	@Override
	protected void performActionOff() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				ContentResolver.setMasterSyncAutomatically(false);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(mClickIntent);
			}
		}.execute();
	}


	@Override
	public boolean isStateOn() {
		return ContentResolver.getMasterSyncAutomatically();
	}

	@Override
	public boolean isStateOff() {
		return !isStateOn();
	}
}
