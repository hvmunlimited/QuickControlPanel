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
package com.woodblockwithoutco.quickcontroldock.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

public abstract class ViewService extends Service {

	protected static ViewService sInstance = null;

	protected WindowManager mWindowManager;
	protected WindowManager.LayoutParams mServiceViewLayoutParams;
	protected WindowManager.LayoutParams mSecondaryViewLayoutParams;
	protected View mServiceView;
	protected View mSecondaryView;
	protected Handler mHandler;

	protected boolean mAttachedToWindow = false;
	protected boolean mAttachedToWindowSecondary = false;
	
	protected View mTempView;
	
	private Runnable mTempViewRemover = new Runnable() {
		@Override
		public void run() {
			if(mTempView != null) {
				mWindowManager.removeView(mTempView);
				mTempView = null;
			}
		}
	};
	
	

	@Override
	public void onCreate() {
		sInstance = this;
		mWindowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		mAttachedToWindow = false;
		mAttachedToWindowSecondary = false;
		
		mHandler = new Handler();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		detachView();
		detachSecondaryView();
		sInstance = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		//binding is not supported
		return null;
	}


	public static ViewService getInstance() {
		return sInstance;
	}
	

	public void attachView() {
		if(mServiceView == null || mServiceViewLayoutParams == null) throw new IllegalStateException("initServiceView() must be called first!");

		if(!mAttachedToWindow) mWindowManager.addView(mServiceView, mServiceViewLayoutParams);
		mAttachedToWindow = true;
	}
	
	public void attachSecondaryView() {
		if(mSecondaryView == null || mSecondaryViewLayoutParams == null) throw new IllegalStateException("initSecondaryView() must be called first!");
		if(!mAttachedToWindowSecondary) mWindowManager.addView(mSecondaryView, mSecondaryViewLayoutParams);
		mAttachedToWindowSecondary = true;
	}

	public void detachView() {
		if(mServiceView != null && mAttachedToWindow) mWindowManager.removeView(mServiceView);
		mAttachedToWindow = false;
	}
	

	public void detachSecondaryView() {
		if(mSecondaryView != null && mAttachedToWindowSecondary) mWindowManager.removeView(mSecondaryView);
		mAttachedToWindowSecondary = false;
	}

	public void initServiceView(View view, WindowManager.LayoutParams params) {
		mServiceView = view;
		mServiceViewLayoutParams = params;
	}

	public void initSecondaryView(View view, WindowManager.LayoutParams params) {
		mSecondaryView = view;
		mSecondaryViewLayoutParams = params;
	}

	public void setServiceView(View view) {
		mServiceView = view;
		if(mServiceView != null && mServiceViewLayoutParams != null) updateServiceView();
	}

	public void setSecondaryView(View view) {
		mSecondaryView = view;
		if(mSecondaryView != null && mSecondaryViewLayoutParams != null) updateSecondaryView();
	}

	void updateServiceView() {
		mWindowManager.updateViewLayout(mServiceView, mServiceViewLayoutParams);
	}

	void updateSecondaryView() {
		mWindowManager.updateViewLayout(mSecondaryView, mSecondaryViewLayoutParams);
	}

	public void setServiceViewLayoutParams(WindowManager.LayoutParams params) {
		mServiceViewLayoutParams = params;
		if(mServiceView != null && mServiceViewLayoutParams != null) updateServiceView();
	}
	
	public void setServiceViewLayoutParamsWithoutUpdating(WindowManager.LayoutParams params) {
		mServiceViewLayoutParams = params;
	}

	public void setSecondaryViewLayoutParams(WindowManager.LayoutParams params) {
		mSecondaryViewLayoutParams = params;
		if(mSecondaryView != null && mSecondaryViewLayoutParams != null) updateSecondaryView();
	}
	
	public void setSecondaryViewLayoutParamsWithoutUpdating(WindowManager.LayoutParams params) {
		mSecondaryViewLayoutParams = params;
	}

	public View getServiceView() {
		return mServiceView;
	}

	public View getSecondaryView() {
		return mSecondaryView;
	}

	public WindowManager.LayoutParams getServiceViewLayoutParams() {
		return mServiceViewLayoutParams;
	}

	public WindowManager.LayoutParams getSecondaryViewLayoutParams() {
		return mSecondaryViewLayoutParams;
	}

	public static boolean isRunning() {
		return sInstance != null;
	}

	public boolean isAttachedToWindow() {
		return mAttachedToWindow;
	}

	public boolean isSecondaryAttachedToWindow() {
		return mAttachedToWindowSecondary;
	}
	
	public void attachTemporaryView(View v, WindowManager.LayoutParams params, long disappearTime) {
		if(mTempView != null) {
			mWindowManager.removeView(mTempView);
			mHandler.removeCallbacks(mTempViewRemover);
		}
		mTempView = v;
		mWindowManager.addView(v, params);
		mHandler.postDelayed(mTempViewRemover, disappearTime);
	}

}
