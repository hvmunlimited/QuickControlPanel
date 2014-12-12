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

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.widget.RemoteViews;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationModel {

	private final int mIconId;
	private final String mPackage;
	private final PendingIntent mAppIntent;
	private final PendingIntent mRemoveIntent;
	private final int mId;
	private final String mTag;
	private final boolean mIsOngoing;
	private final boolean mIsDismissOnClick;
	private final RemoteViews mContentView;
	private final boolean mIsSoundPresent;
	private final int mPriority;

	public NotificationModel(StatusBarNotification n, boolean useBigViewIfAvailable) {
		
		mPriority = n.getNotification().priority;
		
		mIconId = n.getNotification().icon;
		
		mPackage = n.getPackageName();

		mAppIntent = n.getNotification().contentIntent;
		mRemoveIntent = n.getNotification().deleteIntent;

		mIsOngoing = n.isOngoing();
		int notificationFlags = n.getNotification().flags;
		mIsDismissOnClick = (notificationFlags & Notification.FLAG_AUTO_CANCEL) == notificationFlags;

		mId = n.getId();
		mTag = n.getTag();
		
		int defaults = n.getNotification().defaults;
		boolean defaultSound = ((defaults & Notification.DEFAULT_ALL) != 0) || ((defaults & Notification.DEFAULT_SOUND) != 0);
		mIsSoundPresent = n.getNotification().sound != null || defaultSound;

		RemoteViews bigContent = n.getNotification().bigContentView;
		if(bigContent != null && useBigViewIfAvailable) {
			mContentView = bigContent;
		} else {
			mContentView = n.getNotification().contentView;
		}

	}
	
	public boolean isSoundPresent() {
		return mIsSoundPresent;
	}

	public RemoteViews getContentView() {
		return mContentView;
	}

	public PendingIntent getClickIntent() {
		return mAppIntent;
	}

	public PendingIntent getRemoveIntent() {
		return mRemoveIntent;
	}

	public int getId() {
		return mId;
	}

	public String getTag() {
		return mTag;
	}

	public boolean isOngoing() {
		return mIsOngoing;
	}

	public boolean isDismissOnClick() {
		return mIsDismissOnClick;
	}

	public String getPackageName() {
		return mPackage;
	}
	
	public int getIconId() {
		return mIconId;
	}
	
	public int getPriority() {
		return mPriority;
	}
	
	
	public String flattenToString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotificationModel=[");
		builder.append("Package=").append(mPackage).append(";");
		builder.append("Id=").append(mId).append(";");
		builder.append("Tag=").append(mTag);
		builder.append("]");
		return builder.toString();
	}


}
