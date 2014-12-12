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

import java.util.List;

import com.woodblockwithoutco.quickcontroldock.model.buttons.ButtonType;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.AirplaneButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.AutoBrightnessButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.AutoRotateButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.BluetoothButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.DataButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.FlashButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.GpsButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.LockNowButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.ScreenTimeoutButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.SettingsButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.SoundButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.SyncButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.WifiApButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.WifiButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.seekbar.BrightnessSeekBar;
import com.woodblockwithoutco.quickcontroldock.model.impl.seekbar.RingerSeekBar;
import com.woodblockwithoutco.quickcontroldock.model.impl.seekbar.SoundSeekBar;
import com.woodblockwithoutco.quickcontroldock.model.seekbar.SeekbarType;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class TogglesViewFactory {
	private Context mContext;

	public TogglesViewFactory(Context context) {
		mContext = context;
	}

	@SuppressWarnings("deprecation")
	public LinearLayout getTogglesSectionView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		LinearLayout result = new LinearLayout(mContext);
		result.setGravity(Gravity.CENTER);
		result.setOrientation(LinearLayout.VERTICAL);
		result.setLayoutParams(params);

		if(TogglesResolver.isSeekbarsOnTop(mContext)) {
			addSeekbars(result);
			if(TogglesResolver.getButtonsFlags(mContext) != 0) {
				result.addView(getButtonsScrollView());
			}
		} else {
			if(TogglesResolver.getButtonsFlags(mContext) != 0) {
				result.addView(getButtonsScrollView());
			}
			addSeekbars(result);
		}
		
		LayerDrawable bgDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.section_bg);
		GradientDrawable mainDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_main);
		GradientDrawable shadowDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_shadow);
		mainDrawable.setColor(ColorsResolver.getSectionMainBackgroundColor(mContext));
		shadowDrawable.setColor(ColorsResolver.getSectionShadowBackgroundColor(mContext));

		result.setBackgroundDrawable(bgDrawable);

		return result;
	}

	public void addSeekbars(ViewGroup container) {
		List<SeekbarType> seekbarsList = TogglesResolver.getSeekbarOrderList(mContext);
		for(SeekbarType type : seekbarsList) {
			if(TogglesResolver.isSeekbarUsed(mContext, type)) {
				View v = getSeekbarViewForType(type);
				container.addView(v);
			}
		}
	}

	private View getSeekbarViewForType(SeekbarType type) {
		switch(type) {
		case BRIGHTNESS:
			return BrightnessSeekBar.getSeekBarContainer(mContext);
		case MEDIA_VOLUME:
			return SoundSeekBar.getSeekBarContainer(mContext);
		case RINGER_VOLUME:
			return RingerSeekBar.getSeekBarContainer(mContext);
		}
		return null;
	}


	public HorizontalScrollView getButtonsScrollView() {
		HorizontalScrollView scrollView = (HorizontalScrollView) LayoutInflater.from(mContext).inflate(R.layout.buttons_container, null, false);
		RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		scrollParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		scrollView.setLayoutParams(scrollParams);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setVerticalScrollBarEnabled(false);



		LinearLayout container = (LinearLayout) scrollView.findViewById(R.id.toggles_container);
		FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		containerParams.gravity = Gravity.START;
		container.setLayoutParams(containerParams);

		int buttonSize = TogglesResolver.getButtonSize(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(buttonSize, buttonSize);

		int marginPx = TogglesResolver.getButtonDistance(mContext) / 2;
		params.setMargins(marginPx, 0, marginPx, 0);

		List<ButtonType> buttons = TogglesResolver.getButtonsOrderList(mContext);
		for(ButtonType type : buttons) {
			checkAndAddButton(type, params, container);
		}

		int padding = mContext.getResources().getDimensionPixelSize(R.dimen.section_toggles_padding_vertical);
		scrollView.setPadding(0, padding, 0, padding);

		return scrollView;
	}

	private void checkAndAddButton(ButtonType type, LinearLayout.LayoutParams params, ViewGroup container) {
		switch(type) {
		case AIRPLANE:
			if(TogglesResolver.isAirplaneButtonEnabled(mContext)) {
				AirplaneButton button = new AirplaneButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case BLUETOOTH:
			if(TogglesResolver.isBluetoothButtonEnabled(mContext)) {
				BluetoothButton button = new BluetoothButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case DATA:
			if(TogglesResolver.isDataButtonEnabled(mContext)) {
				DataButton button = new DataButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case FLASHLIGHT:
			if(TogglesResolver.isFlashButtonEnabled(mContext)) {
				FlashButton button = new FlashButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case GPS:
			if(TogglesResolver.isGpsButtonEnabled(mContext)) {
				GpsButton button = new GpsButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case LOCK_NOW:
			if(TogglesResolver.isLockNowButtonEnabled(mContext)) {
				LockNowButton button = new LockNowButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case ROTATE:
			if(TogglesResolver.isRotateButtonEnabled(mContext)) {
				AutoRotateButton button = new AutoRotateButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case SETTINGS:
			if(TogglesResolver.isSettingsButtonEnabled(mContext)) {
				SettingsButton button = new SettingsButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case SOUND:
			if(TogglesResolver.isSoundButtonEnabled(mContext)) {
				SoundButton button = new SoundButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case SYNC:
			if(TogglesResolver.isSyncButtonEnabled(mContext)) {
				SyncButton button = new SyncButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case WIFI:
			if(TogglesResolver.isWifiButtonEnabled(mContext)) {
				WifiButton button = new WifiButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case WIFI_AP:
			if(TogglesResolver.isWifiApButtonEnabled(mContext)) {
				WifiApButton button = new WifiApButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case AUTO_BRIGHTNESS:
			if(TogglesResolver.isAutoBrightnessButtonEnabled(mContext)) {
				AutoBrightnessButton button = new AutoBrightnessButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
			break;
		case SCREEN_TIMEOUT:
			if(TogglesResolver.isScreenTimeoutButtonEnabled(mContext)) {
				ScreenTimeoutButton button = new ScreenTimeoutButton(mContext);
				button.setLayoutParams(params);
				container.addView(button);
			}
		}
	}
}
