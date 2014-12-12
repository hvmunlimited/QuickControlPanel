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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;


public class DataLollipopAction extends ToggleAction {
	
	private static final String TAG = "DataAction";
	private TelephonyManager mTelephonyManager;
	private Method mToggleDataMethod;
	private Method mIsDataEnabledMethod;
	private Intent mBroadcastIntent;
	private Context mContext;
	
	public DataLollipopAction(Context context) {
		
		mContext = context;
		
		mBroadcastIntent = new Intent();
		mBroadcastIntent.setAction(context.getPackageName() + ".DATA_BUTTON_CLICKED");
		
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		Class<TelephonyManager> telManClass = TelephonyManager.class;
		try {
			mIsDataEnabledMethod = telManClass.getDeclaredMethod("getDataEnabled");
			mToggleDataMethod = telManClass.getDeclaredMethod("setDataEnabled", boolean.class);
			mIsDataEnabledMethod.setAccessible(true);
			mToggleDataMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "Can't find methods for manipulating mobile data state");
		}
	}

	@Override
	protected void performActionOn() {

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				try {
					mToggleDataMethod.invoke(mTelephonyManager, true);
				} catch (IllegalAccessException e) {
					Log.e(TAG, "Inaccessible data toggle method");
				} catch (IllegalArgumentException e) {
					Log.e(TAG, "Invalid data toggle method signature");
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					Log.e(TAG, "Invalid data toggle method invocation target: " + e.getCause().getMessage());
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(mBroadcastIntent);
			}
			
		}.execute();
	}

	@Override
	protected void performActionOff() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... args) {
				try {
					mToggleDataMethod.invoke(mTelephonyManager, false);
				} catch (IllegalAccessException e) {
					Log.e(TAG, "Inaccessible data toggle method");
				} catch (IllegalArgumentException e) {
					Log.e(TAG, "Invalid data toggle method signature");
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					Log.e(TAG, "Invalid data toggle method invocation target: " + e.getCause().getMessage());
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(mBroadcastIntent);
			}
			
		}.execute();
	}

	@Override
	public boolean isStateOn() {
		boolean result = false;
		try {
			result = (Boolean) mIsDataEnabledMethod.invoke(mTelephonyManager);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Inaccessible data state method");
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Invalid data state method signature");
		} catch (InvocationTargetException e) {
			Log.e(TAG, "Invalid data state method invocation target");
		}
		return result;
	}
	
	@Override
	public boolean isStateOff() {
		return !isStateOn();
	}
}
