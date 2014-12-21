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
package com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight;

import com.woodblockwithoutco.quickcontroldock.global.holder.ConstantHolder;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.managers.Camera2FlashlightManipulator;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.managers.DefaultFlashlightManipulator;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.managers.FlashlightManipulator;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.managers.HTCFlashlightManipulator;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.managers.NoSurfaceViewFlashlightManipulator;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;
import com.woodblockwithoutco.quickcontroldock.R;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FlashlightService extends Service {

	private static final int NOTIFICATION_ID = 14;

	private FlashlightManipulator mFlashManipulator;
	private LinearLayout mContainer;
    private View mDummyView;

	private BroadcastReceiver mScreenOffReceiver;
	private BroadcastReceiver mStopServiceReceiver;

	private Handler mHandler;
	private Runnable mActor;

	private WindowManager mWindowManager;
	private ImageButton mTurnOffButton;

	private static boolean sIsRunning;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent command, int flags, int startId) {
		if(command != null) {
			if(command.getAction().equals(getPackageName()+".START_FLASH")) {
				mFlashManipulator.turnFlashlightOn();
				LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(getPackageName()+".START_FLASH_BUTTON"));
			} else if(command.getAction().equals(getPackageName()+".STOP_FLASH")) {
				mFlashManipulator.turnFlashlightOff();
				stopSelf();
			}
		}
		return START_NOT_STICKY;
	}

	@Override
	public void onCreate() {

		sIsRunning = true;

		NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
		builder.setContentTitle(getResources().getString(R.string.persistent_flash_active));
		builder.setContentText(getResources().getString(R.string.persistent_flash_active_tap));
		builder.setSmallIcon(R.drawable.ic_persistent_flash);

		builder.setContentIntent(PendingIntent.getBroadcast(this, -1, new Intent(getPackageName()+".STOP_FLASH"), 0));
		startForeground(NOTIFICATION_ID, builder.build());

		mContainer = new LinearLayout(getApplicationContext());
		LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mContainer.setLayoutParams(lparams);
		mContainer.setOrientation(LinearLayout.VERTICAL);

		mTurnOffButton = new ImageButton(getApplicationContext());
		mTurnOffButton.setImageResource(R.drawable.ic_persistent_flash);
		mTurnOffButton.setBackgroundColor(0x80000000);
		mTurnOffButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopSelf();
			}
		});

		mWindowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		WindowManager.LayoutParams params=new WindowManager.LayoutParams();
		params.type = ConstantHolder.getLockscreenType();
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		params.x = 0;
		params.y = 0;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = params.width;
		params.gravity = Gravity.CENTER;
		params.format = PixelFormat.TRANSLUCENT;

		mContainer.addView(mTurnOffButton);

		String method = TogglesResolver.getFlashlightType(getApplicationContext());

		if(method.equals("default")) {
            SurfaceView dummySurfaceView = new SurfaceView(getApplicationContext());
            dummySurfaceView.setAlpha(0.01f);
            ViewGroup.LayoutParams sparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            dummySurfaceView.setLayoutParams(sparams);
            mFlashManipulator = new DefaultFlashlightManipulator(dummySurfaceView);
            mContainer.addView(dummySurfaceView);
            mDummyView = dummySurfaceView;
		} else if(method.equals("htc")) {
			mFlashManipulator = new HTCFlashlightManipulator();
		} else if(method.equals("nosurfaceview")) {
			mFlashManipulator = new NoSurfaceViewFlashlightManipulator();
		} else if(method.equals("camera2")) {
            TextureView dummyTextureView = new TextureView(getApplicationContext());
            dummyTextureView.setAlpha(0);
            ViewGroup.LayoutParams sparams = new ViewGroup.LayoutParams(176, 144);
            dummyTextureView.setLayoutParams(sparams);
            mFlashManipulator = new Camera2FlashlightManipulator(getApplicationContext(), dummyTextureView);
            mContainer.addView(dummyTextureView);
            mDummyView = dummyTextureView;
        } else {
            SurfaceView dummySurfaceView = new SurfaceView(getApplicationContext());
			dummySurfaceView.setAlpha(0.01f);
			ViewGroup.LayoutParams sparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			dummySurfaceView.setLayoutParams(sparams);
			mFlashManipulator = new DefaultFlashlightManipulator(dummySurfaceView);
			mContainer.addView(dummySurfaceView);
            mDummyView = dummySurfaceView;
		}

		mWindowManager.addView(mContainer, params);


		mHandler = new Handler();
		mActor = new Runnable() {
			@Override
			public void run() {
				mFlashManipulator.turnFlashlightOn();
			}
		};


		mScreenOffReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mFlashManipulator.turnFlashlightOff();
				mHandler.postDelayed(mActor, 200);
			}
		};

		mStopServiceReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mFlashManipulator.turnFlashlightOff();
				stopSelf();
			}
		};
		registerReceiver(mScreenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(mStopServiceReceiver, new IntentFilter(getPackageName()+".STOP_FLASH"));
	}

	@Override
	public void onDestroy() {

		sIsRunning = false;

		mFlashManipulator.turnFlashlightOff();
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(getPackageName()+".STOP_FLASH_BUTTON"));
		if(mTurnOffButton != null) mWindowManager.removeView(mContainer);

		unregisterReceiver(mScreenOffReceiver);
		unregisterReceiver(mStopServiceReceiver);
		stopForeground(true);
		super.onDestroy();
	}

	public static boolean isRunning() {
		return sIsRunning;
	}

}
