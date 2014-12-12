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

import com.woodblockwithoutco.quickcontroldock.model.action.ToggleTriAction;
import com.woodblockwithoutco.quickcontroldock.model.action.ToggleTriAction.State;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.TogglesResolver;
import com.woodblockwithoutco.quickcontroldock.resource.Res;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.R;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public abstract class BaseTriToggleButton extends ImageView implements View.OnClickListener, View.OnLongClickListener {

	protected ToggleTriAction mAction;
	protected Drawable mFirstDrawable;
	protected Drawable mSecondDrawable;
	protected Drawable mThirdDrawable;
	protected boolean mShouldCloseAfterClick;

	private final Animation mIndefiniteStateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.indef_anim);



	public BaseTriToggleButton(Context context) {
		this(context, null, 0);
	}

	public BaseTriToggleButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseTriToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setAnimation(mIndefiniteStateAnim);
		setOnClickListener(this);
		if(TogglesResolver.isLongClickEnabled(getContext())) setOnLongClickListener(this);
		setClickable(true);
		stopIndefAnim();
		
		mShouldCloseAfterClick = TogglesResolver.shouldCloseAfterClick(getContext());
	}

	protected void setVisualState(State state) {
		switch(state) {
		case FIRST:
			setImageDrawable(mFirstDrawable);
			break;
		case SECOND:
			setImageDrawable(mSecondDrawable);
			break;
		case THIRD:
			setImageDrawable(mThirdDrawable);
			break;
		}
	}

	protected final void startIndefAnim() {
		startAnimation(mIndefiniteStateAnim);
	}

	protected final void stopIndefAnim() {
		clearAnimation();
	}

	protected final void initDrawable(ButtonType type) {
		switch(type) {
		case SOUND:
			mFirstDrawable = Res.drawable.sound_on;
			mSecondDrawable = Res.drawable.vibrate;
			mThirdDrawable = Res.drawable.sound_off;
			break;
		case SCREEN_TIMEOUT:
			mFirstDrawable = Res.drawable.screen_timeout_1;
			mSecondDrawable = Res.drawable.screen_timeout_2;
			mThirdDrawable = Res.drawable.screen_timeout_3;
			break;
		default:
			throw new IllegalArgumentException("Button of type "+type.name()+" must extend BaseToggleButton class!");
		}
		setVisualState(mAction.getCurrentState());
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
	public boolean onLongClick(View v) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(getIntentActionName());

		getContext().startActivity(intent);

		ControlService service = (ControlService) ControlService.getInstance();
		if(service != null) {
			service.close();
		}
		return true;
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

	public final ToggleTriAction getAction() {
		return mAction;
	}

	protected abstract String getIntentActionName();
}
