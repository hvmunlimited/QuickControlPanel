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

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.util.Xml;

public class XmlToPreferenceWrapper {

	private final String mContent;
	private final StringReader mReader;
	private final XmlPullParser mParser = Xml.newPullParser();
	private Map<String, Object> mPrefsMap = new HashMap<String, Object>();
	private boolean mIsValid = false;

	private int mCurrentType = T_UNDEFINED;
	private String mCurrentName = "";
	private Set<String> mCurrentSet;

	private final static int T_UNDEFINED = -1;
	private final static int T_STRING = 0;
	private final static int T_INT = 1;
	private final static int T_LONG = 2;
	private final static int T_FLOAT = 3;
	private final static int T_BOOLEAN = 4;
	private final static int T_MAP = 5;
	private final static int T_SET = 6;
	private final static int T_SET_STRING = 7;
	
	private static final String TAG = "XmlToPreferenceWrapper";

	public XmlToPreferenceWrapper(String content) {
		mContent = content;
		mReader = new StringReader(mContent);
		try {
			mParser.setInput(mReader);
			while(mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
				int type = mParser.getEventType();
				switch(type) {
				case XmlPullParser.START_TAG:
					mCurrentType = getTypeForName(mParser.getName());
					String name = mParser.getAttributeValue("", "name");
					if(name != null) mCurrentName = name;
					if(!processType(mCurrentType)) throw new XmlPullParserException("Invalid XML map! [Wrong tag type]");
					break;
				case XmlPullParser.TEXT:
					if(mCurrentType != T_STRING && mCurrentType != T_SET_STRING) throw new XmlPullParserException("Invalid XML map! [misplaced text]");
					String value = mParser.getText();
					if(mCurrentType == T_STRING) {
						mPrefsMap.put(mCurrentName, value);
					} else if(mCurrentType == T_SET_STRING) {
						if(mCurrentSet != null) {
							mCurrentSet.add(String.valueOf(value));
						} else {
							throw new IllegalStateException("Invalid XML map! [Tag <string_set> is met outside of <set>]");
						}
					}
					break;
				case XmlPullParser.END_TAG:
					String endTagType = mParser.getName();
					if("set".equals(endTagType)) {
						writeStringSet();
					}
					break;
				}
				mParser.next();
			}
			mIsValid = true;
		} catch (XmlPullParserException e) {
			Log.e(TAG, "XmlPullParserException: " + e.getMessage());
			mIsValid = false;
		} catch (IOException e) {
			Log.e(TAG, "IOException: " + e.getMessage());
			mIsValid = false;
		}
	}

	private void writeStringSet() {
		if(mCurrentSet == null) {
			throw new IllegalStateException("Invalid XML map! [</set> tag is not accompanied by <set>]");
		}
		mPrefsMap.put(mCurrentName, mCurrentSet); 
		mCurrentSet = null;
	}

	private boolean processType(int type) {
		String value = "";
		switch(type) {
		case T_STRING:
			return true;
		case T_LONG:
			value = mParser.getAttributeValue("", "value");
			mPrefsMap.put(mCurrentName, Long.valueOf(value));
			return true;
		case T_INT:
			value = mParser.getAttributeValue("", "value");
			mPrefsMap.put(mCurrentName, Integer.valueOf(value));
			return true;
		case T_FLOAT:
			value = mParser.getAttributeValue("", "value");
			mPrefsMap.put(mCurrentName, Float.valueOf(value));
			return true;
		case T_BOOLEAN:
			value = mParser.getAttributeValue("", "value");
			mPrefsMap.put(mCurrentName, Boolean.valueOf(value));
			return true;
		case T_SET:
			mCurrentSet = new HashSet<String>();
			return true;
		case T_SET_STRING:
			return true;
		case T_MAP:
			return true;
		case T_UNDEFINED:
			return false;
		}
		return false;
	}

	private int getTypeForName(String tag) {
		if("string".equals(tag)) {
			return T_STRING;
		} else if("long".equals(tag)) {
			return T_LONG;
		} else if("float".equals(tag)) {
			return T_FLOAT;
		} else if("boolean".equals(tag)) {
			return T_BOOLEAN;
		} else if("int".equals(tag)) {
			return T_INT;
		} else if("map".equals(tag)) {
			return T_MAP;
		} else if("set".equals(tag)) {
			return T_SET;
		} else if("set_string".equals(tag)) {
			return T_SET_STRING;
		} else {
			return T_UNDEFINED;
		}
	}

	private void checkIsValid() {
		if(!mIsValid) throw new IllegalStateException("Can't call methods on corrupt XmlToPreferenceWrapper");
	}

	public Map<String, ?> getPreferencesMap() {
		checkIsValid();
		return mPrefsMap;
	}

	public boolean isValid() {
		return mIsValid;
	}
	
	@SuppressWarnings("unchecked")
	public void applyToPreferences(SharedPreferences prefs) {
		checkIsValid();
		Editor editor = prefs.edit();
		Set<String> keys = mPrefsMap.keySet();
		for(String key : keys) {
			Object o = mPrefsMap.get(key);
			if(o != null) {
				if(o instanceof Boolean) {
					editor.putBoolean(key, (Boolean)o);
				} else if(o instanceof String) {
					editor.putString(key, (String)o);
				} else if(o instanceof Long) {
					editor.putLong(key, (Long)o);
				} else if(o instanceof Integer) {
					editor.putInt(key, (Integer)o);
				} else if(o instanceof Float) {
					editor.putFloat(key, (Float)o);
				} else if(o instanceof Set<?>) {
					editor.putStringSet(key, (Set<String>)o);
				}
				editor.commit();
			}
		}
	}
}
