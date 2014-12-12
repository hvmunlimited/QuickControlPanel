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
package com.woodblockwithoutco.quickcontroldock.resource.loader;

import com.woodblockwithoutco.quickcontroldock.global.app.GlobalApplication;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 *
 * @author Alexander
 *
 */
public class ExternalResources {

	private static final String TYPE_DRAWABLE = "drawable";
	private static final String TAG = "ResourceLoader";

	private Resources mExternalResources;
	private String mPackageName;
	private Resources mFallbackInternalResources;
	private String mLocalPackageName;

	protected ExternalResources(Resources res, String packageName) {
		mExternalResources = res;
		mPackageName = packageName;
		Context context = GlobalApplication.getGlobalContext();
		mFallbackInternalResources = context.getResources();
		mLocalPackageName = context.getPackageName();
	}


	public Drawable getDrawable(String name) {
		int id = getIdForDrawable(name);
		if(id == 0) {
			Log.e(TAG, "Drawable with id ["+name+"] is missing in package ["+mPackageName+"]");
			id = getLocalIdForDrawable(name);
			if(id == 0) {
				id = R.drawable.missing_app;
			}
			return mFallbackInternalResources.getDrawable(id);
		}
		return mExternalResources.getDrawable(id);
	}
	
	private int getIdForDrawable(String name) {
		return getId(name, TYPE_DRAWABLE);
	}
	
	private int getId(String name, String type) {
		int id = mExternalResources.getIdentifier(name, type, mPackageName);
		return id;
	}
	
	private int getLocalId(String name, String type) {
		int id = mFallbackInternalResources.getIdentifier(name, type, mLocalPackageName);
		return id;
	}
	
	private int getLocalIdForDrawable(String name) {
		return getLocalId(name, TYPE_DRAWABLE);
	}
}
