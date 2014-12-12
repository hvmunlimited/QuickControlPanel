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
package com.woodblockwithoutco.quickcontroldock.ui.factory;

import java.util.List;

import com.woodblockwithoutco.remotecontroller.OnArtworkChangeListener;
import com.woodblockwithoutco.remotecontroller.OnMetadataChangeListener;
import com.woodblockwithoutco.remotecontroller.OnPlaybackStateChangeListener;
import com.woodblockwithoutco.remotecontroller.OnRemoteControlFeaturesChangeListener;
import com.woodblockwithoutco.remotecontroller.PlayState;
import com.woodblockwithoutco.remotecontroller.RemoteControlFeature;
import com.woodblockwithoutco.remotecontroller.RemoteController;
import com.woodblockwithoutco.remotecontroller.impl.MusicControlService;
import com.woodblockwithoutco.quickcontroldock.global.holder.RemoteControllerHolder;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.NextButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.PlayPauseButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.buttons.PreviousButton;
import com.woodblockwithoutco.quickcontroldock.model.impl.seekbar.ScrubberSeekBar;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ColorsResolver;
import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.MusicResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;
import com.woodblockwithoutco.quickcontroldock.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MusicViewFactory {

	private Context mContext;

	public MusicViewFactory(Context context) {
		mContext = context;
	}

	@SuppressWarnings("deprecation")
	public View getMusicView() {

		LinearLayout rootContainer = new LinearLayout(mContext);
		LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		
		rootContainer.setLayoutParams(rootParams);
		rootContainer.setGravity(Gravity.CENTER);
		rootContainer.setBackgroundDrawable(getBg());

		boolean enabled = true;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			enabled = MusicControlService.instance != null;
		}
		
		if(enabled) {
			rootContainer.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout musicContainer = new LinearLayout(mContext);
			LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

			musicContainer.setLayoutParams(containerParams);
			musicContainer.setGravity(Gravity.CENTER);
			musicContainer.setOrientation(LinearLayout.VERTICAL);

			LinearLayout mediaButtonsContainer = new LinearLayout(mContext);
			mediaButtonsContainer.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams mediaButtonsContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			mediaButtonsContainer.setLayoutParams(mediaButtonsContainerParams);
			mediaButtonsContainerParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.standard_margin);
			int size = MusicResolver.getButtonSize(mContext);
			int margin = mContext.getResources().getDimensionPixelSize(R.dimen.media_buttons_margin);
			LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(size, size);
			buttonParams.setMargins(margin, 0, margin, 0);

			RemoteController remoteController = RemoteControllerHolder.getInstance();

			PreviousButton prevButton = new PreviousButton(mContext);
			prevButton.setLayoutParams(buttonParams);

			PlayPauseButton playPauseButton = new PlayPauseButton(mContext);
			playPauseButton.setLayoutParams(buttonParams);

			NextButton nextButton = new NextButton(mContext);
			nextButton.setLayoutParams(buttonParams);

			mediaButtonsContainer.addView(prevButton);
			mediaButtonsContainer.addView(playPauseButton);
			mediaButtonsContainer.addView(nextButton);

			RemoteControllerEventListener eventListener = new RemoteControllerEventListener();
			eventListener.setPlayPauseButton(playPauseButton);

			MetadataClickListener metadataClickListener = null;
			boolean isMetadataUsed = MusicResolver.isArtistTextEnabled(mContext) 
					|| MusicResolver.isTitleTextEnabled(mContext) 
					|| MusicResolver.isAlbumTextEnabled(mContext);

			if(isMetadataUsed) {
				if(metadataClickListener == null) metadataClickListener = new MetadataClickListener();
				TextView metadataTextView = getMetadataTextView(mediaButtonsContainerParams, metadataClickListener);
				eventListener.setMetadataTextView(metadataTextView);
				musicContainer.addView(metadataTextView);
			}

			remoteController.setMetadataChangeListener(eventListener);
			remoteController.setPlaybackStateChangeListener(eventListener);
			remoteController.setRemoteControlFeaturesChangeListener(eventListener);

			boolean isAlbumArtUsed = MusicResolver.isArtworkEnabled(mContext);
			if(isAlbumArtUsed) {
				if(metadataClickListener == null) metadataClickListener = new MetadataClickListener();
				ImageView albumArt = new ImageView(mContext);
				int padding = MusicResolver.getArtworkPadding(mContext);
				albumArt.setPadding(padding, padding, padding, padding);
				albumArt.setScaleType(ScaleType.FIT_XY);
				int artSize = MusicResolver.getArtworkSize(mContext);
				LinearLayout.LayoutParams albumArtParams = new LinearLayout.LayoutParams(artSize, artSize);
				albumArt.setLayoutParams(albumArtParams);
				albumArt.setImageResource(R.drawable.art_placeholder);
				rootContainer.addView(albumArt);
				remoteController.setArtworkChangeListener(eventListener);
				eventListener.setArtworkView(albumArt);
				albumArt.setOnClickListener(metadataClickListener);

			}


			musicContainer.addView(mediaButtonsContainer);

			if(MusicResolver.isScrubberEnabled(mContext) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				View v = ScrubberSeekBar.getContainer(mContext);
				ScrubberSeekBar sb = (ScrubberSeekBar) v.findViewById(R.id.seek_bar);
				eventListener.setScrubber(sb);
				musicContainer.addView(v);
			}

			

			rootContainer.addView(musicContainer);
		} else {
			int SIZE = 16;
			rootContainer.setGravity(Gravity.CENTER);
			
			TextView tv = new TextView(mContext);
			tv.setGravity(Gravity.CENTER);
			tv.setText(R.string.music_service_not_active);
			tv.setTextColor(ColorsResolver.getTextColor(mContext));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE);
			
			Button btn = new Button(mContext);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, R.string.enable_music_control_service, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);

					ControlService service = (ControlService) ControlService.getInstance();
					if(service != null && ControlService.isRunning() && service.isAttachedToWindow()) {
						service.close();
					}
				}
			});
			btn.setText(R.string.enable);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE);
			
			rootContainer.setOrientation(LinearLayout.VERTICAL);
			rootContainer.addView(tv);
			rootContainer.addView(btn);
		}

		return rootContainer;
	}

	private TextView getMetadataTextView(ViewGroup.LayoutParams params, OnClickListener l) {
		final ColorStateList colors = new ColorStateList(
				new int[][]{
						new int[]{android.R.attr.state_pressed},
						new int[] { android.R.attr.state_enabled}
				},
				new int[] {
						ColorsResolver.getPressedColor(mContext),
						ColorsResolver.getTextColor(mContext)
				});


		TextView result = new TextView(mContext);
		result.setLayoutParams(params);
		result.setText(R.string.unknown);
		result.setOnClickListener(l);
		result.setMaxLines(1);
		result.setSingleLine();
		result.setMarqueeRepeatLimit(-1);
		result.setSelected(true);
		result.setGravity(Gravity.CENTER);
		result.setEllipsize(TruncateAt.MARQUEE);
		result.setTextColor(colors);
		result.setTextSize(TypedValue.COMPLEX_UNIT_SP, MusicResolver.getMetadataTextSize(mContext));
		return result;
	}

	private Drawable getBg() {
		LayerDrawable bgDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.section_bg);
		GradientDrawable mainDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_main);
		GradientDrawable shadowDrawable = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.section_bg_shadow);
		mainDrawable.setColor(ColorsResolver.getSectionMainBackgroundColor(mContext));
		shadowDrawable.setColor(ColorsResolver.getSectionShadowBackgroundColor(mContext));

		return bgDrawable;
	}

	private class MetadataClickListener implements OnClickListener {

		private RemoteController mRemoteController;

		public MetadataClickListener() {
			mRemoteController = RemoteControllerHolder.getInstance();
		}

		@Override
		public void onClick(View v) {
			Intent intent = mRemoteController.getCurrentClientIntent();
			if(intent != null) {
				mContext.startActivity(intent);
				ControlService service = (ControlService) ControlService.getInstance();
				if(service != null && ControlService.isRunning() && service.isAttachedToWindow()) {
					service.close();
				}
			}
		}

	}

	private class RemoteControllerEventListener implements OnMetadataChangeListener, 
	OnRemoteControlFeaturesChangeListener, OnPlaybackStateChangeListener, OnArtworkChangeListener {

		private TextView mMetadataTextView;
		private boolean mIsArtistEnabled = false;
		private boolean mIsTitleEnabled = false;
		private boolean mIsAlbumEnabled = false;
		private ImageView mArtworkView;
		private Bitmap mPrevArtwork;
		private StringBuilder mMetadataBuilder = new StringBuilder();

		private ScrubberSeekBar mScrubber;
		private PlayPauseButton mPlayPauseButton;


		public void setMetadataTextView(TextView tv) {
			mMetadataTextView = tv;
			mIsArtistEnabled = MusicResolver.isArtistTextEnabled(mContext);
			mIsTitleEnabled = MusicResolver.isTitleTextEnabled(mContext);
			mIsAlbumEnabled = MusicResolver.isAlbumTextEnabled(mContext);
		}

		public void setArtworkView(ImageView iv) {
			mArtworkView = iv;
		}



		public void setScrubber(ScrubberSeekBar sb) {
			mScrubber = sb;
		}


		public void setPlayPauseButton(PlayPauseButton ppb) {
			mPlayPauseButton = ppb;
		}

		@Override
		public void onMetadataChanged(String artist, String title, String album, String albumArtist, long duration) {
			if(mMetadataTextView != null) {
				mMetadataBuilder.delete(0, mMetadataBuilder.length());

				if(mIsArtistEnabled) {
					String processedArtist = artist;
					if(processedArtist == null) {
						processedArtist = nullProofString(albumArtist);
					}
					mMetadataBuilder.append(processedArtist).append(" - ");
				}

				if(mIsTitleEnabled) {
					mMetadataBuilder.append(nullProofString(title)).append(" - ");
				}

				if(mIsAlbumEnabled) {
					mMetadataBuilder.append(nullProofString(album)).append(" - ");
				}

				if(mMetadataBuilder.toString().endsWith(" - ")) {
					int length = mMetadataBuilder.length();
					mMetadataBuilder.delete(length - 2, mMetadataBuilder.length());
				}

				mMetadataTextView.setText(mMetadataBuilder.toString());
			}

			if(mScrubber != null) {
				mScrubber.setDuration(duration / 1000);
			}
		}

		@Override
		public void onFeaturesChanged(List<RemoteControlFeature> usesFeatures) {
			if(mScrubber != null) {
				if(usesFeatures.contains(RemoteControlFeature.USES_SCRUBBING)) {
					mScrubber.requestShow();
				} else {
					mScrubber.requestHide();
				}
			}
		}

		private String nullProofString(String string) {
			return string == null ? "Unknown" : string;
		}

		@Override
		public void onPlaybackStateChanged(PlayState playbackState) {
			if(mScrubber != null) mScrubber.stopPositionUpdate();

			if(mPlayPauseButton != null) {
				switch(playbackState) {
				//�����
				case BUFFERING:
				case ERROR:
				case PAUSED:
				case STOPPED:
					mPlayPauseButton.setDrawableStateToSecond();
					break;

					//������������
				case FAST_FORWARDING:
				case REWINDING:
				case SKIPPING_BACKWARDS:
				case SKIPPING_FORWARDS:
				case PLAYING:
					mPlayPauseButton.setDrawableStateToFirst();
					if(mScrubber != null) mScrubber.startPositionUpdate();
					break;
				}
			}
		}



		@Override
		public void onArtworkChanged(Bitmap artwork) {

			if(mPrevArtwork != null && mPrevArtwork.isRecycled()) {
				mPrevArtwork.recycle();
			}

			mPrevArtwork = artwork;

			if(artwork != null && !artwork.isRecycled()) {
				mArtworkView.setImageBitmap(artwork);
			} else {
				mArtworkView.setImageResource(R.drawable.art_placeholder);
			}
		}

	}

}


