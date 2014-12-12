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

import java.util.List;


import com.woodblockwithoutco.quickcontroldock.R;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.ui.view.LockableScrollView;
import com.woodblockwithoutco.quickcontroldock.util.OnClickListenerExtractor;
import com.woodblockwithoutco.quickcontroldock.util.ScreenUtils;
import com.woodblockwithoutco.quickcontroldock.util.ViewHierarchyHelper;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RemoteViews;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationViewProvider {

	private static final float MARGIN_DP = 4;
	private static final String TAG = "NotificationViewProvider";
	private final int MARGIN_PX;

	private Context mContext;
	private Drawable DEFAULT_NOTIFICATION_BG;

	private OnClickListenerExtractor mOnClickListenerExtractor;
	private LockableScrollView mLockableScrollView;

	public NotificationViewProvider(Context context, LockableScrollView root) {
		try {
			mOnClickListenerExtractor = new OnClickListenerExtractor();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
		
		mLockableScrollView = root;

		MARGIN_PX = ScreenUtils.dipToPixels(context, MARGIN_DP);
		mContext = context;
		DEFAULT_NOTIFICATION_BG = mContext.getResources().getDrawable(R.drawable.notification_bg);
	}

	public void reapplyNotificationView(NotificationModel model, View v) {
		model.getContentView().reapply(mContext, v);
	}

	public View getNotificationView(NotificationModel model) {

		final LinearLayout root = new LinearLayout(mContext);

		NotificationContainer container = new NotificationContainer(mContext);
		
		root.setTag(model.getPackageName() + "/" + model.getId());
		RemoteViews contentView = model.getContentView();
		
		if(contentView == null) {
			Log.e(TAG, "Detected bad notification: " + model.flattenToString());
			return null;
		}
		
		int layoutId = contentView.getLayoutId();
		container.setTag(layoutId);

		GestureListener gestureListener = new GestureListener(container, model, mLockableScrollView);

		View notificationView = model.getContentView().apply(mContext, container);
		List<View> hierarchy = ViewHierarchyHelper.flattenViewsHierarchy(notificationView);
		for(View v : hierarchy) {
			OnClickListener l = mOnClickListenerExtractor.getOnClickListener(v);
			if(l != null) {
				v.setOnClickListener(null);
				GestureDelegator delegator = new GestureDelegator(v, l, gestureListener);
				GestureTouchListener touchListener = new GestureTouchListener(mContext, delegator);
				v.setOnTouchListener(touchListener);
			}
		}

		container.setBackground(DEFAULT_NOTIFICATION_BG);

		notificationView.setTag(model.getContentView().getLayoutId());
		container.addView(notificationView);
		container.setOnGestureListener(gestureListener);
		ViewGroup.LayoutParams params = notificationView.getLayoutParams();
		root.addView(container);

		ViewGroup.MarginLayoutParams containerParams = new LinearLayout.LayoutParams(params);
		containerParams.topMargin = MARGIN_PX;
		container.setLayoutParams(containerParams);

		return root;
	}


	@SuppressLint("ClickableViewAccessibility")
	private static class GestureTouchListener implements OnTouchListener {

		private GestureDetector mDetector;
		private OnGestureListener2 mListener;

		GestureTouchListener(Context context, OnGestureListener2 l) {
			mDetector = new GestureDetector(context, l);
			mListener = l;

		}

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			if(e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
				mListener.onTouchEnd();
			}

			return mDetector.onTouchEvent(e);
		}

	}

	private static class GestureDelegator implements OnGestureListener2 {

		private final OnGestureListener2 mDelegate;
		private final OnClickListener mListener;
		private View mView;

		GestureDelegator(View v, OnClickListener l, OnGestureListener2 delegate) {
			mDelegate = delegate;
			mListener = l;
			mView = v;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			mView.setPressed(true);
			return mDelegate.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return mDelegate.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			mDelegate.onLongPress(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			mView.setPressed(false);
			return mDelegate.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			mListener.onClick(mView);
			return true;
		}

		@Override
		public void onTouchEnd() {
			mDelegate.onTouchEnd();
			mView.setPressed(false);
		}


	}

	private class GestureListener implements OnGestureListener2 {

		private static final float THRESHOLD_NO_DISMISS = 75;
		private static final float NO_DISMISS_DECREMENT = -0.005f;
		private static final boolean ALPHA_ENABLED = false;
		private static final String TAG = "NotificationGestureListener";


		private final NotificationContainer mView;
		private final PendingIntent mRemoveIntent;
		private final PendingIntent mIntent;
		private final boolean mDismissOnClick;
		private final boolean mDismissable;
		private final boolean mOngoing;
		private final int mId;
		private final String mTag;
		private final String mPkg;
		private final int WIDTH;
		private float mAnimateDismissTo = 0;
		private LockableScrollView mScrollView;
		private final TimeInterpolator INTERPOLATOR = new LinearInterpolator();
		private static final long DURATION = 125;

		private Animator.AnimatorListener ANIM_LISTENER = new Animator.AnimatorListener() {

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				removeNotification(mPkg, mTag, mId);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}

		};


		GestureListener(NotificationContainer view, NotificationModel model, LockableScrollView root) {
			mScrollView = root;
			WIDTH = ScreenUtils.getWidthPx(mContext);
			mView = view;
			mIntent = model.getClickIntent();
			mRemoveIntent = model.getRemoveIntent();
			mPkg = model.getPackageName();
			mId = model.getId();
			mTag = model.getTag();
			mDismissOnClick = model.isDismissOnClick();
			mDismissable = !model.isOngoing();
			mOngoing = model.isOngoing();
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			mView.setPressed(false);
			PopupMenu menu = new PopupMenu(mContext, mView);
			menu.inflate(R.menu.notification_menu);
			menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					if(item.getItemId() == R.id.action_info) {
						ControlService service = (ControlService) ControlService.getInstance();
						if(service != null && service.isAttachedToWindow() && ControlService.isRunning()) {
							service.close();
						}
						startApplicationDetailsActivity(mPkg);
						return true;
					}
					return false;
				}

			});
			menu.show();
		}

		private void startApplicationDetailsActivity(String packageName) {
			Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null));
			intent.setComponent(intent.resolveActivity(mContext.getPackageManager()));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			mScrollView.setLocked(true);
			
			float translation  = e2.getRawX() - e1.getRawX();

			if(Math.abs(translation) > WIDTH / 4 && mDismissable) {
				mView.setShouldReturnToOriginalPosition(false);
				mAnimateDismissTo = WIDTH * Math.signum(translation);
			} else {
				mView.setShouldReturnToOriginalPosition(true);
			}


			if(!mDismissable) {
				if(translation > 0) {
					translation = (float) (THRESHOLD_NO_DISMISS * (1 - Math.exp(NO_DISMISS_DECREMENT * Math.abs(translation))));
				} else {
					translation = -(float) (THRESHOLD_NO_DISMISS * (1 - Math.exp(NO_DISMISS_DECREMENT * Math.abs(translation))));
				}
			}
			mView.setTranslationX(translation);
			if(ALPHA_ENABLED) {
				float alpha = Math.max(0, 1 - Math.abs(translation) / WIDTH);
				if(mDismissable) {
					mView.setAlpha(alpha);
					mView.setAlpha(alpha);
				}
			}

			if(mView.isPressed()) mView.setPressed(false);

			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			mView.setPressed(true);
		}



		@Override
		public void onTouchEnd() {
			mScrollView.setLocked(false);
			mView.setPressed(false);
			if(mView.shouldReturnToOriginalPosition()) {
				if(ALPHA_ENABLED) {
					mView.animate().x(0).setInterpolator(INTERPOLATOR).setDuration(DURATION).alpha(1.0f).start();
				} else {
					mView.animate().x(0).setInterpolator(INTERPOLATOR).setDuration(DURATION).start();
				}
			} else {
				if(ALPHA_ENABLED) {
					mView.animate().x(mAnimateDismissTo).alpha(0).setInterpolator(INTERPOLATOR).setDuration(DURATION).setListener(ANIM_LISTENER).start();
				} else {
					mView.animate().x(mAnimateDismissTo).setInterpolator(INTERPOLATOR).setDuration(DURATION).setListener(ANIM_LISTENER).start();
				}

			}
		}

		@SuppressWarnings("deprecation")
		private void removeNotification(String pkg, String tag, int id) {
			NotificationListenerService service = QcpNotificationListenerService.getInstance();
			if(service != null) {
				service.cancelNotification(pkg, tag, id);
			}
			if(mRemoveIntent != null) {
				try {
					mRemoveIntent.send();
				} catch (CanceledException e) {
					Log.e(TAG, "CanceledException while sending remove intent");
				}
			}
		}



		@Override
		public boolean onSingleTapUp(MotionEvent e) {

			if(mIntent != null) {
				try {
					mIntent.send();
					ControlService service = (ControlService) ControlService.getInstance();
					if(service != null && service.isAttachedToWindow() && ControlService.isRunning()) {
						service.close();
					}
					
					if(mDismissOnClick && mDismissable && !mOngoing) {
						removeNotification(mPkg, mTag, mId);
					}
				} catch (CanceledException ex) {
				}
			}
			return false;
		}
	}
}

