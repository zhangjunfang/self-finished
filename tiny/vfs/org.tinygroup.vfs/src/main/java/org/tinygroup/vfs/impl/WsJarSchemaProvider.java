/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.vfs.impl;

import org.tinygroup.vfs.FileObject;

public class WsJarSchemaProvider extends AbstractSchemaProvider {

	public static final String WSJAR_PROTOCOL = "wsjar:";

	public FileObject resolver(String resourceResolve) {
		return new JarFileObject(this, getResourceResolve(resourceResolve,
				WSJAR_PROTOCOL));
	}

	public boolean isMatch(String resource) {
		String lowerCase = resource.toLowerCase();
		return lowerCase.startsWith(WSJAR_PROTOCOL);
	}

	public String getSchema() {
		return WSJAR_PROTOCOL;
	}

}
