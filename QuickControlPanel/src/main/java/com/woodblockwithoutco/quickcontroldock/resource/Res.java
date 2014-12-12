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
package com.woodblockwithoutco.quickcontroldock.resource;

import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ExternalResourceResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;
import com.woodblockwithoutco.quickcontroldock.resource.loader.ExternalResources;
import com.woodblockwithoutco.quickcontroldock.resource.loader.ResourceLoader;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

public class Res {

	private static final String BRIGHTNESS_AUTO_ICON_NAME = "btn_brightness_auto";
	private static final String LOCK_NOW_ICON_NAME = "btn_lock";
	private static final String WIFI_AP_ICON_NAME = "btn_wifi_ap";
	private static final String ROTATE_LOCK_ICON_NAME = "btn_rotation_off";
	private static final String ROTATE_ICON_NAME = "btn_rotation_on";
	private static final String VIBRATE_ICON_NAME = "btn_vibration_mode";
	private static final String SOUND_OFF_ICON_NAME = "btn_silent_mode";
	private static final String SOUND_ON_ICON_NAME = "btn_normal_sound_mode";
	private static final String BLUETOOTH_ICON_NAME = "btn_bluetooth";
	private static final String WIFI_ICON_NAME = "btn_wifi";
	private static final String DATA_ICON_NAME = "btn_mobile_data";
	private static final String FLASH_ICON_NAME = "btn_flashlight";
	private static final String GPS_ICON_NAME = "btn_gps";
	private static final String SYNC_ICON_NAME = "btn_autosync";
	private static final String SETTINGS_ICON_NAME = "btn_settings";
	private static final String AIRPLANE_MODE_ICON_NAME = "btn_airplane_mode";
	private static final String PREVIOUS_SONG_ICON_NAME = "btn_previous_song";
	private static final String NEXT_SONG_ICON_NAME = "btn_next_song";
	private static final String PAUSE_ICON_NAME = "btn_pause_song";
	private static final String PLAY_ICON_NAME = "btn_play_song";
	private static final String RINGER_ICON_NAME = "ic_ringtone_volume";
	private static final String MUSIC_ICON_NAME = "ic_media_volume";
	private static final String BRIGHTNESS_FULL_ICON_NAME = "btn_ic_brightness";
	private static final String SCREEN_TIMEOUT_15_ICON_NAME = "btn_screen_timeout_15";
	private static final String SCREEN_TIMEOUT_30_ICON_NAME = "btn_screen_timeout_30";
	private static final String SCREEN_TIMEOUT_60_ICON_NAME = "btn_screen_timeout_60";
	private static final String SCREEN_TIMEOUT_120_ICON_NAME = "btn_screen_timeout_120";

	private static final String TAG = "ResourceLoader";

	private static ColorFilter sActiveColorFilter = null;

	private static ExternalResources mRes;
	private static boolean sIsResPackageValid = false;

