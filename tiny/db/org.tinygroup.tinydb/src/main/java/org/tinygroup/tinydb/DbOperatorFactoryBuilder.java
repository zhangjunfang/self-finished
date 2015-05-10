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
package org.tinygroup.tinydb;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;


/**
 * 创建DbOperatorFactory
 * 
 * @author renhui
 * 
 */
public class DbOperatorFactoryBuilder {

	public DbOperatorFactory build(Reader reader) {
		return build(reader, null);
	}

	public DbOperatorFactory build(InputStream inputStream, String dataSource) {
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder(
					inputStream, dataSource);
			return build(builder.parser());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// Intentionally ignore. Prefer previous error.
			}
		}

	}

	public DbOperatorFactory build(InputStream stream) {
		return build(stream, null);
	}

	public DbOperatorFactory build(Reader reader, String dataSource) {
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder(reader,
					dataSource);
			return build(builder.parser());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// Intentionally ignore. Prefer previous error.
			}
		}
	}

	public DbOperatorFactory build(Configuration config) {
		return new DbOperatorFactory(config);
	}

}
