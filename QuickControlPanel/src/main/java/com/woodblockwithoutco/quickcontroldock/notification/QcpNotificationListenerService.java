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
package com.woodblockwithoutco.quickcontroldock.notification;


import java.util.Set;

import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.NotificationsResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;

import android.content.Intent;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class QcpNotificationListenerService extends NotificationListenerService {

	public interface OnNotificationListUpdateListener {
		public void onNotificationPosted(StatusBarNotification n, boolean shouldNotify);
		public void onNotificationRemoved(StatusBarNotification n);
	}

	private static final String TAG = "QcpNotificationListenerService";

	private Handler mHandler = new Handler();
	private static NotificationListenerService sInstance;
	private OnNotificationListUpdateListener mListener;

	private boolean mIgnoreQcpForegroundNotification;
	private boolean mShowNotificationToast;
	private Set<String> mIgnoredSet;


	private static final String NOTIFICATION_SERVICE_BIND_ACTION = "com.woodblockwithoutco.quickcontrolpanel.NOTIFICATION_SERVICE_BIND";
	private static final String NOTIFICATION_SERVICE_UNBIND_ACTION = "com.woodblockwithoutco.quickcontrolpanel.NOTIFICATION_SERVICE_UNBIND";
	private static final Intent NOTIFICATION_SERVICE_BIND_INTENT = new Intent(NOTIFICATION_SERVICE_BIND_ACTION);
	private static final Intent NOTIFICATION_SERVICE_UNBIND_INTENT = new Intent(NOTIFICATION_SERVICE_UNBIND_ACTION);


	@Override
	public void onCreate() {
		super.onCreate();
		sendBroadcast(NOTIFICATION_SERVICE_BIND_INTENT);
		sInstance = this;
		updateSettings();
	}

	public void updateSettings() {
		mIgnoreQcpForegroundNotification = NotificationsResolver.isForegroundModeNotificationHidden(getApplicationContext());
		mShowNotificationToast = NotificationsResolver.isNotificationToastsEnabled(getApplicationContext());
		mIgnoredSet = NotificationsResolver.getIgnoredApplicationsSet(getApplicationContext());
		
		if(mListener != null) {
			checkAndPostAllAvailableNotifications();
		}
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		sendBroadcast(NOTIFICATION_SERVICE_UNBIND_INTENT);
		sInstance = null;
	}

	public void setListener(OnNotificationListUpdateListener l) {
		mListener = l;
		try {
			if(l != null) {
				checkAndPostAllAvailableNotifications();
			}
		} catch(Exception e) {
			Log.e(TAG, "Error in notification listener service");
		}

	}

	public static NotificationListenerService getInstance() {
		return sInstance;
	}

	@Override
	public void onNotificationPosted(StatusBarNotification notification) {
		final StatusBarNotification n = notification;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if(mListener != null) {
					checkNotificationAndPost(n);
				}
			}
		});
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification notification) {
		final StatusBarNotification n = notification;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if(mListener != null) {
					mListener.onNotificationRemoved(n);
				}
			}
		});
	}


	public void setIgnoreForegroundModeNotification(boolean ignore) {
		mIgnoreQcpForegroundNotification = ignore;
	}

	private boolean checkIsQcpForegroundNotification(StatusBarNotification n) {
		if(n.getPackageName().equals(getPackageName())) {
			if(n.getId() == ControlService.FOREGROUND_ID) {
				return true;
			}
		}
		return false;
	}
	
	private void checkNotificationAndPost(StatusBarNotification n) {
		boolean shouldPost = true;
		if(mIgnoredSet != null) {
			if(mIgnoredSet.contains(n.getPackageName())) {
				shouldPost = false;
			}
		}
		
		if(mIgnoreQcpForegroundNotification && checkIsQcpForegroundNotification(n)) {
			shouldPost = false;
		}
		
		if(shouldPost) mListener.onNotificationPosted(n, mShowNotificationToast);
	}
	
	private void checkAndPostAllAvailableNotifications() {
		try {
			StatusBarNotification[] notifications = getActiveNotifications();
			if(mListener != null) {
				for(StatusBarNotification n : notifications) {
					checkNotificationAndPost(n);
				}
			}
		} catch(Exception e) {
			Log.e(TAG, "Error in notification listener service");
		}
	}


}