	public static void init(Context context) {

		sActiveColorFilter = new PorterDuffColorFilter(ColorsResolver.getActiveColor(context), PorterDuff.Mode.SRC_ATOP);

		Drawable borderDrawable = null;
		Resources res = context.getResources();
		String borderType = TogglesResolver.getBorderType(context);
		boolean shouldDetermineBorderWidth = true;

		if("none".equals(borderType)) {
			borderDrawable = res.getDrawable(R.drawable.transparent);
			borderDrawable.mutate();
			shouldDetermineBorderWidth = false;
		} else if("circle".equals(borderType)) {
			borderDrawable = res.getDrawable(R.drawable.circle);
			borderDrawable.mutate();
		} else if("square".equals(borderType)) {
			borderDrawable = res.getDrawable(R.drawable.rectangle);
			borderDrawable.mutate();
		} else if("rounded".equals(borderType)) {
			//����� ��� ��������, ��� border - ��� ����� � ������������� ������.
			borderDrawable = res.getDrawable(R.drawable.border);
			borderDrawable.mutate();
			((GradientDrawable)borderDrawable).setCornerRadius(TogglesResolver.getBorderCornerRadius(context));
		}

		if(shouldDetermineBorderWidth) {
			((GradientDrawable)borderDrawable).setStroke(TogglesResolver.getBorderWidth(context), ColorsResolver.getIdleColor(context));
		}

		//������ ��������� ���
		Drawable bgDrawable = null;
		String bgType = TogglesResolver.getBackgroundType(context);
		boolean shouldDetermineBgColor = true;
		if("none".equals(bgType)) {
			bgDrawable = res.getDrawable(R.drawable.transparent);
			bgDrawable.mutate();
			shouldDetermineBgColor = false;
		} else if("circle".equals(bgType)) {
			bgDrawable = res.getDrawable(R.drawable.circle);
			bgDrawable.mutate();
		} else if("square".equals(bgType)) {
			bgDrawable = res.getDrawable(R.drawable.rectangle);
			bgDrawable.mutate();
		} else if("rounded".equals(bgType)) {
			//����� ��� ��������, ��� border - ��� ����� � ������������� ������.
			bgDrawable = res.getDrawable(R.drawable.border);
			bgDrawable.mutate();
			((GradientDrawable)bgDrawable).setCornerRadius(TogglesResolver.getBackgroundCornerRadius(context));
		}

		if(shouldDetermineBgColor) {
			int bgColor = ColorsResolver.getPressedColor(context);
			((GradientDrawable)bgDrawable).setColor(bgColor);
			((GradientDrawable)bgDrawable).setStroke(0, Color.TRANSPARENT);
		}

		drawable.toggle_background = bgDrawable;
		drawable.toggle_background.mutate();
		((GradientDrawable)drawable.toggle_background).setColor(ColorsResolver.getIdleBgColor(context));
		
		drawable.toggle_background_pressed = bgDrawable.getConstantState().newDrawable();
		drawable.toggle_background_pressed.mutate();
		((GradientDrawable)drawable.toggle_background_pressed).setColor(ColorsResolver.getPressedColor(context));
		
		

		if(ExternalResourceResolver.shouldLoadExternalResources(context)) {
			String packageName = ExternalResourceResolver.getExternalPackageName(context);
			initExternal(context, packageName);
			setBordersTypeExternal(context, borderDrawable, packageName);
		} else {
			initInternal(context);
			setBordersTypeInternal(context, borderDrawable);
		}
	}

	private static void initInternal(Context context) {
		Resources res = context.getResources();

		drawable.brightness_full = res.getDrawable(R.drawable.brightness_full);
		drawable.music = res.getDrawable(R.drawable.music);
		drawable.ringer = res.getDrawable(R.drawable.ringer);

		
		drawable.play_song = createFilteredDrawableNoBorders(context, R.drawable.play_song);
		drawable.pause_song = createFilteredDrawableNoBorders(context, R.drawable.pause_song);
		drawable.next_button = createFilteredDrawableNoBorders(context, R.drawable.next_song);
		drawable.previous_button = createFilteredDrawableNoBorders(context, R.drawable.previous_song);
	}

	private static void initExternal(Context context, String packageName) {

		sIsResPackageValid = true;
		try {
			mRes = ResourceLoader.getResourcesForPackage(context, packageName);

			drawable.brightness_full = mRes.getDrawable(BRIGHTNESS_FULL_ICON_NAME);
			drawable.music = mRes.getDrawable(MUSIC_ICON_NAME);
			drawable.ringer = mRes.getDrawable(RINGER_ICON_NAME);

			Drawable play_song_ext = mRes.getDrawable(PLAY_ICON_NAME);
			Drawable pause_song_ext = mRes.getDrawable(PAUSE_ICON_NAME);
			Drawable next_song_ext = mRes.getDrawable(NEXT_SONG_ICON_NAME);
			Drawable previous_song_ext = mRes.getDrawable(PREVIOUS_SONG_ICON_NAME);
			drawable.play_song = createFilteredDrawableNoBorders(context, play_song_ext);
			drawable.pause_song = createFilteredDrawableNoBorders(context, pause_song_ext);
			drawable.next_button = createFilteredDrawableNoBorders(context, next_song_ext);
			drawable.previous_button = createFilteredDrawableNoBorders(context, previous_song_ext);
		} catch (NameNotFoundException e) {
			sIsResPackageValid = false;
			initInternal(context);
			Log.e(TAG, "Falling back to default icons due to missing icon pack application!");
		}
	}

