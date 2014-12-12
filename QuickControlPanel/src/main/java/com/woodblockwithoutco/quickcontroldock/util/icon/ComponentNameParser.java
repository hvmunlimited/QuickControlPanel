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
package com.woodblockwithoutco.quickcontroldock.util.icon;

import android.content.ComponentName;

public class ComponentNameParser {
	
	public static ComponentName parse(String compInfo) {
		
		if(!compInfo.startsWith("ComponentInfo")) return null;
		
		int slashIndex = compInfo.indexOf("/");
		int openingBraceIndex = compInfo.indexOf("{");
		int closingBraceIndex = compInfo.indexOf("}");
		
		if(slashIndex == -1 || closingBraceIndex == -1 || openingBraceIndex == -1) return null;
		
		ComponentName result = null;
		try {
			String pkg = compInfo.substring(openingBraceIndex + 1, slashIndex);
			String clazz = compInfo.substring(slashIndex + 1, closingBraceIndex);
			if(clazz.startsWith(".")) clazz = pkg + clazz;
			result = new ComponentName(pkg, clazz);
		} catch(RuntimeException e) {
			return null;
		}
		return result;
	}

}
