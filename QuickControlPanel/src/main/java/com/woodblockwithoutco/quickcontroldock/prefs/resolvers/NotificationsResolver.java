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
package com.woodblockwithoutco.quickcontroldock.prefs.resolvers;

import java.util.HashSet;
import java.util.Set;

import com.woodblockwithoutco.quickcontroldock.prefs.Keys;

import android.content.Context;
import android.os.Build;

public class NotificationsResolver extends BasePrefsResolver {
	
	private static Set<String> EMPTY_SET = new HashSet<String>();

	public static boolean isNotificationsEnabled(Context context) {
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) return false;
		return getBoolean(context, Keys.Notifications.NOTIFICATIONS_ENABLE, true);
	}
	
	public static boolean isForegroundModeNotificationHidden(Context context) {
		return getBoolean(context, Keys.Notifications.NOTIFICATIONS_HIDE_QCP_FOREGROUND, true);
	}
	
	public static boolean isOngoingIgnored(Context context) {
		return getBoolean(context, Keys.Notifications.NOTIFICATIONS_IGNORE_ONGOING, false);
	}
	
	public static boolean isNotificationToastsEnabled(Context context) {
		return getBoolean(context, Keys.Notifications.NOTIFICATIONS_SHOW_TOAST, false);
	}
	
	public static Set<String> getIgnoredApplicationsSet(Context context) {
		return getStringSet(context, Keys.Notifications.NOTIFICATIONS_IGNORE_SET, EMPTY_SET);
	}
}
