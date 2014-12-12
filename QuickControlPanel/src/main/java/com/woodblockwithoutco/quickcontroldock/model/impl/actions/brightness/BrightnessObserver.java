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
package com.woodblockwithoutco.quickcontroldock.model.impl.actions.brightness;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;

public class BrightnessObserver extends ContentObserver {
	
    private final Uri BRIGHTNESS_MODE_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);

    private Context mContext;
    
    private final Intent BRIGHTNESS_CHANGED_INTENT;
    private final Intent BRIGHTNESS_MODE_CHANGED_INTENT;
    
    
    public BrightnessObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        BRIGHTNESS_CHANGED_INTENT = new Intent(context.getPackageName()+".BRIGHTNESS_CHANGED");
        BRIGHTNESS_MODE_CHANGED_INTENT = new Intent(context.getPackageName()+".BRIGHTNESS_MODE_CHANGED");
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
    	
        if (selfChange) return;
        
        if (BRIGHTNESS_MODE_URI.equals(uri)) {
        	LocalBroadcastManager.getInstance(mContext).sendBroadcast(BRIGHTNESS_MODE_CHANGED_INTENT);
        } else if (BRIGHTNESS_URI.equals(uri)) {
        	LocalBroadcastManager.getInstance(mContext).sendBroadcast(BRIGHTNESS_CHANGED_INTENT);
        }

    }

    public void startObserving() {
        final ContentResolver cr = mContext.getContentResolver();
        cr.unregisterContentObserver(this);
        cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, this);
        cr.registerContentObserver( BRIGHTNESS_URI, false, this);
    }

    public void stopObserving() {
        final ContentResolver cr = mContext.getContentResolver();
        cr.unregisterContentObserver(this);
    }

}

