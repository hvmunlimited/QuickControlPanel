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
package com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments;

import java.util.List;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.SimpleFloatViewManager;
import com.mobeta.android.dslv.DragSortListView.DropListener;
import com.woodblockwithoutco.quickcontroldock.prefs.editors.PanelsOrderEditor;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.PanelsOrderResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.adapters.PanelsDragAdapter;
import com.woodblockwithoutco.quickcontroldock.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PanelsOrderPrefsFragment extends ListFragment {
	
	private PanelsDragAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.panels_order_prefs_layout, container, false);
		DragSortListView dragList = (DragSortListView) v.findViewById(android.R.id.list);
		List<String> panelsOrder = PanelsOrderResolver.getPanelsOrder(getActivity());
		mAdapter = new PanelsDragAdapter(getActivity().getApplicationContext(), 
				R.layout.toggles_list_item, R.id.list_item_text, 
				panelsOrder);
		
		dragList.setAdapter(mAdapter);
		dragList.setDropListener(new DropListener() {
			@Override
			public void drop(int from, int to) {
				String item = mAdapter.getItem(from);
				mAdapter.remove(item);
				mAdapter.insert(item, to);
				PanelsOrderEditor.savePanelsOrder(getActivity(), mAdapter.getList());
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
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(shouldEnableHomeBackButton()) {
			setBackHomeButtonEnabled(true);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(shouldEnableHomeBackButton()) {
			setBackHomeButtonEnabled(false);
		}
	}
	
	private void setBackHomeButtonEnabled(boolean enabled) {
		PreferenceActivity a = getPreferenceActivity();
		if(a != null) {
			ActionBar ab = a.getActionBar();
			if(ab != null) {
				ab.setDisplayHomeAsUpEnabled(enabled);
				ab.setHomeButtonEnabled(enabled);
			}
		}
	}
	
	private boolean shouldEnableHomeBackButton() {
		PreferenceActivity a = getPreferenceActivity();
		boolean result = false;
		if(a != null) {
			result = true;
		}
		return result;
	}
	
	private PreferenceActivity getPreferenceActivity() {
		Activity a = getActivity();
		if (!(a instanceof PreferenceActivity)) {
			return null;
		}
		
		return (PreferenceActivity) a;
	}
}
