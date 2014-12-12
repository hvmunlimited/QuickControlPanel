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

import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier;
import com.woodblockwithoutco.quickcontroldock.model.impl.actions.brightness.BrightnessObserver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.GeneralResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LaunchResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.SettingsActivity;
import com.woodblockwithoutco.quickcontroldock.ui.factory.ServiceViewFactory;
import com.woodblockwithoutco.quickcontroldock.ui.view.drag.DragViewGroup;
import com.woodblockwithoutco.quickcontroldock.util.ScreenUtils;
import com.woodblockwithoutco.quickcontroldock.util.VibrateHelper;
import com.woodblockwithoutco.quickcontroldock.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ControlService extends ViewService {

	public static final int FOREGROUND_ID = 7124;

	private static final int NOTIFICATION_TOAST_PADDING = 10;

	private static final int COLOR_WHITE = 0xFFFFFFFF;
	private int NOTIFICATION_TOAST_PADDING_PX = 0;
	
	private static final int COMPOUND_DRAWABLE_PADDING = 4;
	private int COMPOUND_DRAWABLE_PADDING_PX;
	
	private DragViewGroup mDragView;
	private View mFakeDragView;
	private ServiceViewFactory mServiceViewFactory;

	private boolean mShouldVibrate = true;
	private boolean mSwipeDetectorEnabled = true;
	private boolean mForceForeground = false;
	
	private BrightnessObserver mBrightnessObserver;
	private BroadcastReceiver mBrightnessReceiver = new BrightnessReceiver();
	private BroadcastReceiver mServiceBindReceiver = new BindReceiver();
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Intent serviceIntent = new Intent(getApplicationContext(), ControlService.class);
		serviceIntent.putExtra("openAfterLoaded", isAttachedToWindow());
		stopService(serviceIntent);
		startService(serviceIntent);
	}


	private void startForeground() {
		Resources res = getResources();
		Intent settingsIntent = new Intent(this, SettingsActivity.class);
		PendingIntent settingsPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, settingsIntent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
		builder.setSmallIcon(R.drawable.ic_notification).
		setContentTitle(res.getText(R.string.foreground_mode_title)).
		setContentText(res.getText(R.string.foreground_mode_message)).
		setContentIntent(settingsPendingIntent);
		Notification notification = null;
		notification = builder.build();
		startForeground(FOREGROUND_ID, notification);
	}

	private void stopForeground() {
		stopForeground(true);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mForceForeground = GeneralResolver.isForceForegroundEnabled(getApplicationContext());

		if(mForceForeground) {
			startForeground();
		}

		mShouldVibrate = LaunchResolver.isVibrationEnabled(getApplicationContext());
		mSwipeDetectorEnabled = LaunchResolver.isSwipeDetectorEnabled(getApplicationContext());

		mServiceViewFactory = new ServiceViewFactory(getApplicationContext());
		initServiceView(mServiceViewFactory.getServiceView(), mServiceViewFactory.getServiceViewLayoutParams());
		initSecondaryView(mServiceViewFactory.getDetectorView(), mServiceViewFactory.getDetectorViewLayoutParams());

		mDragView = (DragViewGroup)getServiceView().findViewById(R.id.panel_drag_handler);
		mDragView.initDrag();

		
		//���� ���� ������ ������������ ������ - ������� ��� � ����������� ������� ������ �� ����
		if(GeneralResolver.getPanelSpan(getApplicationContext()) != 0) {
			View v = getServiceView().findViewById(R.id.additional_drag_handler);
			mDragView.attachAdditionalDragView(v);
		}

		mFakeDragView = mDragView.findViewById(R.id.drag_handler_fake);

		if(mSwipeDetectorEnabled) {
			attachSecondaryView();

			//setting up listener for swipe detector
			getSecondaryView().findViewById(R.id.swipe_detector).setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent e) {
					
					switch(e.getAction()) {
					
					case MotionEvent.ACTION_DOWN:
						ViewService service = ControlService.getInstance();
						if(service!= null) {
							if(!isAttachedToWindow()) service.attachView();
						}
						
						if(mShouldVibrate) {
							VibrateHelper.getInstance(getApplicationContext()).vibrate();
						}
						break;
					}
					
					mFakeDragView.dispatchTouchEvent(e);
					return true;
				}
			});
		}
		
		mBrightnessObserver = new BrightnessObserver(getApplicationContext(), new Handler());
		mBrightnessObserver.startObserving();
		
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBrightnessReceiver, new IntentFilter(getPackageName()+".BRIGHTNESS_CHANGED"));
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.woodblockwithoutco.remotecontroller.MUSIC_SERVICE_BIND");
		filter.addAction("com.woodblockwithoutco.remotecontroller.MUSIC_SERVICE_UNBIND");
		filter.addAction("com.woodblockwithoutco.quickcontrolpanel.NOTIFICATION_SERVICE_BIND");
		filter.addAction("com.woodblockwithoutco.quickcontrolpanel.NOTIFICATION_SERVICE_UNBIND");
		registerReceiver(mServiceBindReceiver, filter);
		
		NOTIFICATION_TOAST_PADDING_PX = ScreenUtils.dipToPixels(getApplicationContext(), NOTIFICATION_TOAST_PADDING);
		COMPOUND_DRAWABLE_PADDING_PX = ScreenUtils.dipToPixels(getApplicationContext(), COMPOUND_DRAWABLE_PADDING);
	}

	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		if(intent != null) {
			boolean shouldOpen = intent.getBooleanExtra("openAfterLoaded", false);
			if(shouldOpen) {
				open();
			} else {
				close();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		
		unregisterReceiver(mServiceBindReceiver);

		//���� � ��� ��� �� ��������� View �� ������, �� ���������� ����, ��� �� ������������
		VisibilityEventNotifier notifier = VisibilityEventNotifier.getInstance();
		if(isAttachedToWindow()) {
			notifier.notifyHide();
		}
		notifier.unregisterAllListeners();

		if(mForceForeground) {
			stopForeground();
		}
		
		mBrightnessObserver.stopObserving();
		LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBrightnessReceiver);
	}

	public void close() {
		mDragView.close();
	}

	public void open(){
		mDragView.open();
	}
	
	private class BrightnessReceiver extends BroadcastReceiver {
		
		private final static int MAX_BRIGHTNESS = 255;
		@Override
		public void onReceive(Context context, Intent intent) {
			if(isAttachedToWindow()) {
				WindowManager.LayoutParams params = getServiceViewLayoutParams();
				params.screenBrightness = (float) Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 128) / MAX_BRIGHTNESS;
				setServiceViewLayoutParams(params);
			}
		}
		
	}
	
	@Override
	public void attachView() {
		VisibilityEventNotifier.getInstance().notifyShow();
		super.attachView();
	}
	
	@Override
	public void detachView() {
		VisibilityEventNotifier.getInstance().notifyHide();
		super.detachView();
	}
	
	private class BindReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Intent serviceIntent = new Intent(getApplicationContext(), ControlService.class);
			serviceIntent.putExtra("openAfterLoaded", isAttachedToWindow());
			stopService(serviceIntent);
			startService(serviceIntent);
		}
	}
	
	public void attachTemporaryView(Drawable icon, String appName) {
		LinearLayout l = new LinearLayout(getApplicationContext());
		l.setGravity(Gravity.CENTER_VERTICAL);
		l.setOrientation(LinearLayout.HORIZONTAL);
		l.setPadding(NOTIFICATION_TOAST_PADDING_PX, NOTIFICATION_TOAST_PADDING_PX, NOTIFICATION_TOAST_PADDING_PX, NOTIFICATION_TOAST_PADDING_PX);
		
		l.setBackgroundResource(R.drawable.notification_toast_bg);
		
		TextView tv = new TextView(getApplicationContext());
		tv.setGravity(Gravity.CENTER);
		tv.setCompoundDrawablePadding(COMPOUND_DRAWABLE_PADDING_PX);
		tv.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		
		String notificationText = getString(R.string.notification_from) + "\n" + appName;
		tv.setText(notificationText);
		tv.setTextColor(COLOR_WHITE);

		l.addView(tv);
		
		
		Toast toast = new Toast(getApplicationContext());
		toast.setView(l);
		toast.show();
	}

}
