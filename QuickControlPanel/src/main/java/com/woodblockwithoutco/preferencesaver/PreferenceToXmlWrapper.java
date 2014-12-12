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
package com.woodblockwithoutco.preferencesaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlSerializer;

import android.content.SharedPreferences;
import android.util.Log;
import android.util.Xml;

public class PreferenceToXmlWrapper {

	private static final String TAG = "PreferenceToXmlWrapper";
	private final XmlSerializer mSerializer;
	private final StringWriter mWriter;

	private boolean mIsValid = false;

	public PreferenceToXmlWrapper(SharedPreferences prefs) {

		mSerializer = Xml.newSerializer();
		mWriter = new StringWriter();

		try {
			mSerializer.setOutput(mWriter);
			mSerializer.startDocument("UTF-8", true);
			
			mSerializer.startTag("", "map");

			Map<String, ?> map = prefs.getAll();
			Set<String> set = map.keySet();
			for(String s : set) {
				Object o = map.get(s);
				if(o != null) {
					if(o instanceof String) {
						mSerializer.startTag("", "string");
						mSerializer.attribute("", "name", s);
						mSerializer.text(o.toString());
						mSerializer.endTag("", "string");
					} else if(o instanceof Boolean) {
						mSerializer.startTag("", "boolean");
						mSerializer.attribute("", "name", s);
						mSerializer.attribute("", "value", o.toString());
						mSerializer.endTag("", "boolean");
					} else if(o instanceof Integer) {
						mSerializer.startTag("", "int");
						mSerializer.attribute("", "name", s);
						mSerializer.attribute("", "value", o.toString());
						mSerializer.endTag("", "int");
					} else if(o instanceof Float) {
						mSerializer.startTag("", "float");
						mSerializer.attribute("", "name", s);
						mSerializer.attribute("", "value", o.toString());
						mSerializer.endTag("", "float");
					} else if(o instanceof Long) {
						mSerializer.startTag("", "long");
						mSerializer.attribute("", "name", s);
						mSerializer.attribute("", "value", o.toString());
						mSerializer.endTag("", "long");
					} else if(o instanceof Set<?>) {
						Set<?> stringsSet = (Set<?>)o;
						mSerializer.startTag("", "set");
						mSerializer.attribute("", "name", s);
						for(Object value : stringsSet) {
							if(value instanceof String) {
								mSerializer.startTag("", "set_string");
								mSerializer.text(value.toString());
								mSerializer.endTag("", "set_string");
							}
						}
						mSerializer.endTag("", "set");
					}
				}
			}
			mSerializer.endDocument();
			mIsValid = true;
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "PreferenceToXmlWrapper creation failed due to IllegalArgumentException");
		} catch (IllegalStateException e) {
			Log.e(TAG, "PreferenceToXmlWrapper creation failed due to IllegalStateException");
		} catch (IOException e) {
			Log.e(TAG, "PreferenceToXmlWrapper creation failed due to IOException");
		}
	}
	
	public boolean isValid() {
		return mIsValid;
	}

	public String getXmlContents() {
		checkIsValid();
		return mWriter.toString();
	}

	public void saveToFile(File file) throws IOException {
		checkIsValid();
		FileWriter writer = new FileWriter(file);
		writer.write(mWriter.toString());
		writer.close();
	}

	private void checkIsValid() {
		if(!mIsValid) throw new IllegalStateException("Can't call methods on malformed PreferenceToXmlWrapper");
	}
}


