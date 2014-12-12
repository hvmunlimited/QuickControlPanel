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
package com.woodblockwithoutco.quickcontroldock.global.app;


import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.LaunchResolver;
import com.woodblockwithoutco.quickcontroldock.ui.ControlService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AssistantLauncherActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(LaunchResolver.isServiceEnabled(getApplicationContext())) {
			ControlService service = (ControlService) ControlService.getInstance();
			if(service != null && ControlService.isRunning()) {
				if(!service.isAttachedToWindow()) {
					service.open();
				} else {
					service.close();
				}
			} else {
				Intent intent = new Intent(getApplicationContext(), ControlService.class);
				intent.putExtra("openAfterLoaded", true);
				startService(intent);
			}
		}
		
		finish();
	}
}