	private static void setBordersTypeExternal(Context context, Drawable resource, String packageName) {
		
		if(!sIsResPackageValid) {
			setBordersTypeInternal(context, resource);
			return;
		}
		
		Drawable airplane_ext = mRes.getDrawable(AIRPLANE_MODE_ICON_NAME);
		drawable.airplane_mode = createFilteredDrawable(context, airplane_ext, resource);

		Drawable settings_ext = mRes.getDrawable(SETTINGS_ICON_NAME);
		drawable.settings_active = createFilteredDrawable(context, settings_ext, resource);

		Drawable sync_ext = mRes.getDrawable(SYNC_ICON_NAME);
		drawable.sync = createFilteredDrawable(context, sync_ext, resource);

		Drawable gps_ext = mRes.getDrawable(GPS_ICON_NAME);
		drawable.gps = createFilteredDrawable(context, gps_ext, resource);

		Drawable flash_ext = mRes.getDrawable(FLASH_ICON_NAME);
		drawable.flash = createFilteredDrawable(context, flash_ext, resource);

		Drawable data_ext = mRes.getDrawable(DATA_ICON_NAME);
		drawable.data = createFilteredDrawable(context, data_ext, resource);

		Drawable wifi_ext = mRes.getDrawable(WIFI_ICON_NAME);
		drawable.wifi = createFilteredDrawable(context, wifi_ext, resource);

		Drawable bluetooth_ext = mRes.getDrawable(BLUETOOTH_ICON_NAME);
		drawable.bluetooth = createFilteredDrawable(context, bluetooth_ext, resource);

		Drawable sound_on_ext = mRes.getDrawable(SOUND_ON_ICON_NAME);
		Drawable sound_off_ext = mRes.getDrawable(SOUND_OFF_ICON_NAME);
		Drawable vibrate_ext = mRes.getDrawable(VIBRATE_ICON_NAME);
		drawable.sound_on = createFilteredDrawable(context, sound_on_ext, resource);
		drawable.sound_off = createFilteredDrawable(context, sound_off_ext, resource);
		drawable.vibrate = createFilteredDrawable(context, vibrate_ext, resource);

		Drawable rotate_active_ext = mRes.getDrawable(ROTATE_ICON_NAME);
		Drawable rotate_lock_ext = mRes.getDrawable(ROTATE_LOCK_ICON_NAME);
		drawable.rotate_active = createFilteredDrawable(context, rotate_active_ext, resource);
		drawable.rotate = createFilteredDrawable(context, rotate_lock_ext, resource);

		Drawable wifi_ap_ext = mRes.getDrawable(WIFI_AP_ICON_NAME);
		drawable.wifi_ap = createFilteredDrawable(context, wifi_ap_ext, resource);

		Drawable lock_now_ext = mRes.getDrawable(LOCK_NOW_ICON_NAME);
		drawable.lock_now_active = createFilteredDrawable(context, lock_now_ext, resource);

		Drawable brightness_auto_ext = mRes.getDrawable(BRIGHTNESS_AUTO_ICON_NAME);
		drawable.brightness_auto = createFilteredDrawable(context, brightness_auto_ext, resource);
		
		Drawable timeout_1_ext;
		Drawable timeout_2_ext;
		Drawable timeout_3_ext;
		if(TogglesResolver.getTimeoutModes(context).contains("15s")) {
			timeout_1_ext = mRes.getDrawable(SCREEN_TIMEOUT_15_ICON_NAME);
			timeout_2_ext = mRes.getDrawable(SCREEN_TIMEOUT_30_ICON_NAME);
			timeout_3_ext = mRes.getDrawable(SCREEN_TIMEOUT_60_ICON_NAME);
		} else {
			timeout_1_ext = mRes.getDrawable(SCREEN_TIMEOUT_30_ICON_NAME);
			timeout_2_ext = mRes.getDrawable(SCREEN_TIMEOUT_60_ICON_NAME);
			timeout_3_ext = mRes.getDrawable(SCREEN_TIMEOUT_120_ICON_NAME);
		}
		
		drawable.screen_timeout_1 = createFilteredDrawable(context, timeout_1_ext, resource);
		drawable.screen_timeout_2 = createFilteredDrawable(context, timeout_2_ext, resource);
		drawable.screen_timeout_3 = createFilteredDrawable(context, timeout_3_ext, resource);
	}


