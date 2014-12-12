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
package com.woodblockwithoutco.quickcontroldock.util;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class TimeProfiler {
	
	private static Map<String, Long> sStartTimeMap =  new HashMap<String, Long>();
	private final static String TAG = TimeProfiler.class.getSimpleName();
	
	public static void startMeasureMs(String tag) {
		long startTime = System.currentTimeMillis();
		sStartTimeMap.put(tag, startTime);
	}
	
	public static void printDebugMessage(String message) {
		Log.d(TAG, "MESSAGE: "+message);
	}
	
	public static void stopMeasureMs(String tag) {
		long startTime = sStartTimeMap.get(tag);
		long endTime = System.currentTimeMillis();
		long result = endTime - startTime;
		Log.d(TAG, "Measure with tag \'"+tag+"\' was completed.\nResult: "+result+" ms");
		sStartTimeMap.remove(tag);
	}

}
