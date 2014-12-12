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
package com.woodblockwithoutco.quickcontroldock.global.holder;

import com.woodblockwithoutco.quickcontroldock.global.app.GlobalApplication;
import com.woodblockwithoutco.quickcontroldock.util.AppShortcutResolver;

public class AppShortcutResolverHolder {

	private static AppShortcutResolver sInstance;
	
	public synchronized static AppShortcutResolver getInstance() {
		if(sInstance == null) {
			sInstance = new AppShortcutResolver(GlobalApplication.getGlobalContext());
		}
		return sInstance;
	}
}
