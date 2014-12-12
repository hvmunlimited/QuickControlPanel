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
package com.woodblockwithoutco.quickcontroldock.ui.factory;


import com.woodblockwithoutco.quickcontroldock.R;
import com.woodblockwithoutco.quickcontroldock.notification.NotificationModel;
import com.woodblockwithoutco.quickcontroldock.notification.NotificationViewProvider;
import com.woodblockwithoutco.quickcontroldock.notification.QcpNotificationListenerService;
import com.woodblockwithoutco.quickcontroldock.notification.QcpNotificationListenerService.OnNotificationListUpdateListener;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.NotificationsResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.ui.view.LockableScrollView;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationViewFactory {

	private Context mContext;
	private OnNotificationListUpdateListener mNotificationsUpdateListener;
	

	public NotificationViewFactory(Context context) {
		mContext = context;
	}

	@SuppressWarnings("deprecation")
	public ViewGroup getNotificationsView() {

		LockableScrollView rootContainer = new LockableScrollView(mContext);
		ViewGroup.LayoutParams rootParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		rootContainer.setLayoutParams(rootParams);
		rootContainer.setHorizontalScrollBarEnabled(false);
		rootContainer.setVerticalScrollBarEnabled(false);
		rootContainer.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		rootContainer.setBackgroundDrawable(getBg());


		LinearLayout notificationContainer = new LinearLayout(mContext);
		LinearLayout.LayoutParams notificationsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		notificationContainer.setOrientation(LinearLayout.VERTICAL);
		notificationContainer.setLayoutParams(notificationsParams);
		notificationContainer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		LayoutTransition lt = new LayoutTransition();
		lt.enableTransitionType(LayoutTransition.APPEARING);
		lt.enableTransitionType(LayoutTransition.DISAPPEARING);
		lt.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
		lt.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
		notificationContainer.setLayoutTransition(lt);

		rootContainer.addView(notificationContainer);


		mNotificationsUpdateListener = new NotificationListener(mContext, notificationContainer, rootContainer);
		QcpNotificationListenerService service = (QcpNotificationListenerService) QcpNotificationListenerService.getInstance();
		if(service != null) {
			service.setListener(mNotificationsUpdateListener);
		} else {
			notificationContainer.setGravity(Gravity.CENTER);
			int SIZE = 16;
			TextView tv = new TextView(mContext);
			tv.setGravity(Gravity.CENTER);
			tv.setText(R.string.notification_service_not_active);
			tv.setTextColor(ColorsResolver.getTextColor(mContext));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE);

			Button btn = new Button(mContext);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);

					ControlService service = (ControlService) ControlService.getInstance();
					if(service != null && ControlService.isRunning() && service.isAttachedToWindow()) {
						service.close();
					}
				}
			});
			btn.setText(R.string.enable);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE);

			notificationContainer.addView(tv);
			notificationContainer.addView(btn);
		}

		return rootContainer;
	}

	private Drawable getBg() {
		LayerDrawable bgDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.section_bg);
		GradientDrawable mainDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_main);
		GradientDrawable shadowDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_shadow);
		mainDrawable.setColor(ColorsResolver.getSectionMainBackgroundColor(mContext));
		shadowDrawable.setColor(ColorsResolver.getSectionShadowBackgroundColor(mContext));

		return bgDrawable;
	}


	@SuppressLint("NewApi")
	private static class NotificationListener implements OnNotificationListUpdateListener {

		protected static final String TAG = "QCPNotificationListener";
		private LinearLayout mNotificationPanel;
		private Context mContext;
		private NotificationViewProvider mViewProvider;
		private Handler mHandler = new Handler();
		private PackageManager mPackageManager;
		private boolean mIsOngoingIgnored;
		

		NotificationListener(Context context, LinearLayout notificationPanel, LockableScrollView root) {
			mContext = context;
			mNotificationPanel = notificationPanel;
			mViewProvider = new NotificationViewProvider(mContext, root);
			mPackageManager = mContext.getPackageManager();
			mIsOngoingIgnored = NotificationsResolver.isOngoingIgnored(mContext);
		}


		@Override
		public void onNotificationPosted(StatusBarNotification n, boolean shouldNotify) {
			final boolean shouldNotifyUser = shouldNotify;
			final StatusBarNotification notification = n;
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					NotificationModel m = new NotificationModel(notification, true);
					
					if(mIsOngoingIgnored && m.isOngoing()) return;
					
					boolean shouldAddNew = true;
					int childCount = mNotificationPanel.getChildCount();
					int index = 0;
					String tag = m.getPackageName() + "/" + m.getId();

					while(index < childCount && shouldAddNew) {
						View v = mNotificationPanel.getChildAt(index);
						if(v != null) {
							ViewGroup g = (ViewGroup) v;
							Object o = v.getTag();
							if(o != null) {
								String viewTag = (String)o;
								if(tag.equals(viewTag)) {
									RemoteViews views = m.getContentView();
									View child = g.getChildAt(0);
									int remoteId = views.getLayoutId();
									int currentId = (Integer) child.getTag();
									if(currentId == remoteId) {
										views.reapply(mContext, v);
										shouldAddNew = false;
									} else {
										mNotificationPanel.removeViewAt(index);
									}
								}
							}
						}
						index++;
					}

					if(shouldAddNew) {
						View v = mViewProvider.getNotificationView(m);
						
						if(v == null) return;
						
						mNotificationPanel.addView(v);
						if(m.isSoundPresent() && shouldNotifyUser) {
							ControlService service = (ControlService) ControlService.getInstance();
							if(service != null) {
								Context ctx;
								try {
									ctx = mContext.createPackageContext(m.getPackageName(), Context.CONTEXT_IGNORE_SECURITY);
									String name = mPackageManager.getPackageInfo(m.getPackageName(), 0).applicationInfo.loadLabel(mPackageManager).toString();
									service.attachTemporaryView(ctx.getResources().getDrawable(m.getIconId()), name);
								} catch (NameNotFoundException e) {
									Log.e(TAG, "Failed to find given package - maybe it was uninstalled?");
								}
							}
						}
					}
				}
			});
		}

		@Override
		public void onNotificationRemoved(StatusBarNotification n) {
			int childCount = mNotificationPanel.getChildCount();
			String tag = n.getPackageName()+"/"+n.getId();
			for(int index = 0; index < childCount; index++) {
				View v = mNotificationPanel.getChildAt(index);
				if(v != null) {
					Object o = v.getTag();
					if(o != null) {
						String viewTag = (String)o;
						if(tag.equals(viewTag)) {
							mNotificationPanel.removeViewAt(index);
						}
					}
				}
			}
		}
	}

}
