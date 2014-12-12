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
package com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.managers;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.AsyncTask;

@SuppressWarnings("deprecation")
public class NoSurfaceViewFlashlightManipulator implements FlashlightManipulator {

	static Camera sCam;
	Parameters mParams;


	public void turnFlashlightOn() {
		try {
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						sCam = Camera.open();   
						mParams = sCam.getParameters();
						mParams.setFlashMode(Parameters.FLASH_MODE_TORCH);
						sCam.setParameters(mParams);
					} catch(Exception e) {
						return false;
					}

					return true;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					if(result) {
						sCam.startPreview();
					}
				}

			}.execute();

		} catch(Exception e) {}
	}

	public void turnFlashlightOff() {
		try {
			sCam.stopPreview();
			sCam.release(); 
		} catch(RuntimeException e) {}
	}


}
