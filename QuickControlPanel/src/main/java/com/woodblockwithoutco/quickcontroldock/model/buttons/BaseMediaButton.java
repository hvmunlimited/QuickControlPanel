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
package com.woodblockwithoutco.quickcontroldock.model.buttons;

import com.woodblockwithoutco.remotecontroller.MediaCommand;
import com.woodblockwithoutco.remotecontroller.RemoteController;
import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier;
import com.woodblockwithoutco.quickcontroldock.global.event.VisibilityEventNotifier.OnVisibilityEventListener;
import com.woodblockwithoutco.quickcontroldock.global.holder.RemoteControllerHolder;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.MusicResolver;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnClickListener;

public abstract class BaseMediaButton extends ImageView implements OnClickListener, OnVisibilityEventListener {

	private static final String TAG = "BaseMediaButtonRemoteController";

	private RemoteController mRemoteController;

	private boolean mUseBroadcastClick = true;

	private int mPressedColor;

	private PendingIntent mDefaultPlayerIntent;

	public BaseMediaButton(Context context) {
		this(context, null, 0);
	}

	public BaseMediaButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseMediaButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPressedColor = ColorsResolver.getPressedColor(getContext());
		setClickable(true);
		setImageDrawable(getMediaDrawable());
		setOnClickListener(this);
		mUseBroadcastClick  = MusicResolver.useBroadcastClick(getContext());
		if(mUseBroadcastClick) {
			mDefaultPlayerIntent = MusicResolver.getDefaultPlayerIntent(getContext());
		}
		mRemoteController = RemoteControllerHolder.getInstance();
		VisibilityEventNotifier.getInstance().registerListener(this);
	}

	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if(pressed) {
			setBackgroundColor(mPressedColor);
		} else {
			setBackgroundColor(0x00000000);
		}
	}

	@Override
	public void onClick(View v) {

		boolean clickSent = mRemoteController.sendMediaCommand(getMediaCommand());
		boolean remoteControllerRegistered = mRemoteController.isRegistered();
		boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		if(isKitKat) {
			//���������, ������� �� MusicControlService
			if(remoteControllerRegistered) {
				if(!clickSent) {
					//��� ����������������, ������ ��� ��������� ������
					if(mUseBroadcastClick) {
						//�������� ��������� ������
						if(mDefaultPlayerIntent != null) {
							mRemoteController.sendBroadcastMediaCommand(getMediaCommand(), mDefaultPlayerIntent);
						} else {
							mRemoteController.sendBroadcastMediaCommand(getMediaCommand());
						}
					}
				}
			}
		} else {
			if(remoteControllerRegistered) {
				if(!clickSent) {
					if(mUseBroadcastClick) {
						if(mDefaultPlayerIntent != null) {
							mRemoteController.sendBroadcastMediaCommand(getMediaCommand(), mDefaultPlayerIntent);
						} else {
							mRemoteController.sendBroadcastMediaCommand(getMediaCommand());
						}
					}
				}
			} else {
				Log.e(TAG, "Invalid RemoteController state - it should be registered, but it's not.");
			}
		}
	}

	protected abstract Drawable getMediaDrawable();
	protected abstract MediaCommand getMediaCommand();

	@Override
	public void onShow() {
		if(isMasterButton()) {
			if(mRemoteController != null && !mRemoteController.isRegistered()) {
				if(MusicResolver.isArtworkEnabled(getContext())) {
					int size = MusicResolver.getArtworkSize(getContext());
					mRemoteController.registerRemoteControls(size, size);
				} else {
					mRemoteController.registerRemoteControls();
				}
				mRemoteController.setSynchronizationEnabled(true);
			}
		}
	}

	@Override
	public void onHide() {
		if(isMasterButton()) {
			mRemoteController.unregisterRemoteControls();
		}
	}

	//���������� true, ���� ��� ������ �������� "��������"-�������� �� RemoteController
	//�������������� � �������, ���� �����, ����� �� ������� �� RemoteController'�
	protected boolean isMasterButton() {
		return false;
	}

}
