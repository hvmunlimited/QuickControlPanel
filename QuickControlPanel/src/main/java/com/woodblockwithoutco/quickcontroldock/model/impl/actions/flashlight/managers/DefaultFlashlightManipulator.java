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

import java.io.IOException;


import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.AsyncTask;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;

@SuppressWarnings("deprecation")
public class DefaultFlashlightManipulator implements FlashlightManipulator {

	
	private Camera mCam;
	private Parameters mParams;
	private SurfaceTexture mSurfaceTexture;
    private boolean mPendingRequest = false;

	public DefaultFlashlightManipulator(TextureView textureView) {
        mPendingRequest = false;
        textureView.setSurfaceTextureListener(new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurfaceTexture = surface;
                if(mPendingRequest) {
                    mPendingRequest = false;
                    turnFlashlightOn();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                mSurfaceTexture = surface;
                if(mPendingRequest) {
                    mPendingRequest = false;
                    turnFlashlightOn();
                }
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                mSurfaceTexture = null;
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                mSurfaceTexture = surface;
                if(mPendingRequest) {
                    mPendingRequest = false;
                    turnFlashlightOn();
                }
            }
        });
	}


	public void turnFlashlightOn() {
		try {

			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						mCam = Camera.open();
						mParams = mCam.getParameters();
						mParams.setFlashMode(Parameters.FLASH_MODE_TORCH);
						mCam.setParameters(mParams);
					} catch(Exception e) {
						return false;
					}

					return true;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					if(result) {
						try {
                            mCam.setPreviewTexture(mSurfaceTexture);
						} catch (IOException e) {
							mCam.stopPreview();
							mSurfaceTexture = null;
							return;
						}
						mCam.startPreview();
					}
				}

			}.execute();



		} catch(RuntimeException e) {}
	}

	public void turnFlashlightOff() {
		try {
			mCam.stopPreview();
			mCam.release();
		} catch(RuntimeException e) {}
	}

}
