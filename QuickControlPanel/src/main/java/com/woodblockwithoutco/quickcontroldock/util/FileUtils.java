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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtils {

	public static void copyDirectory(File sourceLocation, File targetLocation, boolean createMissing) throws IOException {
		if (sourceLocation.isDirectory()) {

			if (!targetLocation.exists()) {
				if(createMissing) {
					targetLocation.mkdirs();
				} else {
					targetLocation.mkdir();
				}
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				FileUtils.copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]), createMissing);
			}
		} else {
			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	public static void delete(File path) throws IOException {
		if(path.isDirectory()) {
			for(File name:path.listFiles()) {
				if(name.isDirectory()) {
					delete(name);
				} else {
					if(!name.delete()) throw new IOException("Failed to delete "+name.getAbsolutePath());
				}
			}
		} else {
			if(!path.delete()) throw new IOException("Failed to delete "+path.getAbsolutePath());
		}
	}

	public static String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}

	public static long getSize(File path) {
		if(path.isDirectory()) return -1;
		return path.length();
	}
}
