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
package com.woodblockwithoutco.quickcontroldock.global.event;

import java.util.ArrayList;
import java.util.List;


public class VisibilityEventNotifier {

	private static VisibilityEventNotifier sInstance;
	
	private List<OnVisibilityEventListener> mListeners = new ArrayList<OnVisibilityEventListener>();
	
	private VisibilityEventNotifier() {
	}
	
	public static synchronized VisibilityEventNotifier getInstance() {
		if(sInstance == null) {
			sInstance = new VisibilityEventNotifier();
		}
		return sInstance;
	}
	
	public void registerListener(OnVisibilityEventListener l) {
		mListeners.add(l);
	}
	
	public void unregisterListener(OnVisibilityEventListener l) {
		mListeners.remove(l);
	}
	
	public void unregisterAllListeners() {
		mListeners.clear();
	}
	
	public void notifyHide() {
		for(OnVisibilityEventListener l : mListeners) {
			if(l != null) {
				l.onHide();
			}
		}
	}
	
	public void notifyShow() {
		for(OnVisibilityEventListener l : mListeners) {
			if(l != null) {
				l.onShow();
			}
		}
	}
	
	public interface OnVisibilityEventListener {
		public void onShow();
		public void onHide();
	}
}
