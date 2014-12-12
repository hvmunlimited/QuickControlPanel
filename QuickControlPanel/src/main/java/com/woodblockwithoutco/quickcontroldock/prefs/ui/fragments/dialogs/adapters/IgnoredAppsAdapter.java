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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.woodblockwithoutco.quickcontroldock.R;
import com.woodblockwithoutco.quickcontroldock.prefs.editors.NotificationEditor;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.NotificationsResolver;
import com.woodblockwithoutco.quickcontroldock.util.AppRecord;



import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class IgnoredAppsAdapter extends ArrayAdapter<AppRecord> {

	private List<AppRecord> mList;
	private PackageManager mPackageManager;

	public IgnoredAppsAdapter(Context context, List<AppRecord> objects) {
		super(context, 0, objects);
		mList = objects;
		mPackageManager = context.getPackageManager();
	}

	public List<AppRecord> getList() {
		return mList;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			AppRecord item = mList.get(position);
			View rowView = convertView;

			if(item != null) {

				String pkgName = item.packageName;
				String title = item.appName;
				ApplicationInfo info;
				info = mPackageManager.getPackageInfo(pkgName, 0).applicationInfo;
				Drawable icon = null;
				icon = info.loadIcon(mPackageManager);

				ViewHolder holder;

				if(rowView == null) {
					LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.package_item, parent, false);
					holder = new ViewHolder();
					holder.textView = (TextView) rowView.findViewById(R.id.package_name);
					holder.imageView = (ImageView) rowView.findViewById(R.id.package_icon);
					holder.checkBox = (CheckBox) rowView.findViewById(R.id.package_enabled_checkbox);
					rowView.setTag(holder);
				} else {
					holder = (ViewHolder) rowView.getTag();
				}

				
				holder.imageView.setImageDrawable(icon);
				holder.textView.setText(title);
				
				OnCheckedChangeListener l = new CheckedChangeListener(item.packageName);
				boolean enabled = NotificationsResolver.getIgnoredApplicationsSet(getContext()).contains(item.packageName);
				
				holder.checkBox.setOnCheckedChangeListener(null);
				holder.checkBox.setChecked(enabled);
				holder.checkBox.setOnCheckedChangeListener(l);
			}

			if(rowView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.empty_view, parent, false);
			}
			return rowView;
		} catch (NameNotFoundException e) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.empty_view, parent, false);
			return rowView;
		}
	}

	static class ViewHolder {
		TextView textView;
		ImageView imageView;
		CheckBox checkBox;
	}
	
	private class CheckedChangeListener implements OnCheckedChangeListener {

		private final String mPkg;
		
		protected CheckedChangeListener(String pkgName) {
			mPkg = pkgName;
		}
		
		@Override
		public void onCheckedChanged(CompoundButton v, boolean checked) {
				Set<String> ignoredSet = NotificationsResolver.getIgnoredApplicationsSet(getContext());
				Set<String> setToBeSaved = new HashSet<String>();
				setToBeSaved.addAll(ignoredSet);
				if(checked) {
					setToBeSaved.add(mPkg);
				} else {
					setToBeSaved.remove(mPkg);
				}
				NotificationEditor.saveIgnoredPackages(getContext(), setToBeSaved);
		}
		
	}


}