	private static void setBordersTypeInternal(Context context, Drawable resource) {

		drawable.airplane_mode = createFilteredDrawable(context, R.drawable.airplane_mode, resource);
		drawable.settings_active = createFilteredDrawable(context, R.drawable.settings, resource);
		drawable.sync = createFilteredDrawable(context, R.drawable.sync, resource);
		drawable.gps = createFilteredDrawable(context, R.drawable.gps, resource);
		drawable.flash = createFilteredDrawable(context, R.drawable.flash, resource);
		drawable.data = createFilteredDrawable(context, R.drawable.data, resource);
		drawable.wifi = createFilteredDrawable(context, R.drawable.wifi, resource);
		drawable.bluetooth = createFilteredDrawable(context, R.drawable.bluetooth, resource);
		drawable.sound_on = createFilteredDrawable(context, R.drawable.sound_on, resource);
		drawable.sound_off = createFilteredDrawable(context, R.drawable.sound_off, resource);
		drawable.vibrate = createFilteredDrawable(context, R.drawable.vibrate, resource);
		drawable.rotate_active = createFilteredDrawable(context, R.drawable.rotate, resource);
		drawable.rotate = createFilteredDrawable(context, R.drawable.rotate_lock, resource);
		drawable.wifi_ap = createFilteredDrawable(context, R.drawable.wifi_ap, resource);
		drawable.lock_now_active = createFilteredDrawable(context, R.drawable.lock_now, resource);
		drawable.brightness_auto = createFilteredDrawable(context, R.drawable.brightness_auto, resource);
		if(TogglesResolver.getTimeoutModes(context).contains("15s")) {
			drawable.screen_timeout_1 = createFilteredDrawable(context, R.drawable.timeout_15, resource);
			drawable.screen_timeout_2 = createFilteredDrawable(context, R.drawable.timeout_30, resource);
			drawable.screen_timeout_3 = createFilteredDrawable(context, R.drawable.timeout_60, resource);
		} else {
			drawable.screen_timeout_1 = createFilteredDrawable(context, R.drawable.timeout_30, resource);
			drawable.screen_timeout_2 = createFilteredDrawable(context, R.drawable.timeout_60, resource);
			drawable.screen_timeout_3 = createFilteredDrawable(context, R.drawable.timeout_120, resource);
		}
	}
	

	
	private static Drawable createFilteredDrawable(Context context, Drawable iconDrawable, Drawable borderDrawable) {
		iconDrawable.mutate();
		borderDrawable = borderDrawable.getConstantState().newDrawable();
		LayerDrawable temp = new LayerDrawable(new Drawable[] {borderDrawable, iconDrawable});
		temp.setColorFilter(sActiveColorFilter);
		return temp;
	}
	
	private static Drawable createFilteredDrawable(Context context, int iconId, Drawable borderDrawable) {
		Drawable iconDrawable = context.getResources().getDrawable(iconId);
		borderDrawable = borderDrawable.getConstantState().newDrawable();
		iconDrawable.mutate();
		LayerDrawable temp = new LayerDrawable(new Drawable[] {borderDrawable, iconDrawable});
		temp.setColorFilter(sActiveColorFilter);
		return temp;
	}
	
	private static Drawable createFilteredDrawableNoBorders(Context context, int iconId) {
		Drawable iconDrawable = context.getResources().getDrawable(iconId);
		iconDrawable.mutate();
		iconDrawable.setColorFilter(sActiveColorFilter);
		return iconDrawable;
	}
	
	private static Drawable createFilteredDrawableNoBorders(Context context, Drawable icon) {
		icon.mutate();
		icon.setColorFilter(sActiveColorFilter);
		return icon;
	}

	public static class drawable {
		
		public static Drawable toggle_background_pressed;
		public static Drawable toggle_background;
		public static Drawable sync; 
		public static Drawable gps;
		public static Drawable airplane_mode;
		public static Drawable flash;
		public static Drawable data;
		public static Drawable wifi;
		public static Drawable bluetooth;
		public static Drawable sound_on;
		public static Drawable sound_off;
		public static Drawable vibrate;
		public static Drawable rotate_active;
		public static Drawable rotate;
		public static Drawable wifi_ap;
		public static Drawable lock_now_active;
		public static Drawable brightness_auto;
		public static Drawable settings_active;
		public static Drawable screen_timeout_1;
		public static Drawable screen_timeout_2;
		public static Drawable screen_timeout_3;
		

		//���������������� ������
		public static Drawable play_song;
		public static Drawable pause_song;
		public static Drawable brightness_full;
		public static Drawable thumb_states;
		public static Drawable music;
		public static Drawable ringer;
		public static Drawable next_button;
		public static Drawable previous_button;
		
	}

}
