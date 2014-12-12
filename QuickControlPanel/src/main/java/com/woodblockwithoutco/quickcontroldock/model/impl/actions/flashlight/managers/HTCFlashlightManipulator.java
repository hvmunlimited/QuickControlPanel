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
package com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.managers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.IBinder;
import android.util.Log;

public class HTCFlashlightManipulator implements FlashlightManipulator {
	
	private static final String TAG="HTCFlashManipulator";
	
	private Method setBrightness;
	private Object svc;
	
	public HTCFlashlightManipulator() {
		initFlashMethod();
	}
	
	private final void initFlashMethod() {
		try {
			Object hardwareObject = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[] { String.class }).invoke(null, new Object[] { "htchardware" });
			Method asInterface = Class.forName("android.os.IHtcHardwareService$Stub").getMethod("asInterface", new Class[] { IBinder.class });
			Object[] binders = new Object[1];
			binders[0]=(IBinder)hardwareObject;
			svc=asInterface.invoke(null, binders);
			Class<?> _class=svc.getClass();
			Class<?>[] classes=new Class[1];
			classes[0]=Integer.TYPE;
			setBrightness=_class.getMethod("setFlashlightBrightness", classes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final void setFlash(int i) {
		if(setBrightness!=null) {
			Object[] objects=new Object[1];
			objects[0]=i;
			try {
				setBrightness.invoke(svc, objects);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Failed to start, reason: IllegalArgument");
			} catch (IllegalAccessException e) {
				Log.e(TAG, "Failed to start, reason: IllegalAccess");
			} catch (InvocationTargetException e) {
				Log.e(TAG, "Failed to start, reason: InvokationTarget invalid");
			}
			
		} else {
			Log.e(TAG, "Failed to initialize flashlight");
		}
	}

	public void turnFlashlightOn() {
		setFlash(127);
	}


	public void turnFlashlightOn(boolean withPreview) {
		setFlash(127);		
	}

	public void turnFlashlightOff() {
		setFlash(0);
	}

}
