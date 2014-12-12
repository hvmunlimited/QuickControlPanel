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

import com.woodblockwithoutco.quickcontroldock.util.AppShortcutResolver;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShortcutsPickAdapter extends ArrayAdapter<String> {

	private List<String> mList;
	private AppShortcutResolver mAppShortcutResolver;

	public ShortcutsPickAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		mList = objects;
		mAppShortcutResolver = new AppShortcutResolver(context);
	}

	public List<String> getList() {
		return mList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		String item = mList.get(position);
		View rowView = convertView;

		if(item != null) {
			String[] itemInfo = item.split("/");
			if(itemInfo.length < 2) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.empty_view, parent, false);
				return rowView;
			}

			String pkgName = item.split("/")[0];
			String className = item.split("/")[1];

			String title = mAppShortcutResolver.getLabel(pkgName, className);

			
			Drawable icon = null;
			icon = mAppShortcutResolver.getIcon(pkgName, className);

			ViewHolder holder;

			if(rowView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.shortcuts_list_item, parent, false);
				holder = new ViewHolder();
				holder.textView = (TextView) rowView.findViewById(R.id.list_item_text);
				holder.imageView = (ImageView) rowView.findViewById(R.id.list_item_image);
				rowView.setTag(holder);
			} else {
				holder = (ViewHolder) rowView.getTag();
			}

			holder.imageView.setImageDrawable(icon);
			holder.textView.setText(title);
		}

		if(rowView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.empty_view, parent, false);
		}
		return rowView;
	}

	static class ViewHolder {
		TextView textView;
		ImageView imageView;
	}


}
