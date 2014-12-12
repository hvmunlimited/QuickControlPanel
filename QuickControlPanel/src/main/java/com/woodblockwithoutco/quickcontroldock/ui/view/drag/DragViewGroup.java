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
package com.woodblockwithoutco.quickcontroldock.ui.view.drag;

import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.GeneralResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LaunchResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.ui.ViewService;
import com.woodblockwithoutco.quickcontroldock.util.ScreenUtils;
import com.woodblockwithoutco.quickcontroldock.R;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DragViewGroup extends RelativeLayout {

	private View mDragHandler;
	private ImageView mFakeDragHandler;
	private boolean mIsExpanded = false;
	private OnDragViewTouchListener mDragTouchListener;
	private final int mIdleColor;
	private final int mActiveColor;
	private final int mNavBarSize;
	private final int mStatusBarSize;
	private Handler mHandler = new Handler();


	public DragViewGroup(Context context) {
		this(context, null, 0);
	}

	public DragViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@Override
	public void setPressed(boolean pressed) {

	}

	public DragViewGroup(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		mNavBarSize = ScreenUtils.getNavigationBarHeight(getContext());
		mStatusBarSize = ScreenUtils.getStatusBarHeight(getContext());

		String direction = LaunchResolver.getSwipeDetectorDirection(getContext());
		if("UP".equals(direction)) {
			setX(0);
			setY(ScreenUtils.getHeightPx(getContext())+mNavBarSize);
		} else if("RIGHT".equals(direction)) {
			setX(-ScreenUtils.getWidthPx(getContext())-mNavBarSize);
			setY(GeneralResolver.getPanelSpan(getContext()));
		} else if("LEFT".equals(direction)) {
			setX(ScreenUtils.getWidthPx(getContext())+mNavBarSize);
			setY(GeneralResolver.getPanelSpan(getContext()));
		}
		requestFocus();

		mIdleColor = ColorsResolver.getIdleColor(getContext());
		mActiveColor = ColorsResolver.getActiveColor(getContext());



	}

	public void initDrag() {
		mDragHandler = findViewById(R.id.panel_drag_handler);
		if(mDragHandler == null) throw new NullPointerException("Can't find drag handler for panel!");

		mFakeDragHandler = (ImageView) findViewById(R.id.drag_handler_fake);
		if(mFakeDragHandler == null) throw new NullPointerException("Can't find fake drag handler for panel!");
		mFakeDragHandler.getLayoutParams().width = GeneralResolver.getDragHandlerWidth(getContext());

		String direction = LaunchResolver.getSwipeDetectorDirection(getContext());
		if("UP".equals(direction)) {
			mDragTouchListener = new UpTouchListener();
		} else if("RIGHT".equals(direction)) {
			mDragTouchListener = new RightTouchListener();
		} else if("LEFT".equals(direction)) {
			mDragTouchListener = new LeftTouchListener();
		} else {
			mDragTouchListener = new UpTouchListener();
		}
		mDragHandler.setOnTouchListener(mDragTouchListener);
		mFakeDragHandler.setOnTouchListener(mDragTouchListener);
	}

	public void attachAdditionalDragView(View dragView) {
		boolean closeOnTouch = GeneralResolver.isCloseOnSpanTouch(getContext());
		if(!closeOnTouch) {
			dragView.setOnTouchListener(mDragTouchListener);
		} else {
			dragView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					close();
				}
			});
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK && mIsExpanded) {
			mDragTouchListener.close();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private class UpTouchListener implements OnDragViewTouchListener {
		private float mLastTouchY;
		private float mTouchDownY;
		

		private final DecelerateInterpolator DECELERATOR = new DecelerateInterpolator();
		private final AnimListener ANIM_LISTENER = new AnimListener();


		private final int HEIGHT = ScreenUtils.getHeightPx(getContext());
		private final int ANIMATE_TO_OPEN = GeneralResolver.getPanelSpan(getContext());
		private final int ANIMATE_TO_CLOSE = HEIGHT+mNavBarSize;

		private final int DRAG_THRESHOLD = getContext().getResources().getDimensionPixelSize(R.dimen.drag_threshold);
		

		@Override
		public boolean onTouch(View v, MotionEvent e) {

			switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				final float y = e.getRawY();
				mTouchDownY = e.getRawY();
				mLastTouchY = y;
				mFakeDragHandler.setColorFilter(mActiveColor | 0xFF000000);
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				int animateTo = ANIMATE_TO_CLOSE;
				if(Math.abs(e.getRawY() - mTouchDownY) > DRAG_THRESHOLD) {
					if(!mIsExpanded) {
						boolean openingDirection = e.getRawY() < mTouchDownY;
						if(openingDirection) {
							mIsExpanded = true;
							animateTo = ANIMATE_TO_OPEN;
						} else {
							mIsExpanded = false;
							animateTo = ANIMATE_TO_CLOSE;
						}
					} else {
						boolean closingDirection = e.getRawY() > mTouchDownY;
						if(closingDirection) {
							mIsExpanded = false;
							animateTo = ANIMATE_TO_CLOSE;
						} else {
							mIsExpanded = true;
							animateTo = ANIMATE_TO_OPEN;
						}
					}
				} else {
					animateTo = mIsExpanded ? ANIMATE_TO_OPEN : ANIMATE_TO_CLOSE;

				}
				mFakeDragHandler.setColorFilter(mIdleColor);
				DragViewGroup.this.animate().y(animateTo).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
				break;

			case MotionEvent.ACTION_MOVE:
				final float y2 = e.getRawY();
				final float dy = y2 - mLastTouchY;
				if((getY()+dy > ANIMATE_TO_OPEN) && (getY()+dy < HEIGHT)) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							setY(getY()+dy);
							mLastTouchY = y2;
						}
					});
				}
				break;
			}
			return false;
		}

		@Override
		public void close() {
			mIsExpanded = false;
			DragViewGroup.this.animate().y(ANIMATE_TO_CLOSE).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
		}

		@Override
		public void open() {
			mIsExpanded = true;
			ViewService service = ControlService.getInstance();
			if(service != null) {
				service.attachView();
			}
			mFakeDragHandler.setColorFilter(mIdleColor);
			DragViewGroup.this.animate().y(ANIMATE_TO_OPEN).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
		}
	}

	private class RightTouchListener implements OnDragViewTouchListener {
		private float mLastTouchX;
		private float mTouchDownX;

		private final DecelerateInterpolator DECELERATOR = new DecelerateInterpolator();
		private final AnimListener ANIM_LISTENER = new AnimListener();

		private final int WIDTH = ScreenUtils.getWidthPx(getContext());
		private final int ANIMATE_TO_CLOSE = -WIDTH-mNavBarSize;
		private final int ANIMATE_TO_OPEN = 0;

		private final int DRAG_THRESHOLD = getContext().getResources().getDimensionPixelSize(R.dimen.drag_threshold);

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				final float x = e.getRawX();
				mTouchDownX = e.getRawX();
				mLastTouchX = x;
				mFakeDragHandler.setColorFilter(mActiveColor | 0xFF000000);
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				int animateTo = ANIMATE_TO_OPEN;

				if(Math.abs(e.getRawX() - mTouchDownX) > DRAG_THRESHOLD) {
					if(!mIsExpanded) {
						boolean openingDirection = e.getRawX() > mTouchDownX;
						if(openingDirection) {
							mIsExpanded = true;
							animateTo = ANIMATE_TO_OPEN;
						} else {
							mIsExpanded = false;
							animateTo = ANIMATE_TO_CLOSE;
						}
					} else {
						boolean closingDirection = e.getRawX() < mTouchDownX;
						if(closingDirection) {
							mIsExpanded = false;
							animateTo = ANIMATE_TO_CLOSE;
						} else {
							mIsExpanded = true;
							animateTo = ANIMATE_TO_OPEN;
						}
					}
				} else {
					animateTo = mIsExpanded ? ANIMATE_TO_OPEN : ANIMATE_TO_CLOSE;

				}
				mFakeDragHandler.setColorFilter(mIdleColor);
				DragViewGroup.this.animate().x(animateTo).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
				break;

			case MotionEvent.ACTION_MOVE:
				final float x2 = e.getRawX();
				final float dx = x2 - mLastTouchX;
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if(getX() + dx >= 0) {
							setX(0);
						} else if(getX()+dx <= -2 * WIDTH) {
							setX(-2 * WIDTH);
						} else {
							setX(getX()+dx);
						}
						mLastTouchX = x2;
					}
				});
				break;
			}
			return false;
		}

		@Override
		public void close() {
			mIsExpanded = false;
			DragViewGroup.this.animate().x(ANIMATE_TO_CLOSE).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
		}

		@Override
		public void open() {
			mIsExpanded = true;
			ViewService service = ControlService.getInstance();
			if(service!= null) {
				service.attachView();
			}
			mFakeDragHandler.setColorFilter(mIdleColor);
			DragViewGroup.this.animate().x(ANIMATE_TO_OPEN).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
		}
	}

	private class LeftTouchListener implements OnDragViewTouchListener {
		private float mLastTouchX;
		private float mTouchDownX;

		private final DecelerateInterpolator DECELERATOR = new DecelerateInterpolator();
		private final AnimListener ANIM_LISTENER = new AnimListener();

		private final int WIDTH = ScreenUtils.getWidthPx(getContext());
		private final int ANIMATE_TO_CLOSE = WIDTH+mNavBarSize;
		private final int ANIMATE_TO_OPEN = 0;

		private final int DRAG_THRESHOLD = getContext().getResources().getDimensionPixelSize(R.dimen.drag_threshold);

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				final float x = e.getRawX();
				mTouchDownX = e.getRawX();
				mLastTouchX = x;
				mFakeDragHandler.setColorFilter(mActiveColor | 0xFF000000);
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				int animateTo = ANIMATE_TO_OPEN;
				if(Math.abs(e.getRawX() - mTouchDownX) > DRAG_THRESHOLD) {
					if(!mIsExpanded) {
						boolean openingDirection = e.getRawX() < mTouchDownX;
						if(openingDirection) {
							mIsExpanded = true;
							animateTo = ANIMATE_TO_OPEN;
						} else {
							mIsExpanded = false;
							animateTo = ANIMATE_TO_CLOSE;
						}
					} else {
						boolean closingDirection = e.getRawX() > mTouchDownX;
						if(closingDirection) {
							mIsExpanded = false;
							animateTo = ANIMATE_TO_CLOSE;
						} else {
							mIsExpanded = true;
							animateTo = ANIMATE_TO_OPEN;
						}
					} 
				} else {
					animateTo = mIsExpanded ? ANIMATE_TO_OPEN : ANIMATE_TO_CLOSE;

				}
				mFakeDragHandler.setColorFilter(mIdleColor);
				DragViewGroup.this.animate().x(animateTo).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
				break;

			case MotionEvent.ACTION_MOVE:
				final float x2 = e.getRawX();
				final float dx = x2 - mLastTouchX;
				
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if(getX() + dx <= 0) {
							setX(0);
						} else if(getX()+dx >= WIDTH) {
							setX(WIDTH);
						} else {
							setX(getX()+dx);
						}
						mLastTouchX = x2;
					}
				});
				break;
			}
			return false;
		}

		@Override
		public void close() {
			mIsExpanded = false;
			DragViewGroup.this.animate().x(ANIMATE_TO_CLOSE).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
		}

		@Override
		public void open() {
			mIsExpanded = true;
			ViewService service = ControlService.getInstance();
			if(service!= null) {
				service.attachView();
			}
			mFakeDragHandler.setColorFilter(mIdleColor);
			DragViewGroup.this.animate().x(ANIMATE_TO_OPEN).setListener(ANIM_LISTENER).setInterpolator(DECELERATOR).start();
		}
	}

	private class AnimListener implements AnimatorListener {
		@Override
		public void onAnimationEnd(Animator animation) {
			if(ControlService.isRunning() && ControlService.getInstance() != null) {
				if(!mIsExpanded) {
					ControlService.getInstance().detachView();
				}
			}
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationStart(Animator animation) {
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			if(ControlService.isRunning() && ControlService.getInstance() != null) {
				if(!mIsExpanded) {
					ControlService.getInstance().detachView();
				}
			}
		}
	}

	@Override
	public void onSizeChanged (int w, int h, int oldw, int oldh) {
		int height = ScreenUtils.getHeightPx(getContext()) - GeneralResolver.getPanelSpan(getContext()) + mStatusBarSize;
		super.onSizeChanged(w, height, oldw, oldh);
	}

	public void close() {
		mDragTouchListener.close();
	}

	public void open() {
		mDragTouchListener.open();
	}
}

