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

import java.util.ArrayList;
import java.util.List;

import com.woodblockwithoutco.quickcontroldock.global.holder.ConstantHolder;
import com.woodblockwithoutco.quickcontroldock.model.PanelType;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.GeneralResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.InfoResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LockscreenResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.MusicResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.NotificationsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.PanelsOrderResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ShortcutsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LaunchResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.SwipeDetectorPrefsFragment;
import com.woodblockwithoutco.quickcontroldock.resource.Res;
import com.woodblockwithoutco.quickcontroldock.ui.view.drag.DragViewGroup;
import com.woodblockwithoutco.quickcontroldock.ui.view.pager.LandscapePanelsAdapter;
import com.woodblockwithoutco.quickcontroldock.util.PressImageView;
import com.woodblockwithoutco.quickcontroldock.util.PressImageView.OnPressedStateChangeListener;
import com.woodblockwithoutco.quickcontroldock.util.ScreenUtils;
import com.woodblockwithoutco.quickcontroldock.R;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceViewFactory {

	private static final int VIEW_PAGER_OFFSCREEN_PAGES_COUNT = 3;
	private static final String TAG = "ServiceViewFactory";
	private Context mContext;
	private boolean mOrderFallbackMode = false;

	public ServiceViewFactory(Context context) {
		mContext = context.getApplicationContext();
		Res.init(mContext);
	}

	@SuppressWarnings("deprecation")
	public View getServiceView() {

		boolean sameLayout = GeneralResolver.isSameLayoutForLandscape(mContext);
		int inflateId = 0;
		if(ScreenUtils.getScreenOrientation(mContext) == Configuration.ORIENTATION_PORTRAIT || sameLayout) {
			inflateId = R.layout.panel_layout;
		} else {
			inflateId = R.layout.panel_layout_land;
		}

		View view = LayoutInflater.from(mContext).inflate(inflateId, null, false);

		final DragViewGroup dragView = (DragViewGroup) view.findViewById(R.id.panel_drag_handler);
		dragView.setBackgroundDrawable(ColorsResolver.getBackgroundDrawable(mContext));

		if(ConstantHolder.getIsDebug()) {
			initTestVersionText(dragView);
		}


		ViewGroup viewToHide;
		if(ScreenUtils.getScreenOrientation(mContext) == Configuration.ORIENTATION_PORTRAIT || sameLayout) {
			LinearLayout panelsContainer = (LinearLayout) dragView.findViewById(R.id.panels_container);
			viewToHide = panelsContainer;
			if(ScreenUtils.getScreenOrientation(mContext) == Configuration.ORIENTATION_PORTRAIT) {
				panelsContainer.setTranslationY(GeneralResolver.getPanelsOffset(mContext));
			}

			List<String> panelsOrder = PanelsOrderResolver.getPanelsOrder(mContext);
			if(ShortcutsResolver.isShortcutsEnabled(mContext)) {
				ShortcutsViewFactory svFactory = new ShortcutsViewFactory(mContext);
				int id = getContainerIdForPanelType(panelsOrder, PanelType.SHORTCUTS.name());
				FrameLayout section = (FrameLayout)view.findViewById(id);
				adjustPanelMargins(section);
				section.addView(svFactory.getShortcutsSectionView());
			}

			if(MusicResolver.isMusicPanelEnabled(mContext)) {
				MusicViewFactory mvFactory = new MusicViewFactory(mContext);
				int id = getContainerIdForPanelType(panelsOrder, PanelType.MUSIC.name());
				FrameLayout section = (FrameLayout)view.findViewById(id);
				adjustPanelMargins(section);
				section.addView(mvFactory.getMusicView());
			}

			if(TogglesResolver.isTogglesEnabled(mContext)) {
				TogglesViewFactory tvFactory = new TogglesViewFactory(mContext);
				int id = getContainerIdForPanelType(panelsOrder, PanelType.TOGGLES.name());
				FrameLayout section = (FrameLayout)view.findViewById(id);
				adjustPanelMargins(section);
				section.addView(tvFactory.getTogglesSectionView());
			}

			if(InfoResolver.isInfoPanelEnabled(mContext)) {
				InfoViewFactory ivFactory = new InfoViewFactory(mContext);
				int id = getContainerIdForPanelType(panelsOrder, PanelType.INFO.name());
				FrameLayout section = (FrameLayout)view.findViewById(id);
				adjustPanelMargins(section);
				section.addView(ivFactory.getInfoView());
			}

			if(NotificationsResolver.isNotificationsEnabled(mContext)) {
				final ImageView fakeNotificationsButton = (ImageView) dragView.findViewById(R.id.notifications_button_fake);
				PressImageView notificationsButton = (PressImageView) dragView.findViewById(R.id.notifications_button);
				fakeNotificationsButton.setVisibility(View.VISIBLE);
				notificationsButton.setVisibility(View.VISIBLE);
				fakeNotificationsButton.setImageResource(R.drawable.ic_notification_switch);
				fakeNotificationsButton.setColorFilter(ColorsResolver.getActiveColor(mContext));
				final float SCALE = 1.5f;
				fakeNotificationsButton.setScaleX(SCALE);
				fakeNotificationsButton.setScaleY(SCALE);
				notificationsButton.setOnPressedStateChangeListener(new OnPressedStateChangeListener() {
					private final int BG_COLOR = ColorsResolver.getPressedColor(mContext);
					@Override
					public void onPressedStateChange(ImageView v, boolean pressed) {
						if(pressed) {
							fakeNotificationsButton.setBackgroundColor(BG_COLOR);
						} else {
							fakeNotificationsButton.setBackgroundColor(0x00000000);
						}
					}
				});

				NotificationViewFactory nvFactory = new NotificationViewFactory(mContext);
				ViewGroup notificationsView = nvFactory.getNotificationsView();
				notificationsView.setAlpha(0.0f);
				notificationsView.setVisibility(View.INVISIBLE);
				FrameLayout notificationsContainer = (FrameLayout) dragView.findViewById(R.id.notifications_section);
				notificationsContainer.addView(notificationsView);
				notificationsButton.setOnClickListener(new NotificationButtonClickListener(viewToHide, notificationsView));
			}

		} else {
			
			ViewPager pager = (ViewPager) view.findViewById(R.id.landscape_pager);
			viewToHide = pager;
			List<View> views = new ArrayList<View>();

			if(NotificationsResolver.isNotificationsEnabled(mContext)) {
				NotificationViewFactory nvFactory = new NotificationViewFactory(mContext);
				ViewGroup notificationsView = nvFactory.getNotificationsView();
				views.add(notificationsView);
			}

			List<String> panelsOrder = PanelsOrderResolver.getPanelsOrder(mContext);
			for(String t : panelsOrder) {
				PanelType type = PanelType.valueOf(t);
				View v = null;
				FrameLayout container = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.panel_section_land, null, false);
				switch(type) {
				case INFO:
					//��� ����� ��������� � ��������� ������
					if(InfoResolver.isInfoPanelEnabled(mContext)) {
						InfoViewFactory ivFactory = new InfoViewFactory(mContext);
						FrameLayout fourthSection = (FrameLayout)view.findViewById(R.id.fourth_section);
						fourthSection.addView(ivFactory.getInfoView());
					}
					break;
				case MUSIC:
					if(MusicResolver.isMusicPanelEnabled(mContext)) {
						MusicViewFactory mvFactory = new MusicViewFactory(mContext);
						v = mvFactory.getMusicView();
					}
					break;
				case SHORTCUTS:
					if(ShortcutsResolver.isShortcutsEnabled(mContext)) {
						ShortcutsViewFactory svFactory = new ShortcutsViewFactory(mContext);
						v = svFactory.getShortcutsSectionView();
					}
					break;
				case TOGGLES:
					if(TogglesResolver.isTogglesEnabled(mContext)) {
						TogglesViewFactory tvFactory = new TogglesViewFactory(mContext);
						v = tvFactory.getTogglesSectionView();
					}
					break;
				}

				if(v != null) {
					views.add(v);
					container.addView(v);
				}
			}

			LandscapePanelsAdapter adapter = new LandscapePanelsAdapter(views);
			pager.setAdapter(adapter);
			pager.setOverScrollMode(View.OVER_SCROLL_NEVER);
			pager.setOffscreenPageLimit(VIEW_PAGER_OFFSCREEN_PAGES_COUNT);
			pager.setPageMargin(mContext.getResources().getDimensionPixelSize(R.dimen.pager_margin));
		}

		return view;
	}

	private void initTextView(TextView tv) {
		tv.setTextColor(ColorsResolver.getTextColor(mContext));
		tv.setVisibility(View.VISIBLE);
	}


	private void initTestVersionText(ViewGroup container) {
		TextView tv = (TextView) container.findViewById(R.id.test_version_text);
		initTextView(tv);
	}

	private void adjustPanelMargins(FrameLayout container) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) container.getLayoutParams();
		params.topMargin = GeneralResolver.getPanelsMargin(mContext);
		params.bottomMargin = GeneralResolver.getPanelsMargin(mContext);
		container.setLayoutParams(params);
	}


	public WindowManager.LayoutParams getServiceViewLayoutParams() {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		params.dimAmount = GeneralResolver.getDimAmount(mContext);
		params.format = PixelFormat.TRANSLUCENT;
		params.gravity = Gravity.BOTTOM;
		params.windowAnimations = android.R.style.Animation_Toast;
		int type = 0;
		if(LockscreenResolver.isEnabledOnLockscreen(mContext)) {
			type = ConstantHolder.getLockscreenType();
		} else {
			type = ConstantHolder.getNormalType();
		}
		params.type = type;
		params.flags = ConstantHolder.getPanelViewFlags();
		return params;
	}

	public View getDetectorView() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.detector_layout, null, false);
		if(SwipeDetectorPrefsFragment.isActive) {
			view.findViewById(R.id.swipe_detector).setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_blue_bright));
		} else {
			boolean isDebugColor = GeneralResolver.isSwipeDetectorDebugVisible(mContext);
			int colorId = isDebugColor ? R.color.detector_debug_color : R.color.detector_color;
			view.findViewById(R.id.swipe_detector).setBackgroundColor(mContext.getResources().getColor(colorId));
		}
		return view;
	}

	public WindowManager.LayoutParams getDetectorViewLayoutParams() {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.width = (int) LaunchResolver.getSwipeDetectorSize2(mContext);
		params.height = (int) LaunchResolver.getSwipeDetectorSize1(mContext);
		params.dimAmount = 0.3f;
		params.format = PixelFormat.TRANSLUCENT;
		params.gravity = LaunchResolver.getSwipeDetectorAlignment(mContext);
		params.windowAnimations = android.R.style.Animation_Toast;
		int type = 0;
		if(LockscreenResolver.isEnabledOnLockscreen(mContext)) {
			type = ConstantHolder.getLockscreenType();
		} else {
			type = ConstantHolder.getNormalType();
		}
		params.type = type;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		if(LaunchResolver.getSwipeDetectorAlignment(mContext) == Gravity.BOTTOM) {
			params.x = LaunchResolver.getSwipeDetectorOffset(mContext);
		} else {
			params.y = LaunchResolver.getSwipeDetectorOffset(mContext);
		}

		//if we have to show it on lockscreen
		if(LockscreenResolver.isEnabledOnLockscreen(mContext)) {
			params.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
		}

		return params;
	}


	private int getContainerIdForPanelType(List<String> panelsOrder, String type) {
		int index = panelsOrder.indexOf(type);
		if(index == -1) {
			mOrderFallbackMode = true;
			Log.e(TAG, "Switching to order fallback mode");
		}
		if(mOrderFallbackMode) {
			PanelType ptype = PanelType.valueOf(type);
			switch(ptype) {
			case INFO:
				return R.id.fourth_section;
			case MUSIC:
				return R.id.second_section;
			case SHORTCUTS:
				return R.id.first_section;
			case TOGGLES:
				return R.id.third_section;
			}
		}
		switch(index) {
		case 0:
			return R.id.first_section;
		case 1:
			return R.id.second_section;
		case 2:
			return R.id.third_section;
		case 3:
			return R.id.fourth_section;
		}
		return 0;
	}

	private static class NotificationButtonClickListener implements OnClickListener {

		private ViewGroup mPanelsView;
		private ViewGroup mNotificationsView;

		private ValueAnimator.AnimatorListener NOTIFICATIONS_SHOW_LISTENER = new ValueAnimator.AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
				mNotificationsView.setVisibility(View.VISIBLE);
			}
		};

		private ValueAnimator.AnimatorListener PANELS_SHOW_LISTENER = new ValueAnimator.AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
				mPanelsView.setVisibility(View.VISIBLE);
			}
		};

		private ValueAnimator.AnimatorListener NOTIFICATIONS_HIDE_LISTENER = new ValueAnimator.AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mNotificationsView.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
		};

		private ValueAnimator.AnimatorListener PANELS_HIDE_LISTENER = new ValueAnimator.AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mPanelsView.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
		};

		public NotificationButtonClickListener(ViewGroup panelsView, ViewGroup notificationsView) {
			mPanelsView = panelsView;
			mNotificationsView = notificationsView;
		}

		@Override
		public void onClick(View v) {
			if(mPanelsView.getAlpha() == 1.0f) {
				mPanelsView.animate().alpha(0.0f).setListener(PANELS_HIDE_LISTENER).start();
				mNotificationsView.animate().alpha(1.0f).setListener(NOTIFICATIONS_SHOW_LISTENER).start();
			} else {
				mPanelsView.animate().alpha(1.0f).setListener(PANELS_SHOW_LISTENER).start();
				mNotificationsView.animate().alpha(0.0f).setListener(NOTIFICATIONS_HIDE_LISTENER).start();
			}
		}
	}


}
