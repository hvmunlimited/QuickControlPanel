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

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;


public class BluetoothAction extends ToggleAction {

	private BluetoothAdapter mBluetoothAdapter;

	public BluetoothAction(Context context) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null) {
			Log.e(getClass().getName(), "Bluetooth hardware is missing!");
		}
	}

	@Override
	protected void performActionOn() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				if(mBluetoothAdapter != null) {
					mBluetoothAdapter.enable();
				}
				return null;
			}
		}.execute();
	}

	@Override
	protected void performActionOff() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				if(mBluetoothAdapter != null) {
					mBluetoothAdapter.disable();
				}
				return null;
			}
		}.execute();
	}

	@Override
	public boolean isStateOn() {
		if(mBluetoothAdapter != null) {
			return mBluetoothAdapter.getState() == BluetoothAdapter.STATE_CONNECTED ||
					mBluetoothAdapter.getState() == BluetoothAdapter.STATE_CONNECTING ||
					mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON ||
					mBluetoothAdapter.getState() == BluetoothAdapter.STATE_DISCONNECTED ||
					mBluetoothAdapter.getState() == BluetoothAdapter.STATE_DISCONNECTING;
		} else {
			return false;
		}
	}

	@Override
	public boolean isStateOff() {
		if(mBluetoothAdapter != null) {
			return mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF;
		} else {
			return false;
		}
	}

	@Override
	public boolean isStateIndefinite() {
		if(mBluetoothAdapter != null) {
			return mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_OFF || 
					mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON;
		} else {
			return false;
		}
	}

	public int getState() {
		return mBluetoothAdapter.getState();
	}

}
