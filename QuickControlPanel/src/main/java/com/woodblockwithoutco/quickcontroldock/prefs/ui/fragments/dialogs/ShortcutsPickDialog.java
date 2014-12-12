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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.DropListener;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;
import com.mobeta.android.dslv.SimpleFloatViewManager;
import com.woodblockwithoutco.quickcontroldock.prefs.editors.ShortcutsEditor;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ShortcutsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LaunchResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.ui.fragments.dialogs.adapters.ShortcutsPickAdapter;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.util.BitmapUtils;
import com.woodblockwithoutco.quickcontroldock.util.InvalidSizeException;
import com.woodblockwithoutco.quickcontroldock.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShortcutsPickDialog extends DialogFragment {

	private final static int REQUEST_PICK = 9999;
	private static final int MAX_SHORTCUT_COUNT = 100;

	private static final float DISABLED_ALPHA = 0.5f;
	private static final float NORMAL_ALPHA = 1.0f;

	private View mAddNewShortcutButton;
	private ShortcutsPickAdapter mAdapter;
	
	private ProgressDialog mSaveCustomIconDialog;
	private Intent mServiceIntent;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mServiceIntent = new Intent(getActivity().getApplicationContext(), ControlService.class);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		mSaveCustomIconDialog = new ProgressDialog(getActivity());
		mSaveCustomIconDialog.setCancelable(false);
		mSaveCustomIconDialog.setMessage(getActivity().getResources().getString(R.string.shortcuts_save_custom_icon_progress));
		
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.shortcuts_prefs_layout, null, false);
		mAddNewShortcutButton = view.findViewById(R.id.add_new_app);
		mAddNewShortcutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startPickerActivity();
			}
		});


		List<String> pkgs = ShortcutsResolver.getShortcutsOrder(getActivity());
		mAdapter = new ShortcutsPickAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, pkgs);
		initDslv(view);

		builder.setView(view);

		DialogInterface.OnClickListener listener;
		listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
				case DialogInterface.BUTTON_NEUTRAL:
					dismiss();
					break;
				}
			}
		};
		builder.setNeutralButton(R.string.close, listener);

		return builder.create();
	}
	
	

	private void initDslv(View root) {
		DragSortListView dragList = (DragSortListView) root.findViewById(android.R.id.list);
		dragList.setAdapter(mAdapter);
		dragList.setDropListener(new DropListener() {
			@Override
			public void drop(int from, int to) {
				String item = mAdapter.getItem(from);
				mAdapter.remove(item);
				mAdapter.insert(item, to);
				ShortcutsEditor.saveShortcuts(getActivity(), mAdapter.getList());
			}
		});

		dragList.setRemoveListener(new RemoveListener() {
			@Override
			public void remove(int which) {
				String className = mAdapter.getItem(which).split("/")[1];
				ShortcutsEditor.setCustomIconEnabled(getActivity(), className, false);
				mAdapter.remove(mAdapter.getList().get(which));
				ShortcutsEditor.saveShortcuts(getActivity(), mAdapter.getList());
				if(mAdapter.getList().size() >= MAX_SHORTCUT_COUNT) {
					mAddNewShortcutButton.setEnabled(false);
					mAddNewShortcutButton.setAlpha(DISABLED_ALPHA);
				} else {
					mAddNewShortcutButton.setEnabled(true);
					mAddNewShortcutButton.setAlpha(NORMAL_ALPHA);
				}
			}
		});
		
		dragList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				String className = mAdapter.getItem(position).split("/")[1];
				ShortcutsEditor.setCustomIconEnabled(getActivity(), className, false);
				return true;
			}
			
		});

		dragList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				startImagePicker(position);
			}
		});

		DragSortController controller = new DragSortController(dragList);
		controller.setDragHandleId(R.id.list_item_image);
		controller.setRemoveEnabled(true);
		controller.setRemoveMode(DragSortController.FLING_REMOVE);
		controller.setSortEnabled(true);
		controller.setDragInitMode(DragSortController.ON_DOWN);

		dragList.setFloatViewManager(controller);
		dragList.setOnTouchListener(controller);
		dragList.setDragEnabled(true);

		SimpleFloatViewManager simpleFloatViewManager = new SimpleFloatViewManager(dragList);
		simpleFloatViewManager.setBackgroundColor(Color.TRANSPARENT);
		dragList.setFloatViewManager(simpleFloatViewManager);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_PICK) {
			if(resultCode == Activity.RESULT_OK) {
				String pkg = data.getComponent().getPackageName();
				String clazz = data.getComponent().getClassName();
				List<String> pkgs = mAdapter.getList();
				pkgs.add(pkg+"/"+clazz);
				mAdapter.notifyDataSetChanged();
				if(pkgs.size() >= MAX_SHORTCUT_COUNT) {
					mAddNewShortcutButton.setEnabled(false);
					mAddNewShortcutButton.setAlpha(DISABLED_ALPHA);
				} else {
					mAddNewShortcutButton.setEnabled(true);
					mAddNewShortcutButton.setAlpha(NORMAL_ALPHA);
				}
				ShortcutsEditor.saveShortcuts(getActivity(), mAdapter.getList());
				if(LaunchResolver.isServiceEnabled(getActivity())) {
					getActivity().stopService(mServiceIntent);
					getActivity().startService(mServiceIntent);
				} else {
					getActivity().stopService(mServiceIntent);
				}
			}
		} else {
			if(resultCode == Activity.RESULT_OK) {
				int position = requestCode - 1;
				if(position >= 0) {
					if(data != null) {
						Uri imageUri = data.getData();
						if(imageUri == null) 
							return;
						String className = mAdapter.getItem(position).split("/")[1];
						File file = new File(getActivity().getFilesDir(), className+".png");
						new SaveCustomIconTask().execute(file, imageUri, className);
					}
				}
			}
		}
	}

	private void startPickerActivity() {
		Intent pickActivityIntent = new Intent("android.intent.action.PICK_ACTIVITY");
		Intent filterIntent = new Intent("android.intent.action.MAIN", null);
		filterIntent.addCategory("android.intent.category.LAUNCHER");   
		pickActivityIntent.putExtra("android.intent.extra.INTENT", filterIntent);
		startActivityForResult(pickActivityIntent, REQUEST_PICK);
	}

	private void startImagePicker(int position) {
		Intent imagePickIntent=new Intent(Intent.ACTION_PICK);
		imagePickIntent.setType("image/*");
		startActivityForResult(imagePickIntent, position + 1);
	}


	@Override
	public void onResume() {
		super.onResume();
		if(mAdapter.getList().size() >= MAX_SHORTCUT_COUNT) {
			mAddNewShortcutButton.setEnabled(false);
			mAddNewShortcutButton.setAlpha(DISABLED_ALPHA);
		} else {
			mAddNewShortcutButton.setEnabled(true);
			mAddNewShortcutButton.setAlpha(NORMAL_ALPHA);
		}
	}
	

	private class SaveCustomIconTask extends AsyncTask<Object, Void, Integer> {
		
		@Override
		protected void onPreExecute() {
			mSaveCustomIconDialog.show();
		}
		
		@Override
		protected Integer doInBackground(Object... arguments) {
			File file = (File) arguments[0];
			Uri imgUri = (Uri) arguments[1];
			String className = (String) arguments[2];
			
			int result = R.string.shortcuts_failed_to_save_icon;
			try {
				FileOutputStream out = new FileOutputStream(file);
				BitmapUtils.
				decodeUri(imgUri).
				compress(Bitmap.CompressFormat.PNG, 100, out);
				result = 0;
			} catch (FileNotFoundException e) {
				result = R.string.shortcuts_failed_to_save_icon;
			} catch (InvalidSizeException e) {
				result = R.string.shortcuts_invalid_icon_dimension;
			}
			ShortcutsEditor.setCustomIconEnabled(getActivity(), className, result == 0);
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			mSaveCustomIconDialog.dismiss();
			if(result != 0) {
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
			}
		}

	}

}
