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

import com.woodblockwithoutco.quickcontroldock.model.PanelType;
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

public class PanelsDragAdapter extends ArrayAdapter<String> {

	private static final int COLOR_WHITE = 0xFFFFFFFF;
	private List<String> mList;
	
	public PanelsDragAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		mList = objects;
	}
	
	public List<String> getList() {
		return mList;
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
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		String stringType = getItem(position);
		PanelType type = PanelType.valueOf(stringType);
		int stringId = R.string.panel_toggles;
		int drawableId = R.drawable.drag_handle;
		
		switch(type) {
		case MUSIC:
			stringId = R.string.panel_music;
			break;
		case SHORTCUTS:
			stringId = R.string.panel_shortcuts;
			break;
		case TOGGLES:
			stringId = R.string.panel_toggles;
			break;
		case INFO:
			stringId = R.string.panel_info;
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
