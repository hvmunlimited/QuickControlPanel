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
package com.woodblockwithoutco.quickcontroldock.resource.loader;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;

public class ResourceLoader {
	
	public static ExternalResources getResourcesForPackage(Context context, String packageName) throws NameNotFoundException {
		Resources res;
		try {
			res = context.getPackageManager().getResourcesForApplication(packageName);
			return new ExternalResources(res, packageName);
		} catch (NameNotFoundException e) {
			throw new NameNotFoundException("Error loading resources for package: "+packageName);
		}
		
	}
}
