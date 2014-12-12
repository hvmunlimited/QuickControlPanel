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
package com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.adapters;

import java.util.List;

import com.woodblockwithoutco.quickcontroldock.model.buttons.ButtonType;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;
import com.woodblockwithoutco.quickcontroldock.R;



import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TogglesDragAdapter extends ArrayAdapter<String> {

	private static final int COLOR_WHITE = 0xFFFFFFFF;
	private static final int COLOR_GRAY = 0xFF909090;

	
	private List<String> mList;
	
	public TogglesDragAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		mList = objects;
	}
	
	public List<String> getList() {
		return mList;
	}
	
	private void refreshEnabledState(View v, int position) {
		boolean isButtonEnabled = isButtonEnabled(position);
		setItemEnabled(v, isButtonEnabled);
	}
	
	private void setItemEnabled(View v, boolean enabled) {
		TextView tv = (TextView) v.findViewById(R.id.list_item_text);
		ImageView iv = (ImageView) v.findViewById(R.id.list_item_image);
		if(enabled) {
			tv.setTextColor(COLOR_WHITE);
			iv.setColorFilter(COLOR_WHITE, PorterDuff.Mode.SRC_ATOP);
		} else {
			tv.setTextColor(COLOR_GRAY);
			iv.setColorFilter(COLOR_GRAY, PorterDuff.Mode.SRC_ATOP);
		}
	}
	
	private boolean isButtonEnabled(int position) {
		String stringType = getItem(position);
		ButtonType type = ButtonType.valueOf(stringType);
		return TogglesResolver.checkButton(getContext(), type);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.toggles_list_item, parent, false);
			holder = new ViewHolder();
			holder.textView = (TextView) rowView.findViewById(R.id.list_item_text);
			holder.imageView = (ImageView) rowView.findViewById(R.id.list_item_image);
			rowView.setTag(holder);
			refreshEnabledState(rowView, position);
		} else {
			holder = (ViewHolder) rowView.getTag();
			refreshEnabledState(rowView, position);
		}
		
		String stringType = getItem(position);
		ButtonType type = ButtonType.valueOf(stringType);
		int stringId = R.string.toggle_data;
		int drawableId = R.drawable.data;
		
		switch(type) {
		case AIRPLANE:
			stringId = R.string.toggle_airplane;
			drawableId = R.drawable.airplane_mode;
			break;
		case BLUETOOTH:
			stringId = R.string.toggle_bluetooth;
			drawableId = R.drawable.bluetooth;
			break;
		case DATA:
			stringId = R.string.toggle_data;
			drawableId = R.drawable.data;
			break;
		case FLASHLIGHT:
			stringId = R.string.toggle_flash;
			drawableId = R.drawable.flash;
			break;
		case GPS:
			stringId = R.string.toggle_gps;
			drawableId = R.drawable.gps;
			break;
		case LOCK_NOW:
			stringId = R.string.toggle_lock_now;
			drawableId = R.drawable.lock_now;
			break;
		case ROTATE:
			stringId = R.string.toggle_rotate;
			drawableId = R.drawable.rotate;
			break;
		case SETTINGS:
			stringId = R.string.toggle_settings;
			drawableId = R.drawable.settings;
			break;
		case SOUND:
			stringId = R.string.toggle_sound;
			drawableId = R.drawable.sound_on;
			break;
		case SYNC:
			stringId = R.string.toggle_sync;
			drawableId = R.drawable.sync;
			break;
		case WIFI:
			stringId = R.string.toggle_wifi;
			drawableId = R.drawable.wifi;
			break;
		case WIFI_AP:
			stringId = R.string.toggle_wifi_ap;
			drawableId = R.drawable.wifi_ap;
			break;
		case AUTO_BRIGHTNESS:
			stringId = R.string.toggle_auto_brightness;
			drawableId = R.drawable.brightness_auto;
			break;
		case SCREEN_TIMEOUT:
			stringId = R.string.toggle_screen_timeout;
			drawableId = R.drawable.timeout_30;
			break;
		}
		Drawable iconDrawable = getContext().getResources().getDrawable(drawableId);
		iconDrawable.setColorFilter(COLOR_WHITE, PorterDuff.Mode.SRC_ATOP);

		holder.textView.setText(getContext().getString(stringId));
		holder.imageView.setImageDrawable(iconDrawable);
		return rowView;
	}
	
	static class ViewHolder {
		TextView textView;
		ImageView imageView;
	}

}
