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
package com.woodblockwithoutco.quickcontroldock.util;
import android.content.Context;
import android.content.ComponentName;
import java.util.List;
import java.util.ArrayList;

import com.woodblockwithoutco.quickcontroldock.R;

import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ActivityInfo;
import android.app.PendingIntent;

public class MediaReceiverResolver {


	public static String[] getMediaComponents(Context context) {

		//intent for <intent-filter>
		final Intent intent=new Intent(Intent.ACTION_MEDIA_BUTTON);
		List<String> list=new ArrayList<String>();

		//if we are calling this method from getAppNames(), it will add "broadcast to a list, otherwise not
		list.add("broadcast");

		//just a ordinary magic to get all the receivers for selected Intent.
		PackageManager pm=context.getPackageManager();
		final List<ResolveInfo> activities = pm.queryBroadcastReceivers(intent, 0);
		for (ResolveInfo resolveInfo : activities) {
			ActivityInfo activityInfo = resolveInfo.activityInfo;
			if (activityInfo != null) list.add(activityInfo.packageName+"/"+activityInfo.name);
		}
		return list.toArray(new String[0]);
	}

	private static String[] getMediaComponents(Context context, boolean forInternalUse) {

		//intent for <intent-filter>
		final Intent intent=new Intent(Intent.ACTION_MEDIA_BUTTON);
		List<String> list=new ArrayList<String>();

		//if we are calling this method from getAppNames(), it will add "broadcast to a list, otherwise not
		if(!forInternalUse) list.add("broadcast");

		//just a ordinary magic to get all the receivers for selected Intent.
		PackageManager pm=context.getPackageManager();
		final List<ResolveInfo> activities = pm.queryBroadcastReceivers(intent, 0);
		for (ResolveInfo resolveInfo : activities) {
			ActivityInfo activityInfo = resolveInfo.activityInfo;
			if (activityInfo != null) list.add(activityInfo.packageName+"/"+activityInfo.name);
		}
		return list.toArray(new String[0]);
	}

	public static PendingIntent getMediaPendingIntent(Context context, String name) {

		String[] temp=name.split("/");

		//creating description of receiver component
		ComponentName myEventReceiver = new ComponentName(temp[0], temp[1]);

		Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		mediaButtonIntent.setComponent(myEventReceiver);
		return PendingIntent.getBroadcast(context, 0, mediaButtonIntent, 0);
	}

	private static String getAppName(Context context, String name) {
		String[] temp=name.split("/");
		PackageManager pm=context.getPackageManager();
		try {
			return (String)pm.getApplicationLabel(pm.getApplicationInfo(temp[0], 0));
		}
		catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}

	public static String[] getAppNames(Context context) {
		List<String> list=new ArrayList<String>();

		//adding "Broadcast" option
		list.add(context.getResources().getString(R.string.broadcast_to_all));

		//'true' means that we are calling this method for internal use, so we don't need to add "broadcast" item
		String[] comps=MediaReceiverResolver.getMediaComponents(context, true);

		for(String s:comps) {
			list.add(MediaReceiverResolver.getAppName(context, s.split("/")[0]));
		}	
		return list.toArray(new String[0]);
	}
}
