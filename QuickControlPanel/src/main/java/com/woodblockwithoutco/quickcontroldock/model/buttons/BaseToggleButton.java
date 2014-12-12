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

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleAction;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;
import com.woodblockwithoutco.quickcontroldock.resource.Res;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.R;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public abstract class BaseToggleButton extends ImageView implements View.OnClickListener, View.OnLongClickListener {

	protected ToggleAction mAction;
	protected Drawable mDrawable;
	protected boolean mShouldCloseAfterClick = false;

	private int mActiveColor = 0;
	private int mIdleColor = 0;

	private final Animation mIndefiniteStateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.indef_anim);


	public BaseToggleButton(Context context) {
		this(context, null, 0);
	}

	public BaseToggleButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mActiveColor = ColorsResolver.getActiveColor(getContext());
		mIdleColor = ColorsResolver.getIdleColor(getContext());

		setAnimation(mIndefiniteStateAnim);
		stopIndefAnim();

		setOnClickListener(this);
		if(TogglesResolver.isLongClickEnabled(getContext())) setOnLongClickListener(this);
		setClickable(true);

		mShouldCloseAfterClick = TogglesResolver.shouldCloseAfterClick(getContext());
	}


	protected void setVisualStateActive() {
		mDrawable.setColorFilter(mActiveColor, PorterDuff.Mode.SRC_ATOP);
	}

	protected void setVisualStateIdle() {
		mDrawable.setColorFilter(mIdleColor, PorterDuff.Mode.SRC_ATOP);
	}

	protected final void startIndefAnim() {
		startAnimation(mIndefiniteStateAnim);
	}

	protected final void stopIndefAnim() {
		clearAnimation();
	}

	protected final void initDrawable(ButtonType type) {
		switch(type) {
		case WIFI:
			mDrawable = Res.drawable.wifi;
			break;
		case DATA:
			mDrawable = Res.drawable.data;
			break;
		case BLUETOOTH:
			mDrawable = Res.drawable.bluetooth;
			break;
		case SOUND:
		case SCREEN_TIMEOUT:
			throw new IllegalArgumentException("Button of type "+type.name()+" must extend BaseTriToggleButton class!");
		case AIRPLANE:
			mDrawable = Res.drawable.airplane_mode;
			break;
		case ROTATE:
			mDrawable = Res.drawable.rotate;
			break;
		case GPS:
			mDrawable = Res.drawable.gps;
			break;
		case SETTINGS:
			mDrawable = Res.drawable.settings_active;
			break;
		case FLASHLIGHT:
			mDrawable = Res.drawable.flash;
			break;
		case SYNC:
			mDrawable = Res.drawable.sync;
			break;
		case WIFI_AP:
			mDrawable = Res.drawable.wifi_ap;
			break;
		case LOCK_NOW:
			mDrawable = Res.drawable.lock_now_active;
			break;
		case AUTO_BRIGHTNESS:
			mDrawable = Res.drawable.brightness_auto;
			break;
		}
		
		setImageDrawable(mDrawable);
		setPressed(false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if(pressed) {
			setBackgroundDrawable(Res.drawable.toggle_background_pressed);
		} else {
			setBackgroundDrawable(Res.drawable.toggle_background);
		}
	}

	@Override
	public void onClick(View v) {
		mAction.performAction();

		if(mShouldCloseAfterClick) {
			ControlService service = (ControlService) ControlService.getInstance();
			if(service != null && service.isAttachedToWindow() && ControlService.isRunning()) {
				service.close();
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		try {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(getIntentActionName());

			getContext().startActivity(intent);

			ControlService service = (ControlService) ControlService.getInstance();
			if(service != null && service.isAttachedToWindow() && ControlService.isRunning()) {
				service.close();
			}
		} catch(ActivityNotFoundException e) {
			setOnLongClickListener(null);
		}
		return true;
	}

	public final ToggleAction getAction() {
		return mAction;
	}

	protected abstract String getIntentActionName();
}
