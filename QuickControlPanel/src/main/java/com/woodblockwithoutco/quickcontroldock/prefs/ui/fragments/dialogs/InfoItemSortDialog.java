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
package com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs;


import java.util.List;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.DropListener;
import com.mobeta.android.dslv.SimpleFloatViewManager;
import com.woodblockwithoutco.quickcontroldock.model.text.InfoItemType;
import com.woodblockwithoutco.quickcontroldock.prefs.editors.InfoItemEditor;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.InfoResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.adapters.InfoItemsDragAdapter;
import com.woodblockwithoutco.quickcontroldock.R;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class InfoItemSortDialog extends DialogFragment {

	private static final int COLOR_WHITE = 0xFFFFFFFF;
	private static final int COLOR_GRAY = 0xFF909090;

	public static final float ALPHA_ENABLED = 1.0f;
	public static final float ALPHA_DISABLED = 0.3f;

	private InfoItemsDragAdapter mAdapter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sort_toggles, null);
		DragSortListView dragList = (DragSortListView) view.findViewById(R.id.listview);

		List<String> infoItemsOrderList = InfoResolver.getInfoItemsOrderListAsString(getActivity());
		
		mAdapter = new InfoItemsDragAdapter(getActivity().getApplicationContext(), 
				R.layout.toggles_list_item, R.id.list_item_text, 
				infoItemsOrderList);

		dragList.setAdapter(mAdapter);
		dragList.setDropListener(new DropListener() {
			@Override
			public void drop(int from, int to) {
				String item = mAdapter.getItem(from);
				mAdapter.remove(item);
				mAdapter.insert(item, to);
			}
		});

		DragSortController controller = new DragSortController(dragList);
		controller.setDragHandleId(R.id.list_item_image);
		controller.setRemoveEnabled(false);
		controller.setSortEnabled(true);
		controller.setDragInitMode(DragSortController.ON_DRAG);

		dragList.setFloatViewManager(controller);
		dragList.setOnTouchListener(controller);
		dragList.setDragEnabled(true);

		SimpleFloatViewManager simpleFloatViewManager = new SimpleFloatViewManager(dragList);
		simpleFloatViewManager.setBackgroundColor(Color.TRANSPARENT);
		dragList.setFloatViewManager(simpleFloatViewManager);

		dragList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String type = mAdapter.getItem(position);
				InfoItemType infoItemType = InfoItemType.valueOf(type);
				boolean enabled = InfoResolver.isInfoItemEnabled(getActivity(), infoItemType);
				InfoItemEditor.setInfoItemEnabled(getActivity(), infoItemType, !enabled);
				setItemEnabled(view, !enabled);
			}
		});

		builder.setView(view);

		DialogInterface.OnClickListener listener;
		listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
				case DialogInterface.BUTTON_POSITIVE:
					InfoItemEditor.saveInfoItemsOrder(getActivity(), mAdapter.getList());
					dismiss();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dismiss();
					break;
				}
			}
		};
		builder.setPositiveButton(android.R.string.ok, listener);
		builder.setNegativeButton(android.R.string.cancel, listener);

		return builder.create();
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

}
