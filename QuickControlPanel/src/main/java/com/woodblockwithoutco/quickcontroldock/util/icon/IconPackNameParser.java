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

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.woodblockwithoutco.quickcontroldock.prefs.resolvers.ShortcutsResolver;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class IconPackNameParser {

	private static final String TAG = "IconPackNameParser";
	private static String sPackageName;
	private static Resources sRes;
	private static XmlPullParser sParser;


	public static void load(Context ctx, String pkg) throws NameNotFoundException, XmlPullParserException, IOException {
		
		sPackageName = pkg;
		Context extCtx = ctx.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
		sRes = extCtx.getResources();
		int id = sRes.getIdentifier("appfilter", "xml", sPackageName);
		boolean loadFromAssets = false;
		if(id != 0) {
			sParser = sRes.getXml(id);
		} else {
			loadFromAssets = true;
			Log.e(TAG, "Failed to get appfilter.xml from resources of package "+sPackageName+", attempting load from assets...");
		}

		if(loadFromAssets) {
			InputStream is = sRes.getAssets().open("appfilter.xml");
			sParser = XmlPullParserFactory.newInstance().newPullParser();
			sParser.setInput(is, null);
		}

		if(sParser == null) throw new XmlPullParserException("Parser is null!");

		SharedPreferences prefs = ctx.getSharedPreferences("icon_pack", Context.MODE_PRIVATE);
		prefs.edit().clear().commit();
		SharedPreferences.Editor editor = prefs.edit();

		while(sParser.getEventType() != XmlPullParser.END_DOCUMENT) {
			int type = sParser.getEventType();
			switch(type) {
			case XmlPullParser.START_TAG:
				String currentComponent = sParser.getAttributeValue(null, "component");
				String drawable = sParser.getAttributeValue(null, "drawable");
				if(currentComponent != null) {
					ComponentName cmpName = ComponentNameParser.parse(currentComponent);
					if(cmpName != null && drawable != null) {
						int drawableId = sRes.getIdentifier(drawable, "drawable", sPackageName);
						if(drawableId != 0) {
							//TODO: replace by SQL database
							String pref = cmpName.getPackageName()+"/"+cmpName.getClassName();
							editor.putInt(pref, drawableId);
						}
					}
				}
				break;
			}

			sParser.next();
		}
		editor.commit();
	}
	
	public static Drawable getIconForComponent(Context ctx, String component) {
		if(sRes == null) {
			Context extCtx;
			try {
				extCtx = ctx.createPackageContext(ShortcutsResolver.getExternalIconPackName(ctx), Context.CONTEXT_IGNORE_SECURITY);
				sRes = extCtx.getResources();
			} catch (NameNotFoundException e) {
				return null;
			}
		}
		int id = ctx.getSharedPreferences("icon_pack", Context.MODE_PRIVATE).getInt(component, 0);
		if(id == 0) return null;
		return sRes.getDrawable(id);
	}
}
