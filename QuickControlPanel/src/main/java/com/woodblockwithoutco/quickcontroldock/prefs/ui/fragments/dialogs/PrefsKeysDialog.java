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

import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class PrefsKeysDialog extends DialogFragment {
	
	private TextView mTextView;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		mTextView = new TextView(getActivity().getApplicationContext());
		mTextView.setScrollBarStyle(View.SCROLLBAR_POSITION_RIGHT);
		mTextView.setMovementMethod(new ScrollingMovementMethod());
	    builder.setView(mTextView);
	    builder.setPositiveButton(android.R.string.ok, null);
	    update();
	    return builder.create();
	}
	
	@Override
	public void show(FragmentManager manager, String tag) {
		super.show(manager, tag);
	}
	
	private void update() {
		Context context = getActivity().getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Map<String, ?> map = prefs.getAll();
		Set<String> set = map.keySet();
		StringBuilder builder = new StringBuilder();
		for(String s : set) {
			builder.append("Key : ").append(s).append("\nKey type: ").
			append(map.get(s).getClass().getName()).append("\nKey value: ").append(map.get(s)).append("\n\n");
		}
		mTextView.setText(builder.toString());
	}
}
