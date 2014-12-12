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
package com.woodblockwithoutco.quickcontroldock.util;

import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.woodblockwithoutco.quickcontroldock.global.app.GlobalApplication;

public class BitmapUtils {

	public static Bitmap decodeUri(Uri image) throws FileNotFoundException, InvalidSizeException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(GlobalApplication.getGlobalContext().getContentResolver().openInputStream(image), null, o);
		final int SIZE = ScreenUtils.getIconSize(GlobalApplication.getGlobalContext());
		
		
		int tmpWidth = o.outWidth;
		int tmpHeight = o.outHeight;
		
		if(tmpWidth != tmpHeight) throw new InvalidSizeException();
		
		int scale = 1;
		
		while(true) {
			if(tmpWidth / 2 < SIZE || tmpHeight / 2 < SIZE) {
				break;
			}
			tmpWidth = tmpWidth / 2;
			tmpHeight = tmpHeight / 2;
			scale = scale*2;
		}
		
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		
		return BitmapFactory.decodeStream(GlobalApplication.getGlobalContext().getContentResolver().openInputStream(image), null, o2);
	}
}
